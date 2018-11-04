package com.jba.dao2.search.entity;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.user.enitity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class SearchHistoryTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    @Transactional
    public void createNewSearchUserAssociation(){
        Session session = sessionFactory.getCurrentSession();
        User user = session.createQuery("from User ",User.class).getResultList().get(0);
        Search search = session.createQuery("from Search", Search.class).getResultList().get(0);

        SearchHistory history = new SearchHistory(user,search);
        try {
            session.save(history);
        }
        catch (Exception e){
            e.printStackTrace();
            fail(e.getMessage());
        }
        session.getTransaction().commit();
    }

    @Test
    @Transactional
    public void deleteTest(){
        Session session = sessionFactory.getCurrentSession();
        SearchHistory history = session.createQuery("from SearchHistory h where h.searchOwner.id=1 and h.usersSearch.id=1", SearchHistory.class).getSingleResult();
        session.delete(history);
        session.getTransaction().commit();
    }
}