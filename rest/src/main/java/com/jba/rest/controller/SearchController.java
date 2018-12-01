package com.jba.rest.controller;

import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.dao2.entity.WPLResponse;
import com.jba.service.ifs.SearchService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @ApiOperation(value = "Get users search history", notes = "Fetches users search history from database.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersSearchHistory(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name = "userId", required = true) Integer userId
    ){
        return new WPLResponse<>(HttpStatus.OK, searchService.getUsersSearchHistory(userId), SearchHistory.class);
    }

    @ApiOperation(value = "Add a new search", notes = "Adds a new search to the search history.",
            consumes = "application/json")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse registerNewSearch(
            @ApiParam(name = "userId", value="An Integer referring to User entity", required = true,
                    type = "Integer")
            @RequestParam(name="userId", required = true) Integer userId,
            @ApiParam(name = "search", value="A JSON representing the Search entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Search search
    ){
        return new WPLResponse<>(HttpStatus.CREATED, searchService.registerNewSearch(userId, search));
    }

    @ApiOperation(value = "Find route", notes="Searches for a route with given criteria")
    @GetMapping("/route")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse findRoute(
            @RequestParam(name="fromLocation", required = true) String fromLoc,
            @RequestParam(name="toLocation", required = true) String toLoc
    ){
        return new WPLResponse<>(HttpStatus.CREATED, searchService.findRouteByCriteria(fromLoc, toLoc), Route.class);
    }
}
