package com.jba.rest.controller;

import com.jba.dao2.route.entity.Route;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
public class RouteController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getRoute(
            @RequestParam(name = "routeId", required = false) Integer routeId
    ){
        if(routeId!=null)
            return new WPLResponse<>(HttpStatus.OK, searchService.getRouteById(routeId));
        else return new WPLResponse<>(HttpStatus.OK,searchService.getAllRoutes());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRoute(
            @RequestBody(required = true) Route route
    ){
        return new WPLResponse<>(HttpStatus.CREATED, searchService.addRoute(route));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse editRoute(
            @RequestBody(required = true) Route route
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, searchService.updateRoute(route));
    }
}
