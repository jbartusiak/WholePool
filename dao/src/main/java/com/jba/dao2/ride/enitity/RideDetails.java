package com.jba.dao2.ride.enitity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "RideDetails")
@NoArgsConstructor
@RequiredArgsConstructor
public class RideDetails implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name = "FK_RIDE_ID")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NonNull
    private Ride rideId;

    @Column(name = "RIDE_DATE_OF_DEPARTURE", columnDefinition = "DATETIME")
    @NonNull
    private LocalDateTime dateOfDeparture;

    @Column(name = "RIDE_DATE_OF_ARRIVAL", columnDefinition = "DATETIME")
    @NonNull
    private LocalDateTime dateOfArrival;

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
