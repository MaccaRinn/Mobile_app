package com.example.dr_pet.activity;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dr_pet.R;
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

public class AdminActivity extends AppCompatActivity {

    Button btn_adminShop, btn_adminGrooming, btn_adminBoarding, btn_adminMedical, btn_adminLogout;
    TextView txtTotalPet, txtBoarding, txtMedical, txtGrooming,txtShopping;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        //Mapping
        btn_adminShop = findViewById(R.id.btn_adminShop);
        btn_adminGrooming = findViewById(R.id.btn_adminGrooming);
        btn_adminBoarding = findViewById(R.id.btn_adminBoarding);
        btn_adminMedical = findViewById(R.id.btn_adminMedical);
        btn_adminLogout = findViewById(R.id.btn_adminLogout);

        txtTotalPet = findViewById(R.id.txtTotalPet);
        txtBoarding = findViewById(R.id.txtBoarding);
        txtGrooming = findViewById(R.id.txtGrooming);

        txtMedical = findViewById(R.id.txtMedical);
        txtShopping = findViewById(R.id.txtShopping);

        // check admin user and load data from database
        checkAdminPermissionAndLoadData();

        btn_adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                AuthManager.setLoggedIn(AdminActivity.this, false);
                startActivity(new Intent(AdminActivity.this, HomeActivity.class));
                Toast.makeText(AdminActivity.this, "Log out", Toast.LENGTH_SHORT).show();
            }
        });



        btn_adminBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        btn_adminGrooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AdminGroomingActivity.class));
            }
        });


        btn_adminMedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        btn_adminShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }

    private void checkAdminPermissionAndLoadData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("Auth", "User not authenticated");
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String uid = user.getUid();
        Log.d("Auth", "Current user UID: " + uid);

        // Kiểm tra role của user hiện tại
        DatabaseReference currentUserRef = FirebaseDatabase.getInstance()
                .getReference("Account")
                .child(uid)
                .child("role");

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String role = snapshot.getValue(String.class);
                Log.d("Role", "Current user role: " + role);

                if ("ROLE_ADMIN".equals(role)) {
                    Log.d("Permission", "User is ADMIN - loading data");
                    loadAdminData();
                } else {
                    Log.e("Permission", "User is not ADMIN: " + role);
                    Toast.makeText(AdminActivity.this, "You don't have permission to access", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Role Check", "Error: " + error.getMessage());
                Toast.makeText(AdminActivity.this, "Authentication false: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadAdminData() {
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account");

        accountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalPet = 0;
                int totalAdmin = 0;
                int totalUser = 0;
                int totalGrooming = 0;
                int totalBoarding = 0;
                int totalMedical = 0;
                String petId = "";

                Log.d("Data", "Total accounts: " + snapshot.getChildrenCount());


                Map<String, String> petIdToAccountId = new HashMap<>(); // code them



                for (DataSnapshot accountSnapshot : snapshot.getChildren()) {
                    try {
                        // count pet
                        if (accountSnapshot.hasChild("pet")) {
                            DataSnapshot petSnapshot = accountSnapshot.child("pet");
                            totalPet += (int) petSnapshot.getChildrenCount();
                            for (DataSnapshot pet : petSnapshot.getChildren()) { // code them
                                 petId = pet.getKey();
                                if (petId != null) {
                                    petIdToAccountId.put(petId, accountSnapshot.getKey());
                                }
                            }
                        }

                        // count role
                        String role = accountSnapshot.child("role").getValue(String.class);
                        if ("ROLE_ADMIN".equals(role)) {
                            totalAdmin++;
                            Log.d("Admin Account", "Found admin: " + accountSnapshot.getKey());
                        } else {
                            totalUser++;
                        }

                        //count pending boarding service
                        if (accountSnapshot.hasChild("service") && accountSnapshot.child("service").hasChild("boarding")) {
                            DataSnapshot boardingSnapshot = accountSnapshot.child("service").child("boarding");
                            for (DataSnapshot boardingIdSnapshot : boardingSnapshot.getChildren()) {
                                String status = boardingIdSnapshot.child("status").getValue(String.class);
                                if ("pending".equals(status)) {
                                    totalBoarding++;
                                    if (petId != null && petIdToAccountId.containsKey(petId)) {
                                        String ownerId = petIdToAccountId.get(petId);
                                        Log.d("BoardingOwner", "PetId " + petId + " belong to account: " + ownerId);
                                    }
                                }
                            }
                        }

                        //count pending grooming service
                        if (accountSnapshot.hasChild("service") && accountSnapshot.child("service").hasChild("grooming")) {
                            DataSnapshot groomingSnapshot = accountSnapshot.child("service").child("grooming");
                            for (DataSnapshot groomingIdSnapshot : groomingSnapshot.getChildren()) {
                                String status = groomingIdSnapshot.child("status").getValue(String.class);
                                if ("pending".equals(status)) {
                                    totalGrooming++;
                                }
                            }
                        }


                        //count pending medical service
                        if (accountSnapshot.hasChild("service") && accountSnapshot.child("service").hasChild("medical")) {
                            DataSnapshot medicalSnapshot = accountSnapshot.child("service").child("medical");
                            for (DataSnapshot medicalIdSnapshot : medicalSnapshot.getChildren()) {
                                String status = medicalIdSnapshot.child("status").getValue(String.class);
                                if ("pending".equals(status)) {
                                    totalMedical++;
                                }
                            }
                        }




                    } catch (Exception e) {
                        Log.e("Data Processing", "Error processing account: " + accountSnapshot.getKey(), e);
                    }
                }

                // update ui
                txtBoarding.setText(String.valueOf(totalBoarding));
                txtGrooming.setText(String.valueOf(totalGrooming));
                txtMedical.setText(String.valueOf(totalMedical));
                txtTotalPet.setText(String.valueOf(totalPet));


                Log.d("Statistics", "Total pets: " + totalPet + ", Admins: " + totalAdmin + ", Users: " + totalUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Permission denied or other error: " + error.getMessage());
                if (error.getCode() == DatabaseError.PERMISSION_DENIED) {
                    Toast.makeText(AdminActivity.this, "Không có quyền truy cập dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdminActivity.this, "Lỗi tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}