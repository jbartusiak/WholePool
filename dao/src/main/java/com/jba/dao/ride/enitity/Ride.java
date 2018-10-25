package com.jba.dao.ride.enitity;

import com.jba.dao.route.entity.Route;
import com.jba.dao.source.entity.Source;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int rideId;

    @ManyToOne
    @JoinColumn(name = "FK_RIDE_SOURCE_ID")
    @NotNull
    private Source route;

    /* TODO: Implement Route and then uncomment
    @ManyToMany
    @JoinColumn(name = "FK_RIDE_ROUTE_ID")
    @ToString.Exclude
    @NotNull
    private Set<Route> route;
    */

    @Column(name = "RIDE_CAN_SMOKE")
    private boolean allowSmokers;

    @Column(name = "RIDE_ALLOW_ANIMALS")
    private boolean allowAnimals;

    @Column(name="RIDE_NO_OF_SEATS")
    private int nrOfSeats;
}
