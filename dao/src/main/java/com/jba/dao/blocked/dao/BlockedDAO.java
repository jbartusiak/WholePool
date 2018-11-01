package com.jba.dao.blocked.dao;

import com.jba.dao.blocked.entity.BlockStatus;
import com.jba.dao.blocked.entity.BlockedUsers;
import com.jba.dao.user.enitity.User;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import java.sql.Date;
import java.time.Instant;
import java.util.Calendar;
import java.util.Set;

@UtilityClass
public class BlockedDAO {

    private final static Logger logger = Logger.getLogger(BlockedDAO.class);

    public static BlockStatus addNewBlockStatus(String name, boolean reversible) {
        BlockStatus blockStatus = new BlockStatus(name, reversible);
        return (BlockStatus) DBUtils.saveOrUpdate(blockStatus);
/*
        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            session.save(blockStatus);

            session.getTransaction().commit();

            return blockStatus;
        }
        catch (Exception e){
            logger.error("Error ",e);
            throw e;
        }*/
    }

    public static BlockStatus deleteBlockStatus(BlockStatus s){
        Set<BlockedUsers> blockadesToBeDeleted = s.getUsersWithThisStatus();

        Session session = WPLSessionFactory.getDBSession();

        try(session){
            session.beginTransaction();

            if(blockadesToBeDeleted!=null) {

                logger.info("Deleting usages of BlockStatus");
                for (BlockedUsers user : blockadesToBeDeleted) {
                    logger.info("Deleting " + user);
                    session.delete(user);
                }
                logger.info("Done");

            }
            logger.info("Deleting block status "+s);
            session.delete(s);

            session.getTransaction().commit();
            return s;
        }
        catch (Exception e){
            logger.error("Error deleting BlockStatus "+s, e);
            throw e;
        }
    }

    public static BlockedUsers blockUser(User userToBeBlocked, User userPerformingBlock, BlockStatus blockType, String reasonDescription) {
        Instant instant = Calendar.getInstance().getTime().toInstant();
        java.util.Date dateOfBlock = java.sql.Date.from(instant);

        BlockedUsers blockedUsers = new BlockedUsers(userToBeBlocked, blockType, dateOfBlock);

        if (userPerformingBlock != null) {
            logger.info("Setting user performing block to " + userPerformingBlock);
            blockedUsers.setBlockedBy(userPerformingBlock);
        }
        if (reasonDescription != null) {
            logger.info("Setting block reason description to " + reasonDescription);
            blockedUsers.setBlockReasonDescription(reasonDescription);
        }
        return (BlockedUsers) DBUtils.saveOrUpdate(blockedUsers);
    }

    public static BlockedUsers getUserBlockedStatus(User user) {
        Session session = WPLSessionFactory.getDBSession();

        try (session) {
            session.beginTransaction();

            BlockedUsers blockedUser = session.createQuery("from BlockedUsers b where b.user=:user", BlockedUsers.class).
                    setParameter("user", user).
                    getSingleResult();

            session.getTransaction().commit();

            return blockedUser;
        }
        catch (NoResultException e){
            logger.info("User "+user+" is not locked.");
            return null;
        }
        catch (Exception e) {
            logger.debug("Error trying to get users " + user + " block status");
            throw e;
        }
    }

    public static User unlockUser(User user) throws UnsupportedOperationException{
        Session session = WPLSessionFactory.getDBSession();

        session.beginTransaction();

        BlockedUsers blockedUser = getUserBlockedStatus(user);

        if (blockedUser == null) {
            throw new UnsupportedOperationException("This user is not locked, so he cannot be unlocked");
        }

        if (!blockedUser.getBlockStatus().isReversible()) {
            throw new UnsupportedOperationException("This users lock is permanent!");
        }

        try (session) {
            session.delete(blockedUser);
            session.getTransaction().commit();
        }
        catch (Exception e) {
            logger.error("Errror removing users lock!",e);
            throw e;
        }
        return user;
    }
}
