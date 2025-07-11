package com.example.dr_pet.controller.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddPetActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button btn_growth,btn_vaccine, btn_grooming, btn_savePet, btn_delete;

    LinearLayout sectionGrowth,sectionVaccine,sectionGrooming;

    EditText edtPName,edt_weight, edt_gender, edt_birthDate, edt_note;

    Spinner spinner_species;

    String petId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_pet);

        auth = FirebaseAuth.getInstance();

        //mapping
        btn_growth = findViewById(R.id.btn_growth);
        btn_vaccine = findViewById(R.id.btn_vaccine);
        btn_grooming = findViewById(R.id.btn_grooming);
        btn_delete = findViewById(R.id.btn_delete);
        btn_savePet = findViewById(R.id.btn_savePet);
        sectionGrooming = findViewById(R.id.section_grooming);
        sectionGrowth = findViewById(R.id.section_growth);
        sectionVaccine = findViewById(R.id.section_vaccine);
        setupToggle(btn_growth, sectionGrowth);
        setupToggle(btn_vaccine, sectionVaccine);
        setupToggle(btn_grooming, sectionGrooming);


        //mapping edit text
        edtPName = findViewById(R.id.edtPName);
        edt_gender = findViewById(R.id.edt_gender);
        edt_birthDate = findViewById(R.id.edt_birth_date);
        edt_note = findViewById(R.id.edt_note);
        edt_weight = findViewById(R.id.edt_weight);


        spinner_species = findViewById(R.id.spinner_species);


        String[] speciesList = {"Chó", "Mèo"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, speciesList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_species.setAdapter(adapter);






        //set up date picker;
        edt_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month  = calendar.get(Calendar.MONTH);
                int day    = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddPetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                edt_birthDate.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.show();
            }
        });



        //load detail pet
        Pet pet = (Pet) getIntent().getSerializableExtra("pet_detail");
        if (pet != null) {
            petId = pet.getPetId();
            edtPName.setText(pet.getName());
            edt_weight.setText(String.valueOf(pet.getWeight()));
            edt_gender.setText(pet.getGender());
            edt_note.setText(pet.getNote());
            edt_birthDate.setText(pet.getBrithDate());

            // set species spinner
            String[] List = {"Chó", "Mèo"};
            for (int i = 0; i < List.length; i++) {
                if (List[i].equals(pet.getSpecies())) {
                    spinner_species.setSelection(i);
                    break;
                }
            }
        }

        // save pet or update detail
        btn_savePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndUpdatePets();
            }
        });



        //delete
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (petId == null) {
                    Toast.makeText(AddPetActivity.this, "Không thể xoá vì petId null", Toast.LENGTH_SHORT).show();
                    return;
                }

                String uid = auth.getCurrentUser().getUid();
                DatabaseReference dbRef = FirebaseDatabase.getInstance()
                        .getReference("Account")
                        .child(uid)
                        .child("pet")
                        .child(petId);

                dbRef.removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddPetActivity.this, "Đã xoá thú cưng", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AddPetActivity.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
        });


    }


    private void setupToggle(Button button, LinearLayout section) {
        button.setOnClickListener(v -> {
            if (section.getVisibility() == View.GONE) {
                section.setVisibility(View.VISIBLE);
                section.setAlpha(0f);
                section.animate().alpha(1f).setDuration(300).start();
            } else {
                section.animate().alpha(0f).setDuration(300).withEndAction(() -> section.setVisibility(View.GONE)).start();
            }
        });
    }


    public void SaveAndUpdatePets(){
        String uid = auth.getCurrentUser().getUid();
        Pet newPet = new Pet();
        String selectedSpecies = spinner_species.getSelectedItem().toString();
        newPet.setSpecies(selectedSpecies);
        newPet.setName(edtPName.getText().toString().trim());
        String dateString = edt_birthDate.getText().toString();
        Log.d("DATE_INPUT", "Date string: " + dateString);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthDate = LocalDate.parse(dateString, formatter);
                newPet.setBrithDate(birthDate.format(formatter)); // convert to String
            }
        } catch (Exception e) {
            Toast.makeText(AddPetActivity.this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        newPet.setNote(edt_note.getText().toString());

        try {
            float weight = Float.parseFloat(edt_weight.getText().toString());
            newPet.setWeight(weight);
        } catch (NumberFormatException e) {
            Toast.makeText(AddPetActivity.this, "Invalid weight, please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String petId = dbRef.child("users").child(uid).child("pet").push().getKey(); // Tạo key mới

        if (petId != null) {
            dbRef.child("Account").child(uid).child("pet").child(petId)
                    .setValue(newPet)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddPetActivity.this, "Pet saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddPetActivity.this, "Failed to save pet", Toast.LENGTH_SHORT).show();
                    });
        }
    }


}