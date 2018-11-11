package com.jba.dao2.search.dao;

import com.jba.dao2.DAOConfig;
import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DAOConfig.class })
@WebAppConfiguration
public class SearchDAOTest {

    @Autowired 
    private SearchDAO searchDAO;

    @Autowired
    private SessionFactory sessionFactory;
    
    @Test
    public void addRoute() {
        Route newRoute = new Route("Czy zapisano", "Zapisano");
        newRoute = searchDAO.addRoute(newRoute);
        assertNotEquals(0,newRoute.getRouteId());
    }

    @Test
    public void updateRoute() {
        List<Route> allRoutes = searchDAO.getAllRoutes();

        int selection = (int)(Math.random()*allRoutes.size());

        Route routeToBeChanged = allRoutes.get(selection);

        routeToBeChanged.setRouteFromLocation("Adamowo");
        routeToBeChanged.setRouteToLocation("Drągowo");

        searchDAO.updateRoute(routeToBeChanged);

        Route fromDB = searchDAO.getRouteById(Route.of(routeToBeChanged.getRouteId()));

        assertEquals(fromDB.getRouteId(), routeToBeChanged.getRouteId());
    }

    @Test
    public void updatePopularRoute() {
        try {
            PopularRoute popularRoute = searchDAO.getPopularRouteById(2);
            long initialUseCount = popularRoute.getUseCount();

            searchDAO.updatePopularRoute(Route.of(2), popularRoute);

            popularRoute = searchDAO.getPopularRouteById(2);

            assertNotEquals("Counter should have been increased!", initialUseCount, popularRoute.getUseCount());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void registerNewSearch() {
        Search search = new Search(Route.of(2), "Example3", "Example4");
        SearchHistory searchHistory = null;
        try{
            searchHistory = searchDAO.registerNewSearch(User.of(2),search);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        Session session = sessionFactory.getCurrentSession();

        try{
            session.delete(searchHistory);
            session.delete(search);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }
}