package com.jba.dao.blocked.entity;

import com.jba.dao.user.enitity.User;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="blockedusers")
@Data
public class BlockedUsers implements Serializable {

    @Id
    @OneToOne(optional = false)
    @JoinColumn(name="fk_blocked_user_id")
    private User user;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name="fk_blocked_status_id")
    private BlockStatus blockStatus;

    @Column(name = "blocked_date")
    private Date blockedDate;

    @Column(name= "blocked_reason_description")
    private String blockReasonDescription;

    @ManyToOne(optional = true)
    @JoinColumn(name = "FK_BLOCKED_ADMIN_USER_ID")
    private User blockedBy;

    public BlockedUsers(){}

}
