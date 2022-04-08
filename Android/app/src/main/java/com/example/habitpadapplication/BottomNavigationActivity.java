package com.example.habitpadapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_home:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_tip:
                        startActivity(new Intent(getApplicationContext(), ViewTipActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.bottom_appointment:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

                    case R.id.bottom_profile:
//                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;

                }
                return false;
            }
        });
    }
}