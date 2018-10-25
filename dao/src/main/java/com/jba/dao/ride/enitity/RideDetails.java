package com.jba.dao.ride.enitity;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "ridedetails")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class RideDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rideDetailsId;

    @Transient
    @Column(name = "RIDE_DATE_OF_DEPARTURE")
    @NotNull
    private Date dateOfDeparture;

    @Transient
    @Column(name = "RIDE_DATE_OF_ARRIVAL")
    @NotNull
    private Date dateOfArrival;

    @Column(name="RIDE_TRAVEL_TIME")
    @NotNull
    private int travelTime;

    @Column(name="RIDE_PRICE")
    @NotNull
    private double price;

    @Column(name = "RIDE_DESCRIPTION")
    @NotNull
    private String description;

}
