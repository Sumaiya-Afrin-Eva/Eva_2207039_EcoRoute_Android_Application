package com.example.eco_route;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<HistoryModel> historyList;
    private final Context context;
    private final String userEmail;

    public HistoryAdapter(Context context,
                          List<HistoryModel> historyList,
                          String userEmail) {
        this.context = context;
        this.historyList = historyList;
        this.userEmail = userEmail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HistoryModel model = historyList.get(position);

        holder.businessName.setText(model.businessName);
        holder.routeText.setText("Route: " + model.routePath);
        holder.distanceText.setText("Distance: " + model.distance + " km");
        holder.timeText.setText("Time: " + model.time + " hrs");
        holder.fuelText.setText("Fuel: " + model.fuel + " L");
        holder.ecoScoreText.setText("Eco Score: " + model.ecoScore);
        holder.costText.setText("Cost: Rs " + model.cost);

        if (holder.restaurantText != null) {
            holder.restaurantText.setText("Restaurants: " +
                    (model.restaurant != null && !model.restaurant.isEmpty() ? model.restaurant : "N/A"));
        }

        if (holder.fuelStationText != null) {
            holder.fuelStationText.setText("Fuel Stations: " +
                    (model.fuelStation != null && !model.fuelStation.isEmpty() ? model.fuelStation : "N/A"));
        }

        holder.deleteButton.setOnClickListener(v -> {

            SharedPreferences prefs =
                    context.getSharedPreferences("EcoRoutePrefs",
                            Context.MODE_PRIVATE);

            Set<String> historySet =
                    prefs.getStringSet("history_" + userEmail,
                            new HashSet<>());

            Set<String> updatedSet = new HashSet<>(historySet);
            updatedSet.remove(model.rawData);

            prefs.edit()
                    .putStringSet("history_" + userEmail, updatedSet)
                    .apply();

            historyList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, historyList.size());
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView businessName, routeText, distanceText,
                timeText, fuelText, ecoScoreText, costText,
                restaurantText, fuelStationText;
        Button deleteButton;

        ViewHolder(View itemView) {
            super(itemView);

            businessName = itemView.findViewById(R.id.businessName);
            routeText = itemView.findViewById(R.id.routeText);
            distanceText = itemView.findViewById(R.id.distanceText);
            timeText = itemView.findViewById(R.id.timeText);
            fuelText = itemView.findViewById(R.id.fuelText);
            ecoScoreText = itemView.findViewById(R.id.ecoScoreText);
            costText = itemView.findViewById(R.id.costText);
            restaurantText = itemView.findViewById(R.id.restaurantText);
            fuelStationText = itemView.findViewById(R.id.fuelStationText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}