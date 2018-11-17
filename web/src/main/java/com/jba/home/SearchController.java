package com.jba.home;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class SearchController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;

    String rideBaseURL = "ride";

    @GetMapping("/search")
    public String search(Model model){
        String getAllRidesQuery = RestRequestBuilder
                .builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(getAllRidesQuery, String.class);

        Ride[] rides = Deserializer.getResultArrayFor(result, Ride[].class);

        model.addAttribute("rides", rides);

        return "search";
    }
}
