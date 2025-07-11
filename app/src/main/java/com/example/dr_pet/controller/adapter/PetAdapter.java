package com.example.dr_pet.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.AuthManager;
import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.activity.AddPetActivity;
import com.example.dr_pet.controller.activity.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private List<Pet> petList;
    private Context context;

    private final static int TYPE_PET = 0;
    private final static int TYPE_ADD_NEW = 1;



    public PetAdapter(List<Pet> petList, Context context) {
        this.petList = petList;
        this.context = context;
        loadPetsFromFirebase();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagePet;
        TextView textPetName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePet = itemView.findViewById(R.id.imagePet);
            textPetName = itemView.findViewById(R.id.txtPetname);
        }
    }

    @NonNull
    @Override
    public PetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PET) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pet, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pet_add, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PetAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PET) {
            Pet pet = petList.get(position);
            holder.textPetName.setText(pet.getName());
            holder.imagePet.setImageResource(pet.getPetUrl());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AddPetActivity.class);
                intent.putExtra("pet_detail", pet); // truyền pet qua intent
                context.startActivity(intent);
            });
        } else {
            // Đây là item "Add new pet"
            holder.itemView.setOnClickListener(v -> {
                if (!AuthManager.isLoggedIn(context)){
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                else {
                    context.startActivity(new Intent(context, AddPetActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       return petList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == petList.size()) {
            return TYPE_ADD_NEW;
        } else {
            return TYPE_PET;
        }
    }

    private void loadPetsFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference petRef = FirebaseDatabase.getInstance()
                .getReference("Account")
                .child(uid)
                .child("pet");

        petRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                petList.clear();
                for (DataSnapshot petSnap : snapshot.getChildren()) {
                    Pet pet = petSnap.getValue(Pet.class);
                    if (pet != null) {
                        pet.setPetId(petSnap.getKey()); // <-- gán ID cho Pet
                        petList.add(pet);
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load pets", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
