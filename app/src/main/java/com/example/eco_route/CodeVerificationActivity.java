package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CodeVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code_verification);

        Button verifyButton = findViewById(R.id.verifyCode);

        verifyButton.setOnClickListener(v -> {
            Intent intent = new Intent(
                    CodeVerificationActivity.this,
                    UserActivity.class
            );
            startActivity(intent);
        });
    }
}
