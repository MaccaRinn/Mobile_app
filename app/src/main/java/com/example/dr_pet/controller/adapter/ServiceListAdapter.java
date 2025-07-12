
package com.example.dr_pet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dr_pet.R;
import com.example.dr_pet.Model.GroomingOrder;
import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder> {
    private List<GroomingOrder> serviceList;

    public ServiceListAdapter(List<GroomingOrder> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        GroomingOrder order = serviceList.get(position);
        holder.txtServiceName.setText(order.getName());
        holder.txtServiceDate.setText("Date: " + order.getDate());
        holder.txtServiceNote.setText("Note: " + (order.getNote() == null ? "" : order.getNote()));
        holder.txtPetName.setText("Pet: " + order.getPet());
        holder.txtServicePrice.setText("Price: " + order.getPrice());
        // Ảnh minh họa: luôn dùng drawable bong, có thể sửa lại logic nếu cần
        holder.imgService.setImageResource(R.drawable.bong);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imgService;
        TextView txtServiceName, txtServiceDate, txtServiceNote, txtPetName, txtServicePrice;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgService = itemView.findViewById(R.id.imgService);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServiceDate = itemView.findViewById(R.id.txtServiceDate);
            txtServiceNote = itemView.findViewById(R.id.txtServiceNote);
            txtPetName = itemView.findViewById(R.id.txtPetName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
        }
    }
}
