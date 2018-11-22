package com.jba.user;

import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.enitity.User;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${wholepool.rest.url.base.url}")
    String WPLBaseURL;

    @GetMapping("/settings")
    public String setting(){
        return "user-settings";
    }

    @GetMapping("/settings/preferences")
    public String getPreferencesSettings(){return "user-settings-preferences";}

    @GetMapping("/settings/data")
    public String getDataSettings(){
        return "user-settings-data";
    }

    @GetMapping("/settings/accountType")
    public String getAccountType(){return "user-settings-accountType";}

    @GetMapping("/settings/changePassword")
    public String getChangePassword(){
        return "user-settings-changePassword";
    }

    @GetMapping("/settings/myCars")
    public String getCars(){return "user-settings-myCars";}

    @GetMapping("/settings/addCar")
    public String getAddCar(Model model){
        String getCarTypesQuery = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("cars")
                .addPathParam("type")
                .build();

        RestTemplate template = new RestTemplate();

        CarType[] carTypes = Deserializer.getResultArrayFor(template.getForObject(getCarTypesQuery, String.class), CarType[].class);

        model.addAttribute("carTypes", carTypes);

        return "user-settings-addCar";
    }

    @PostMapping("/settings/addCar")
    public String addCar(Car car, HttpSession session, RedirectAttributes redirectAttributes){
        car.toString();

        car.setCarTypeId(CarType.of(Integer.parseInt(car.getCarTypeId().getCarTypeName())));

        User userInSession = (User) session.getAttribute("user");

        String postNewCarQuery = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("cars")
                .addPathParam("user")
                .addParam("userId", userInSession.getUserId())
                .build();

        RestTemplate restTemplate = new RestTemplate();

        Car result = Deserializer.getSingleItemFor(restTemplate.postForObject(postNewCarQuery, car, String.class), Car.class);

        redirectAttributes.addAttribute("message", "Auto o nr rejestracyjnym "+car.getCarRegistrationNumber()+" zostało zapisane");

        return "redirect:/user/settings/confirm";
    }

    @GetMapping("/rides")
    public String getUsersRides(){
        return "user-rides";
    }

    @GetMapping("/searchHistory")
    public String getUserSearchHistory(){
        return "user-search-history";
    }

    @GetMapping("/settings/confirm")
    public String getConfirmation(@RequestParam("message") String message, Model model){
        model.addAttribute("message", message);
        return "user-settings-confirm";
    }
}
