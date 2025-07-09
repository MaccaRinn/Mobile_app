package com.example.dr_pet.controller.adapter;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;

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

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;
import com.example.dr_pet.controller.activity.AddPetActivity;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {

    private List<Pet> petList;
    private Context context;

    private final static int TYPE_PET = 0;
    private final static int TYPE_ADD_NEW = 1;



    public PetAdapter(List<Pet> petList, Context context) {
        this.petList = petList;
        this.context = context;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagePet;
        TextView textPetName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePet = itemView.findViewById(R.id.imagePet);
            textPetName = itemView.findViewById(R.id.textPetName);
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
                Toast.makeText(context, "Clicked: " + pet.getName(), Toast.LENGTH_SHORT).show();
                // TODO: Chuyển sang màn hình chi tiết pet
            });
        } else {
            // Đây là item "Add new pet"
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Add new pet clicked", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, AddPetActivity.class));
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
}
