package com.jba.dao.preferences.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
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
