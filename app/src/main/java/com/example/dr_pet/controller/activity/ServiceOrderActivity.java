package com.example.dr_pet.controller.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import com.example.dr_pet.R;

public class ServiceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Toast.makeText(this, "ServiceOrderActivity started", Toast.LENGTH_LONG).show();
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_service_order);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


            ImageView imgService = findViewById(R.id.imgServiceOrder);
            TextView txtName = findViewById(R.id.txtServiceOrderName);
            TextView txtPrice = findViewById(R.id.txtServiceOrderPrice);

            TextView txtPetChooser = findViewById(R.id.txtPetChooser);

            final ArrayList<String> petNames = new ArrayList<>();
            final boolean[] isPetLoaded = {false};

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference petsRef = FirebaseDatabase.getInstance().getReference("Account").child(userId).child("pet");
                petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        petNames.clear();
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                                String petName = petSnapshot.child("name").getValue(String.class);
                                if (petName != null) {
                                    petNames.add(petName);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        isPetLoaded[0] = true;
                        Toast.makeText(ServiceOrderActivity.this, "Lỗi truy vấn pet: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            txtPetChooser.setOnClickListener(v -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ServiceOrderActivity.this);
                builder.setTitle("Chọn pet");
                builder.setItems(petNames.toArray(new String[0]), (dialog, which) -> {
                    String selectedPet = petNames.get(which);
                    txtPetChooser.setText(selectedPet);
                });
                builder.show();
            });

            // Nhận dữ liệu từ Intent
            String name = getIntent().getStringExtra("service_name");
            int price = getIntent().getIntExtra("service_price", 0);
            int imgRes = getIntent().getIntExtra("service_img", 0);

            txtName.setText(name);
            txtPrice.setText(price + " VNĐ");
            imgService.setImageResource(imgRes);
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi UI: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }



        TextView txtDetail = findViewById(R.id.textView10);
        String detail = getIntent().getStringExtra("service_detail");
        txtDetail.setText(detail);

        ImageView imgService = findViewById(R.id.imgServiceOrder);
        TextView txtName = findViewById(R.id.txtServiceOrderName);
        TextView txtPrice = findViewById(R.id.txtServiceOrderPrice);

        String name = getIntent().getStringExtra("service_name");
        int price = getIntent().getIntExtra("service_price", 0);
        int imgRes = getIntent().getIntExtra("service_img", 0);

        txtName.setText(name);
        txtPrice.setText(price + " VNĐ");
        imgService.setImageResource(imgRes);

        // Lấy ngày đặt từ DatePicker khi xác nhận
        EditText edtOrderDate = findViewById(R.id.edtOrderDate);
        edtOrderDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        edtOrderDate.setText(date);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Khi xác nhận đặt dịch vụ, lấy ngày đã chọn:
        Button btnConfirm = findViewById(R.id.btnConfirmOrder);
        btnConfirm.setOnClickListener(v -> {
            String date = edtOrderDate.getText().toString();
            // ... xử lý tiếp ...
        });
        android.widget.Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Đã hủy xác nhận dịch vụ", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}