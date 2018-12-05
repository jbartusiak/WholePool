package com.jba.source;

import com.jaunt.JauntException;
import com.jaunt.UserAgent;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.utils.RestRequestBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Service
public class BlaBlaCarWebFetcher extends SingleSourceFetch {
    @Autowired
    UserAgent agent;

    @Override
    public String getResultsForQuery(String from, String to) {
        return null;
    }

    @Override
    public void doImplementationSpecificInitialization() {

    }

    @Override
    public void search(String from, String to, String dateOfDeparture, String dateOfArrival) {
        String searchString = searchBaseUrl+"";
        try {
            searchString = searchString.replace(DEPARTURE_PLACEHOLDER, URLEncoder.encode(from, StandardCharsets.UTF_8.toString()));
            searchString = searchString.replace(ARRIVAL_PLACEHOLDER, URLEncoder.encode(to, StandardCharsets.UTF_8.toString()));
            searchString = searchString.replace(DATE_OF_DEPARTURE_PLACEHOLDER, dateOfDeparture);
        }
        catch (Exception e){

        }
        try {
            agent.visit(searchString);
            parse(agent.doc.innerHTML(), from, to);
        }
        catch (JauntException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parse(String input, String from, String to) {
        Document doc = Jsoup.parse(input);

        Elements elements= doc.getElementsByClass("tripsList");

        Elements ul = elements.get(0).getElementsByTag("ul");

        Elements li = ul.get(0).getElementsByTag("li");

        logger.info("String to parse results");

        li.forEach(element ->{
            try {
                if (element.hasAttr("itemtype")) {
                    parseAsIndividual(element.toString(), from, to);
                }
            }
            catch (Exception e){

            }
        });

        logger.info("finished parsing result");
    }

    @Async("sourceTaskExecutor")
    public void parseAsIndividual(String input, String from, String to){
        Document doc = Jsoup.parse(input);

        Elements elements= doc.getElementsByTag("meta");
        String rideId="";
        String departureLocation="";
        String arrivalLocation="";
        String startDate="";
        String endDate="";
        String price="";

        price = doc.getElementsByClass("kirk-tripCard-price").text();
        price = price.substring(0,4).replace(",",".");
        logger.info("finished parsing single result");

        List<Element> elementsArray = elements.stream().collect(Collectors.toList());
        for(Element element: elementsArray){
            String attribute = element.attr("itemprop");
            switch (attribute){
                case "url":{
                    rideId=element.attr("content");
                    rideId=rideId.substring(rideId.lastIndexOf("/")+1);
                    break;
                }
                case "name":{
                    String fromTo = element.attr("content");
                    fromTo = fromTo.substring(fromTo.lastIndexOf("/")+1);
                    String fromToArray[] = fromTo.split(" → ");
                    departureLocation=fromToArray[0];
                    arrivalLocation=fromToArray[1];
                    break;
                }
                case "startDate":{
                    startDate = element.attr("content");
                    startDate = startDate.substring(startDate.lastIndexOf("/")+1);
                    break;
                }
                case "endDate":{
                    endDate = element.attr("content");
                    endDate = endDate.substring(endDate.lastIndexOf("/")+1);
                }
                default:{
                    break;
                }
            }
        }

        RestTemplate template = new RestTemplate();

        String checkIsRidePresentQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("ride")
                .addParam("directLink", "https://www.blablacar.pl/"+rideId)
                .build();

        Ride[] ridesAvailable = deserializer.getResultArrayFor(template.getForObject(checkIsRidePresentQuery, String.class), Ride[].class);

        if(ridesAvailable.length>0){
            logger.info("Ride of URL "+"https://www.blablacar.pl/"+rideId+" already in DB. Skipping...");
            return;
        }

        String findRouteQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("search")
                .addPathParam("route")
                .addParam("fromLocation", departureLocation)
                .addParam("toLocation", arrivalLocation)
                .build();


        logger.info("putting result in DB");
        Route[] r = deserializer.getResultArrayFor(template.getForObject(findRouteQuery, String.class), Route[].class);
        List<Route> routes = Arrays.asList(r);

        if (routes.size() == 0) {
            Route newRoute = new Route(departureLocation, arrivalLocation);

            String postNewRoute = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("route").build();

            newRoute = deserializer.getSingleItemFor(template.postForObject(postNewRoute, newRoute, String.class), Route.class);
            routes = new ArrayList<>();
            routes.add(newRoute);
        }

        Ride ride = new Ride(definition, routes.get(0));
        ride.setNrOfSeats(-1);
        ride.setDirectURL("https://www.blablacar.pl/" + rideId);

        String postRideQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("ride")
                .addParam("userId", 1)
                .build();

        ride = deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);

        RideDetails rideDetails = new RideDetails(ride, LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate), 1, Double.parseDouble(price), "No description");

        String postRideDetailsQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("ride")
                .addPathParam("details")
                .addParam("rideId", ride.getRideId())
                .build();

        template.postForObject(postRideDetailsQuery, rideDetails, String.class);
        logger.info("Result: done");
    }

    @Bean
    UserAgent getUserAgent(){
        return new UserAgent();
    }
}
