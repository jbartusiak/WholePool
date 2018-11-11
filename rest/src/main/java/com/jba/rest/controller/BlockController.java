package com.jba.rest.controller;

import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.entity.WPLResponse;
import com.jba.rest.exception.UserLockedPermanentlyException;
import com.jba.service.ifs.LockService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
public class BlockController {

    @Autowired
    private LockService lockService;

    @ApiOperation(value = "Returns types of locks available in system", notes = "Returns a complete list of different " +
            "locks available in the system. Cannot be null.")
    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getLockTypes() {
        return new WPLResponse<>(HttpStatus.OK, lockService.getAllBlockStatuses(), BlockStatus.class);
    }

    @ApiOperation(value = "Adds new lock type", notes = "Adds a new lock type to the system. Name of the lock type " +
            "must be unique.", consumes = "BlockStatus")
    @PostMapping("/type")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addNewLockType(
            @ApiParam(name="blockStatus", value = "JSON representing BlockStatus entity", required = true, type = "BlockStatus entity")
            @RequestBody(required = true) BlockStatus blockStatus
    ) {
        return new WPLResponse<>(HttpStatus.CREATED, lockService.addNewBlockStatus(blockStatus));
    }

    @ApiOperation(value = "Delete lock type", notes = "Deletes a lock type from the system. Deletes locks applied " +
            "using this lock, so users already locked using this status will be unlocked!")
    @DeleteMapping("/type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLockType(
            @ApiParam(name="userId", value = "Integer referring to the primary key of this lock type", required = true, type = "Integer")
            @RequestParam(name = "lockTypeId") Integer lockTypeId
    ) {
        lockService.deleteBlockStatus(lockTypeId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersLockStaus(
            @RequestParam(name = "userId", required = false) Integer userId
    ) {
        if (userId == null)
            return new WPLResponse<>(HttpStatus.OK, lockService.getAllBlockedUsers(), BlockedUsers.class);
        else
            return new WPLResponse<>(HttpStatus.OK, lockService.getUserBlockedStatus(userId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse lockUser(
            @RequestBody(required = true) BlockedUsers blockedUsers
    ) {
        return new WPLResponse<>(HttpStatus.CREATED, lockService.blockUser(
                blockedUsers.getUser().getUserId(),
                blockedUsers.getBlockedBy().getUserId(),
                blockedUsers.getBlockStatus().getBlockStatusId(),
                blockedUsers.getBlockReasonDescription()
        ));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlockUser(
            @RequestParam(name = "userId", required = true) Integer userId,
            @RequestParam(name = "force", required = false) Boolean force
    ) {
        if(force==null){
            try {
                lockService.unlockUser(userId);
            }
            catch (UnsupportedOperationException e){
                throw new UserLockedPermanentlyException(e.getMessage());
            }
        }
        else
            lockService.forceUnlockUser(userId);
    }
}
