package com.jba.service;

import com.jba.dao2.route.entity.PopularRoute;
import com.jba.dao2.route.entity.Route;
import com.jba.dao2.search.dao.SearchDAO;
import com.jba.dao2.search.entity.Search;
import com.jba.dao2.search.entity.SearchHistory;
import com.jba.dao2.user.dao.UserDAO;
import com.jba.service.ifs.SearchService;
import com.jba.service.ifs.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDAO searchDAO;

    @Autowired
    private UserService userService;

    @Override
    public Route getRouteById(Integer routeId) {
        return searchDAO.getRouteById(routeId);
    }

    @Override
    public List<Route> getAllRoutes() {
        return searchDAO.getAllRoutes();
    }

    @Override
    public Route addRoute(Route route) {
        route.setRouteId(0);
        return searchDAO.addRoute(route);
    }

    @Override
    public Route updateRoute(Route route) {
        return searchDAO.updateRoute(route);
    }

    @Override
    public PopularRoute updatePopularRoute(Integer routeId, PopularRoute popularRoute) {
        return searchDAO.updatePopularRoute(getRouteById(routeId), popularRoute);
    }

    @Override
    public SearchHistory registerNewSearch(Integer userId, Search search) {
        return searchDAO.registerNewSearch(userService.getUser(userId), search);
    }

    @Override
    public List<SearchHistory> getUsersSearchHistory(Integer userId) {
        return searchDAO.getUsersSearchHistory(userService.getUser(userId));
    }

    @Override
    public PopularRoute getPopularRouteById(Integer id) {
        return searchDAO.getPopularRouteById(id);
    }

    @Override
    public List<PopularRoute> getAllPopularRoutes() {
        return searchDAO.getAllPopularRoutes();
    }

    @Override
    public PopularRoute getPopularRouteByRoute(Integer routeId) {
        return searchDAO.getPopularRouteByRoute(getRouteById(routeId));
    }
}
