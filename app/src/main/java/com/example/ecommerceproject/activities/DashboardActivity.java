package com.example.ecommerceproject.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ecommerceproject.R;
import com.example.ecommerceproject.databinding.ActivityDashboardBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;
    private BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_cart, R.id.navigation_order, R.id.navigation_user_information)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(
                new NavigationBarView.OnItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home: {
                                navController.navigate(R.id.navigation_home);
                                return true;
                            }
                            case R.id.navigation_cart: {
                                navController.navigate(R.id.navigation_cart);
                                return true;
                            }
                            case R.id.navigation_order: {
                                navController.navigate(R.id.navigation_order);
                                return true;
                            }
                            case R.id.navigation_user_information: {
                                navController.navigate(R.id.navigation_user_information);
                                return true;
                            }
                            default: {
                                return false;
                            }
                        }
                    }
                });
    }

}