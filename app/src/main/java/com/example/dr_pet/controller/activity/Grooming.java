package com.example.dr_pet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dr_pet.AuthManager;
import com.example.dr_pet.R;
import com.example.dr_pet.ServiceOrderActivity;

public class Grooming extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Kiểm tra đăng nhập trước khi cho vào dịch vụ grooming
        if (!AuthManager.isLoggedIn(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
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

        btnDetail1.setOnClickListener(v -> openServiceOrder(
                "Cắt tỉa lông cơ bản", 100000, R.drawable.bong, "Cắt tỉa lông, tạo kiểu cơ bản cho thú cưng."
        ));

        btnDetail2.setOnClickListener(v ->
                openServiceOrder("Chăm sóc móng", 80000, R.drawable.bi, "Chăm sóc móng cho thú cưng.")
        );


        btnDetail3.setOnClickListener(v ->
                openServiceOrder("Cạo lông", 50000, R.drawable.care, "Cạo lông.")
        );


        btnDetail4.setOnClickListener(v ->
                openServiceOrder("Tắm gội", 60000, R.drawable.bgpet, "Tắm gội sạch sẽ, khử mùi và làm mượt lông.")
        );
    }



    private void openServiceOrder(String name, int price, int imgRes, String detail) {
        Intent intent = new Intent(this, ServiceOrderActivity.class);
        intent.putExtra("service_name", name);
        intent.putExtra("service_price", price);
        intent.putExtra("service_img", imgRes);
        intent.putExtra("service_detail", detail);
        startActivity(intent);
    }



}
