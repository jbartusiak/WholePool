package com.jba.dao2.blocked.dao;

import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BlockedDAO {

    @Transactional
    List<BlockStatus> getAllBlockStatuses();

    @Transactional
    BlockStatus addNewBlockStatus(String name, boolean reversible);

    @Transactional
    BlockStatus deleteBlockStatus(BlockStatus s);

    @Transactional
    BlockedUsers blockUser(User userToBeBlocked, User userPerformingBlock, BlockStatus blockType, String reasonDescription);

    @Transactional
    List<BlockedUsers> getAllBlockedUsers();

    @Transactional
    BlockedUsers getUserBlockedStatus(User user);

    @Transactional
    User unlockUser(User user, boolean force) throws UnsupportedOperationException;

}
