package com.example.dr_pet.controller.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.adapter.BoardingAdapter;

import java.util.ArrayList;
import java.util.List;

public class BoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_boarding);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerBoarding);
        List<Pet> petList = new ArrayList<>();
        petList.add(new Pet("Mon Pet", "Cat", R.drawable.miu, 0, "", null, "Vân Lôi - Bình Yên - Thạch Thất - Hà Nội"));
        petList.add(new Pet("Bông", "Cat", R.drawable.bong, 0, "", null, "bla bla bla"));
        petList.add(new Pet("Bí", "Cat", R.drawable.bi, 0, "", null, "ble ble ble"));
        petList.add(new Pet("Toán", "Cat", R.drawable.bong, 0, "", null, "blu blu blu"));
        BoardingAdapter adapter = new BoardingAdapter(petList, pet -> {
            // Xử lý khi bấm Book now
        });
        findViewById(R.id.btn_grooming_back).setOnClickListener(v -> onBackPressed());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}