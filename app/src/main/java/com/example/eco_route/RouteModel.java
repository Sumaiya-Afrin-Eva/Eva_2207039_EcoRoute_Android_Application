package com.example.eco_route;

import java.util.List;

public class RouteModel {
    public String businessName;
    public List<String> routePath;
    public double distance;
    public double time;
    public double fuel;
    public double cost;
    public String restaurant;
    public String fuelStation;

    public RouteModel(String businessName, List<String> routePath,
                      double distance, double time, double fuel, double cost,
                      String restaurant, String fuelStation) {
        this.businessName = businessName;
        this.routePath = routePath;
        this.distance = distance;
        this.time = time;
        this.fuel = fuel;
        this.cost = cost;
        this.restaurant = restaurant;
        this.fuelStation = fuelStation;
    }

    public RouteModel(String businessName, List<String> routePath,
                      double distance, double time, double fuel, double cost) {
        this(businessName, routePath, distance, time, fuel, cost, "", "");
    }

    public double getEcoScore() {
        return 1000 / (time * fuel * cost);
    }
}