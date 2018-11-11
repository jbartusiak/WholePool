package com.jba.rest.controller;

import com.jba.dao2.source.entity.Source;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getSource(
            @RequestParam(value = "name", required = false) String sourceName
    ){
        if(sourceName==null)
            return new WPLResponse<>(HttpStatus.OK, sourceService.getAllSources());
        else
            return new WPLResponse<>(HttpStatus.OK, sourceService.getSourceByName(sourceName));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addSource(
            @RequestBody(required = true) Source source
    ){
        return new WPLResponse<>(HttpStatus.CREATED, sourceService.addSource(source));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse editSource(
            @RequestBody(required = true) Source source
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, sourceService.editSource(source));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSource(
            @RequestParam(required = true) Integer sourceId
    ){
        sourceService.deleteSource(Source.of(sourceId));
    }
}