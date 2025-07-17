package com.example.dr_pet.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.BoardingAdapter;
import com.example.dr_pet.model.HotelOption;

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
        List<HotelOption> hotelOptions = new ArrayList<>();
        hotelOptions.add(new HotelOption("Khách sạn A", "Phòng riêng, có camera giám sát 24/7, điều hòa, thức ăn cao cấp", 200000, R.drawable.miu));
        hotelOptions.add(new HotelOption("Khách sạn B", "Phòng tập thể, giám sát định kỳ, thức ăn tiêu chuẩn", 120000, R.drawable.miu));
        hotelOptions.add(new HotelOption("Khách sạn C", "Phòng cơ bản, không điều hòa, tự mang thức ăn", 80000, R.drawable.miu));
        BoardingAdapter adapter = new BoardingAdapter(hotelOptions, option -> {
            android.content.Intent intent = new android.content.Intent(BoardingActivity.this, ServiceOrderActivity.class);
            intent.putExtra("service_type", "boarding");
            intent.putExtra("service_name", option.getName());
            intent.putExtra("service_desc", option.getDesc());
            intent.putExtra("service_price", option.getPrice());
            intent.putExtra("service_img", option.getImageResId());
            startActivity(intent);
        });
        findViewById(R.id.btn_grooming_back).setOnClickListener(v -> onBackPressed());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}