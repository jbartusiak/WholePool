package com.jba.dao2.route.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jba.dao2.ride.enitity.Ride;
import com.jba.dao2.search.entity.Search;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "Route")
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

    public static final Route of(int id){
        Route route = new Route();
        route.setRouteId(id);
        return route;
    }

    public Route(int id){
        this.routeId=id;
    }

    @OneToMany(mappedBy = "routeForThisRide")
    @JsonIgnore
    @ToString.Exclude
    private Set<Ride> ridesThatTakePlaceOnThisRoute;

    @OneToMany(mappedBy = "route")
    @ToString.Exclude
    @JsonIgnore
    private Set<Search> existsInSearches;
}
