package com.jba.dao.route.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PopularRoute")
public class PopularRoute implements Serializable {

    @Id
    @OneToOne(optional = false)
    @JoinColumn(name = "FK_POP_RIDE_ID")
    private Route rideId;

    @Column(name = "POPULAR_AVERAGE_PRICE")
    @NonNull
    private double avgPrice;

    @Column(name = "POPULAR_MAXIMUM_PRICE")
    @NonNull
    private double maxPrice;

    @Column(name="POPULAR_MINIMUM_PRICE")
    @NonNull
    private double minPrice;

    @Column(name="POPULAR_USE_COUNT")
    @NonNull
    private long useCount;

}
