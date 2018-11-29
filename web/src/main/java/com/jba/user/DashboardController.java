package com.jba.user;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.user.enitity.User;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/user/dashboard")
public class DashboardController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;

    @Autowired
    Deserializer deserializer;

    @GetMapping
    public String getUserDashboard(HttpSession session, Model model){
        if(session.getAttribute("user")==null) {
            return "error/401";
        }

        User user = (User) session.getAttribute("user");

        RestTemplate template = new RestTemplate();

        String getUserOfferedRidesRequest = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam("ride")
                .addParam("userId", user.getUserId())
                .build();

        Ride[] rides = deserializer.getResultArrayFor(template.getForObject(getUserOfferedRidesRequest, String.class), Ride[].class);

        List<RideDetails> rideDetails = new ArrayList<>();

        for (Ride r: rides){
            String getRideDetailsRequest = RestRequestBuilder.builder(WPLRestURL)
                    .addPathParam("ride")
                    .addPathParam("details")
                    .addParam("rideId", r.getRideId())
                    .build();

            RideDetails temp = deserializer.getSingleItemFor(template.getForObject(getRideDetailsRequest, String.class), RideDetails.class);

            rideDetails.add(temp);
        }

        model.addAttribute("offeredRides", rideDetails);

        String getUserIncomingRides = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam("ride")
                .addPathParam("passenger")
                .addParam("userId", user.getUserId())
                .addParam("trim", false)
                .build();

        RideDetails[] details = deserializer.getResultArrayFor(template.getForObject(getUserIncomingRides, String.class), RideDetails[].class);

        if(details!=null) {
            List<RideDetails> rideDetailsList = Arrays.asList(details);
            rideDetailsList.sort(Comparator.comparing(RideDetails::getDateOfDeparture));
            model.addAttribute("incomingRides", rideDetailsList);
        }
        else
            model.addAttribute("incomingRides", new ArrayList<RideDetails>());

        return "dashboard";
    }
}
