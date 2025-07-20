package com.example.dr_pet.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dr_pet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity extends AppCompatActivity {

    Button btn_adminShop, btn_adminGrooming, btn_adminBoarding, btn_adminMedical;
    TextView txtTotalPet, txtBoarding, txtMedical, txtShopping;

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

        txtTotalPet = findViewById(R.id.txtTotalPet);
        txtBoarding = findViewById(R.id.txtBoarding);
        txtMedical = findViewById(R.id.txtMedical);
        txtShopping = findViewById(R.id.txtShopping);

        // Kiểm tra quyền admin trước
        checkAdminPermissionAndLoadData();
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

                Log.d("Data", "Total accounts: " + snapshot.getChildrenCount());

                for (DataSnapshot accountSnapshot : snapshot.getChildren()) {
                    try {
                        // Đếm pet
                        if (accountSnapshot.hasChild("pet")) {
                            DataSnapshot petSnapshot = accountSnapshot.child("pet");
                            totalPet += (int) petSnapshot.getChildrenCount();
                        }

                        // Đếm role
                        String role = accountSnapshot.child("role").getValue(String.class);
                        if ("ROLE_ADMIN".equals(role)) {
                            totalAdmin++;
                            Log.d("Admin Account", "Found admin: " + accountSnapshot.getKey());
                        } else {
                            totalUser++;
                        }

                    } catch (Exception e) {
                        Log.e("Data Processing", "Error processing account: " + accountSnapshot.getKey(), e);
                    }
                }

                // update ui
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