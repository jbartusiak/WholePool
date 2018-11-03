package com.jba.daodeprecated.blocked.entity;

import com.jba.daodeprecated.user.enitity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    @OneToOne(optional = false)
    @JoinColumn(name="fk_blocked_user_id")
    private User user;

    @Id
    @ManyToOne(optional = false)
    @NonNull
    @JoinColumn(name="fk_blocked_status_id")
    private BlockStatus blockStatus;

    @Column(name = "blocked_date")
    @NonNull
    private Date blockedDate;

    @Column(name= "blocked_reason_description")
    private String blockReasonDescription;

    @ManyToOne(optional = true)
    @JoinColumn(name = "FK_BLOCKED_ADMIN_USER_ID")
    private User blockedBy;

}
