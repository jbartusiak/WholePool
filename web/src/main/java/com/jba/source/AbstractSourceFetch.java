package com.jba.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaunt.UserAgent;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
import com.jba.dao2.user.enitity.User;
import com.jba.source.exception.MissingPropertiesException;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
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
public abstract class AbstractSourceFetch {

    protected Logger logger = Logger.getLogger(getClass());

    @Value("${wholepool.rest.url.base.url}")
    protected String wholepoolRestBaseURL;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected UserAgent agent;

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

    public AbstractSourceFetch(){
        rides = new ArrayList<>();
        rideDetails = new ArrayList<>();
        properties = new Properties();
    }

    public abstract String getResultsForQuery(String from, String to);


    public void setDefinition(Source source) throws MissingPropertiesException{
        this.definition=source;

        try {
            logger.info("Loading preferences for source "+source.getSourceName());
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

    @Async("sourceTaskExecutor")
    public abstract void search(String from, String to, String dateOfDeparture, String dateOfArrival);

    public abstract void parse(String input, String from, String to);

    public abstract void parseIndividual(Element element, String from, String to);

    protected void saveToDB(){
        if(rides.size()!=rideDetails.size()){
            throw new IllegalArgumentException("Rides and ride details should be equal, something wrong happened while parsing results.");
        }

        RestTemplate template = new RestTemplate();

        for(int i=0; i<rides.size(); i++){
            Ride ride = rides.get(i);
            RideDetails details = rideDetails.get(i);

            String postRideQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addParam("userId", definition.getSourceId())
                    .build();

            ride = deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);

            details.setRideId(ride);

            String postRideDetailsQuery = RestRequestBuilder.builder(wholepoolRestBaseURL)
                    .addPathParam("ride")
                    .addPathParam("details")
                    .addParam("rideId", ride.getRideId())
                    .build();

            template.postForObject(postRideDetailsQuery, details, String.class);

            logger.info("Ride "+ride.getRideId()+" created successfully!");
        }
    }

    @Bean(name="sourceTaskExecutor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(12);
        executor.setThreadNamePrefix("wholepool-sourcer");
        executor.initialize();
        return executor;
    }


}
