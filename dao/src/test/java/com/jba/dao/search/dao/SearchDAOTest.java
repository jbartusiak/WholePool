package com.jba.dao.search.dao;

import com.jba.dao.route.entity.PopularRoute;
import com.jba.dao.route.entity.Route;
import com.jba.dao.search.entity.Search;
import com.jba.dao.search.entity.SearchHistory;
import com.jba.dao.user.enitity.User;
import com.jba.session.WPLSessionFactory;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchDAOTest {

    @Test
    void addRoute() {
        Route newRoute = new Route("Warszawa", "Gdańsk");
        newRoute = SearchDAO.addRoute(newRoute);
        assertNotEquals(0,newRoute.getRouteId());
    }

    @Test
    void updateRoute() {
        List<Route> allRoutes = SearchDAO.getAllRoutes();

        int selection = (int)(Math.random()*allRoutes.size());

        Route routeToBeChanged = allRoutes.get(selection);

        routeToBeChanged.setRouteFromLocation("Adamowo");
        routeToBeChanged.setRouteToLocation("Drągowo");

        SearchDAO.updateRoute(routeToBeChanged);

        Route fromDB = SearchDAO.getRouteById(Route.of(routeToBeChanged.getRouteId()));

        assertEquals(fromDB.getRouteId(), routeToBeChanged.getRouteId());
    }

    @Test
    void updatePopularRoute() {
        try {
            PopularRoute popularRoute = SearchDAO.updatePopularRoute(Route.of(2), null);
        }
        catch (Exception e){
            fail(e);
        }
    }

    @Test
    void registerNewSearch() {
        Search search = new Search(Route.of(2), "Search criteria", "Order by");
        SearchHistory searchHistory = null;
        try{
            searchHistory = SearchDAO.registerNewSearch(User.of(2),search);
        }
        catch (Exception e){
            fail(e);
        }
        Session session = WPLSessionFactory.getDBSession();

        try{
            session.beginTransaction();
            session.delete(searchHistory);
            session.delete(search);
            session.getTransaction().commit();
            session.close();
        }
        catch (Exception e){
            session.close();
            fail(e);
        }
    }
}