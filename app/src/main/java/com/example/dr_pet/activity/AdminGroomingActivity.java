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
import com.example.dr_pet.adapter.AdminGroomingAdapter;
import com.example.dr_pet.adapter.GroomingAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdminGroomingActivity extends AppCompatActivity {

    RecyclerView groomingListRe;
    List<String> groomingItems;

    AdminGroomingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_grooming);
        groomingListRe = findViewById(R.id.groomingListRe);


        // Gắn Adapter vào RecyclerView
        groomingListRe.setAdapter(adapter);



    }
}