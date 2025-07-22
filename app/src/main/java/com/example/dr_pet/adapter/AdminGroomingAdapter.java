package com.example.dr_pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Grooming;

import java.util.List;

public class AdminGroomingAdapter extends RecyclerView.Adapter<AdminGroomingAdapter.ViewHolder> {
   private List<Grooming> groomingList;

   public AdminGroomingAdapter(List<Grooming> groomingList){
       this.groomingList = groomingList;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_grooming, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grooming grooming = groomingList.get(position);
        holder.txtGName.setText(grooming.getName());
    }

    @Override
    public int getItemCount() {
        return groomingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtGName, txtD, txtPhone;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone =  itemView.findViewById(R.id.txtPhone);
            txtD = itemView.findViewById(R.id.txtD);
            txtGName = itemView.findViewById(R.id.txtGName);

        }
    }


}
