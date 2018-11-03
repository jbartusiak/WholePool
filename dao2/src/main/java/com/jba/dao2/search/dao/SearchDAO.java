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

import java.util.List;

@UtilityClass
public class SearchDAO {

    private final static Logger logger = Logger.getLogger(SearchDAO.class);

    @Autowired
    private SessionFactory sessionFactory;

    public static Route addRoute(Route route){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(route);
        return route;
    }

    public static Route getRouteById(int id){
        Session session = sessionFactory.getCurrentSession();

        try{
            session.beginTransaction();

            Route result = session.
                    createQuery("from Route r where r.id=:id", Route.class).
                    setParameter("id", id).
                    getSingleResult();

            session.getTransaction().commit();

            session.close();

            return result;
        }
        catch (Exception e){
            logger.error("Error retrieving Route of id "+id);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public static Route getRouteById(Route route){
        return getRouteById(route.getRouteId());
    }

    public static List<Route> getAllRoutes(){
        Session session = sessionFactory.getCurrentSession();

        try{
            session.beginTransaction();

            List<Route> results = session.
                    createQuery("from Route", Route.class).
                    getResultList();

            session.close();

            return results;
        }
        catch (Exception e){
            logger.error("Error occured while getting Routes",e);
            if(session.isOpen())
                session.close();
            throw e;
        }
    }

    public static Route updateRoute(Route route){
        return addRoute(route);
    }

    public static PopularRoute updatePopularRoute(Route route, @Nullable PopularRoute popularRoute){
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

    public static SearchHistory registerNewSearch(User user, Search search){
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(search);

        SearchHistory searchHistory = new SearchHistory(user,search);
        session.saveOrUpdate(searchHistory);
        return searchHistory;
    }

}
