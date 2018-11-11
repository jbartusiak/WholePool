package com.jba.dao2.cars.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "Car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CAR_ID")
    private int carId;

    @ManyToOne(optional = false)
    @NonNull
    @JoinColumn(name = "FK_CAR_TYPE_ID")
    private CarType carTypeId;

    @Column(name="CAR_NAME")
    @NonNull
    private String carName;

    @Column(name="CAR_REGISTRATION_NUMBER")
    @NonNull
    private String carRegistrationNumber;

    @Column(name="CAR_PRODUCTION_YEAR")
    @NonNull
    private int carProductionYear;

    public static Car of(int id){
        Car car = new Car();
        car.setCarId(id);
        return car;
    }
}
