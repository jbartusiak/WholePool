package com.jba.rest.controller;

import com.jba.dao2.source.entity.Source;
import com.jba.dao2.entity.WPLResponse;
import com.jba.service.ifs.SourceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/source")
public class SourceController {

    @Autowired
    private SourceService sourceService;

    @ApiOperation(value = "Get source(s)", notes = "Fetches source(s) from the database. If optional parameter " +
            "name is given, fetches details for that single source. If parameter is not provided, fetches all " +
            "sources.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getSource(
            @ApiParam(name = "name", value="A String referring to Source entity", required = false,
                    type = "String")
            @RequestParam(value = "name", required = false) String sourceName
    ){
        if(sourceName==null)
            return new WPLResponse<>(HttpStatus.OK, sourceService.getAllSources());
        else
            return new WPLResponse<>(HttpStatus.OK, sourceService.getSourceByName(sourceName));
    }

    @ApiOperation(value = "Add a new source", notes = "Adds a new source to the database", consumes = "application/json")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addSource(
            @ApiParam(name = "source", value="A JSON representing the Source entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Source source
    ){
        return new WPLResponse<>(HttpStatus.CREATED, sourceService.addSource(source));
    }

    @ApiOperation(value = "Updates a source", notes = "Updates a source in the database", consumes = "application/json")
    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public WPLResponse editSource(
            @ApiParam(name = "source", value="A JSON representing the Source entity", required = true,
                    type = "application/json")
            @RequestBody(required = true) Source source
    ){
        return new WPLResponse<>(HttpStatus.ACCEPTED, sourceService.editSource(source));
    }

    @ApiOperation(value = "Delete a source", notes = "Deletes a source from the database")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSource(
            @ApiParam(name = "sourceId", value="An Integer referring to Source entity", required = true,
                    type = "Integer")
            @RequestParam(required = true) Integer sourceId
    ){
        sourceService.deleteSource(Source.of(sourceId));
    }
}
