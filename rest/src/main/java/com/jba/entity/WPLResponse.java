package com.jba.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
public class WPLResponse<TYPE> {
    private int statusCode;
    private String message;
    private Date timestamp;
    private TYPE objectAffected;
}
