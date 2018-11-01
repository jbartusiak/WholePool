package com.jba.dao.user.enitity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name="UserType")
public class UserType {

    @Id
    @Column(name="pk_type_id")
    int typeId;

    @Column(name="type_name")
    String typeName;

    @OneToMany(mappedBy = "userType")
    @ToString.Exclude
    private Set<User> usersOfThisType;

    public UserType(){}

    public UserType(String typeName){
        this.typeName=typeName;
    }

    public static UserType of(int id){
        UserType userType = new UserType();
        userType.setTypeId(id);
        return userType;
    }
}
