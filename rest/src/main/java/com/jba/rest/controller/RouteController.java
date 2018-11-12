package com.jba.rest.controller;

import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.entity.WPLResponse;
import com.jba.rest.exception.SearchCriteriaNotSpecifiedException;
import com.jba.service.ifs.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
public class RouteController {
    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "Get route(s)", notes = "Fetches route(s) from the database. If optional routeId parameter " +
            "is given, fetches details for route by that id.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getRoute(
            @ApiParam(name = "routeId", value="An Integer referring to Route entity", required = false,
                    type = "Integer")
            @RequestParam(name = "routeId", required = false) Integer routeId
    ){
        if(routeId!=null)
            return new WPLResponse<>(HttpStatus.OK, searchService.getRouteById(routeId));
        else return new WPLResponse<>(HttpStatus.OK,searchService.getAllRoutes(), Route.class);
    }

    @ApiOperation(value = "Add route", notes = "Adds a new route to the system.", consumes = "application/json")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addRoute(
            @ApiParam(name = "route", value="A JSON representing the Route entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Route route
    ){
        return new WPLResponse<>(HttpStatus.CREATED, searchService.addRoute(route));
    }

    @ApiOperation(value = "Update a route", notes = "Updates a route in the system.", consumes = "application/json")
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse editRoute(
            @ApiParam(name = "route", value="A JSON representing the Route entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Route route
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, searchService.updateRoute(route));
    }

    @ApiOperation(value = "Get popular route(s)", notes = "Gets popular routes. If routeId is given, fetches details " +
            "for that route only. Otherwise, fetches all popular routes available in the system.")
    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getPopularRoute(
            @ApiParam(name = "routeId", value="An Integer referring to Route entity", required = false,
                    type = "Integer")
            @RequestParam(name="routeId", required = false) Integer routeId
    ) throws SearchCriteriaNotSpecifiedException
    {
        if(routeId==null)
            return new WPLResponse<>(HttpStatus.OK, searchService.getAllPopularRoutes(), PopularRoute.class);
        else return new WPLResponse<>(HttpStatus.OK, searchService.getPopularRouteById(routeId));
    }

    @ApiOperation(value = "Update popular route", notes = "Updates a popular route in the system.",
            consumes = "application/json")
    @PutMapping("/popular")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse updatePopularRoute(
            @ApiParam(name = "route", value="A JSON representing the PopularRoute entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) PopularRoute popularRoute
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, searchService.updatePopularRoute(popularRoute.getRideId().getRouteId(), popularRoute));
    }
}
