package com.example.dr_pet.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddPetActivity extends AppCompatActivity {

    FirebaseAuth auth;

    Button btn_growth, btn_medical, btn_grooming, btn_boarding,btn_savePet, btn_delete;

    ImageButton btn_back;

    LinearLayout sectionGrowth, sectionGrooming, sectionBoarding;

    ImageView  imagePet;

    EditText edtPName, edt_weight, edt_gender, edt_birthDate, edt_note;

    Spinner spinner_species;

    String petId;

    TextView txtGroomingDetail, txtBoardingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_pet);

        auth = FirebaseAuth.getInstance();

        // Mapping
        btn_back = findViewById(R.id.btn_back);
        btn_growth = findViewById(R.id.btn_growth);
        btn_boarding = findViewById(R.id.btn_boarding);
        btn_grooming = findViewById(R.id.btn_grooming);

        btn_delete = findViewById(R.id.btn_delete);
        btn_savePet = findViewById(R.id.btn_savePet);
        sectionGrooming = findViewById(R.id.section_grooming);
        sectionGrowth = findViewById(R.id.section_growth);
        sectionBoarding = findViewById(R.id.section_boarding);

        txtGroomingDetail = findViewById(R.id.txtGroomingDetail);
        txtBoardingDetail = findViewById(R.id.txtBoardingDetail);


        LinearLayout[] allSections = {
               sectionBoarding,sectionGrooming,sectionGrowth
        };

        setupToggle(btn_growth, sectionGrowth, allSections);
        setupToggle(btn_boarding, sectionBoarding, allSections);
        setupToggle(btn_grooming, sectionGrooming, allSections);


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

            String uid = auth.getCurrentUser().getUid();

            DatabaseReference groomingRef = FirebaseDatabase.getInstance().getReference("Account").
                    child(uid).child("service").child("grooming");

            groomingRef.orderByChild("petId").equalTo(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        StringBuilder groomingDetails = new StringBuilder();
                        for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                            String groomingDate = serviceSnapshot.child("date").getValue(String.class);
                            String groomingNotes = serviceSnapshot.child("name").getValue(String.class);
                            String status = serviceSnapshot.child("status").getValue(String.class);
                            groomingDetails.append(groomingDate)
                                    .append(" - ").append(groomingNotes)
                                    .append(status != null ? " (" + status + ")" : "")
                                    .append("\n");
                        }
                        txtGroomingDetail.setText(groomingDetails.toString());
                    }
                    else {
                        txtGroomingDetail.setText("No grooming details available.");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            DatabaseReference broadingRef = FirebaseDatabase.getInstance().getReference("Account").
                    child(uid).child("service").child("boarding");

            broadingRef.orderByChild("petId").equalTo(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        StringBuilder boardingDetails = new StringBuilder();
                        for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                            String boardingDate = serviceSnapshot.child("date").getValue(String.class);
                            String boardingNotes = serviceSnapshot.child("name").getValue(String.class);
                            String status = serviceSnapshot.child("status").getValue(String.class);
                            boardingDetails.append(boardingDate)
                                    .append(" - ").append(boardingNotes)
                                    .append(status != null ? " (" + status + ")" : "")
                                    .append("\n");
                        }
                        txtBoardingDetail.setText(boardingDetails.toString());
                    } else {
                        txtBoardingDetail.setText("No boarding details available.");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
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
            DatabaseReference petRef = FirebaseDatabase.getInstance()
                    .getReference("Account")
                    .child(uid)
                    .child("pet")
                    .child(petId);

            petRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        DatabaseReference groomingRef = FirebaseDatabase.getInstance().getReference("Account")
                                .child(uid).child("service").child("grooming");

                        // xoa grooming
                        groomingRef.orderByChild("petId").equalTo(petId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot serviceSnapshot : snapshot.getChildren()) {
                                            serviceSnapshot.getRef().removeValue();
                                        }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(AddPetActivity.this, "Delete Service fail", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        DatabaseReference boardingRef = FirebaseDatabase.getInstance().
                                getReference("Account").child(uid).child("service").child("boarding");

                        // delete boarding
                        boardingRef.orderByChild("petId").equalTo(petId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot serviceSnapshot : snapshot.getChildren()){
                                    serviceSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(AddPetActivity.this, "Delete Service fail", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(AddPetActivity.this, "Delete Pet successfully", Toast.LENGTH_SHORT).show();
                        finish();

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AddPetActivity.this, "Delete Pet fail", Toast.LENGTH_SHORT).show();

                    });
        });


         btn_back.setOnClickListener(v -> {
             startActivity(new Intent(AddPetActivity.this, HomeActivity.class));
             finish();
         });
    }


    private void setupToggle(Button button, LinearLayout section, LinearLayout[] allSections) {
        button.setOnClickListener(v -> {
            // Hide all sections first
            for (LinearLayout sec : allSections) {
                if (sec != section) {
                    sec.animate().alpha(0f).setDuration(300).withEndAction(() -> sec.setVisibility(View.GONE)).start();
                }
            }

            // Toggle the visibility of the current section
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

        String dateString = edt_birthDate.getText().toString();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate birthDate = LocalDate.parse(dateString, formatter);

                // Get the current date
                LocalDate currentDate = LocalDate.now();

                // Check if the birth date is after the current date
                if (birthDate.isAfter(currentDate)) {
                    Toast.makeText(this, "Birth date cannot be after today's date", Toast.LENGTH_SHORT).show();
                    return;
                }

                newPet.setBrithDate(birthDate.format(formatter));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Please enter a valid birthday", Toast.LENGTH_SHORT).show();
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
