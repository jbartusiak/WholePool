package com.jba.dao.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="UserType")
@NoArgsConstructor
@Data
public class UserType {

    @Id
    @Column(name="pk_type_id")
    int typeId;

    @Column(name="type_name")
    String typeName;

    public UserType(String typeName){
        this.typeName=typeName;
    }

}
