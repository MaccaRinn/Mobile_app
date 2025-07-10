package com.example.dr_pet.controller.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.dr_pet.R;

public class ServiceOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView imgService = findViewById(R.id.imgServiceOrder);
        TextView txtName = findViewById(R.id.txtServiceOrderName);
        TextView txtPrice = findViewById(R.id.txtServiceOrderPrice);

        // Nhận dữ liệu từ Intent
        String name = getIntent().getStringExtra("service_name");
        int price = getIntent().getIntExtra("service_price", 0);
        int imgRes = getIntent().getIntExtra("service_img", 0);

        txtName.setText(name);
        txtPrice.setText(price + " VNĐ");
        imgService.setImageResource(imgRes);
    }
}