package com.jba.dao.user.enitity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="usertype")
public class UserType {

    @Id
    @Column(name="pk_type_id")
    int typeId;

    @Column(name="type_name")
    String typeName;

    public UserType(){}

    public UserType(String typeName){
        this.typeName=typeName;
    }

}
