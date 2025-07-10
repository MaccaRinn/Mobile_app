package com.example.dr_pet.controller.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dr_pet.R;

public class AddPetActivity extends AppCompatActivity {

    Button btn_growth,btn_vaccine, btn_grooming;

    LinearLayout sectionGrowth,sectionVaccine,sectionGrooming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_pet);
        btn_growth = findViewById(R.id.btn_growth);
        btn_vaccine = findViewById(R.id.btn_vaccine);
        btn_grooming = findViewById(R.id.btn_grooming);
        sectionGrooming = findViewById(R.id.section_grooming);
        sectionGrowth = findViewById(R.id.section_growth);
        sectionVaccine = findViewById(R.id.section_vaccine);
        setupToggle(btn_growth, sectionGrowth);
        setupToggle(btn_vaccine, sectionVaccine);
        setupToggle(btn_grooming, sectionGrooming);

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





}