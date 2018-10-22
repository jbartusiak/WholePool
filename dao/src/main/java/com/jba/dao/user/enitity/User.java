package com.jba.dao.user.enitity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="User")
@Data
public class User {

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_user_id")
    private int userId;

    @OneToOne
    @JoinColumn(name="fk_user_type_id")
    private UserType userType;

    @Column(name="user_email_address")
    private String emailAddress;

    @Column(name="user_password_hash")
    private String passwordHash;

    @Column(name="user_first_name")
    private String firstName;

    @Column(name="user_last_name")
    private String lastName;

    @Temporal(TemporalType.DATE)
    @Column(name="user_date_of_birth")
    private Date dateOfBirth;

    @Column(name="user_name")
    private String userName;
}