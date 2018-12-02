package com.jba.home;

import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Controller
public class SearchController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;

    String rideBaseURL = "ride";

    @Autowired
    Deserializer deserializer;

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

        model.addAttribute("rides", rides);

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

        String findRouteQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam("search")
                .addPathParam("route")
                .addParam("fromLocation", searchFrom)
                .addParam("toLocation", searchTo)
                .build();

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

        RideDetails[] rideDetails = deserializer.getResultArrayFor(template.getForObject(findRidesQuery, String.class), RideDetails[].class);

        if(rideDetails.length==0){
            model.addAllAttributes(getNoResultsMap(searchFrom, searchTo, localDateTime));

            return "search";
        }

        model.addAttribute("rides", rideDetails);

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
