package com.ricky.bluejackpharmacy.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    private EditText etEmail, etPassword;
    private Button btnLogin, btnToRegister;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        initializeVariable();
        setListeners();
    }

    private void initializeVariable() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        tvError = findViewById(R.id.tvError);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            validateCredentials(email, password);
        });

        btnToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void validateCredentials(String email, String password) {
        if (email.isEmpty()) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Email must be filled");
        }
        else if (password.isEmpty()) {
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("Password must be filled");
        }
        else {
            Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);
            String verifiedStatus = databaseHelper.checkVerified(email);

            if (checkCredentials) {
                if (verifiedStatus.equals("yes")) {
                    Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(LoginActivity.this, OTPActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}