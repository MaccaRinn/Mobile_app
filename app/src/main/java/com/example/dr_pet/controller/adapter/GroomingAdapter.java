package com.example.dr_pet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.Model.GroomingService;
import com.example.dr_pet.R;

import java.util.List;

public class GroomingAdapter extends RecyclerView.Adapter<GroomingAdapter.ViewHolder> {
    private List<GroomingService> serviceList;
    private OnOrderClickListener listener;
    private OnEditClickListener editListener;
    private OnDeleteClickListener deleteListener;

    public interface OnOrderClickListener {
        void onOrderClick(GroomingService service);
    }
    public interface OnEditClickListener {
        void onEditClick(int position, GroomingService service);
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public GroomingAdapter(List<GroomingService> serviceList, OnOrderClickListener listener) {
        this.serviceList = serviceList;
        this.listener = listener;
    }

    public void setOnEditClickListener(OnEditClickListener editListener) {
        this.editListener = editListener;
    }
    public void setOnDeleteClickListener(OnDeleteClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grooming, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroomingService service = serviceList.get(position);
        holder.txtServiceName.setText(service.getName());
        holder.txtServicePrice.setText(service.getPrice() + " VNĐ");
        holder.imgService.setImageResource(service.getImageResId());
        holder.btnOrder.setOnClickListener(v -> listener.onOrderClick(service));
        // Edit and Delete buttons (optional, add to item_grooming.xml if needed)
        if (holder.btnEdit != null && editListener != null) {
            holder.btnEdit.setOnClickListener(v -> editListener.onEditClick(position, service));
        }
        if (holder.btnDelete != null && deleteListener != null) {
            holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(position));
        }
    }

    @Override
    public int getItemCount() {
        return serviceList != null ? serviceList.size() : 0;
    }

    // Thêm dịch vụ
    public void addService(GroomingService service) {
        serviceList.add(service);
        notifyItemInserted(serviceList.size() - 1);
    }

    // Sửa dịch vụ
    public void updateService(int position, GroomingService service) {
        serviceList.set(position, service);
        notifyItemChanged(position);
    }

    // Xóa dịch vụ
    public void removeService(int position) {
        serviceList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView txtServiceName, txtServicePrice;
        Button btnOrder;
        Button btnEdit, btnDelete; // Optional: add these buttons to item_grooming.xml if you want
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            // Optional: find edit/delete buttons if present
            //btnEdit = itemView.findViewById(R.id.btnEdit);
            //btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
