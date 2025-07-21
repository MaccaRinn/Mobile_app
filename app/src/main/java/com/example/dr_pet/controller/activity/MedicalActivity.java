package com.example.dr_pet.controller.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.Model.AuthManager;
import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.adapter.MedicalAdapter;

import java.util.ArrayList;
import java.util.List;

public class MedicalActivity extends AppCompatActivity {

    private RecyclerView recyclerMedical;
    private MedicalAdapter adapter;
    private List<Pet> petList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        // Kiểm tra đăng nhập trước khi cho vào dịch vụ grooming
        if (!AuthManager.isLoggedIn(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        recyclerMedical = findViewById(R.id.recyclerMedical);

        adapter = new MedicalAdapter(this, new MedicalAdapter.OnViewDetailClickListener() {
            @Override
            public void onViewDetailClick(Pet pet) {
                Intent intent = new Intent(MedicalActivity.this, DetailActivity.class);
                intent.putExtra("pet", pet);
                startActivity(intent);
            }
        });

        recyclerMedical.setLayoutManager(new LinearLayoutManager(this));
        recyclerMedical.setAdapter(adapter);

    }

    private void bindingAction() {
        findViewById(R.id.btn_medical_back).setOnClickListener(v -> finish());
        loadPets();
    }
    private void loadPets() {
        petList.add(new Pet("Bông", "Cat", 3, 1, R.drawable.bong, "Female", "16/01/2021", "Không có"));
        petList.add(new Pet("Bi", "Cat", 4, 1.5, R.drawable.bi, "Female", "18/05/2023", "Không có"));
        petList.add(new Pet("Miu", "Cat", 4, 1.5, R.drawable.miu, "Male", "02/04/2022", "Không có"));

        adapter.setPets(petList);
    }
}