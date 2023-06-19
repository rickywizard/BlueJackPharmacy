package com.ricky.bluejackpharmacy.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;

import java.util.Locale;
import java.util.Random;

public class OTPActivity extends AppCompatActivity {

    private String OTP = generateOTP();
    private String PHONE_NUMBER = "5544";
    private String message = "is your verification code. Do not share to anyone.";
    private SmsManager smsManager;
    private EditText etOTP;
    private Button btnCheckOTP;
    private String email;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        initializeVariable();

        checkSMSPermission();
        sendOTP(this);

        btnCheckOTP.setOnClickListener(view -> {
            String input = etOTP.getText().toString();
            checkOTP(input, email);
        });
    }

    private void initializeVariable() {
        databaseHelper = new DatabaseHelper(this);
        etOTP = findViewById(R.id.etOTP);
        btnCheckOTP = findViewById(R.id.btnCheckOTP);
        email = getIntent().getExtras().getString("email");
    }

    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(OTPActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OTPActivity.this, new String[] { Manifest.permission.SEND_SMS }, 0);
        }
    }

    private String generateOTP() {
        Random random = new Random();
        int digit = random.nextInt(999999);

        return String.format(Locale.ENGLISH, "%06d", digit);
    }

    private void sendOTP(Context context) {
        smsManager = context.getSystemService(SmsManager.class);
        String completeMsg = OTP + " " + message;
        smsManager.sendTextMessage(PHONE_NUMBER, null, completeMsg, null, null);
    }

    private void checkOTP(String input, String email) {
        if (input.isEmpty()) {
            Toast.makeText(this, "Field must be filled!", Toast.LENGTH_SHORT).show();
        }
        else {
            if (input.equals(OTP)) {
                Boolean updateVerified = databaseHelper.updateVerifiedStatus(email);
                if (updateVerified) {
                    Toast.makeText(this, "You are verified!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OTPActivity.this, HomeActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Verification failed. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Wrong OTP", Toast.LENGTH_SHORT).show();
            }
        }
    }
}