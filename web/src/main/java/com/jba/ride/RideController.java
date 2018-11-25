package com.jba.ride;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
import com.jba.dao2.user.enitity.User;
import com.jba.ride.form.NewRideForm;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDateTime;

@Controller
public class RideController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;
    @Value("${message.403}")
    String msg403;
    @Value("${message.404}")
    String msg404;

    String rideBaseURL = "ride";

    @ModelAttribute("greeting")
    public NewRideForm getGreetingObject() {
        return new NewRideForm();
    }

    @GetMapping("/ride/{rideId}")
    public String viewRideDetails(@PathVariable String rideId, Model model){
        String getRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addParam("rideId", rideId)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        try {
            String result = restTemplate.getForObject(getRideQuery, String.class);
            Ride ride = Deserializer.getSingleItemFor(result, Ride.class);

            model.addAttribute("ride", ride);


            if(ride.getSourceId().getSourceName().equals("localhost")) {

                return "ride-details";
            }
            else{
                return "ride-details-off-wholepool";
            }
        }
        catch (Exception e){
            model.addAttribute("status", 404);
            model.addAttribute("message", "Przejazd o numerze "+rideId+" nie został odnaleziony.");
            return "error";
        }
    }

    @GetMapping("/ride/add")
    public String getNewRideView(HttpSession session, Model model){

        /*if(session.getAttribute("user")==null) {
            model.addAttribute("status", 403);
            model.addAttribute("message", msg403);
            return "error";
        }*/

        model.addAttribute("form", new NewRideForm());

        return "new-ride";
    }

    @PostMapping("/ride/add")
    public String addNewRide(@ModelAttribute NewRideForm form, HttpSession session, Model model){
        /*if(session.getAttribute("user")==null){
            model.addAttribute("status", 403);
            model.addAttribute("message", msg403);
            return "error";
        }*/
        System.out.println(form);

        User userInSession = (User) session.getAttribute("user");

        RestTemplate template = new RestTemplate();

        Route route = new Route(form.getInputAddressFrom(), form.getInputAddressTo());

        String postRouteQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam("route")
                .build();

        route = Deserializer.getSingleItemFor(template.postForObject(postRouteQuery, route, String.class), Route.class);

        Ride ride = new Ride(Source.of(1), route);
        ride.setNrOfSeats(form.getInputAvailableSpots());

        String postRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addParam("userId", userInSession.getUserId())
                .build();

        ride = Deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);

        String departureDateTime = form.getInputDOD()+"T"+form.getInputHOD();
        String arrivalDateTime = form.getInputDOA()+"T"+form.getInputHOA();

        RideDetails rideDetails = new RideDetails(ride, LocalDateTime.parse(departureDateTime),
                LocalDateTime.parse(arrivalDateTime), 1, form.getInputPrice(), form.getInputDescription());

        //TODO: FIX
        /*ride.setRideDetails(rideDetails);

        String postRideDetailsQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .addParam("rideId", ride.getRideId())
                .build();

        template.postForObject(postRideDetailsQuery, rideDetails, String.class);*/

        return "redirect:/";
    }
}
