package com.ricky.bluejackpharmacy.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ricky.bluejackpharmacy.R;

public class HomeActivity extends AppCompatActivity {

    Toolbar toolbar;
    BottomNavigationView navigationView;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initializeVariable();
        replaceFragment(new HomeFragment());

        navigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home_menu) {
                replaceFragment(new HomeFragment());
            }
            else if (item.getItemId() == R.id.transaction_menu) {
                replaceFragment(new TransactionFragment());
            }

            return true;
        });
    }

    private void initializeVariable() {
        email = getIntent().getExtras().getString("email");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        navigationView = findViewById(R.id.botNav);

        setSupportActionBar(toolbar);
    }

    private void replaceFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putString("user", email);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Toast.makeText(this, "You are logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if (item.getItemId() == R.id.info) {
            startActivity(new Intent(HomeActivity.this, InfoActivity.class));
        }

        return true;
    }
}