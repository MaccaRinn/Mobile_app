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

    LinearLayout layoutGrowth, layoutVaccine,layoutGrooming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_pet);
        btn_growth = findViewById(R.id.btn_growth);
        btn_vaccine = findViewById(R.id.btn_vaccine);
        btn_grooming = findViewById(R.id.btn_grooming);



        btn_growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutGrowth);
            }
        });

        btn_vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutVaccine);
            }
        });

        btn_grooming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVisibility(layoutGrooming);
            }
        });
    }


    private void toggleVisibility(LinearLayout layout) {
        if (layout.getVisibility() == View.GONE) {
            layout.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

}