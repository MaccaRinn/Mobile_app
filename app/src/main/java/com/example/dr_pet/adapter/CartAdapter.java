package com.example.dr_pet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.manager.CartManager;
import com.example.dr_pet.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartManager cartManager;
    private OnCartItemClickListener listener;

    // Interface cho click events
    public interface OnCartItemClickListener {
        void onItemClick(CartItem item);
        void onRemoveClick(CartItem item);
        void onQuantityChanged(CartItem item, int newQuantity);
    }

    public CartAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
        this.cartManager = CartManager.getInstance();
    }

    public void setOnCartItemClickListener(OnCartItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    /**
     * Cập nhật danh sách cart items
     */
    public void updateCartItems(List<CartItem> newItems) {
        this.cartItems.clear();
        if (newItems != null) {
            this.cartItems.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    /**
     * Thêm cart item
     */
    public void addCartItem(CartItem item) {
        if (item != null) {
            cartItems.add(item);
            notifyItemInserted(cartItems.size() - 1);
        }
    }

    /**
     * Xóa cart item
     */
    public void removeCartItem(String productId) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProductId().equals(productId)) {
                cartItems.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    /**
     * Cập nhật quantity của item
     */
    public void updateItemQuantity(String productId, int newQuantity) {
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            if (item.getProductId().equals(productId)) {
                if (newQuantity <= 0) {
                    removeCartItem(productId);
                } else {
                    item.setQuantity(newQuantity);
                    notifyItemChanged(i);
                }
                break;
            }
        }
    }

    /**
     * Lấy tổng tiền của tất cả items
     */
    public int getTotalAmount() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private TextView txtProductName;
        private TextView txtProductPrice;
        private TextView txtQuantity;
        private TextView txtTotalPrice;
        private Button btnDecrease;
        private Button btnIncrease;
        private ImageButton btnRemove;
        private View itemContainer;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            txtProductName = itemView.findViewById(R.id.txtCartProductName);
            txtProductPrice = itemView.findViewById(R.id.txtCartProductPrice);
            txtQuantity = itemView.findViewById(R.id.txtCartQuantity);
            txtTotalPrice = itemView.findViewById(R.id.txtCartTotalPrice);
            btnDecrease = itemView.findViewById(R.id.btnCartDecrease);
            btnIncrease = itemView.findViewById(R.id.btnCartIncrease);
            btnRemove = itemView.findViewById(R.id.btnCartRemove);
            itemContainer = itemView.findViewById(R.id.cartItemContainer);
        }

        public void bind(CartItem item) {
            // Hiển thị thông tin sản phẩm
            imgProduct.setImageResource(item.getImageRes());
            txtProductName.setText(item.getProductName());
            txtProductPrice.setText(item.getFormattedPrice());
            txtQuantity.setText(String.valueOf(item.getQuantity()));
            txtTotalPrice.setText(item.getFormattedTotalPrice());

            // Click listeners
            setupClickListeners(item);
        }

        private void setupClickListeners(CartItem item) {
            // Click vào item để xem chi tiết
            itemContainer.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });

            // Giảm số lượng
            btnDecrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity > 1) {
                    int newQuantity = currentQuantity - 1;
                    updateQuantity(item, newQuantity);
                } else {
                    // Nếu quantity = 1, hỏi xem có muốn xóa không
                    showRemoveConfirmation(item);
                }
            });

            // Tăng số lượng
            btnIncrease.setOnClickListener(v -> {
                int currentQuantity = item.getQuantity();
                if (currentQuantity < 99) {
                    int newQuantity = currentQuantity + 1;
                    updateQuantity(item, newQuantity);
                } else {
                    Toast.makeText(context, "Số lượng tối đa là 99", Toast.LENGTH_SHORT).show();
                }
            });

            // Xóa sản phẩm
            btnRemove.setOnClickListener(v -> {
                showRemoveConfirmation(item);
            });
        }

        private void updateQuantity(CartItem item, int newQuantity) {
            // Cập nhật UI ngay lập tức
            item.setQuantity(newQuantity);
            txtQuantity.setText(String.valueOf(newQuantity));
            txtTotalPrice.setText(item.getFormattedTotalPrice());

            // Cập nhật CartManager
            cartManager.updateQuantity(item.getProductId(), newQuantity);

            // Notify listener
            if (listener != null) {
                listener.onQuantityChanged(item, newQuantity);
            }
        }

        private void showRemoveConfirmation(CartItem item) {
            // Simple confirmation với Toast - có thể thay bằng AlertDialog
            Toast.makeText(context, "Nhấn lại để xóa " + item.getProductName(),
                    Toast.LENGTH_SHORT).show();

            // Set a flag or use a timer for double-tap confirmation
            btnRemove.setTag(item.getProductId());
            btnRemove.postDelayed(() -> {
                if (btnRemove.getTag() != null && btnRemove.getTag().equals(item.getProductId())) {
                    btnRemove.setTag(null);
                }
            }, 3000);

            // If already tagged (double tap), remove item
            if (item.getProductId().equals(btnRemove.getTag())) {
                removeItem(item);
                btnRemove.setTag(null);
            }
        }

        private void removeItem(CartItem item) {
            // Xóa khỏi CartManager
            cartManager.removeFromCart(item.getProductId());

            // Notify listener
            if (listener != null) {
                listener.onRemoveClick(item);
            }

            Toast.makeText(context, "Đã xóa " + item.getProductName() + " khỏi giỏ hàng",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Helper method để format currency
     */
    public static String formatCurrency(int amount) {
        return String.format("%,d VND", amount);
    }

    /**
     * Check if cart is empty
     */
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Get total items count
     */
    public int getTotalItemsCount() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Clear all items
     */
    public void clearAllItems() {
        cartItems.clear();
        notifyDataSetChanged();
    }
}