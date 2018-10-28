package com.jba.dao.cars.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "cartype")
public class CarType {

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
}