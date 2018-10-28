package com.jba.dao.cars.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Year;

@Entity
@Data
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CAR_ID")
    private int carId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FK_CAR_TYPE_ID")
    private CarType carTypeId;

    @Column(name="CAR_NAME")
    private String carName;

    @Column(name="CAR_REGISTRATION_NUMBER")
    private String carRegistrationNumber;

    @Column(name="CAR_PRODUCTION_YEAR")
    private int carProductionYear;

    public Car(){}

    public Car(CarType carTypeId, String carName, String carRegistrationNumber, int carProductionYear){
        this.carTypeId=carTypeId;
        this.carName=carName;
        this.carRegistrationNumber=carRegistrationNumber;
        this.carProductionYear=carProductionYear;
    }
}
