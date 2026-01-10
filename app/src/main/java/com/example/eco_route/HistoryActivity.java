package com.example.eco_route;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eco_route.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<HistoryModel> historyList;
    HistoryAdapter adapter;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.historyRecyclerView);
        Button backButton = findViewById(R.id.backButton);
        Button deleteAllButton = findViewById(R.id.deleteAllButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs =
                getSharedPreferences("EcoRoutePrefs", MODE_PRIVATE);

        email = prefs.getString("current_user_email", "");

        Set<String> historySet =
                prefs.getStringSet("history_" + email,
                        new HashSet<>());

        historyList = new ArrayList<>();
        for (String item : historySet) {
            historyList.add(new HistoryModel(item));
        }

        adapter = new HistoryAdapter(this, historyList, email);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, UserActivity.class));
            finish();
        });

        deleteAllButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete All History")
                    .setMessage("Are you sure you want to delete all history?")
                    .setPositiveButton("Yes", (d, w) -> {

                        prefs.edit()
                                .remove("history_" + email)
                                .apply();

                        historyList.clear();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
