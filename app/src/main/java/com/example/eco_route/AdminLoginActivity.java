package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eco_route.R;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        EditText businessName = findViewById(R.id.businessName);
        EditText ownerName = findViewById(R.id.ownerName);
        EditText vehicleType = findViewById(R.id.vehicleType);
        EditText businessEmail = findViewById(R.id.businessEmail);
        EditText phoneNumber = findViewById(R.id.phoneNumber);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {

            if (businessName.getText().toString().trim().isEmpty() ||
                    ownerName.getText().toString().trim().isEmpty() ||
                    vehicleType.getText().toString().trim().isEmpty() ||
                    businessEmail.getText().toString().trim().isEmpty() ||
                    phoneNumber.getText().toString().trim().isEmpty()) {

                new AlertDialog.Builder(AdminLoginActivity.this)
                        .setTitle("Incomplete Form")
                        .setMessage("Please fill in all required fields before registering.")
                        .setPositiveButton("OK", null)
                        .show();

            } else {
                RouteRepository.businessName = businessName.getText().toString();

                Intent intent = new Intent(
                        AdminLoginActivity.this,
                        AdminActivity.class
                );
                startActivity(intent);
            }
        });
    }
}
