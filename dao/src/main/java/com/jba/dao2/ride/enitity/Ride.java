package com.jba.dao2.ride.enitity;

import com.jba.dao2.route.entity.Route;
import com.jba.dao2.source.entity.Source;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Ride")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_RIDE_ID")
    private int rideId;

    @ManyToOne
    @JoinColumn(name = "FK_RIDE_SOURCE_ID")
    @NonNull
    private Source sourceId;

    @ManyToOne
    @JoinColumn(name = "FK_RIDE_ROUTE_ID")
    @NonNull
    private Route routeForThisRide;

    @Column(name = "RIDE_CAN_SMOKE")
    private boolean allowSmokers;

    @Column(name = "RIDE_ALLOW_ANIMALS")
    private boolean allowAnimals;

    @Column(name="RIDE_NO_OF_SEATS")
    private int nrOfSeats;

    @Column(name="RIDE_DIRECT_URL")
    private String directURL;

    @OneToMany(mappedBy = "ride")
    @ToString.Exclude
    private Set<RidePassangers> passengers;

    @OneToOne(mappedBy = "rideId")
    @ToString.Exclude
    private RideDetails rideDetails;
}
