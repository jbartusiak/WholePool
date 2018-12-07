package com.jba.source;

import com.jaunt.JauntException;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.utils.RestRequestBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GenericWebFetcher extends AbstractSourceFetch {

    /*
    * Defines labels for properties.
    * */
    private final static String
            URL_BASE="urlbase",
            CONTAINER="container",
            RIDE_ID_SELECTOR="rideId",
            RIDE_ID_CONTAINER="rideId.container",
            DEPARTURE_SELECTOR="departure",
            DEPARTURE_CONTAINER="departure.container",
            ARRIVAL_SELECTOR="arrival",
            ARRIVAL_CONTAINER="arrival.container",
            DATE_OF_DEPARTURE_SELECTOR="dateOfDeparture",
            DATE_OF_DEPARTURE_CONTAINER="dateOfDeparture.container",
            DATE_OF_DEPARTURE_FORMAT="dateOfDeparture.format",
            DATE_OF_ARRIVAL_SELECTOR="dateOfArrival",
            DATE_OF_ARRIVAL_CONTAINER="dateOfArrival.container",
            DATE_OF_ARRIVAL_FORMAT="dateOfArrival.format",
            PRICE_SELECTOR="price",
            PRICE_CONTAINER="price.container",
            PRICE_SEPARATOR="price.separator",
            PRICE_DIVISION_FACTOR="price.div-factor";

    @Override
    public String getResultsForQuery(String from, String to) {
        return null;
    }

    @Override
    public void doImplementationSpecificInitialization() {
    }

    @Override
    public void search(String from, String to, String dateOfDeparture, String dateOfArrival) {
        if(rides.size()>0){
            rides.clear();
        }
        if(rideDetails.size()>0){
            rideDetails.clear();
        }

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
        Document document = Jsoup.parse(input);

        Elements elements = document.select(properties.getProperty(CONTAINER));
        try {
            elements.iterator().forEachRemaining(element -> {
                logger.debug("Parsing: result");
                parseIndividual(element, from, to);
            });
        }
        catch (Exception e){
            logger.error("Error occured while parsing result. It would be dangerous to continue...", e);
            throw e;
        }
        saveToDB();
    }

    @Override
    public void parseIndividual(Element element, String from, String to) {
        String rideId=getRideId(element);
        String departureLocation=getDepartureLocation(element);
        String arrivalLocation=getArrivalLocation(element);
        String startDate=getStartDate(element);
        String endDate=getEndDate(element);
        String price=getPrice(element);
        String directURL=rideId.startsWith(properties.getProperty(URL_BASE))?rideId:properties.getProperty(URL_BASE)+rideId;

        RestTemplate template = new RestTemplate();

        //check is item in DB already
        String checkIsRidePresentQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("ride")
                .addParam("directLink", directURL)
                .build();

        Ride[] ridesAvailable = deserializer.getResultArrayFor(template.getForObject(checkIsRidePresentQuery, String.class), Ride[].class);

        if(ridesAvailable.length>0){
            logger.info("Ride of URL "+directURL+" already in DB. Skipping...");
            return;
        }

        String findRouteQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("search")
                .addPathParam("route")
                .addParam("fromLocation", departureLocation)
                .addParam("toLocation", arrivalLocation)
                .build();

        //check if route is already in DB
        Route[] r = deserializer.getResultArrayFor(template.getForObject(findRouteQuery, String.class), Route[].class);
        List<Route> routes = Arrays.asList(r);

        if (routes.size() == 0) {
            //if not, create a new route;
            Route newRoute = new Route(departureLocation, arrivalLocation);

            String postNewRoute = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("route").build();

            newRoute = deserializer.getSingleItemFor(template.postForObject(postNewRoute, newRoute, String.class), Route.class);
            routes = new ArrayList<>();
            routes.add(newRoute);
        }

        Ride ride = new Ride(definition, routes.get(0));
        ride.setNrOfSeats(-1);
        ride.setDirectURL(directURL);

        rides.add(ride);

        RideDetails rideDet = new RideDetails(ride, LocalDateTime.parse(startDate),
                LocalDateTime.parse(endDate), 1, Double.parseDouble(price), "No description");

        rideDetails.add(rideDet);
    }

    private String extractValue(Element element, String property){
        if(property.contains("property")){
            String attr = property.substring(property.indexOf("[")+1, property.indexOf("]"));
            return element.attr(attr);
        }
        else if (property.equals("text")){
            return element.text();
        }
        else return "null";
    }

    private String getRideId(Element element){
        Element rideId = element.select(properties.getProperty(RIDE_ID_SELECTOR)).first();
        return extractValue(rideId, properties.getProperty(RIDE_ID_CONTAINER));
    }

    private String getDepartureLocation(Element element){
        Element departureLocation = element.select(properties.getProperty(DEPARTURE_SELECTOR)).first();
        return extractValue(departureLocation, properties.getProperty(DEPARTURE_CONTAINER));
    }

    private String getArrivalLocation(Element element){
        Element arrivalLocation = element.select(properties.getProperty(ARRIVAL_SELECTOR)).first();
        return extractValue(arrivalLocation, properties.getProperty(ARRIVAL_CONTAINER));
    }

    private String getStartDate(Element element){
        Element startDate = element.select(properties.getProperty(DATE_OF_DEPARTURE_SELECTOR)).first();
        String dateOfDeparture = extractValue(startDate, properties.getProperty(DATE_OF_DEPARTURE_CONTAINER));
        DateTimeFormatter formatter = getFormatter(properties.getProperty(DATE_OF_DEPARTURE_FORMAT));

        return dateOfDeparture;
    }

    private String getEndDate(Element element){
        Element endDate = element.select(properties.getProperty(DATE_OF_ARRIVAL_SELECTOR)).first();
        String dateOfArrival = extractValue(endDate, properties.getProperty(DATE_OF_ARRIVAL_CONTAINER));
        DateTimeFormatter formatter=getFormatter(properties.getProperty(DATE_OF_ARRIVAL_FORMAT));

        return dateOfArrival;
    }

    private DateTimeFormatter getFormatter(String format){
        if(properties.getProperty(DATE_OF_DEPARTURE_FORMAT).equals("YYYY-MM-DDTHH:mm:SS")) {
            return DateTimeFormatter.ISO_LOCAL_DATE;
        }
        else return DateTimeFormatter.ISO_LOCAL_DATE;
    }

    private String getPrice(Element element){
        Element price = element.select(properties.getProperty(PRICE_SELECTOR)).first();
        String priceRaw = extractValue(price, properties.getProperty(PRICE_CONTAINER));
        priceRaw = priceRaw.replaceAll("[^0-9\\,\\.]", "");
        priceRaw = priceRaw.replace(properties.getProperty(PRICE_SEPARATOR), ".");
        Double result = Double.parseDouble(priceRaw);
        result/=Double.parseDouble(properties.getProperty(PRICE_DIVISION_FACTOR));
        return result.toString();
    }
}
