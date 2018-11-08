package com.jba.entity;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class WPLResponse<TYPE> {
    private static SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    private int statusCode;
    private String message;
    private String timestamp;
    private String type;
    private TYPE result;

    public WPLResponse(HttpStatus status, TYPE result){
        statusCode=status.value();
        message=status.getReasonPhrase();
        timestamp=formatter.format(new Date());
        type = result.getClass().getSimpleName();
        this.result=result;
    }

    public WPLResponse(HttpStatus status, TYPE result, Class objectsContainedInArray){
        statusCode=status.value();
        message=status.getReasonPhrase();
        timestamp=formatter.format(new Date());
        if(result.getClass().getSimpleName().contains("List")){
            type = "List<"+objectsContainedInArray.getSimpleName()+">";
        }
        else if(result.getClass().getSimpleName().contains("Set")){
            type = "Set<"+objectsContainedInArray.getSimpleName()+">";
        }
        else if(result.getClass().getSimpleName().contains("Map")){
            type = "Map<"+objectsContainedInArray.getSimpleName()+">";
        }
        else type = result.getClass().getSimpleName();
        this.result=result;
    }
}
