package com.jba.dao.preferences.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preference")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PK_PREFERENCE_ID")
    private long preferenceId;

    @Column(name="PREFERENCE_NAME")
    @NonNull
    private String preferenceName;

    @Column(name = "PREFERENCE_TYPE")
    @NonNull
    private String preferenceType;
}
