package com.example.signtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class Home extends AppCompatActivity {
    private MeowBottomNavigation bottomNavigation;
    // give ids to fragments
    private final static int Map = 1;
    private final static int Garage = 2;
    private final static int Profile = 3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigation = findViewById(R.id.bottom_navi);
        bottomNavigation.add(new MeowBottomNavigation.Model(Map, R.drawable.map));
        bottomNavigation.add(new MeowBottomNavigation.Model(Garage, R.drawable.ic_garage));
        bottomNavigation.add(new MeowBottomNavigation.Model(Profile, R.drawable.ic_profile));
        // set current selected Fragment
        bottomNavigation.show(Garage, true);

        // add methods
        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // Initialize Fragments according to its ids
                Fragment fragment = null;
                if (model.getId() == 1) {
                    fragment = new Map_Fragment();
                } else if (model.getId() == 2) {
                    fragment = new Garag_Fragment();
                } else {
                    fragment = new Profile_Fragment();
                }
                // create Method for load and replace fragments
                LoadAndReplaceFragment(fragment);
                return null;
            }
        });
    }

    private void LoadAndReplaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, null)
                .commit();
    }
}
