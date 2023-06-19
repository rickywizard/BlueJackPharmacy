package com.ricky.bluejackpharmacy.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ricky.bluejackpharmacy.R;
import com.ricky.bluejackpharmacy.model.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicineDetailActivity extends AppCompatActivity {

    public static final String MED_IMG = "med_img";
    public static final String MED_NAME = "med_name";
    public static final String MED_FAC = "med_fac";
    public static final String MED_PRICE = "med_price";
    public static final String MED_DESC = "med_desc";

    private String loginUserEmail;
    private ImageView imgMed;
    private TextView tvMedName, tvPrice, tvManufacturer, tvDescription;
    private EditText etQuantity;
    private Button btnBuy;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_detail);

        initializeVariable();

        btnBuy.setOnClickListener(view -> {
            String quantity = etQuantity.getText().toString();

            if (quantity.isEmpty()) {
                Toast.makeText(this, "Quantity can not be empty", Toast.LENGTH_SHORT).show();
            }
            else if (quantity.equals("0")) {
                Toast.makeText(this, "Quantity must more than 0", Toast.LENGTH_SHORT).show();
            }
            else {
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                Integer medicineID = databaseHelper.getMedicineID(getIntent().getStringExtra(MED_NAME));
                Integer userID = databaseHelper.getUserID(loginUserEmail);
                Integer intQuantity = Integer.parseInt(quantity);

                // insert into transactions database
                Boolean insertTransaction = databaseHelper.insertTransaction(medicineID, userID, currentDate, intQuantity);

                if (insertTransaction) {
                    Toast.makeText(this, "You bought " + quantity + " " + tvMedName.getText().toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MedicineDetailActivity.this, HomeActivity.class);
                    intent.putExtra("email", loginUserEmail);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Buy failed. Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeVariable() {
        loginUserEmail = getIntent().getStringExtra("user");
        databaseHelper = new DatabaseHelper(this);

        imgMed = findViewById(R.id.medImgReceived);
        String imgUrl = getIntent().getStringExtra(MED_IMG);
        Picasso.get()
                .load(imgUrl)
                .into(imgMed);

        tvMedName = findViewById(R.id.medNameReceived);
        tvMedName.setText(getIntent().getStringExtra(MED_NAME));

        tvPrice = findViewById(R.id.medPriceReceived);
        tvPrice.setText(getIntent().getStringExtra(MED_PRICE));

        tvManufacturer = findViewById(R.id.medFacReceived);
        tvManufacturer.setText(getIntent().getStringExtra(MED_FAC));

        tvDescription = findViewById(R.id.medDescReceived);
        tvDescription.setText(getIntent().getStringExtra(MED_DESC));

        etQuantity = findViewById(R.id.etQuantity);
        btnBuy = findViewById(R.id.btnBuy);
    }
}