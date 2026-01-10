package com.example.eco_route;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecorouteandroid.R;

public class EmailLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_login);

        EditText emailEditText = findViewById(R.id.emailEditText);
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Incomplete Information")
                        .setMessage("Please enter a valid email.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                SharedPreferences prefs =
                        getSharedPreferences("EcoRoutePrefs", MODE_PRIVATE);
                prefs.edit().putString("current_user_email", email).apply();

                startActivity(new Intent(this, CodeVerificationActivity.class));
            }
        });
    }
}
