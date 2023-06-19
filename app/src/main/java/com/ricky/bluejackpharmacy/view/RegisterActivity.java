package com.ricky.bluejackpharmacy.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    private EditText etName, etEmail, etPassword, etConfirm, etPhone;
    private Button btnRegister, btnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        initializeVariable();
        setListeners();
    }

    private void initializeVariable() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnToLogin = findViewById(R.id.btnToLogin);
    }

    private void setListeners() {
        btnRegister.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirm = etConfirm.getText().toString();
            String phone = etPhone.getText().toString();

            if (validateInput(name, email, password, confirm, phone)) {
                if (!databaseHelper.checkEmail(email)) {
                    Boolean insert = databaseHelper.insertUser(email, name, password, phone);

                    if (insert) {
                        Toast.makeText(this, "Registration success", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Registration failed. Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Email already taken. Use other email or login instead", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnToLogin.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private Boolean validateInput(String name, String email, String password, String confirm, String phone) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            if (name.length() < 5) {
                Toast.makeText(this, "Name must be at least 5 characters", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!email.matches(".+@.+\\.com$")) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return false;
            }
            Pattern pattern;
            Matcher matcher;

            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$";

            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                Toast.makeText(this, "Password must be alphanumeric", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!password.equals(confirm)) {
                Toast.makeText(this, "Password and confirm password are not same", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }
}