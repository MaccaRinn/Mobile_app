package com.example.dr_pet.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName;
        private TextView txtProductPrice;
        private LinearLayout layoutProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            layoutProduct = itemView.findViewById(R.id.layoutProduct);
        }

        public void bind(Product product) {
            imgProduct.setImageResource(product.getImageRes());
            txtProductName.setText(product.getName());
            txtProductPrice.setText(String.format("%,dđ", product.getPrice()));

            // Set background color based on product type
            if ("food".equals(product.getType())) {
                layoutProduct.setBackgroundResource(R.drawable.product_background_pink);
            } else {
                layoutProduct.setBackgroundResource(R.drawable.product_background_blue);
            }

            // Set click listener
            layoutProduct.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}