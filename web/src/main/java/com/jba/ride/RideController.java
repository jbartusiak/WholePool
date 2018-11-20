package com.jba.ride;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class RideController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;

    String rideBaseURL = "ride";

    @GetMapping("/ride/{rideId}")
    public String viewRideDetails(@PathVariable String rideId, Model model){
        String getRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addParam("rideId", rideId)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(getRideQuery, String.class);

        Ride ride = Deserializer.getSingleItemFor(result, Ride.class);

        model.addAttribute("ride", ride);
        return "ride-details";
    }
}
