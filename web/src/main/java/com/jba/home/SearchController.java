package com.jba.home;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
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

import java.util.Arrays;
import java.util.List;

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


        return "search";
    }
}
