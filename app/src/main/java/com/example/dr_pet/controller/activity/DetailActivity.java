package com.example.dr_pet.controller.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.example.dr_pet.Model.Appointment;
import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button btnVaccine, btnGrooming, btnDelete;

    TextView tvName, tvSpecies, tvGender, tvBirth;

    LinearLayout sectionVaccine, sectionGrooming;
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

        btnVaccine = findViewById(R.id.btnVaccine);
        btnGrooming = findViewById(R.id.btnGrooming);
        btnDelete = findViewById(R.id.btnDelete);

        imgPet = findViewById(R.id.imgPet);

        tvName = findViewById(R.id.tvName);
        tvSpecies = findViewById(R.id.tvSpecies);
        tvGender = findViewById(R.id.tvGender);
        tvBirth = findViewById(R.id.tvBirth);

        sectionVaccine  = findViewById(R.id.sectionVaccine);
        sectionGrooming = findViewById(R.id.sectionGrooming);

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

        btnVaccine.setOnClickListener(v -> {
            if (sectionVaccine.getVisibility() == View.GONE) {
                sectionVaccine.setVisibility(View.VISIBLE);
                sectionVaccine.setAlpha(0f);
                sectionVaccine.animate().alpha(1f).setDuration(300).start();
            } else {
                sectionVaccine.animate().alpha(0f).setDuration(300).withEndAction(() -> sectionVaccine.setVisibility(View.GONE)).start();
            }
        });

        btnGrooming.setOnClickListener(v -> {
            if (sectionGrooming.getVisibility() == View.GONE) {
                sectionGrooming.setVisibility(View.VISIBLE);
                sectionGrooming.setAlpha(0f);
                sectionGrooming.animate().alpha(1f).setDuration(300).start();
            } else {
                sectionGrooming.animate().alpha(0f).setDuration(300).withEndAction(() -> sectionGrooming.setVisibility(View.GONE)).start();
            }
        });
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
            new DatePickerDialog(this,
                    (dp, year, month, day) -> {
                        String formatted = String.format(Locale.getDefault(),
                                "%02d/%02d/%04d", day, month + 1, year);
                        etDate.setText(formatted);
                    },
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH))
                    .show();
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

            // Save to Realtime Database under /Account/{uid}/appointments
            String uid = auth.getCurrentUser().getUid();
            DatabaseReference dbRef = FirebaseDatabase.getInstance()
                    .getReference("Account")
                    .child(uid)
                    .child("appointments");

            if (appointmentId == null) {
                appointmentId = dbRef.push().getKey();
            }

            Appointment appt = new Appointment(
                    name, phone, date, service, pet.getPetId()
            );
            appt.setAppointmentId(appointmentId);

            dbRef.child(appointmentId)
                    .setValue(appt)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this,
                                "Đặt lịch thành công, chờ admin xác nhận",
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

}