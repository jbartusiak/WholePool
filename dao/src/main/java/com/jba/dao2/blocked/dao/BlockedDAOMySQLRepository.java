package com.jba.dao2.blocked.dao;

import com.jba.dao2.blocked.entity.BlockStatus;
import com.jba.dao2.blocked.entity.BlockedUsers;
import com.jba.dao2.user.enitity.User;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Repository
public class BlockedDAOMySQLRepository implements BlockedDAO{

    private final static Logger logger = Logger.getLogger(BlockedDAOMySQLRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<BlockStatus> getAllBlockStatuses() {
        Session session = sessionFactory.getCurrentSession();

        return session.
                createQuery("from BlockStatus", BlockStatus.class).
                getResultList();
    }

    public BlockStatus addNewBlockStatus(String name, boolean reversible) {
        BlockStatus blockStatus = new BlockStatus(name, reversible);

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(blockStatus);
        return blockStatus;
    }

    public BlockStatus deleteBlockStatus(BlockStatus s){
        Set<BlockedUsers> blockadesToBeDeleted = s.getUsersWithThisStatus();

        Session session = sessionFactory.getCurrentSession();

        try{
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
            return s;
        }
        catch (Exception e){
            logger.error("Error deleting BlockStatus "+s, e);
            throw e;
        }
    }

    public BlockedUsers blockUser(User userToBeBlocked, User userPerformingBlock, BlockStatus blockType, String reasonDescription) {
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

        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(blockedUsers);

        return blockedUsers;
    }

    @Override
    public List<BlockedUsers> getAllBlockedUsers() {
        Session session = sessionFactory.getCurrentSession();

        return session.
                createQuery("from BlockedUsers", BlockedUsers.class).
                getResultList();
    }

    public BlockedUsers getUserBlockedStatus(User user) {
        Session session = sessionFactory.getCurrentSession();

        try  {
            BlockedUsers blockedUser = session.createQuery("from BlockedUsers b where b.user=:user", BlockedUsers.class).
                    setParameter("user", user).
                    getSingleResult();

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

    public User unlockUser(User user, boolean force) throws UnsupportedOperationException{
        Session session = sessionFactory.getCurrentSession();

        BlockedUsers blockedUser = getUserBlockedStatus(user);

        if (blockedUser == null) {
            throw new UnsupportedOperationException("This user is not locked, so he cannot be unlocked");
        }

        if (!blockedUser.getBlockStatus().isReversible()&&!force) {
            throw new UnsupportedOperationException("This users lock is permanent!");
        }

        try {
            session.delete(blockedUser);
        }
        catch (Exception e) {
            logger.error("Errror removing users lock!",e);
            throw e;
        }
        return user;
    }
}
