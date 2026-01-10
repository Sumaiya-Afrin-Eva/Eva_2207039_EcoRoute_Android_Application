package com.example.eco_route;

import java.util.ArrayList;
import java.util.List;

public class RouteRepository {

    private static final List<RouteModel> routes = new ArrayList<>();
    public static String businessName = "";

    public static void addRoute(RouteModel route) {
        routes.add(route);
    }

    public static List<RouteModel> getRoutes() {
        return routes;
    }

    public static RouteModel getRouteByPath(String routeKey) {
        for (RouteModel route : routes) {
            String key = String.join(" → ", route.routePath);
            if (key.equals(routeKey)) {
                return route;
            }
        }
        return null;
    }

    public static void updateRoute(String routeKey, RouteModel updatedRoute) {
        for (int i = 0; i < routes.size(); i++) {
            String key = String.join(" → ", routes.get(i).routePath);
            if (key.equals(routeKey)) {
                routes.set(i, updatedRoute);
                return;
            }
        }
    }

}
