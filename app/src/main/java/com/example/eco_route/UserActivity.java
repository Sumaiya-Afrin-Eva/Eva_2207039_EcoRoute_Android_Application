package com.example.eco_route;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eco_route.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserActivity extends AppCompatActivity {

    Spinner originSpinner, destinationSpinner;
    Button searchButton;
    LinearLayout routesContainer;

    List<String> allPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(v -> {
            startActivity(new Intent(UserActivity.this, HistoryActivity.class));
        });

        routesContainer = findViewById(R.id.routesContainer);
        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        searchButton = findViewById(R.id.searchButton);

        allPlaces = Arrays.asList(
                "Select Origin",
                "Dhaka", "Chittagong", "Khulna", "Rajshahi", "Sylhet",
                "Barisal", "Rangpur", "Mymensingh", "Cumilla", "Noakhali",
                "Jessore", "Faridpur", "Bogura", "Dinajpur", "Pabna",
                "Kushtia", "Tangail", "Narayanganj", "Gazipur"
        );

        ArrayAdapter<String> originAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                allPlaces
        );
        originSpinner.setAdapter(originAdapter);

        setDestinationSpinner(null);

        originSpinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(android.widget.AdapterView<?> parent,
                                               View view, int position, long id) {
                        String selectedOrigin =
                                originSpinner.getSelectedItem().toString();
                        setDestinationSpinner(selectedOrigin);
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                });

        searchButton.setOnClickListener(v -> {

            String origin = originSpinner.getSelectedItem().toString();
            String destination = destinationSpinner.getSelectedItem().toString();

            if (origin.equals("Select Origin") ||
                    destination.equals("Select Destination")) {

                new AlertDialog.Builder(this)
                        .setTitle("Missing Input")
                        .setMessage("Please select both origin and destination.")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            routesContainer.removeAllViews();

            for (RouteModel route : RouteRepository.getRoutes()) {

                if (route.routePath.contains(origin)
                        && route.routePath.contains(destination)) {

                    View card = LayoutInflater.from(this)
                            .inflate(R.layout.item_route_card,
                                    routesContainer, false);

                    ((TextView) card.findViewById(R.id.businessName))
                            .setText(route.businessName);

                    ((TextView) card.findViewById(R.id.routeText))
                            .setText("Route: "
                                    + String.join(" → ", route.routePath));

                    ((TextView) card.findViewById(R.id.distanceText))
                            .setText("Distance: " + route.distance + " km");

                    ((TextView) card.findViewById(R.id.timeText))
                            .setText("Time: " + route.time + " hrs");

                    ((TextView) card.findViewById(R.id.fuelText))
                            .setText("Fuel: " + route.fuel + " L");

                    ((TextView) card.findViewById(R.id.ecoScoreText))
                            .setText("Eco Score: "
                                    + String.format("%.2f",
                                    route.getEcoScore()));

                    ((TextView) card.findViewById(R.id.costText))
                            .setText("Cost: Rs " + route.cost);
                    ((TextView) card.findViewById(R.id.restaurantText))
                            .setText("Restaurants: " + (route.restaurant != null && !route.restaurant.isEmpty() ? route.restaurant : "N/A"));

                    ((TextView) card.findViewById(R.id.fuelStationText))
                            .setText("Fuel Stations: " + (route.fuelStation != null && !route.fuelStation.isEmpty() ? route.fuelStation : "N/A"));

                    Button selectRouteBtn =
                            card.findViewById(R.id.selectRouteButton);

                    selectRouteBtn.setOnClickListener(b -> {

                        SharedPreferences prefs =
                                getSharedPreferences("EcoRoutePrefs",
                                        MODE_PRIVATE);

                        String email =
                                prefs.getString("current_user_email", "");

                        Set<String> history =
                                prefs.getStringSet("history_" + email,
                                        new HashSet<>());

                        Set<String> updatedHistory =
                                new HashSet<>(history);

                        String historyItem =
                                route.businessName + "|" +
                                        String.join(" → ", route.routePath) + "|" +
                                        route.distance + "|" +
                                        route.time + "|" +
                                        route.fuel + "|" +
                                        String.format("%.2f", route.getEcoScore()) + "|" +
                                        route.cost + "|" +
                                        (route.restaurant != null ? route.restaurant : "") + "|" +
                                        (route.fuelStation != null ? route.fuelStation : "");

                        updatedHistory.add(historyItem);


                        prefs.edit()
                                .putStringSet("history_" + email,
                                        updatedHistory)
                                .apply();

                        Toast.makeText(
                                UserActivity.this,
                                "Route saved to history",
                                Toast.LENGTH_SHORT
                        ).show();
                    });

                    routesContainer.addView(card);
                }
            }
        });
    }

    private void setDestinationSpinner(String selectedOrigin) {

        List<String> destinationList = new ArrayList<>();
        destinationList.add("Select Destination");

        for (String place : allPlaces) {
            if (!place.equals(selectedOrigin)
                    && !place.equals("Select Origin")) {
                destinationList.add(place);
            }
        }

        ArrayAdapter<String> destinationAdapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        destinationList
                );

        destinationSpinner.setAdapter(destinationAdapter);
    }
}
