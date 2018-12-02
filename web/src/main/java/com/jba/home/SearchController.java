package com.jba.home;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        @RequestParam String inputHOD,
        Model model
    ){
        LocalDateTime localDateTime = LocalDateTime.parse(dateOfDeparture+"T"+inputHOD);

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

        Route[] routes = deserializer.getResultArrayFor(template.getForObject(findRouteQuery, String.class), Route[].class);

        if(routes.length==0){
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("message", "no-results");
            paramMap.put("searchFrom", searchFrom);
            paramMap.put("searchTo", searchTo);
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern("d MMMM, HH:mm:ss", new Locale("pl"));
            paramMap.put("dateTime", sdf.format(localDateTime));

            model.addAllAttributes(paramMap);

            return "search";
        }



        return "search";
    }
}
