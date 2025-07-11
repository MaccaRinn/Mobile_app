package com.example.dr_pet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.Model.Pet;
import com.example.dr_pet.R;

import java.util.List;

public class BoardingAdapter extends RecyclerView.Adapter<BoardingAdapter.ViewHolder> {
    private List<Pet> petList;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Pet pet);
    }

    public BoardingAdapter(List<Pet> petList, OnBookClickListener listener) {
        this.petList = petList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_boarding, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.txtPetName.setText(pet.getName());
        holder.txtPetDesc.setText(pet.getNote() != null ? pet.getNote() : "");
        // Sử dụng petUrl nếu có, nếu không thì dùng ảnh mặc định
        if (pet.getPetUrl() != 0) {
            holder.imgPet.setImageResource(pet.getPetUrl());
        } else {
            holder.imgPet.setImageResource(R.drawable.miu); // Đảm bảo có ảnh này trong drawable
        }
        holder.btnBookNow.setOnClickListener(v -> listener.onBookClick(pet));
    }

    @Override
    public int getItemCount() {
        return petList != null ? petList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet;
        TextView txtPetName, txtPetDesc;
        Button btnBookNow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            txtPetName = itemView.findViewById(R.id.txtPetname);
            txtPetDesc = itemView.findViewById(R.id.txtPetDesc);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}
