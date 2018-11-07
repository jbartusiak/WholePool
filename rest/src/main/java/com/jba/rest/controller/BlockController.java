package com.jba.rest.controller;

import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.entity.WPLResponse;
import com.jba.service.ifs.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
public class BlockController {

    @Autowired
    private LockService lockService;

    @GetMapping("/type")
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getLockTypes(){
        return new WPLResponse<>(HttpStatus.OK, lockService.getAllBlockStatuses(), BlockStatus.class);
    }

    @PostMapping("/type")
    @ResponseStatus(HttpStatus.CREATED)
    public WPLResponse addNewLockType(
            @RequestBody(required = true) BlockStatus blockStatus
    ){
        return new WPLResponse<>(HttpStatus.CREATED, lockService.addNewBlockStatus(blockStatus));
    }

    @DeleteMapping("/type")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLockType(
            @RequestParam(name = "lockTypeId") Integer lockTypeId
    ){
        lockService.deleteBlockStatus(lockTypeId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public WPLResponse getUsersLockStaus(
            @RequestParam(name = "userId", required = false) Integer userId
    ){
        if(userId==null)
            return new WPLResponse<>(HttpStatus.OK, lockService.getAllBlockedUsers(), BlockedUsers.class);
        else
            return new WPLResponse<>(HttpStatus.OK, lockService.getUserBlockedStatus(userId));
    }
}
