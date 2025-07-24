package com.example.dr_pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Boarding;
import com.example.dr_pet.model.Grooming;

import java.util.List;

public class AdminBoardingAdapter extends RecyclerView.Adapter<AdminBoardingAdapter.ViewHolder>{

    private List<Boarding> boardingsList;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onDenyB(Boarding boarding);
        void onAcceptB(Boarding boarding);
        void onCallB(Boarding boarding);
    }

    public AdminBoardingAdapter(List<Boarding> boardingsList, OnItemActionListener listener){
        this.boardingsList = boardingsList;
        this.listener =listener;
    }

    @NonNull
    @Override
    public AdminBoardingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_grooming, parent, false);
        return new AdminBoardingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Boarding boarding = boardingsList.get(position);
        holder.txtGName.setText(boarding.getName());
        holder.txtD.setText(boarding.getDate());
        holder.txtPhone.setText(boarding.getPhone());

        holder.btn_deny.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDenyB(boarding); // Gọi callback xoá
            }
        });
        holder.btn_accept.setOnClickListener(v->{
            if (listener != null){
                listener.onAcceptB(boarding);
            }
        });
        holder.btn_call.setOnClickListener(v -> {
            if (listener != null){
                listener.onCallB(boarding);
            }
        });
    }


    @Override
    public int getItemCount() {
        return boardingsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtGName, txtD, txtPhone;
        private Button btn_accept, btn_deny, btn_call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPhone =  itemView.findViewById(R.id.txtPhone);
            txtD = itemView.findViewById(R.id.txtD);
            txtGName = itemView.findViewById(R.id.txtGName);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_deny = itemView.findViewById(R.id.btn_deny);
            btn_call = itemView.findViewById(R.id.btn_call);

        }
    }


}
