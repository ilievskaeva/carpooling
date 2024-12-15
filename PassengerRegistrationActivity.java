package com.example.carpooling_f;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PassengerRegistrationActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button registerButton;
    private PassengerDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_registration);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        dbHelper = new PassengerDBHelper(this);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (isEmailExists(email)) {
                Toast.makeText(PassengerRegistrationActivity.this, "Емаилот веќе постои! Најави се!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent (PassengerRegistrationActivity.this, PassengerLoginActivity2.class);
                startActivity(intent);
            } else {
                registerPassenger(email, password);
            }
        });
    }

    private boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(PassengerDBHelper.TABLE_NAME,
                new String[]{PassengerDBHelper.COLUMN_EMAIL},
                PassengerDBHelper.COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean emailExists = cursor.getCount() > 0;
        cursor.close();
        return emailExists;
    }

    private void registerPassenger(String email, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PassengerDBHelper.COLUMN_EMAIL, email);
        values.put(PassengerDBHelper.COLUMN_PASSWORD, password);

        long newRowId = db.insert(PassengerDBHelper.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Toast.makeText(PassengerRegistrationActivity.this, "Регистрацијата е успешна!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}