package com.jba.dao.blocked.dao;

import com.jba.dao.blocked.entity.BlockStatus;
import com.jba.dao.blocked.entity.BlockedUsers;
import com.jba.dao.user.enitity.User;
import com.jba.session.WPLSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockedDAOTest {

    private final static Logger logger = Logger.getLogger(BlockedDAO.class);

    @Test
    void addNewBlockStatus() {
        BlockStatus blockStatus = BlockedDAO.addNewBlockStatus("another block status", true);

        assertNotEquals(0, blockStatus.getBlockStatusId());

        blockStatus = BlockedDAO.deleteBlockStatus(blockStatus);
    }

    @Test
    void blockUser() {
        User admin = User.of(1);
        User user = User.of(2);

        BlockedUsers blockedUsers = BlockedDAO.blockUser(user, admin, BlockStatus.of(1),"Just because!!!!");

        assertNotNull(blockedUsers, "Blocked user should not be null");

        user = BlockedDAO.unlockUser(user);
    }

    @Test
    void getUserBlockedStatus() {
        User user = User.of(2);

        BlockedUsers blockedUser= BlockedDAO.getUserBlockedStatus(user);

        assertNull(blockedUser);
    }

    @Test
    void unlockUser() {
        User user = User.of(2);

        logger.info("Locking user "+user+" with reversible!");
        BlockedUsers blockedUsers = BlockedDAO.blockUser(user, User.of(1), BlockStatus.of(1), "Just becaouse");

        logger.info("Unlocking user "+user);
        BlockedDAO.unlockUser(user);

        logger.info("Locking user "+user+" with irreversible lock");
        blockedUsers = BlockedDAO.blockUser(user,User.of(1),BlockStatus.of(2),"Perma ban");
        logger.info("User is now permanently locked");

        logger.info("Trying to unlock user");
        try{
            BlockedDAO.unlockUser(user);
            fail("Unlocking perma-banned user should be impossible");
        }
        catch (UnsupportedOperationException e){
            logger.info("Unsupported exception caught with message "+e.getMessage());

            Session session = WPLSessionFactory.getDBSession();
            try(session){
                session.beginTransaction();
                session.delete(blockedUsers);
                session.getTransaction().commit();
            }
            catch (Exception ex){
                fail(ex);
            }
        }
    }
}