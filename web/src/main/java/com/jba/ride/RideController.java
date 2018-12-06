package com.jba.ride;

import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.ride.enitity.RideDetails;
import com.jba.dao2.ride.enitity.RidePassangers;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
import com.jba.dao2.user.enitity.User;
import com.jba.ride.form.NewRideForm;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;

@Controller
public class RideController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLRestURL;
    @Value("${message.403}")
    String msg403;
    @Value("${message.404}")
    String msg404;

    @Autowired
    Deserializer deserializer;

    String rideBaseURL = "ride";

    @ModelAttribute("greeting")
    public NewRideForm getGreetingObject() {
        return new NewRideForm();
    }

    @GetMapping("/ride/{rideId}")
    public String viewRideDetails(@PathVariable String rideId, Model model){
        String getRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .addParam("rideId", rideId)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        try {
            String result = restTemplate.getForObject(getRideQuery, String.class);
            RideDetails ride = deserializer.getSingleItemFor(result, RideDetails.class);

            model.addAttribute("ride", ride);

            if(ride.getRideId().getSourceId().getSourceName().equals("localhost")) {
                String getRideOffererQuery = RestRequestBuilder.builder(WPLRestURL)
                        .addPathParam(rideBaseURL)
                        .addPathParam("offerer")
                        .addParam("rideId", ride.getRideId().getRideId())
                        .build();

                User offerer = deserializer.getSingleItemFor(restTemplate.getForObject(getRideOffererQuery, String.class), User.class);

                model.addAttribute("offerer", offerer);

                String getPassengersQuery = RestRequestBuilder.builder(WPLRestURL)
                        .addPathParam(rideBaseURL)
                        .addPathParam("passengers")
                        .addParam("rideId", ride.getRideId().getRideId())
                        .build();

                User[] passengers = deserializer.getResultArrayFor(restTemplate.getForObject(getPassengersQuery, String.class), User[].class);

                model.addAttribute("passengers", passengers);

                if(passengers!=null)
                    model.addAttribute("freeSeats", ride.getRideId().getNrOfSeats()-passengers.length);
                else
                    model.addAttribute("freeSeats", ride.getRideId().getNrOfSeats());

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

    @GetMapping("/ride/{rideId}/register")
    public String getRideRegister(@PathVariable String rideId, Model model, HttpSession session, RedirectAttributes redirectAttributes){

        if(session.getAttribute("user")==null){
            redirectAttributes.addAttribute("message", "registration-required");
            return "redirect:/register";
        }

        String getRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .addParam("rideId", rideId)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        RideDetails ride = deserializer.getSingleItemFor(restTemplate.getForObject(getRideQuery, String.class), RideDetails.class);

        model.addAttribute("ride", ride);

        return "ride-registration";
    }

    @GetMapping("/ride/{rideId}/resign")
    public String getRideResign(@PathVariable String rideId, Model model, HttpSession session, RedirectAttributes redirectAttributes){
        String getRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .addParam("rideId", rideId)
                .build();

        RestTemplate restTemplate = new RestTemplate();

        RideDetails ride = deserializer.getSingleItemFor(restTemplate.getForObject(getRideQuery, String.class), RideDetails.class);

        model.addAttribute("ride", ride);

        return "ride-resignation";
    }

    @PostMapping("/ride/resign")
    public String doRideResign(String ride, String passenger){
        String deleteRegistrationQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("register")
                .addParam("rideId", ride)
                .addParam("userId", passenger)
                .build();

        RestTemplate template = new RestTemplate();

        template.delete(deleteRegistrationQuery);

        return "redirect:/user/dashboard";
    }

    @PostMapping("/ride/register")
    public String doRideRegister(String ride, String passenger){
        String registerForRideRequest = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("register")
                .addParam("rideId", ride)
                .addParam("userId", passenger)
                .build();

        RestTemplate template = new RestTemplate();

        RidePassangers ridePassangers = deserializer.getSingleItemFor(template.postForObject(registerForRideRequest, null, String.class), RidePassangers.class);

        return "redirect:/user/dashboard";
    }

    @GetMapping("/ride/add")
    public String getNewRideView(HttpSession session, Model model, RedirectAttributes redirectAttributes){
        if(session.getAttribute("user")==null) {
            return "error/401";
        }

        User user = (User) session.getAttribute("user");

        if(!user.getUserType().getTypeName().equals("Kierowca")){
            redirectAttributes.addAttribute("message", "not-a-driver");
            return "redirect:/user/settings/accountType";
        }

        model.addAttribute("form", new NewRideForm());

        return "new-ride";
    }

    @PostMapping("/ride/add")
    public String addNewRide(@ModelAttribute NewRideForm form, HttpSession session, Model model){
        if(session.getAttribute("user")==null){
            return "401";
        }
        System.out.println(form);

        User userInSession = (User) session.getAttribute("user");

        RestTemplate template = new RestTemplate();

        Route route = new Route(form.getInputAddressFrom(), form.getInputAddressTo());

        String postRouteQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam("route")
                .build();

        route = deserializer.getSingleItemFor(template.postForObject(postRouteQuery, route, String.class), Route.class);

        Ride ride = new Ride(Source.of(1), route);
        ride.setNrOfSeats(form.getInputAvailableSpots());

        String postRideQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addParam("userId", userInSession.getUserId())
                .build();

        ride = deserializer.getSingleItemFor(template.postForObject(postRideQuery, ride, String.class), Ride.class);

        String departureDateTime = form.getInputDOD()+"T"+form.getInputHOD();
        String arrivalDateTime = form.getInputDOA()+"T"+form.getInputHOA();

        RideDetails rideDetails = new RideDetails(ride, LocalDateTime.parse(departureDateTime),
                LocalDateTime.parse(arrivalDateTime), 1, form.getInputPrice(), form.getInputDescription());

        String postRideDetailsQuery = RestRequestBuilder.builder(WPLRestURL)
                .addPathParam(rideBaseURL)
                .addPathParam("details")
                .addParam("rideId", ride.getRideId())
                .build();

        template.postForObject(postRideDetailsQuery, rideDetails, String.class);

        return "redirect:/";
    }
}
