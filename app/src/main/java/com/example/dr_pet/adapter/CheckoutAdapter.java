package com.example.dr_pet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CheckoutViewHolder> {

    private Context context;
    private List<CartItem> orderItems;

    public CheckoutAdapter(Context context, List<CartItem> orderItems) {
        this.context = context;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }

    @NonNull
    @Override
    public CheckoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new CheckoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutViewHolder holder, int position) {
        CartItem item = orderItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    /**
     * Cập nhật danh sách items
     */
    public void updateItems(List<CartItem> newItems) {
        this.orderItems.clear();
        if (newItems != null) {
            this.orderItems.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    /**
     * Lấy tổng tiền của tất cả items
     */
    public int getTotalAmount() {
        int total = 0;
        for (CartItem item : orderItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    /**
     * Lấy tổng số lượng sản phẩm
     */
    public int getTotalQuantity() {
        int total = 0;
        for (CartItem item : orderItems) {
            total += item.getQuantity();
        }
        return total;
    }

    class CheckoutViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName;
        private TextView txtProductPrice;
        private TextView txtQuantity;
        private TextView txtTotalPrice;
        private TextView txtProductCategory;

        public CheckoutViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgCheckoutProduct);
            txtProductName = itemView.findViewById(R.id.txtCheckoutProductName);
            txtProductPrice = itemView.findViewById(R.id.txtCheckoutProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtCheckoutQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtCheckoutTotalPrice);
            txtProductCategory = itemView.findViewById(R.id.txtCheckoutProductCategory);
        }

        public void bind(CartItem item) {
            // Hiển thị thông tin sản phẩm
            imgProduct.setImageResource(item.getImageRes());
            txtProductName.setText(item.getProductName());
            txtProductPrice.setText(item.getFormattedPrice());
            txtQuantity.setText(String.format("x%d", item.getQuantity()));
            txtTotalPrice.setText(item.getFormattedTotalPrice());

            // Hiển thị category với icon
            String categoryText = getCategoryText(item.getCategory(), item.getType());
            txtProductCategory.setText(categoryText);
            txtProductCategory.setTextColor(context.getResources().getColor(getCategoryColor(item.getType())));
        }

        private String getCategoryText(String category, String type) {
            String categoryIcon = "";
            String typeText = "";

            // Category icon
            switch (category) {
                case "cat":
                    categoryIcon = "🐱 ";
                    break;
                case "dog":
                    categoryIcon = "🐶 ";
                    break;
                default:
                    categoryIcon = "🐾 ";
                    break;
            }

            // Type text
            switch (type) {
                case "food":
                    typeText = "Thức ăn";
                    break;
                case "accessory":
                    typeText = "Phụ kiện";
                    break;
                default:
                    typeText = "Sản phẩm";
                    break;
            }

            return categoryIcon + typeText;
        }

        private int getCategoryColor(String type) {
            switch (type) {
                case "food":
                    return android.R.color.holo_orange_dark;
                case "accessory":
                    return android.R.color.holo_blue_dark;
                default:
                    return android.R.color.darker_gray;
            }
        }
    }

    /**
     * Helper method để format currency
     */
    public static String formatCurrency(int amount) {
        return String.format("%,d VND", amount);
    }

    /**
     * Lấy danh sách items (read-only)
     */
    public List<CartItem> getOrderItems() {
        return new ArrayList<>(orderItems);
    }

    /**
     * Check if có items không
     */
    public boolean isEmpty() {
        return orderItems.isEmpty();
    }

    /**
     * Lấy item theo position
     */
    public CartItem getItem(int position) {
        if (position >= 0 && position < orderItems.size()) {
            return orderItems.get(position);
        }
        return null;
    }

    /**
     * Tạo order summary text
     */
    public String getOrderSummary() {
        if (orderItems.isEmpty()) {
            return "Không có sản phẩm";
        }

        return String.format("Đơn hàng: %d sản phẩm (%d loại) - Tổng: %s",
                getTotalQuantity(),
                orderItems.size(),
                formatCurrency(getTotalAmount()));
    }
}