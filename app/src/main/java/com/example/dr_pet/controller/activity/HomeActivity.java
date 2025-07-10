package com.example.dr_pet.controller.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.dr_pet.Model.SessionManager;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.Fragment.AccountFragment;
import com.example.dr_pet.controller.Fragment.HomeFragment;
import com.example.dr_pet.controller.Fragment.PetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.dr_pet.AuthManager;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    public SessionManager sessionManager;

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

            if (itemId == R.id.nav_profile && !AuthManager.isLoggedIn(this)) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                return false;
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









