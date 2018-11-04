package com.jba.dao2.search.dao;

import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.dao2.user.enitity.User;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class SearchDAOMySQLRepository implements SearchDAO{

    private final Logger logger = Logger.getLogger(SearchDAOMySQLRepository.class);

    @Autowired
    private SessionFactory sessionFactory;

    public Route addRoute(Route route){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(route);
        return route;
    }

    public Route getRouteById(int id){
        Session session = sessionFactory.getCurrentSession();

        try{
            Route result = session.
                    createQuery("from Route r where r.id=:id", Route.class).
                    setParameter("id", id).
                    getSingleResult();

            return result;
        }
        catch (Exception e){
            logger.error("Error retrieving Route of id "+id);
            throw e;
        }
    }

    public Route getRouteById(Route route){
        return getRouteById(route.getRouteId());
    }

    public List<Route> getAllRoutes(){
        Session session = sessionFactory.getCurrentSession();

        try{
            List<Route> results = session.
                    createQuery("from Route", Route.class).
                    getResultList();

            return results;
        }
        catch (Exception e){
            logger.error("Error occured while getting Routes",e);
            throw e;
        }
    }

    public Route updateRoute(Route route){
        return addRoute(route);
    }

    public PopularRoute updatePopularRoute(Route route, @Nullable PopularRoute popularRoute){
        if(popularRoute==null){
            PopularRoute pop = new PopularRoute(route, 0.0, 0.0, 0.0, 1);
            Session session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(pop);
        }
        else{
            popularRoute.setUseCount(popularRoute.getUseCount()+1);
            Session session = sessionFactory.getCurrentSession();
            session.saveOrUpdate(popularRoute);
        }
        return popularRoute;
    }

    public SearchHistory registerNewSearch(User user, Search search){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(search);

        SearchHistory searchHistory = new SearchHistory(user,search);
        session.saveOrUpdate(searchHistory);
        return searchHistory;
    }

    @Override
    public PopularRoute getPopularRouteById(int id) {
        Session session = sessionFactory.getCurrentSession();

        try {
            PopularRoute popularRoute = session.
                    createQuery("from PopularRoute pop where pop.rideId.id=:id", PopularRoute.class).
                    setParameter("id", id).
                    getSingleResult();

            return popularRoute;
        }
        catch (NoResultException e){
            logger.info("There were no results for popularroute id "+id);
            return null;
        }
    }

    @Override
    public PopularRoute getPopularRouteByRoute(Route route) {
        return getPopularRouteById(route.getRouteId());
    }


}
