package com.example.dr_pet.controller.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.widget.ImageButton;

import com.example.dr_pet.R;
import com.example.dr_pet.ServiceOrderActivity;

public class Grooming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grooming);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageButton btnDetail1 = findViewById(R.id.btnDetail1);
        ImageButton btnDetail2 = findViewById(R.id.btnDetail2);
        ImageButton btnDetail3 = findViewById(R.id.btnDetail3);
        ImageButton btnDetail4 = findViewById(R.id.btnDetail4);

        btnDetail1.setOnClickListener(v -> openServiceOrder("Cắt tỉa lông cơ bản", 100000, R.drawable.bong));
        btnDetail2.setOnClickListener(v -> openServiceOrder("Tắm gội", 80000, R.drawable.bi));
        btnDetail3.setOnClickListener(v -> openServiceOrder("Cạo lông vệ sinh", 50000, R.drawable.care));
        btnDetail4.setOnClickListener(v -> openServiceOrder("Chăm sóc móng", 60000, R.drawable.bgpet));
    }

    private void openServiceOrder(String name, int price, int imgRes) {
        Intent intent = new Intent(this, ServiceOrderActivity.class);
        intent.putExtra("service_name", name);
        intent.putExtra("service_price", price);
        intent.putExtra("service_img", imgRes);
        startActivity(intent);
    }
    }
