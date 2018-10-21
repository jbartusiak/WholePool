package com.jba.dao.user.enitity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="User")
@NoArgsConstructor
@Data
public class User {

    @Id
    @Column(name="pk_user_id")
    private int userId;

    @OneToMany(mappedBy = "pk_type_id")
    private int userType;

    private String emailAddress;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String userName;
}
