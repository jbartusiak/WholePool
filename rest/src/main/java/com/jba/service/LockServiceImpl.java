package com.jba.service;

import com.jba.dao2.blocked.dao.BlockedDAO;
import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.dao2.user.enitity.User;
import com.jba.rest.exception.UserLockedPermanentlyException;
import com.jba.service.ifs.LockService;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;

@Service
public class LockServiceImpl implements LockService {

    @Autowired
    private BlockedDAO blockedDAO;

    @Autowired
    private UserService userService;

    @Override
    public List<BlockStatus> getAllBlockStatuses() {
        return blockedDAO.getAllBlockStatuses();
    }

    @Override
    public BlockStatus addNewBlockStatus(BlockStatus blockStatus) {
        return blockedDAO.addNewBlockStatus(blockStatus.getStatusType(), blockStatus.isReversible());
    }

    @Override
    public BlockStatus deleteBlockStatus(Integer blockStatus) {
        return blockedDAO.deleteBlockStatus(BlockStatus.of(blockStatus));
    }

    @Override
    public List<BlockedUsers> getAllBlockedUsers() {
        return blockedDAO.getAllBlockedUsers();
    }

    @Override
    public BlockedUsers blockUser(Integer userIdToBeBlocked, Integer userIdPerformingBlock, Integer blockStatusId, String reasonDescription){
        return blockedDAO
                .blockUser(
                        userService.getUser(userIdToBeBlocked),
                        userService.getUser(userIdPerformingBlock),
                        BlockStatus.of(blockStatusId),
                        reasonDescription
                );
    }

    @Override
    public BlockedUsers getUserBlockedStatus(Integer userId) {
        BlockedUsers result = blockedDAO
                    .getUserBlockedStatus(
                            userService.getUser(userId)
                    );
        if(result==null){
            throw new NoResultException("User "+userId+" is not locked!");
        }
        else return result;
    }

    @Override
    public User unlockUser(Integer userId) throws UnsupportedOperationException{
        try {
            return blockedDAO.unlockUser(
                    userService.getUser(userId),
                    false
            );
        }
        catch (UnsupportedOperationException e){
            throw new UserLockedPermanentlyException(e);
        }
    }

    @Override
    public User forceUnlockUser(Integer userId) {
        return blockedDAO.unlockUser(
                userService.getUser(userId),
                true
        );
    }
}
