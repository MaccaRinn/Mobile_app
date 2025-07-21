package com.example.dr_pet.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.Model.GroomingService;
import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;

import java.util.ArrayList;
import java.util.List;

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.MedicalViewHolder> {
    private List<Pet> pets;

    private Context context;

    private OnViewDetailClickListener listener;

    public interface OnViewDetailClickListener {
        void onViewDetailClick(Pet pet);
    }

    public MedicalAdapter(Context context, OnViewDetailClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.pets = new ArrayList<>();
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets != null ? pets : new ArrayList<>();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical, parent, false);
        return new MedicalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalViewHolder holder, int position) {
        Pet pet = pets.get(position);
        holder.bind(pet);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    public List<Pet> getPets() {
        return pets;
    }

    class MedicalViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet;
        TextView tvName, tvSpecies, tvGender, tvWeight, tvHeight;

        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            tvName = itemView.findViewById(R.id.tvName);
            tvSpecies = itemView.findViewById(R.id.tvSpecies);
            tvGender = itemView.findViewById(R.id.tvGender);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvHeight = itemView.findViewById(R.id.tvHeight);
        }

        void bind(Pet pet) {
            imgPet.setImageResource(pet.getPetUrl());
            tvName.setText(pet.getName());
            tvSpecies.setText(pet.getSpecies());
            tvGender.setText(pet.getGender());
            tvWeight.setText(String.valueOf(pet.getWeight()));
            tvHeight.setText(String.valueOf(pet.getHeight()));

//            xử lý bên ngoài adapter
            itemView.setOnClickListener(v -> listener.onViewDetailClick(pet));
        }
    }
}
