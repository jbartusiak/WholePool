package com.jba.dao.cars.entity;

import com.jba.dao.user.enitity.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "UsersCars")
public class UsersCars implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_CARS_USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_CARS_CAR_ID")
    private Car car;

    public UsersCars(){}

    public UsersCars(User user, Car car){
        this.user=user;
        this.car=car;
    }
}
