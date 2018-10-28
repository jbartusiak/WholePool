package com.jba.dao.ride.enitity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@Entity
@Table(name = "ridedetails")
@NoArgsConstructor
@RequiredArgsConstructor
public class RideDetails implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "FK_RIDE_ID")
    @NonNull
    private Ride rideId;

    @Column(name = "RIDE_DATE_OF_DEPARTURE")
    @NonNull
    private Date dateOfDeparture;

    @Column(name = "RIDE_DATE_OF_ARRIVAL")
    @NonNull
    private Date dateOfArrival;

    @Column(name="RIDE_TRAVEL_TIME")
    @NonNull
    private int travelTime;

    @Column(name="RIDE_PRICE")
    @NonNull
    private double price;

    @Column(name = "RIDE_DESCRIPTION")
    @NonNull
    private String description;

}
