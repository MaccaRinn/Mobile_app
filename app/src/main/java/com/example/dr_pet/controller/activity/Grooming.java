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

import com.example.dr_pet.AuthManager;
import com.example.dr_pet.Model.GroomingService;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.adapter.GroomingAdapter;

import java.util.ArrayList;
import java.util.List;

public class Grooming extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GroomingAdapter adapter;
    private List<GroomingService> serviceList;

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

        recyclerView = findViewById(R.id.recyclerGrooming);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách dịch vụ mẫu
        serviceList = new ArrayList<>();
        serviceList.add(new GroomingService("Cắt tỉa lông cơ bản", "Cắt tỉa lông, tạo kiểu cơ bản cho thú cưng.", R.drawable.bong, 50000));
        serviceList.add(new GroomingService("Chăm sóc móng", "Chăm sóc móng cho thú cưng.", R.drawable.bi, 30000));
        serviceList.add(new GroomingService("Cạo lông", "Cạo lông.", R.drawable.care, 40000));
        serviceList.add(new GroomingService("Tắm gội", "Tắm gội sạch sẽ, khử mùi và làm mượt lông.", R.drawable.bgpet, 60000));

        adapter = new GroomingAdapter(serviceList, service -> openServiceOrder(service));
        recyclerView.setAdapter(adapter);
        findViewById(R.id.btn_grooming_back1).setOnClickListener(v -> onBackPressed());
    }

    private void openServiceOrder(GroomingService service) {
        Intent intent = new Intent(this, ServiceOrderActivity.class);
        intent.putExtra("service_name", service.getName());
        intent.putExtra("service_desc", service.getDesc());
        intent.putExtra("service_img", service.getImageResId());
        intent.putExtra("service_price", service.getPrice());
        startActivity(intent);
    }


}
