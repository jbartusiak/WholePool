package com.jba.rest.controller;

import com.jba.entity.WPLResponse;
import com.jba.service.ifs.PreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preference")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getPreferences(
            @RequestParam(name = "id", required = false) Integer id
    ){
        if(id!=null)
            return new WPLResponse<>(HttpStatus.OK, preferenceService.getPreferenceById(id));
        else return new WPLResponse<>(HttpStatus.OK,preferenceService.getAllPreferences());
    }

}
