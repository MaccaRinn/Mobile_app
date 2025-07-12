package com.example.dr_pet.controller.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.dr_pet.controller.adapter.ServiceListAdapter;
import com.example.dr_pet.R;

public class ServiceListActivity extends AppCompatActivity {
    private androidx.recyclerview.widget.RecyclerView recyclerServiceList;
    private ServiceListAdapter adapter;
    private java.util.List<com.example.dr_pet.Model.GroomingOrder> serviceList = new java.util.ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_service_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerServiceList = findViewById(R.id.recyclerServiceList);
        recyclerServiceList.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        adapter = new ServiceListAdapter(serviceList);
        recyclerServiceList.setAdapter(adapter);

        android.widget.ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadServiceList();
    }

    private void loadServiceList() {
        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String userId = user.getUid();
        com.google.firebase.database.DatabaseReference ref = com.google.firebase.database.FirebaseDatabase.getInstance().getReference()
                .child("Account").child(userId)
                .child("service").child("grooming");
        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                serviceList.clear();
                for (com.google.firebase.database.DataSnapshot orderSnap : snapshot.getChildren()) {
                    com.example.dr_pet.Model.GroomingOrder order = orderSnap.getValue(com.example.dr_pet.Model.GroomingOrder.class);
                    if (order != null) serviceList.add(order);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(com.google.firebase.database.DatabaseError error) {
                // Handle error if needed
            }
        });
    }
}