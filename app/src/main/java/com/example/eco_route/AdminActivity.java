package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    Spinner originSpinner, destinationSpinner, routeSpinner;
    LinearLayout selectedRoutesContainer;
    EditText fuelField, timeField, distanceField, priceField, restaurantField, fuelStationField;
    Button saveRouteButton, resetButton;

    List<String> allPlaces;
    List<String> selectedRoutes = new ArrayList<>();
    ArrayAdapter<String> routeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        Button manageRouteButton = findViewById(R.id.manageRouteButton);

        manageRouteButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ManageRouteActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });


        originSpinner = findViewById(R.id.originSpinner);
        destinationSpinner = findViewById(R.id.destinationSpinner);
        routeSpinner = findViewById(R.id.routeSpinner);
        selectedRoutesContainer = findViewById(R.id.selectedRoutesContainer);

        fuelField = findViewById(R.id.fuelField);
        timeField = findViewById(R.id.timeField);
        distanceField = findViewById(R.id.distanceField);
        priceField = findViewById(R.id.priceField);
        restaurantField = findViewById(R.id.restaurantField);
        fuelStationField = findViewById(R.id.fuelStationField);

        saveRouteButton = findViewById(R.id.saveRouteButton);
        resetButton = findViewById(R.id.resetButton);

        allPlaces = new ArrayList<>();
        allPlaces.add("Select Origin");
        allPlaces.addAll(Arrays.asList(getResources().getStringArray(R.array.origin_places)));

        setupOriginSpinner();
        setupRouteSpinner();
        setupButtons();
    }

    private void setupOriginSpinner() {
        ArrayAdapter<String> originAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allPlaces);

        originSpinner.setAdapter(originAdapter);
        originSpinner.setSelection(0);

        setupEmptyDestinationSpinner();

        originSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    setupEmptyDestinationSpinner();
                    return;
                }

                String selectedOrigin = allPlaces.get(position);

                List<String> destinationList = new ArrayList<>(allPlaces);
                destinationList.remove(0);
                destinationList.remove(selectedOrigin);
                destinationList.add(0, "Select Destination");

                ArrayAdapter<String> destinationAdapter =
                        new ArrayAdapter<>(AdminActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, destinationList);

                destinationSpinner.setAdapter(destinationAdapter);
                destinationSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupEmptyDestinationSpinner() {
        List<String> placeholder = new ArrayList<>();
        placeholder.add("Select Destination");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item, placeholder);

        destinationSpinner.setAdapter(adapter);
        destinationSpinner.setSelection(0);
    }

    private void setupRouteSpinner() {
        List<String> routeList = new ArrayList<>();
        routeList.add("Select Route");
        routeList.addAll(allPlaces.subList(1, allPlaces.size()));

        routeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, routeList);

        routeSpinner.setAdapter(routeAdapter);
        routeSpinner.setSelection(0);

        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) return;

                String route = routeList.get(position);

                if (!selectedRoutes.contains(route)) {
                    selectedRoutes.add(route);
                    addRouteChip(route);
                }

                routeSpinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void addRouteChip(String route) {
        View chip = LayoutInflater.from(this)
                .inflate(R.layout.item_selection, selectedRoutesContainer, false);

        TextView routeName = chip.findViewById(R.id.routeName);
        ImageView remove = chip.findViewById(R.id.removeRoute);

        routeName.setText(route);

        remove.setOnClickListener(v -> {
            selectedRoutes.remove(route);
            selectedRoutesContainer.removeView(chip);
        });

        selectedRoutesContainer.addView(chip);
    }
    private void setupButtons() {
        saveRouteButton.setOnClickListener(v -> {
            if (originSpinner.getSelectedItemPosition() == 0 ||
                    destinationSpinner.getSelectedItemPosition() == 0 ||
                    fuelField.getText().toString().isEmpty() ||
                    timeField.getText().toString().isEmpty() ||
                    distanceField.getText().toString().isEmpty() ||
                    priceField.getText().toString().isEmpty() ||
                    selectedRoutes.isEmpty()) {

                new AlertDialog.Builder(this)
                        .setTitle("Missing Information")
                        .setMessage("Please complete all fields and make valid selections.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                RouteModel route = new RouteModel(
                        RouteRepository.businessName,
                        new ArrayList<>(selectedRoutes),
                        Double.parseDouble(distanceField.getText().toString()),
                        Double.parseDouble(timeField.getText().toString()),
                        Double.parseDouble(fuelField.getText().toString()),
                        Double.parseDouble(priceField.getText().toString()),
                        restaurantField.getText().toString().trim(),
                        fuelStationField.getText().toString().trim()
                );

                RouteRepository.addRoute(route);

                Toast.makeText(this, "Route saved successfully", Toast.LENGTH_SHORT).show();
            }
        });

        resetButton.setOnClickListener(v -> {
            fuelField.setText("");
            timeField.setText("");
            distanceField.setText("");
            priceField.setText("");
            restaurantField.setText("");
            fuelStationField.setText("");
            selectedRoutes.clear();
            selectedRoutesContainer.removeAllViews();

            originSpinner.setSelection(0);
            setupEmptyDestinationSpinner();
            routeSpinner.setSelection(0);
        });
    }
}
