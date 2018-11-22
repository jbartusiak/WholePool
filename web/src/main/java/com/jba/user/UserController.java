package com.jba.user;

import com.jba.dao2.cars.entity.Car;
import com.jba.dao2.cars.entity.CarType;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import com.jba.dao2.user.enitity.UserType;
import com.jba.utils.Deserializer;
import com.jba.utils.RestRequestBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    UserDAO userDAO;

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

    @PostMapping("/settings/data")
    public String updateUserData(User user, HttpSession session, RedirectAttributes redirectAttributes){
        User userFromSession = (User) session.getAttribute("user");

        logger.debug("User start values: "+userFromSession);
        logger.debug("User from form: "+user);

        if(!userFromSession.getUserName().equals(user.getUserName()))
            userFromSession.setUserName(user.getUserName());
        if(!userFromSession.getFirstName().equals(user.getFirstName()))
            userFromSession.setFirstName(user.getFirstName());
        if(!userFromSession.getLastName().equals(user.getLastName()))
            userFromSession.setLastName(user.getLastName());
        if(!userFromSession.getEmailAddress().equals(user.getEmailAddress()))
            userFromSession.setEmailAddress(user.getEmailAddress());
        if(!userFromSession.getDateOfBirth().equals(user.getDateOfBirth()))
            userFromSession.setDateOfBirth(user.getDateOfBirth());

        logger.debug("User data changed to: "+userFromSession);

        String updateUserQuery = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("users")
                .build();

        RestTemplate template = new RestTemplate();
        template.put(updateUserQuery, userFromSession);
        logger.info("Updated user data successfully");

        String getUserQuery = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("users")
                .addParam("id", userFromSession.getUserId())
                .build();

        userFromSession = Deserializer.getSingleItemFor(template.getForObject(getUserQuery, String.class), User.class);

        logger.info("Updating user data in session to "+userFromSession);

        session.removeAttribute("user");
        session.setAttribute("user", userFromSession);

        redirectAttributes.addAttribute("message", "Twoje dane zostały zaktualizowane.");

        return "redirect:/user/settings/confirm";
    }

    @GetMapping("/settings/accountType")
    public String getAccountType(Model model){
        String getUserTypesRequest = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("users")
                .addPathParam("type")
                .build();

        RestTemplate template= new RestTemplate();

        UserType[] userTypes = Deserializer.getResultArrayFor(template.getForObject(getUserTypesRequest, String.class), UserType[].class);

        List<UserType> filtered = Arrays.stream(userTypes)
                .filter(userType -> userType.getTypeName().equals("Pasażer")||userType.getTypeName().equals("Kierowca"))
                .sorted(Comparator.comparing(UserType::getTypeId))
                .collect(Collectors.toList());

        model.addAttribute("userTypes", filtered);

        filtered.forEach(
                userType -> logger.debug(userType + "retrieved.")
        );

        return "user-settings-accountType";
    }

    @GetMapping("/settings/changePassword")
    public String getChangePassword(){
        return "user-settings-changePassword";
    }

    @PostMapping("/settings/changePassword")
    public String updatePassword(RedirectAttributes redirectAttributes){
        return "redirect:/user/settings/confirm";
    }

    @GetMapping("/settings/myCars")
    public String getCars(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");

        String getCarsQuery = RestRequestBuilder
                .builder(WPLBaseURL)
                .addPathParam("cars")
                .addParam("userId", user.getUserId())
                .build();

        RestTemplate template = new RestTemplate();

        Car[] cars = Deserializer.getResultArrayFor(template.getForObject(getCarsQuery, String.class), Car[].class);

        model.addAttribute("cars", cars);

        return "user-settings-myCars";
    }

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
