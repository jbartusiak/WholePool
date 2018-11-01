package com.jba.dao.blocked.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name="BlockStatus")
public class BlockStatus {

    public BlockStatus(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_status_id")
    private int blockStatusId;

    @Column(name="status_type")
    private String statusType;

    @Column(name="status_reversible")
    private boolean isReversible;

    @OneToMany(mappedBy = "blockStatus", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<BlockedUsers> usersWithThisStatus;
}
