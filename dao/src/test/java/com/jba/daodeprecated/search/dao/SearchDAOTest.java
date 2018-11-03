package com.jba.daodeprecated.search.dao;

import com.jba.daodeprecated.route.entity.PopularRoute;
import com.jba.daodeprecated.route.entity.Route;
import com.jba.daodeprecated.search.entity.Search;
import com.jba.daodeprecated.search.entity.SearchHistory;
import com.jba.daodeprecated.user.enitity.User;
import com.jba.WPLSessionFactory;
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