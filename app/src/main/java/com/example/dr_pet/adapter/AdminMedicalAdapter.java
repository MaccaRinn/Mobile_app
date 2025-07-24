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
import com.example.dr_pet.model.Medical;

import java.util.List;

public class AdminMedicalAdapter extends RecyclerView.Adapter<AdminMedicalAdapter.ViewHolder>{


    private List<Medical> medicalList;
    private OnItemActionListener listener;


    public interface OnItemActionListener {
        void onDenyM(Medical medical);
        void onAcceptM(Medical medical);

        void onCallM(Medical medical);
    }

    public AdminMedicalAdapter(List<Medical> medicalList, AdminMedicalAdapter.OnItemActionListener listener) {
        this.medicalList = medicalList;
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
    public void onBindViewHolder(@NonNull AdminMedicalAdapter.ViewHolder holder, int position) {
        Medical medical = medicalList.get(position);
        holder.txtGName.setText(medical.getName());
        holder.txtD.setText(medical.getDate());
        holder.txtPhone.setText(medical.getPhone());


        holder.btn_deny.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDenyM(medical); // Gọi callback xoá
            }
        });
        holder.btn_accept.setOnClickListener(v->{
            if (listener != null){
                listener.onAcceptM(medical);
            }
        });
        holder.btn_call.setOnClickListener(v -> {
            if (listener != null){
                //
                listener.onCallM(medical);
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicalList.size();
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
