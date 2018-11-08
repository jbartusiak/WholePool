package com.jba.rest.controller;

import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersSearchHistory(
            @RequestParam(name = "userId", required = true) Integer userId
    ){
        return new WPLResponse<>(HttpStatus.OK, searchService.getUsersSearchHistory(userId), SearchHistory.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse registerNewSearch(
            @RequestParam(name="userId", required = true) Integer userId,
            @RequestBody(required = true) Search search
    ){
        return new WPLResponse<>(HttpStatus.CREATED, searchService.registerNewSearch(userId, search));
    }
}
