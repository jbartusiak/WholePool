package com.jba.rest.controller;

import com.jba.dao2.blocked.entity.BlockStatus;
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
        return new WPLResponse<>(HttpStatus.OK, lockService.getAllBlockStatuses());
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
}
