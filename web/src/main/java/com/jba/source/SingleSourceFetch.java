package com.jba.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
import com.jba.dao2.user.enitity.User;
import com.jba.source.exception.MissingPropertiesException;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Collects data from external sources, and parses them as needed.
 */

@Service
public abstract class SingleSourceFetch {

    protected Logger logger = Logger.getLogger(getClass());

    protected String wholepoolRestBaseURL = "http://localhost:5000/api";

    @Autowired
    protected ObjectMapper mapper;

    //https://api.dosiadam.pl/api/v1/search/rides?departure=%API_DEPARTURE%&arrival=%API_ARRIVAL%&departureDateFrom=%API_DATE_OF_DEPARTURE%

    protected final static String DEPARTURE_PLACEHOLDER="%API_DEPARTURE%", ARRIVAL_PLACEHOLDER = "%API_ARRIVAL%", DATE_OF_DEPARTURE_PLACEHOLDER="%API_DATE_OF_DEPARTURE%";

    protected boolean debug;

    protected String exampleValue;

    protected List<Ride> rides;
    protected List<RideDetails> rideDetails;

    protected Source definition;
    protected HttpMethod method;

    protected Properties properties;

    protected String searchBaseUrl;

    @Autowired
    protected Deserializer deserializer;

    public SingleSourceFetch(){
        rides = new ArrayList<>();
        rideDetails = new ArrayList<>();
        properties = new Properties();

        deserializer = new Deserializer();
    }

    public abstract String getResultsForQuery(String from, String to);

    public void setDefinition(Source source) throws MissingPropertiesException{
        this.definition=source;

        try {
            properties.load(new StringReader(source.getResultsParseRules()));
        }
        catch (IOException e){
            throw new MissingPropertiesException("Properties malformed, or IOException occured.", e);
        }

        switch (properties.getProperty("method")){
            case "get": method = HttpMethod.GET; break;
            case "post": method = HttpMethod.POST; break;
            default: throw new MissingPropertiesException("No property found for key 'method' in properties from DB "+source.toString());
        }

        searchBaseUrl= definition.getSearchBaseUrl();
    }

    public abstract void doImplementationSpecificInitialization();

    public abstract void search(String from, String to, String dateOfDeparture, String dateOfArrival);

    public abstract void parse(String input, String from, String to);

    protected void saveToDB(String from, String to){
        String findRouteQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("search")
                .addPathParam("route")
                .addParam("fromLocation", from)
                .addParam("toLocation", to)
                .build();

        RestTemplate template = new RestTemplate();

        Route[] res = deserializer.getResultArrayFor(template.getForObject(findRouteQuery, String.class), Route[].class);
        List<Route> routes = Arrays.stream(res).collect(Collectors.toList());

        if(routes.size()==0){
            String postNewRouteQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("route")
                    .build();

            Route routeToPost = new Route(from, to);

            routeToPost = deserializer.getSingleItemFor(template.postForObject(postNewRouteQuery, routeToPost, String.class), Route.class);

            routes.clear();
            routes.add(routeToPost);
        }

        String postRideQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                .addPathParam("ride")
                .addParam("userId", User.of(definition.getSourceId()))
                .build();


        for(Ride ride:rides) {
            ride = deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);
        }

        for(int i=0; i<rideDetails.size(); i++) {
            String postRideDetailsQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addPathParam("details")
                    .addParam("rideId", rides.get(i).getRideId())
                    .build();

            rideDetails.set(i, deserializer.getSingleItemFor(template.postForObject(postRideDetailsQuery, rideDetails, String.class), RideDetails.class));
        }
    }
}
