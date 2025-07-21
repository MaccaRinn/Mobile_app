package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import com.example.dr_pet.R;
import com.example.dr_pet.fragment.AccountFragment;
import com.example.dr_pet.fragment.HomeFragment;
import com.example.dr_pet.fragment.PetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.dr_pet.model.AuthManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        checkIfUserIsAdmin();

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


    private void checkIfUserIsAdmin() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Account").child(uid);
        userRef.child("role").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.getValue(String.class);
                    if ("ROLE_ADMIN".equals(role)) {
                        Intent intent = new Intent(HomeActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }


}









