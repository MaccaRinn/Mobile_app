
package com.example.dr_pet.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.Model.HotelOption;

import java.util.List;

public class BoardingAdapter extends RecyclerView.Adapter<BoardingAdapter.ViewHolder> {
    private List<HotelOption> hotelOptions;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(HotelOption option);
    }

    public BoardingAdapter(List<HotelOption> hotelOptions, OnBookClickListener listener) {
        this.hotelOptions = hotelOptions;
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
        HotelOption option = hotelOptions.get(position);
        holder.txtHotelName.setText(option.getName());
        holder.txtHotelDesc.setText(option.getDesc());
        holder.txtHotelPrice.setText("Giá: " + option.getPrice() + " VNĐ");
        holder.imgHotel.setImageResource(option.getImageResId());
        holder.btnBookNow.setOnClickListener(v -> listener.onBookClick(option));
    }

    @Override
    public int getItemCount() {
        return hotelOptions != null ? hotelOptions.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHotel;
        TextView txtHotelName, txtHotelDesc, txtHotelPrice;
        Button btnBookNow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHotel = itemView.findViewById(R.id.imgHotel);
            txtHotelName = itemView.findViewById(R.id.txtHotelName);
            txtHotelDesc = itemView.findViewById(R.id.txtHotelDesc);
            txtHotelPrice = itemView.findViewById(R.id.txtHotelPrice);
            btnBookNow = itemView.findViewById(R.id.btnBookNow);
        }
    }
}
