package com.jba.service.ifs;

import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.enitity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LockService {
    List<BlockStatus> getAllBlockStatuses();
    BlockStatus addNewBlockStatus(BlockStatus blockStatus);
    BlockStatus deleteBlockStatus(BlockStatus blockStatus);
    List<BlockedUsers> getAllBlockedUsers();
    BlockedUsers blockUser(Integer userIdToBeBlocked, Integer userIdPerformingBlock, Integer blockStatusId, String reasonDescription);
    BlockedUsers getUserBlockedStatus(Integer userId);
    User unlockUser(Integer userId);
    User forceUnlockUser(Integer userId);
}
