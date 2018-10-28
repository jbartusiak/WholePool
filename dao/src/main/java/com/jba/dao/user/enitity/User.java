package com.jba.dao.user.enitity;

import com.jba.dao.blocked.entity.BlockedUsers;
import com.jba.dao.ride.enitity.RidePassangers;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="User")
@Data
public class User {

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_user_id")
    private int userId;

    @ManyToOne
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

    @OneToMany(mappedBy = "blockedBy")
    @ToString.Exclude
    private Set<BlockedUsers> blockedByThisUser;

    @OneToMany(mappedBy = "passenger")
    @ToString.Exclude
    private Set<RidePassangers> isPassengerAtRides;
}