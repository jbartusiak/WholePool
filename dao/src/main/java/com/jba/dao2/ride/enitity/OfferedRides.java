package com.jba.dao2.ride.enitity;

import com.jba.dao2.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "OfferedRides")
public class OfferedRides implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_OFFERS_RIDE_ID")
    private Ride ride;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_OFFERS_USER_ID")
    private User offerer;

}
