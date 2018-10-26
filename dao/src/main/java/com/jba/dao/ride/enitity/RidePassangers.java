package com.jba.dao.ride.enitity;

import com.jba.dao.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ridepassengers")
public class RidePassangers implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_PASSENGERS_USER_ID")
    private User passenger;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_PASSENGERS_RIDE_ID")
    private Ride ride;

}
