package com.example.dr_pet.controller.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
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
    private TextView txtName;
    private TextView txtPrice;
    private Button btnConfirmOrder;

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
            txtName = findViewById(R.id.txtServiceOrderName);
            txtPrice = findViewById(R.id.txtServiceOrderPrice);

            TextView txtPetChooser = findViewById(R.id.txtPetChooser);
            EditText edtOrderNote = findViewById(R.id.edtOrderNote);
            EditText edtOrderDate = findViewById(R.id.edtOrderDate);
            btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
            android.util.Log.d("ServiceOrder", "txtName null? " + (txtName == null));
            android.util.Log.d("ServiceOrder", "txtPrice null? " + (txtPrice == null));
            android.util.Log.d("ServiceOrder", "edtOrderNote null? " + (edtOrderNote == null));
            android.util.Log.d("ServiceOrder", "edtOrderDate null? " + (edtOrderDate == null));
            android.util.Log.d("ServiceOrder", "txtPetChooser null? " + (txtPetChooser == null));
            android.util.Log.d("ServiceOrder", "btnConfirmOrder null? " + (btnConfirmOrder == null));

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



        EditText edtOrderNote = findViewById(R.id.edtOrderNote);
        // Nếu muốn truyền ghi chú từ intent, có thể lấy ở đây
        String note = getIntent().getStringExtra("service_note");
        if (note != null) edtOrderNote.setText(note);

        // ...existing code...
        btnConfirmOrder.setOnClickListener(v -> {
            EditText edtOrderDate = findViewById(R.id.edtOrderDate);
            String dateStr = edtOrderDate != null ? edtOrderDate.getText().toString() : "";
            String noteText = edtOrderNote.getText().toString();
            TextView txtPetChooser = findViewById(R.id.txtPetChooser);
            String selectedPet = txtPetChooser.getText().toString();
            String serviceName = txtName.getText().toString();
            String priceStr = txtPrice.getText().toString();
            // Kiểm tra ngày hợp lệ
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                java.util.Date selectedDate = sdf.parse(dateStr);
                java.util.Calendar now = java.util.Calendar.getInstance();
                now.set(java.util.Calendar.HOUR_OF_DAY, 0);
                now.set(java.util.Calendar.MINUTE, 0);
                now.set(java.util.Calendar.SECOND, 0);
                now.set(java.util.Calendar.MILLISECOND, 0);
                now.add(java.util.Calendar.DATE, 1); // Ngày hợp lệ phải sau hôm nay 1 ngày
                if (selectedDate == null || !selectedDate.after(now.getTime())) {
                    Toast.makeText(ServiceOrderActivity.this, "Yêu cầu chọn ngày hợp lệ", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception ex) {
                Toast.makeText(ServiceOrderActivity.this, "Yêu cầu chọn ngày hợp lệ", Toast.LENGTH_LONG).show();
                return;
            }
            // Lưu xuống Firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                Toast.makeText(ServiceOrderActivity.this, "Chưa đăng nhập!", Toast.LENGTH_LONG).show();
                return;
            }
            String userId = user.getUid();
            DatabaseReference groomingRef = FirebaseDatabase.getInstance()
                .getReference("Account")
                .child(userId)
                .child("service")
                .child("grooming");
            // Tạo node mới cho mỗi đơn đặt dịch vụ
            String serviceOrderId = groomingRef.push().getKey();
            if (serviceOrderId == null) {
                Toast.makeText(ServiceOrderActivity.this, "Lỗi tạo đơn hàng!", Toast.LENGTH_LONG).show();
                return;
            }
            GroomingOrder order = new GroomingOrder(serviceOrderId, serviceName, dateStr, noteText, selectedPet, priceStr);
            groomingRef.child(serviceOrderId).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ServiceOrderActivity.this, "Đặt dịch vụ thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ServiceOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ServiceOrderActivity.this, "Lỗi lưu đơn hàng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });

        // GroomingOrder class
        // Bạn có thể đặt class này ở ngoài hoặc trong file này
        class GroomingOrder {
            public String serviceOrderId;
            public String name;
            public String date;
            public String note;
            public String pet;
            public String price;
            public GroomingOrder() {}
            public GroomingOrder(String serviceOrderId, String name, String date, String note, String pet, String price) {
                this.serviceOrderId = serviceOrderId;
                this.name = name;
                this.date = date;
                this.note = note;
                this.pet = pet;
                this.price = price;
            }
        }

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

        android.widget.Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            android.widget.Toast.makeText(this, "Đã hủy xác nhận dịch vụ", android.widget.Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Thêm class GroomingOrder để lưu đơn đặt dịch vụ grooming
    class GroomingOrder {
        public String serviceOrderId;
        public String name;
        public String date;
        public String note;
        public String pet;
        public String price;
        public GroomingOrder() {}
        public GroomingOrder(String serviceOrderId, String name, String date, String note, String pet, String price) {
            this.serviceOrderId = serviceOrderId;
            this.name = name;
            this.date = date;
            this.note = note;
            this.pet = pet;
            this.price = price;
        }
    }
}