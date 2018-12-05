package com.jba.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.utils.RestRequestBuilder;
import org.jsoup.nodes.Element;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@EnableAsync
public class DosiadamRestFetcher extends AbstractSourceFetch {
    private String container, innerContainer;

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
        searchString=searchString.replace(DEPARTURE_PLACEHOLDER, from);
        searchString=searchString.replace(ARRIVAL_PLACEHOLDER, to);
        searchString=searchString.replace(DATE_OF_DEPARTURE_PLACEHOLDER, dateOfDeparture);

        RestTemplate template = new RestTemplate();

        String result = template.getForObject(searchString, String.class);

        parse(result, from, to);
    }

    @Override
    public void parse(String input, String from, String to) {
        try {
            JsonNode actualObj = mapper.readTree(input);

            JsonNode array = actualObj.get("data");

            //go through every result
            array.forEach(jsonNode -> parseAsIndividual(jsonNode, from, to));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void parseIndividual(Element element, String from, String to) {

    }

    @Async("sourceTaskExecutor")
    public void parseAsIndividual(JsonNode node, String from, String to){
        try {
            String rideId = node.get("rideId").asText();
            String dateOfDeparture = node.get("departureAt").asText();
            String price = node.get("priceAmount").asText();
            AtomicReference<String> departureLocation = new AtomicReference<>(node.get("departureLocation").get("name").asText());
            AtomicReference<String> arrivalLocation = new AtomicReference<>(node.get("arrivalLocation").get("name").asText());

            RestTemplate template = new RestTemplate();

            String checkIsRidePresentQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addParam("directLink", "https://dosiadam.pl/ride/"+rideId)
                    .build();

            Ride[] ridesAvailable = deserializer.getResultArrayFor(template.getForObject(checkIsRidePresentQuery, String.class), Ride[].class);

            if(ridesAvailable.length>0){
                logger.info("Ride of URL "+"https://dosiadam.pl/ride/"+rideId+" already in DB. Skipping...");
                return;
            }

            //from/to differs
            if (!departureLocation.get().contains(from) || !arrivalLocation.get().contains(to)) {
                //check stopovers
                node.get("stopovers").forEach(stopover -> {
                    if (stopover.get("location").get("name").textValue().contains(from)) {
                        departureLocation.set(stopover.get("location").get("name").textValue());
                    }
                    if (stopover.get("location").get("name").textValue().contains(to)) {
                        arrivalLocation.set(stopover.get("location").get("name").textValue());
                    }
                });
            }



            String findRouteQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("search")
                    .addPathParam("route")
                    .addParam("fromLocation", departureLocation.get())
                    .addParam("toLocation", arrivalLocation.get())
                    .build();



            Route[] r = deserializer.getResultArrayFor(template.getForObject(findRouteQuery, String.class), Route[].class);
            List<Route> routes = Arrays.asList(r);

            if (routes.size() == 0) {
                Route newRoute = new Route(departureLocation.get(), arrivalLocation.get());

                String postNewRoute = RestRequestBuilder.builder(wholepoolRestBaseURL)
                        .addPathParam("route").build();

                newRoute = deserializer.getSingleItemFor(template.postForObject(postNewRoute, newRoute, String.class), Route.class);
                routes = new ArrayList<>();
                routes.add(newRoute);
            }

            Ride ride = new Ride(definition, routes.get(0));
            ride.setNrOfSeats(-1);
            ride.setDirectURL("https://dosiadam.pl/ride/" + rideId);

            String postRideQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addParam("userId", 1)
                    .build();

            ride = deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);

            dateOfDeparture = dateOfDeparture.substring(0, dateOfDeparture.lastIndexOf("+"));

            RideDetails rideDetails = new RideDetails(ride, LocalDateTime.parse(dateOfDeparture),
                    LocalDateTime.parse("9999-12-01T23:59:59"), 1, Double.parseDouble(price) / 100, "No description");

            String postRideDetailsQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addPathParam("details")
                    .addParam("rideId", ride.getRideId())
                    .build();

            template.postForObject(postRideDetailsQuery, rideDetails, String.class);
        }
        catch (Exception e){
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }
}
