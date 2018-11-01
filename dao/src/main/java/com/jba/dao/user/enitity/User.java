package com.jba.dao.user.enitity;

import com.jba.dao.blocked.entity.BlockedUsers;
import com.jba.dao.ride.enitity.RidePassangers;
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

    @ManyToOne
    @NonNull
    @JoinColumn(name="fk_user_type_id")
    private UserType userType;

    @Column(name="user_email_address")
    @NonNull
    private String emailAddress;

    @Column(name="user_password_hash")
    @NonNull
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
    private Set<BlockedUsers> blockedByThisUser;

    @OneToMany(mappedBy = "passenger")
    @ToString.Exclude
    private Set<RidePassangers> isPassengerAtRides;

    public static User of(int id){
        User user = new User();
        user.setUserId(id);
        return user;
    }
}