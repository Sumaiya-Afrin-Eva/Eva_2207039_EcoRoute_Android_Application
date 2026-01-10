package com.example.eco_route;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eco_route.R;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);

        Button btnContinueWithEmail = findViewById(R.id.emailButton);

        btnContinueWithEmail.setOnClickListener(v -> {
            Intent intent = new Intent(UserLoginActivity.this, EmailLoginActivity.class);
            startActivity(intent);
        });
    }
}
