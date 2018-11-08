package com.jba.dao2.cars.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "CarType")
public class CarType {

    public CarType(int carTypeId){
        this.carTypeId=carTypeId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_CAR_TYPE_ID")
    private int carTypeId;

    @Column(name = "CAR_TYPE_NAME")
    private String carTypeName;

    public CarType(){}

    public CarType(String carTypeName){
        this.carTypeName=carTypeName;
    }

    public static CarType of(int id){
        CarType carType = new CarType();
        carType.setCarTypeId(id);
        return carType;
    }
}
