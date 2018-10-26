package com.jba.dao.ride.enitity;

import com.jba.dao.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offeredrides")
public class OfferedRides {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_OFFERS_RIDE_ID")
    private Ride ride;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_OFFERS_USER_ID")
    private User offerer;

}
