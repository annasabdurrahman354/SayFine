package com.healthymeals.sayfine.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.fragment.AboutFragment;
import com.healthymeals.sayfine.fragment.AccountFragment;
import com.healthymeals.sayfine.fragment.HomeFragment;
import com.healthymeals.sayfine.fragment.OrderFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavBar = findViewById(R.id.mainBottomNavBar);
        loadFragment(new HomeFragment());
        bottomNavBar.setOnNavigationItemSelectedListener(this);

    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.navHome:
                fragment = new HomeFragment();
                break;
            case R.id.navOrder:
                fragment = new OrderFragment();
                break;
            case R.id.navAccount:
                fragment = new AccountFragment();
                break;
            case R.id.navAbout:
                fragment = new AboutFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainFrameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}