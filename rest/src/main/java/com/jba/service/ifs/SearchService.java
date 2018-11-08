package com.jba.service.ifs;

import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.dao2.user.enitity.User;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    Route getRouteById(Integer routeId);
    List<Route> getAllRoutes();
    Route addRoute(Route route);
    Route updateRoute(Route route);
    PopularRoute updatePopularRoute(Route route, @Nullable PopularRoute popularRoute);
    SearchHistory registerNewSearch(Integer userId, Search search);
    List<SearchHistory> getUsersSearchHistory(Integer userId);
    PopularRoute getPopularRouteById(Integer id);
    PopularRoute getPopularRouteByRoute(Integer routeId);
}
