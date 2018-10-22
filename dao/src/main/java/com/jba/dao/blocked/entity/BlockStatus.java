package com.jba.dao.blocked.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="blockstatus")
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
}
