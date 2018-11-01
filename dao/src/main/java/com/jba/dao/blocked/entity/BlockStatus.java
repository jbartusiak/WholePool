package com.jba.dao.blocked.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="BlockStatus")
public class BlockStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="pk_status_id")
    private int blockStatusId;

    @Column(name="status_type")
    @NonNull
    private String statusType;

    @Column(name="status_reversible")
    @NonNull
    private boolean isReversible;

    @OneToMany(mappedBy = "blockStatus", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<BlockedUsers> usersWithThisStatus;

    public static BlockStatus of(int id){
        BlockStatus blockStatus = new BlockStatus();
        blockStatus.setBlockStatusId(id);
        return blockStatus;
    }
}
