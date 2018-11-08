package com.jba.dao2.blocked.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jba.dao2.user.enitity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="BlockedUsers")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BlockedUsers implements Serializable {

    @Id
    @NonNull
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="fk_blocked_user_id")
    private User user;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name="fk_blocked_status_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private BlockStatus blockStatus;

    @Column(name = "blocked_date")
    @NonNull
    private Date blockedDate;

    @Column(name= "blocked_reason_description")
    private String blockReasonDescription;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_BLOCKED_ADMIN_USER_ID")
    private User blockedBy;

}
