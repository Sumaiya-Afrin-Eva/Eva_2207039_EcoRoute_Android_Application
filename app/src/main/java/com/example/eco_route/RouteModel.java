package com.example.eco_route;

import java.util.List;

public class RouteModel {

    public String businessName;
    public List<String> routePath;
    public double distance;
    public double time;
    public double fuel;
    public double cost;

    public RouteModel(String businessName, List<String> routePath,
                      double distance, double time, double fuel, double cost) {
        this.businessName = businessName;
        this.routePath = routePath;
        this.distance = distance;
        this.time = time;
        this.fuel = fuel;
        this.cost = cost;
    }

    public double getEcoScore() {
        return 1000 / (time * fuel * cost);
    }
}
