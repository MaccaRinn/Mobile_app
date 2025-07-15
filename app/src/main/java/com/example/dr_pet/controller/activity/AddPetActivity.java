package com.example.dr_pet.controller.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddPetActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button btn_growth, btn_vaccine, btn_grooming, btn_savePet, btn_delete;

    ImageButton btn_back;

    LinearLayout sectionGrowth, sectionVaccine, sectionGrooming;

    ImageView  imagePet;

    EditText edtPName, edt_weight, edt_gender, edt_birthDate, edt_note;

    Spinner spinner_species;

    String petId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_pet);

        auth = FirebaseAuth.getInstance();

        // Mapping
        btn_back = findViewById(R.id.btn_back);
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

        imagePet = findViewById(R.id.imagePet);
        edtPName = findViewById(R.id.edtPName);
        edt_gender = findViewById(R.id.edt_gender);
        edt_birthDate = findViewById(R.id.edt_birth_date);
        edt_note = findViewById(R.id.edt_note);
        edt_weight = findViewById(R.id.edt_weight);
        spinner_species = findViewById(R.id.spinner_species);

        // Spinner species
        String[] speciesList = {"Chó", "Mèo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, speciesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_species.setAdapter(adapter);

        // Date picker setup
        edt_birthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddPetActivity.this,
                    (view, year1, month1, dayOfMonth) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                        edt_birthDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Load pet detail (if exists)
        Pet pet = (Pet) getIntent().getSerializableExtra("pet_detail");
        if (pet != null) {
            petId = pet.getPetId();
            edtPName.setText(pet.getName());
            imagePet.setImageResource(pet.getPetUrl());
            edt_weight.setText(String.valueOf(pet.getWeight()));
            edt_gender.setText(pet.getGender());
            edt_note.setText(pet.getNote());
            edt_birthDate.setText(pet.getBrithDate());

            // Set species
            for (int i = 0; i < speciesList.length; i++) {
                if (speciesList[i].equals(pet.getSpecies())) {
                    spinner_species.setSelection(i);
                    break;
                }
            }
        } else {
            // Nếu là tạo mới thì ẩn nút xóa
            btn_delete.setVisibility(View.GONE);
        }

        // Save or Update pet
        btn_savePet.setOnClickListener(v -> SaveAndUpdatePets());

        // Delete pet
        btn_delete.setOnClickListener(v -> {
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
        });


         btn_back.setOnClickListener(v -> {
             startActivity(new Intent(AddPetActivity.this, HomeActivity.class));
             finish();
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

    public void SaveAndUpdatePets() {
        String uid = auth.getCurrentUser().getUid();
        Pet newPet = new Pet();

        //name
        String name = edtPName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter pet name", Toast.LENGTH_SHORT).show();
            return;
        }
        newPet.setName(name);

        //gender
        String gender = edt_gender.getText().toString().trim();
        if (gender.isEmpty()) {
            Toast.makeText(this, "Please enter gender of pet", Toast.LENGTH_SHORT).show();
            return;
        }
        newPet.setGender(gender);

       // species
        String selectedSpecies = spinner_species.getSelectedItem().toString();
        newPet.setSpecies(selectedSpecies);
        if (selectedSpecies.equals("Chó")){
            newPet.setPetUrl(R.drawable.default_dog_avatar);
        }else{
            newPet.setPetUrl(R.drawable.default_cat_avatar);
        }

       // birthday
        String dateString = edt_birthDate.getText().toString();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthDate = LocalDate.parse(dateString, formatter);
                newPet.setBrithDate(birthDate.format(formatter));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please enter the valid birhtday", Toast.LENGTH_SHORT).show();
            return;
        }

        //note
        newPet.setNote(edt_note.getText().toString());


        try {
            float weight = Float.parseFloat(edt_weight.getText().toString());
            newPet.setWeight(weight);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter the valid weight", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase
        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                .getReference("Account")
                .child(uid)
                .child("pet");

        if (petId == null) {
            petId = dbRef.push().getKey();
        }

        newPet.setPetId(petId);

        dbRef.child(petId).setValue(newPet)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Save successfull", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                });
    }

}
