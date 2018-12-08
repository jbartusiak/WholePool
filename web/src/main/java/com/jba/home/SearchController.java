package com.jba.home;

import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.source.SourceRepository;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;

    String rideBaseURL = "ride";

    @Autowired
    Deserializer deserializer;

    @Autowired
    SourceRepository repository;

    @Value("${wholepool.search.wait.time}")
    Integer waitTime;

    @GetMapping("/search")
    public String search(Model model){
        String getAllRidesQuery = RestRequestBuilder
                .builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .build();

        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(getAllRidesQuery, String.class);

        RideDetails[] rides = deserializer.getResultArrayFor(result, RideDetails[].class);

        List<RideDetails> rideDetailsList = Arrays.asList(rides);

        rideDetailsList = rideDetailsList.stream()
                .filter(rideDetails -> rideDetails.getDateOfDeparture().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparingInt(o -> o.getRideId().getSourceId().getSourceId()))
                .collect(Collectors.toList());

        model.addAttribute("rides", rideDetailsList.toArray());

        return "search";
    }

    @GetMapping("/search/rides")
    public String doSearch(
        @RequestParam String searchFrom,
        @RequestParam String searchTo,
        @RequestParam String dateOfDeparture,
        @RequestParam Optional<String> dateOfArrival,
        @RequestParam String inputHOD,
        Model model
    ){
        LocalDateTime localDateTime = null;
        if(dateOfDeparture!=null) {
            if(inputHOD!=null)
                localDateTime = LocalDateTime.parse(dateOfDeparture + "T" + inputHOD);
            else
                localDateTime = LocalDateTime.parse(dateOfDeparture + "T00:00:00");
        }

        if(searchFrom.endsWith(", Polska"))
            searchFrom = searchFrom.substring(0, searchFrom.lastIndexOf(","));
        if(searchTo.endsWith(", Polska"))
            searchTo = searchTo.substring(0, searchTo.lastIndexOf(","));

        repository.searchInSources(searchFrom, searchTo, dateOfDeparture, "9999-12-01T23:59:59");

        RestTemplate template = new RestTemplate();

        String findRidesQuery;

        if(dateOfDeparture!=null&&dateOfArrival.isPresent()){
            findRidesQuery = RestRequestBuilder.builder(WPLRestURL)
                    .addPathParam("ride")
                    .addPathParam("find")
                    .addParam("routeFrom", searchFrom)
                    .addParam("routeTo", searchTo)
                    .addParam("dateOfDeparture", localDateTime.toString())
                    .addParam("dateOfArrival", dateOfArrival.toString())
                    .build();
        }
        else if (dateOfDeparture!=null){
            findRidesQuery = RestRequestBuilder.builder(WPLRestURL)
                    .addPathParam("ride")
                    .addPathParam("find")
                    .addParam("routeFrom", searchFrom)
                    .addParam("routeTo", searchTo)
                    .addParam("dateOfDeparture", localDateTime.toString())
                    .build();
        }
        else if (dateOfArrival.isPresent()){
            findRidesQuery = RestRequestBuilder.builder(WPLRestURL)
                    .addPathParam("ride")
                    .addPathParam("find")
                    .addParam("routeFrom", searchFrom)
                    .addParam("routeTo", searchTo)
                    .addParam("dateOfArrival", dateOfArrival.toString())
                    .build();
        }
        else{
            findRidesQuery = RestRequestBuilder.builder(WPLRestURL)
                    .addPathParam("ride")
                    .addPathParam("find")
                    .addParam("routeFrom", searchFrom)
                    .addParam("routeTo", searchTo)
                    .build();
        }

        try{
            Thread.sleep(waitTime);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        RideDetails[] rideDetails = deserializer.getResultArrayFor(template.getForObject(findRidesQuery, String.class), RideDetails[].class);

        List<RideDetails> rideDetailsList = Arrays.asList(rideDetails);

        rideDetailsList = rideDetailsList.stream()
                .filter(rd -> rd.getDateOfDeparture().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparingInt(o -> o.getRideId().getSourceId().getSourceId()))
                .collect(Collectors.toList());

        if(rideDetails.length==0){
            model.addAllAttributes(getNoResultsMap(searchFrom, searchTo, localDateTime));

            return "search";
        }

        model.addAttribute("rides", rideDetailsList.toArray());

        return "search";
    }

    private Map<String, String> getNoResultsMap(String searchFrom, String searchTo, LocalDateTime localDateTime){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("message", "no-results");
        paramMap.put("searchFrom", searchFrom);
        paramMap.put("searchTo", searchTo);
        if(localDateTime!=null) {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("d MMMM, HH:mm:ss", new Locale("pl"));
            paramMap.put("dateTime", sdf.format(localDateTime));
        }
        return paramMap;
    }
}
