package com.healthymeals.sayfine.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.fragment.AboutFragment;
import com.healthymeals.sayfine.fragment.AccountFragment;
import com.healthymeals.sayfine.fragment.HomeFragment;
import com.healthymeals.sayfine.fragment.OrderFragment;
import com.healthymeals.sayfine.model.User;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavBar = findViewById(R.id.mainBottomNavBar);
        bottomNavBar.setOnNavigationItemSelectedListener(this);
        changeFragment(new HomeFragment(), HomeFragment.class
                .getSimpleName());
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        switch (item.getItemId()){
            case R.id.navHome:
                changeFragment(new HomeFragment(), HomeFragment.class
                        .getSimpleName());
                break;
            case R.id.navOrder:
                changeFragment(new OrderFragment(),OrderFragment.class
                        .getSimpleName());
                break;
            case R.id.navAccount:
                changeFragment(new AccountFragment(), AccountFragment.class
                        .getSimpleName());;
                break;
            case R.id.navAbout:
                changeFragment(new AboutFragment(), AboutFragment.class
                        .getSimpleName());
                break;
        }

        return true;
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

    public void changeFragment(Fragment fragment, String tagFragmentName) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment currentFragment = mFragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragmentTemp = mFragmentManager.findFragmentByTag(tagFragmentName);
        if (fragmentTemp == null) {
            fragmentTemp = fragment;
            fragmentTransaction.add(R.id.mainFrameLayout, fragmentTemp, tagFragmentName);
        } else {
            fragmentTransaction.show(fragmentTemp);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}