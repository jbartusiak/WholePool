package com.jba.dao2.ride.enitity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@Entity
@Table(name = "RideDetails")
@NoArgsConstructor
@RequiredArgsConstructor
public class RideDetails implements Serializable {

    @Id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_RIDE_ID")
    @NonNull
    private Ride rideId;

    @Column(name = "RIDE_DATE_OF_DEPARTURE")
    @NonNull
    private LocalDateTime dateOfDeparture;

    @Column(name = "RIDE_DATE_OF_ARRIVAL")
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

    public String getFormattedDate(Integer choose, @Nullable String format){
        if(format!=null){
            DateTimeFormatter df= getFormatter(format);
            if(choose==0)
                return df.format(dateOfDeparture);
            else
                return df.format(dateOfArrival);
        }
        else{
            DateTimeFormatter df= getFormatter("d MMMM");
            if(choose==0)
                return df.format(dateOfDeparture);
            else return df.format(dateOfArrival);
        }
    }

    private DateTimeFormatter getFormatter(String format){
        return DateTimeFormatter.ofPattern(format, new Locale("pl"));
    }
}
