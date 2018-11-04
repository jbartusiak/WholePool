package com.jba.dao2.blocked.dao;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.enitity.User;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class BlockedDAOTest {

    private final static Logger logger = Logger.getLogger(BlockedDAOTest.class);

    @Autowired
    private BlockedDAO blockedDAOMySQLRepository;

    @Test
    public void addNewBlockStatus() {
        BlockStatus blockStatus = blockedDAOMySQLRepository.addNewBlockStatus("another block status", true);

        assertNotEquals(0, blockStatus.getBlockStatusId());

        blockedDAOMySQLRepository.deleteBlockStatus(BlockStatus.of(blockStatus.getBlockStatusId()));
    }

    @Test
    public void blockUser() {
        BlockedUsers blockedUsers = blockedDAOMySQLRepository.blockUser(User.of(2), User.of(1), BlockStatus.of(1),"Just because!!!!");

        assertNotNull("Blocked user should not be null",blockedUsers);

        blockedDAOMySQLRepository.unlockUser(User.of(2), false);
    }

    @Test
    public void getUserBlockedStatus() {
        User user = User.of(2);

        BlockedUsers blockedUser= blockedDAOMySQLRepository.getUserBlockedStatus(user);

        assertNull(blockedUser);
    }

    @Test
    public void unlockUser() {
        User user = User.of(2);

        logger.info("Locking user "+user+" with reversible!");
        BlockedUsers blockedUsers = blockedDAOMySQLRepository.blockUser(user, User.of(1), BlockStatus.of(1), "Just becaouse");

        logger.info("Unlocking user "+user);
        blockedDAOMySQLRepository.unlockUser(user, false);

        logger.info("Locking user "+user+" with irreversible lock");
        blockedUsers = blockedDAOMySQLRepository.blockUser(user,User.of(1),BlockStatus.of(2),"Perma ban");
        logger.info("User is now permanently locked");

        logger.info("Trying to unlock user");
        try{
            blockedDAOMySQLRepository.unlockUser(user, false);
            fail("Unlocking perma-banned user should be impossible");
        }
        catch (UnsupportedOperationException e){
            logger.info("Unsupported exception caught with message "+e.getMessage());
            blockedDAOMySQLRepository.unlockUser(user, true);
        }
    }
}