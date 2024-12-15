package com.example.carpooling_f;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DriverRegistrationActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private DriverDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new DriverDatabaseHelper(this);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(DriverRegistrationActivity.this, "Пополни ги сите полиња!", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.isEmailExists(email)) {
                    Toast.makeText(DriverRegistrationActivity.this, "Емаилот веќе постои! Најави се! ", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent (DriverRegistrationActivity.this, DriverLoginActivity2.class);
                    startActivity(intent);
                } else {
                    dbHelper.addDriver(email, password);
                    Toast.makeText(DriverRegistrationActivity.this, "Успешна регистрација! Сега можеш да се најавиш! ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

        });
    }
}