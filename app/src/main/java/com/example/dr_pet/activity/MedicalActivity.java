package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.MedicalAdapter;
import com.example.dr_pet.model.AuthManager;
import com.example.dr_pet.model.Pet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MedicalActivity extends AppCompatActivity {
    private RecyclerView recyclerMedical;
    private MedicalAdapter adapter;
    private List<Pet> petList = new ArrayList<>();
    private DatabaseReference petRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);

        if (!AuthManager.isLoggedIn(this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        auth = FirebaseAuth.getInstance();
        petRef = FirebaseDatabase.getInstance().getReference("pets");

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
        if (auth.getCurrentUser() == null) return;
        
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference accountRef = FirebaseDatabase.getInstance().getReference("Account").child(userId).child("pet");
        
        accountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                petList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Pet pet = data.getValue(Pet.class);
                    if (pet != null) {
                        pet.setPetId(data.getKey());
                        petList.add(pet);
                    }
                }
                adapter.setPets(petList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
}