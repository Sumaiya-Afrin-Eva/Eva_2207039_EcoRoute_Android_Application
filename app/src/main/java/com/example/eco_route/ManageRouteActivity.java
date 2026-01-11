package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ManageRouteActivity extends AppCompatActivity {

    Spinner routeSpinner;
    EditText originField, destinationField, fuelField, timeField, ticketField, restaurantField, fuelStationField;
    Button updateRouteButton, resetButton, backButton, deleteRouteButton;

    RouteModel selectedRoute;
    List<String> routeKeys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_route);

        routeSpinner = findViewById(R.id.routeSpinner);
        originField = findViewById(R.id.originField);
        destinationField = findViewById(R.id.destinationField);
        fuelField = findViewById(R.id.fuelField);
        timeField = findViewById(R.id.timeField);
        ticketField = findViewById(R.id.ticketField);
        restaurantField = findViewById(R.id.restaurantField);
        fuelStationField = findViewById(R.id.fuelStationField);

        updateRouteButton = findViewById(R.id.updateRouteButton);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);
        deleteRouteButton = findViewById(R.id.deleteRouteButton);

        setupRouteSpinner();
        setupButtons();
    }

    private void setupRouteSpinner() {
        refreshRouteSpinner();

        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (position == 0) {
                    selectedRoute = null;
                    return;
                }

                selectedRoute = RouteRepository.getRouteByPath(routeKeys.get(position));
                fillRouteData(selectedRoute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void refreshRouteSpinner() {
        routeKeys.clear();
        routeKeys.add("Select Route");

        for (RouteModel route : RouteRepository.getRoutes()) {
            routeKeys.add(String.join(" → ", route.routePath));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                routeKeys
        );

        routeSpinner.setAdapter(adapter);
        routeSpinner.setSelection(0);
    }

    private void fillRouteData(RouteModel route) {
        if (route == null) return;

        originField.setText(route.routePath.get(0));
        destinationField.setText(route.routePath.get(route.routePath.size() - 1));
        fuelField.setText(String.valueOf(route.fuel));
        timeField.setText(String.valueOf(route.time));
        ticketField.setText(String.valueOf(route.cost));
        restaurantField.setText(route.restaurant != null ? route.restaurant : "");
        fuelStationField.setText(route.fuelStation != null ? route.fuelStation : "");
    }

    private void setupButtons() {
        updateRouteButton.setOnClickListener(v -> {
            if (selectedRoute == null) {
                Toast.makeText(this, "Select a route first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validateInputs()) {
                return;
            }

            RouteModel updatedRoute = new RouteModel(
                    selectedRoute.businessName,
                    selectedRoute.routePath,
                    selectedRoute.distance,
                    Double.parseDouble(timeField.getText().toString()),
                    Double.parseDouble(fuelField.getText().toString()),
                    Double.parseDouble(ticketField.getText().toString()),
                    restaurantField.getText().toString().trim(),
                    fuelStationField.getText().toString().trim()
            );

            String key = String.join(" → ", selectedRoute.routePath);
            RouteRepository.updateRoute(key, updatedRoute);

            Toast.makeText(this, "Route updated successfully", Toast.LENGTH_SHORT).show();

            // Refresh the spinner to show any changes
            refreshRouteSpinner();
        });

        // DELETE ROUTE BUTTON FUNCTIONALITY
        deleteRouteButton.setOnClickListener(v -> {
            if (selectedRoute == null) {
                Toast.makeText(this, "Select a route first", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Delete Route")
                    .setMessage("Are you sure you want to delete this route?\n\n" +
                            "Route: " + String.join(" → ", selectedRoute.routePath) + "\n" +
                            "Business: " + selectedRoute.businessName)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Get the route key for deletion
                        String routeKey = String.join(" → ", selectedRoute.routePath);

                        // Delete from repository (you need to add deleteRoute method to RouteRepository)
                        deleteRouteFromRepository(routeKey);

                        // Clear the form
                        clearForm();

                        // Refresh the spinner
                        refreshRouteSpinner();

                        Toast.makeText(ManageRouteActivity.this,
                                "Route deleted successfully",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        resetButton.setOnClickListener(v -> {
            clearForm();
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        });
    }

    private void deleteRouteFromRepository(String routeKey) {
        // Use the new delete method in RouteRepository
        RouteRepository.deleteRouteByKey(routeKey);
    }
    private boolean validateInputs() {
        if (fuelField.getText().toString().isEmpty() ||
                timeField.getText().toString().isEmpty() ||
                ticketField.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        routeSpinner.setSelection(0);
        originField.setText("");
        destinationField.setText("");
        fuelField.setText("");
        timeField.setText("");
        ticketField.setText("");
        restaurantField.setText("");
        fuelStationField.setText("");
        selectedRoute = null;
    }
}