package com.example.dr_pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Grooming;

import java.util.List;

public class AdminGroomingAdapter extends RecyclerView.Adapter<AdminGroomingAdapter.ViewHolder> {
    private List<Grooming> groomingList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onDeny(Grooming grooming);
        void onAccept(Grooming grooming);
    }

    public AdminGroomingAdapter(List<Grooming> groomingList, OnItemActionListener listener) {
        this.groomingList = groomingList;
        this.listener = listener;
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
        holder.txtD.setText(grooming.getDate());
        holder.txtPhone.setText(grooming.getPhone());

        holder.btn_deny.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeny(grooming); // Gọi callback xoá
            }
        });
        holder.btn_accept.setOnClickListener(v->{
            if (listener != null){
                listener.onAccept(grooming);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groomingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtGName, txtD, txtPhone;
        Button btn_accept, btn_deny;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone =  itemView.findViewById(R.id.txtPhone);
            txtD = itemView.findViewById(R.id.txtD);
            txtGName = itemView.findViewById(R.id.txtGName);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_deny = itemView.findViewById(R.id.btn_deny);
        }
    }
}

