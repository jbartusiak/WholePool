package com.jba.dao.preferences.entity;

import com.jba.dao.user.enitity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "userspreference")
public class UsersPreference {

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_PREFERENCE_USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "FK_PREFERENCE_PREF_ID")
    private Preference preference;

    @Column(name = "PREFERENCE_VALUE")
    @NonNull
    private String value;

}
