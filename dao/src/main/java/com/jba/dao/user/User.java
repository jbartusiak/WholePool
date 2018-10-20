package com.jba.dao.user;

import lombok.Data;

import java.sql.Date;

@Data
public class User {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
}
