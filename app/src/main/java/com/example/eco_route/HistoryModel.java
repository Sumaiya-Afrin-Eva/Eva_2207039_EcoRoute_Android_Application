package com.example.eco_route;
public class HistoryModel {

    public String businessName;
    public String routePath;
    public String distance;
    public String time;
    public String fuel;
    public String ecoScore;
    public String cost;
    public String rawData;

    public HistoryModel(String rawData) {
        this.rawData = rawData;

        String[] parts = rawData.split("\\|");

        businessName = parts[0];
        routePath = parts[1];
        distance = parts[2];
        time = parts[3];
        fuel = parts[4];
        ecoScore = parts[5];
        cost = parts[6];
    }
}
