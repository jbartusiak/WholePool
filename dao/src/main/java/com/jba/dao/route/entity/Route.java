package com.jba.dao.route.entity;

import com.jba.dao.ride.enitity.Ride;
import com.jba.dao.search.entity.Search;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "route")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_ROUTE_ID")
    private int routeId;

    @Column(name="ROUTE_FROM_LAT")
    private float routeFromLatitude;

    @Column(name="ROUTE_FROM_LONG")
    private float routeFromLongitude;

    @NonNull
    @Column(name="ROUTE_FROM_LOCATION")
    private String routeFromLocation;

    @Column(name="ROUTE_TO_LAT")
    private float routeToLatitude;

    @Column(name="ROUTE_TO_LONG")
    private float routeToLongitude;

    @NonNull
    @Column(name="ROUTE_TO_LOCATION")
    private String routeToLocation;

    @OneToMany(mappedBy = "routeForThisRide")
    @ToString.Exclude
    private Set<Ride> ridesThatTakePlaceOnThisRoute;

    @OneToMany(mappedBy = "route")
    @ToString.Exclude
    private Set<Search> existsInSearches;
}
