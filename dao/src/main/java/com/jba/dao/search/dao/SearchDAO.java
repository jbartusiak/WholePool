package com.jba.dao.search.dao;

import com.jba.dao.route.entity.PopularRoute;
import com.jba.dao.route.entity.Route;
import com.jba.dao.search.entity.Search;
import com.jba.dao.search.entity.SearchHistory;
import com.jba.dao.user.enitity.User;
import com.jba.session.DBUtils;
import com.jba.session.WPLSessionFactory;
import com.sun.istack.Nullable;
import lombok.experimental.UtilityClass;
import org.apache.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

@UtilityClass
public class SearchDAO {

    private final static Logger logger = Logger.getLogger(SearchDAO.class);

    public static Route addRoute(Route route){
        return (Route) DBUtils.saveOrUpdate(route);
    }

    public static Route getRouteById(int id){
        Session session = WPLSessionFactory.getDBSession();

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
        Session session = WPLSessionFactory.getDBSession();

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
            DBUtils.saveOrUpdate(pop);
        }
        else{
            popularRoute.setUseCount(popularRoute.getUseCount()+1);
            DBUtils.saveOrUpdate(popularRoute);
        }
        return popularRoute;
    }

    public static SearchHistory registerNewSearch(User user, Search search){
        DBUtils.saveOrUpdate(search);

        SearchHistory searchHistory = new SearchHistory(user,search);
        return (SearchHistory) DBUtils.saveOrUpdate(searchHistory);
    }

}
