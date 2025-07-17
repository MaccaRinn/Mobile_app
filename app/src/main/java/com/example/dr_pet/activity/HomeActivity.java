package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dr_pet.R;
import com.example.dr_pet.fragment.AccountFragment;
import com.example.dr_pet.fragment.HomeFragment;
import com.example.dr_pet.fragment.PetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.dr_pet.model.AuthManager;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_menu);

        bottomNavigationView.setSelectedItemId(R.id.nav_home);


        Map<Integer, Fragment> fragmentMap = new HashMap<>();
        fragmentMap.put(R.id.nav_home, new HomeFragment());
        fragmentMap.put(R.id.nav_profile, new AccountFragment());
        fragmentMap.put(R.id.nav_petprofile, new PetFragment());



        loadFragment(fragmentMap.get(R.id.nav_home));

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_profile) {
                if (!AuthManager.isLoggedIn(this)) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return false; // Ngăn fragment profile load khi chưa login
                }
            }

            Fragment fragment = fragmentMap.get(itemId);
            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }

            return false;
        });

    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


}









