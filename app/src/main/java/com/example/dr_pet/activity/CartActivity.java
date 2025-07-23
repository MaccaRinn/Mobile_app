package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.CartAdapter;
import com.example.dr_pet.manager.CartManager;
import com.example.dr_pet.model.Cart;
import com.example.dr_pet.model.CartItem;
import com.example.dr_pet.model.Product;

public class CartActivity extends AppCompatActivity implements CartManager.CartUpdateListener, CartAdapter.OnCartItemClickListener {

    private RecyclerView recyclerViewCart;
    private LinearLayout layoutEmptyCart;
    private LinearLayout layoutCartSummary;
    private TextView txtEmptyMessage;
    private TextView txtTotalItems;
    private TextView txtTotalAmount;
    private Button btnClearCart;
    private Button btnCheckout;
    private Button btnContinueShopping;
    private ImageButton btnBack;

    private CartAdapter cartAdapter;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CartManager
        cartManager = CartManager.getInstance();
        cartManager.addCartUpdateListener(this);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadCartData();
    }

    private void initViews() {
        // Header
        btnBack = findViewById(R.id.btnBack);

        // RecyclerView
        recyclerViewCart = findViewById(R.id.recyclerViewCart);

        // Empty cart layout
        layoutEmptyCart = findViewById(R.id.layoutEmptyCart);
        txtEmptyMessage = findViewById(R.id.txtEmptyMessage);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        // Cart summary
        layoutCartSummary = findViewById(R.id.layoutCartSummary);
        txtTotalItems = findViewById(R.id.txtTotalItems);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        btnClearCart = findViewById(R.id.btnClearCart);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this);
        cartAdapter.setOnCartItemClickListener(this);

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Add item decoration for spacing
        recyclerViewCart.addItemDecoration(new androidx.recyclerview.widget.DividerItemDecoration(
                this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL));
    }

    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Continue shopping
        btnContinueShopping.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShopActivity.class);
            startActivity(intent);
            finish();
        });

        // Clear cart
        btnClearCart.setOnClickListener(v -> showClearCartConfirmation());

        // Checkout
        btnCheckout.setOnClickListener(v -> proceedToCheckout());
    }

    private void loadCartData() {
        Cart currentCart = cartManager.getCurrentCart();
        if (currentCart != null && !currentCart.isEmpty()) {
            cartAdapter.updateCartItems(currentCart.getItemsList());
            showCartContent();
            updateCartSummary();
        } else {
            showEmptyCart();
        }
    }

    private void showCartContent() {
        recyclerViewCart.setVisibility(View.VISIBLE);
        layoutCartSummary.setVisibility(View.VISIBLE);
        layoutEmptyCart.setVisibility(View.GONE);
    }

    private void showEmptyCart() {
        recyclerViewCart.setVisibility(View.GONE);
        layoutCartSummary.setVisibility(View.GONE);
        layoutEmptyCart.setVisibility(View.VISIBLE);

        txtEmptyMessage.setText("Giỏ hàng của bạn đang trống\n\nHãy thêm sản phẩm để tiếp tục mua sắm!");
    }

    private void updateCartSummary() {
        int totalItems = cartManager.getTotalItemCount();
        int totalAmount = cartManager.getTotalAmount();

        txtTotalItems.setText(String.format("Tổng cộng (%d sản phẩm)", totalItems));
        txtTotalAmount.setText(CartManager.formatCurrency(totalAmount));

        // Enable/disable checkout button
        btnCheckout.setEnabled(totalItems > 0);
        btnClearCart.setEnabled(totalItems > 0);
    }

    private void showClearCartConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa giỏ hàng")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ sản phẩm trong giỏ hàng?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    cartManager.clearCart();
//                    Toast.makeText(this, "Đã xóa toàn bộ giỏ hàng", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    private void proceedToCheckout() {
        if (cartManager.isCartEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        // Navigate to CheckoutActivity
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivityForResult(intent, 1001);
    }

    private void processOrder() {
        // TODO: Implement actual order processing
        // - Save order to Firebase
        // - Clear cart
        // - Navigate to order confirmation

        Toast.makeText(this, "Đặt hàng thành công! (Demo)", Toast.LENGTH_LONG).show();

        // For demo, just clear cart and go back
        cartManager.clearCart();

        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    // ================== CartUpdateListener Implementation ==================
    @Override
    public void onCartUpdated(Cart cart) {
        runOnUiThread(() -> {
            loadCartData();
        });
    }

    @Override
    public void onCartItemAdded(CartItem item) {
        runOnUiThread(() -> {
            cartAdapter.addCartItem(item);
            updateCartSummary();
            showCartContent();
        });
    }

    @Override
    public void onCartItemRemoved(String productId) {
        runOnUiThread(() -> {
            cartAdapter.removeCartItem(productId);
            updateCartSummary();

            if (cartManager.isCartEmpty()) {
                showEmptyCart();
            }
        });
    }

    @Override
    public void onCartItemQuantityChanged(String productId, int newQuantity) {
        runOnUiThread(() -> {
            cartAdapter.updateItemQuantity(productId, newQuantity);
            updateCartSummary();
        });
    }

    @Override
    public void onCartCleared() {
        runOnUiThread(() -> {
            cartAdapter.clearAllItems();
            showEmptyCart();
        });
    }

    // ================== CartAdapter.OnCartItemClickListener Implementation ==================
    @Override
    public void onItemClick(CartItem item) {
        // Navigate to product detail
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra("service_name", item.getProductName());
        intent.putExtra("service_price", item.getPrice());
        intent.putExtra("service_img", item.getImageRes());
        intent.putExtra("service_description", item.getDescription());
        intent.putExtra("product_type", item.getType());
        intent.putExtra("product_category", item.getCategory());
        startActivity(intent);
    }

    @Override
    public void onRemoveClick(CartItem item) {
        // Item already removed in adapter, just update UI
        updateCartSummary();

        if (cartManager.isCartEmpty()) {
            showEmptyCart();
        }
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        // Quantity already updated in adapter, just update summary
        updateCartSummary();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove listener to prevent memory leaks
        if (cartManager != null) {
            cartManager.removeCartUpdateListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh cart data when returning to this activity
        loadCartData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Navigate back to shop
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}