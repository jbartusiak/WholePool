package com.jba.rest.controller;

import com.jba.entity.WPLResponse;
import com.jba.service.ifs.PreferenceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preference")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @ApiOperation(value = "Get preference(s)", notes = "Gets preferences available in the system. If optional " +
            "preferenceId parameter is given, returns details for that single preference.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getPreferences(
            @ApiParam(name = "id", value="An Integer referring to Preference entity", required = false,
                    type = "Integer")
            @RequestParam(name = "id", required = false) Integer id
    ){
        if(id!=null)
            return new WPLResponse<>(HttpStatus.OK, preferenceService.getPreferenceById(id));
        else return new WPLResponse<>(HttpStatus.OK,preferenceService.getAllPreferences());
    }

}
