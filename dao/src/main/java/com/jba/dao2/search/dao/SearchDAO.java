package com.jba.dao2.search.dao;

import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.dao2.user.enitity.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SearchDAO {

    @Transactional
    Route addRoute(Route route);

    @Transactional
    Route getRouteById(int id);

    @Transactional
    Route getRouteById(Route route);

    @Transactional
    List<Route> getAllRoutes();

    @Transactional
    Route updateRoute(Route route);

    @Transactional
    PopularRoute updatePopularRoute(Route route, @Nullable PopularRoute popularRoute);

    @Transactional
    SearchHistory registerNewSearch(User user, Search search);

    @Transactional
    PopularRoute getPopularRouteById(int id);

    @Transactional
    PopularRoute getPopularRouteByRoute(Route route);

    @Transactional
    List<SearchHistory> getUsersSearchHistory(User user);

}
