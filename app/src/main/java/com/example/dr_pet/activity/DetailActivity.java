package com.example.dr_pet.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Appointment;
import com.example.dr_pet.model.Pet;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button btnDelete;

    TextView tvName, tvSpecies, tvGender, tvBirth;

    ImageView imgPet;

    private Pet pet;

    private String appointmentId;
    private TextInputEditText etDateDummy; // if you have an EditText for date in main layout
    private Spinner spinnerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        auth = FirebaseAuth.getInstance();

        imgPet = findViewById(R.id.imgPet);

        tvName = findViewById(R.id.tvName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvGender = findViewById(R.id.tvGender);
        tvBirth = findViewById(R.id.tvBirth);

        pet = (Pet) getIntent().getSerializableExtra("pet");
        if (pet != null) {
            imgPet.setImageResource(pet.getPetUrl());
            tvName.setText(pet.getName());
            tvSpecies.setText(pet.getSpecies());
            tvGender.setText(pet.getGender());
            tvBirth.setText(pet.getBrithDate());
        }
    }

    private void bindingAction() {
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnMedical).setOnClickListener(v -> showBookingDialog());
    }

    private void showBookingDialog() {
        // Inflate custom dialog layout
        View view = getLayoutInflater()
                .inflate(R.layout.dialog_medical_simple, null, false);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(view)
                .create();
        dialog.show();

        ImageView   btnClose       = view.findViewById(R.id.btnClose);
        TextInputEditText etName   = view.findViewById(R.id.etName);
        TextInputEditText etPhone  = view.findViewById(R.id.etPhone);
        TextInputEditText etDate   = view.findViewById(R.id.etDate);
        Spinner     spinnerService = view.findViewById(R.id.spinnerService);
        Button      btnSubmit      = view.findViewById(R.id.btnSubmit);

        // Close dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // DatePicker for date field
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, 1);
            
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (dp, year, month, day) -> {
                        String formatted = String.format(Locale.getDefault(),
                                "%02d/%02d/%04d", day, month + 1, year);
                        etDate.setText(formatted);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        });

        // Service spinner adapter
        ArrayAdapter<CharSequence> srvAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.services,
                android.R.layout.simple_spinner_item
        );
        srvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(srvAdapter);

        // Submit booking
        btnSubmit.setOnClickListener(v -> {
            String name    = etName.getText().toString().trim();
            String phone   = etPhone.getText().toString().trim();
            String date    = etDate.getText().toString().trim();
            String service = spinnerService.getSelectedItem().toString();

            if (name.isEmpty() || phone.isEmpty() || date.isEmpty()) {
                Toast.makeText(this,
                        "Vui lòng điền đủ các trường *",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidBookingDate(date)) {
                Toast.makeText(this,
                        "Vui lòng đặt lịch đúng",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = auth.getCurrentUser().getUid();
            // Lưu giống như grooming/boarding: Account/{userId}/service/{serviceType}
            DatabaseReference dbRef = FirebaseDatabase.getInstance()
                    .getReference("Account")
                    .child(uid)
                    .child("service")
                    .child("medical");

            if (appointmentId == null) {
                appointmentId = dbRef.push().getKey();
            }

            Appointment appt = new Appointment(
                    name, phone, date, service, pet.getPetId()
            );
            appt.setAppointmentId(appointmentId);
            appt.setUserId(uid);

            dbRef.child(appointmentId)
                    .setValue(appt)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this,
                                "Đặt lịch thành công",
                                Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this,
                                "Lỗi khi lưu: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    });

            dialog.dismiss();
        });
    }

    /**
     * Kiểm tra ngày đặt lịch có hợp lệ không (phải từ ngày mai trở đi)
     * @param dateString Ngày dạng dd/MM/yyyy
     * @return true nếu hợp lệ, false nếu không hợp lệ
     */
    private boolean isValidBookingDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date selectedDate = sdf.parse(dateString);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            Date todayDate = today.getTime();

            return selectedDate != null && selectedDate.after(todayDate);
        } catch (ParseException e) {
            return false;
        }
    }
}