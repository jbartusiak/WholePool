package com.jba.dao2.user.enitity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.ride.enitity.RidePassangers;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name="User")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_user_id")
    private int userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name="fk_user_type_id")
    private UserType userType;

    @Column(name="user_email_address")
    @NonNull
    private String emailAddress;

    @Column(name="user_password_hash")
    @NonNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;

    @Column(name="user_first_name")
    @NonNull
    private String firstName;

    @Column(name="user_last_name")
    @NonNull
    private String lastName;

    @Column(name="user_date_of_birth")
    @NonNull
    private Date dateOfBirth;

    @Column(name="user_name")
    @NonNull
    private String userName;

    @OneToMany(mappedBy = "blockedBy")
    @ToString.Exclude
    @JsonIgnore
    private Set<BlockedUsers> blockedByThisUser;

    @OneToMany(mappedBy = "passenger")
    @ToString.Exclude
    @JsonIgnore
    private Set<RidePassangers> isPassengerAtRides;

    public static User of(int id){
        User user = new User();
        user.setUserId(id);
        return user;
    }
}