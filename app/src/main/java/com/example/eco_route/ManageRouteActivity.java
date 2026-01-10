package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eco_route.R;

import java.util.ArrayList;
import java.util.List;

public class ManageRouteActivity extends AppCompatActivity {

    Spinner routeSpinner;
    EditText originField, destinationField, fuelField, timeField, ticketField;
    Button updateRouteButton, resetButton, backButton;

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

        updateRouteButton = findViewById(R.id.updateRouteButton);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);

        setupRouteSpinner();
        setupButtons();
    }

    private void setupRouteSpinner() {

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

        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {

                if (position == 0) return;

                selectedRoute = RouteRepository.getRouteByPath(routeKeys.get(position));
                fillRouteData(selectedRoute);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fillRouteData(RouteModel route) {
        if (route == null) return;

        originField.setText(route.routePath.get(0));
        destinationField.setText(route.routePath.get(route.routePath.size() - 1));
        fuelField.setText(String.valueOf(route.fuel));
        timeField.setText(String.valueOf(route.time));
        ticketField.setText(String.valueOf(route.cost));
    }

    private void setupButtons() {

        updateRouteButton.setOnClickListener(v -> {

            if (selectedRoute == null) {
                Toast.makeText(this, "Select a route first", Toast.LENGTH_SHORT).show();
                return;
            }

            RouteModel updatedRoute = new RouteModel(
                    selectedRoute.businessName,
                    selectedRoute.routePath,
                    selectedRoute.distance,
                    Double.parseDouble(timeField.getText().toString()),
                    Double.parseDouble(fuelField.getText().toString()),
                    Double.parseDouble(ticketField.getText().toString())
            );

            String key = String.join(" → ", selectedRoute.routePath);
            RouteRepository.updateRoute(key, updatedRoute);

            Toast.makeText(this, "Route updated successfully", Toast.LENGTH_SHORT).show();
        });

        resetButton.setOnClickListener(v -> {
            routeSpinner.setSelection(0);
            originField.setText("");
            destinationField.setText("");
            fuelField.setText("");
            timeField.setText("");
            ticketField.setText("");
            selectedRoute = null;
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        });
    }
}
