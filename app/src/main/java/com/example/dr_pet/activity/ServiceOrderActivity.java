package com.example.dr_pet.activity;

import android.app.AlertDialog;
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
import android.widget.Spinner;
import android.widget.ArrayAdapter;

import android.widget.Toast;
import com.example.dr_pet.model.ServiceOrder;

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
    private String selectedPetId = null;  // Biến lưu petId được chọn
    private ArrayList<String> petNames = new ArrayList<>();
    private ArrayList<String> petIds = new ArrayList<>();
    private boolean isPetLoaded = false;

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
            // Giờ hẹn hợp lệ
            TextView txtOrderHour = findViewById(R.id.txtOrderHour);
            ArrayList<String> validHours = new ArrayList<>();
            txtOrderHour.setOnClickListener(v -> {
                // Mở TimePickerDialog dạng spinner
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = calendar.get(java.util.Calendar.MINUTE);
                android.app.TimePickerDialog timePickerDialog = new android.app.TimePickerDialog(
                        ServiceOrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        (view, selectedHour, selectedMinute) -> {
                            String picked = String.format("%02d:%02d", selectedHour, selectedMinute);
                            boolean isValid = false;
                            int totalMinutes = selectedHour * 60 + selectedMinute;
                            if ((totalMinutes >= 8*60+30 && totalMinutes <= 10*60+30) || (totalMinutes >= 13*60+30 && totalMinutes <= 17*60+30)) {
                                isValid = true;
                            }
                            if (isValid) {
                                txtOrderHour.setText(picked);
                            } else {
                                Toast.makeText(ServiceOrderActivity.this, "Chỉ được chọn từ 08:30-10:30 hoặc 13:30-17:30!", Toast.LENGTH_LONG).show();
                                txtOrderHour.setText("");
                            }
                        },
                        hour, minute, true
                );
                timePickerDialog.show();
            });
            txtName = findViewById(R.id.txtServiceOrderName);
            txtPrice = findViewById(R.id.txtServiceOrderPrice);

            TextView txtPetChooser = findViewById(R.id.txtPetChooser);
            EditText edtOrderNote = findViewById(R.id.edtOrderNote);
            EditText edtOrderDate = findViewById(R.id.edtOrderDate);
            btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference petsRef = FirebaseDatabase.getInstance().getReference("Account").child(userId).child("pet");
                petsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        petNames.clear();
                        petIds.clear();
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            for (DataSnapshot petSnapshot : dataSnapshot.getChildren()) {
                                String petId  = petSnapshot.getKey();
                                String petName = petSnapshot.child("name").getValue(String.class);
                                if (petName != null && petId != null) {
                                    petNames.add(petName);
                                    petIds.add(petId);
                               }
                            }
                        }
                        isPetLoaded = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ServiceOrderActivity.this, "Lỗi truy vấn pet: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        isPetLoaded = true;
                    }
                });
            }

            txtPetChooser.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ServiceOrderActivity.this);
                builder.setTitle("Chọn pet");
                builder.setItems(petNames.toArray(new String[0]), (dialog, which) -> {
                    selectedPetId = petIds.get(which);
                    txtPetChooser.setText(petNames.get(which));
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
            Toast.makeText(this, "error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        EditText edtOrderNote = findViewById(R.id.edtOrderNote);
        String note = getIntent().getStringExtra("service_note");
        if (note != null) edtOrderNote.setText(note);

        btnConfirmOrder.setOnClickListener(v -> {
            EditText edtOrderDate = findViewById(R.id.edtOrderDate);
            String dateStr = edtOrderDate != null ? edtOrderDate.getText().toString() : "";
            String noteText = edtOrderNote.getText().toString();
            String serviceName = txtName.getText().toString();
            String priceStr = txtPrice.getText().toString();
            TextView txtOrderHour = findViewById(R.id.txtOrderHour);
            String hourStr = txtOrderHour.getText().toString();
            if (hourStr.isEmpty()) {
                Toast.makeText(ServiceOrderActivity.this, "Vui lòng chọn giờ hẹn!", Toast.LENGTH_LONG).show();
                return;
            }

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
                now.add(java.util.Calendar.DATE, 1);
                if (selectedDate == null || !selectedDate.after(now.getTime())) {
                    Toast.makeText(ServiceOrderActivity.this, "Yêu cầu chọn ngày hợp lệ", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception ex) {
                Toast.makeText(ServiceOrderActivity.this, "Yêu cầu chọn ngày hợp lệ", Toast.LENGTH_LONG).show();
                return;
            }

            if (selectedPetId == null) {
                Toast.makeText(ServiceOrderActivity.this, "Vui lòng chọn pet trước khi đặt dịch vụ", Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = user.getUid();

            String serviceType = getIntent().getStringExtra("service_type");
            if (serviceType == null || (!serviceType.equals("grooming") && !serviceType.equals("boarding"))) {
                serviceType = "grooming";
            }

            DatabaseReference serviceRef = FirebaseDatabase.getInstance()
                    .getReference("Account")
                    .child(userId)
                    .child("service")
                    .child(serviceType);

            String serviceOrderId = serviceRef.push().getKey();
            if (serviceOrderId == null) {
                Toast.makeText(ServiceOrderActivity.this, "Lỗi tạo đơn hàng!", Toast.LENGTH_LONG).show();
                return;
            }

            //Status dịch vụ
            String status;
            try {
                java.util.Date selectedDate = sdf.parse(dateStr);
                java.util.Calendar selectedCal = java.util.Calendar.getInstance();
                selectedCal.setTime(selectedDate);

                int hour = 0, minute = 0;
                if (hourStr != null && hourStr.contains(":")) {
                    String[] parts = hourStr.split(":");
                    hour = Integer.parseInt(parts[0]);
                    minute = Integer.parseInt(parts[1]);
                }
                selectedCal.set(java.util.Calendar.HOUR_OF_DAY, hour);
                selectedCal.set(java.util.Calendar.MINUTE, minute);
                selectedCal.set(java.util.Calendar.SECOND, 0);
                selectedCal.set(java.util.Calendar.MILLISECOND, 0);
                java.util.Calendar now = java.util.Calendar.getInstance();
                if (selectedCal.getTime().before(now.getTime())) {
                    status = "missed";
                } else {
                    status = "pending";
                }
            } catch (Exception ex) {
                status = "pending";
            }
            ServiceOrder order = new ServiceOrder(serviceOrderId, serviceName, dateStr, hourStr, noteText, selectedPetId, priceStr, status);
            serviceRef.child(serviceOrderId).setValue(order)
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

        // Lấy ngày đặt từ DatePicker
        EditText edtOrderDate = findViewById(R.id.edtOrderDate);
        edtOrderDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
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
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        android.widget.Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã hủy xác nhận dịch vụ", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
