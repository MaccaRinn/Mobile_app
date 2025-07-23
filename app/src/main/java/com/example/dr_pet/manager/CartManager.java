package com.example.dr_pet.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.dr_pet.model.Cart;
import com.example.dr_pet.model.CartItem;
import com.example.dr_pet.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private static final String PREFS_NAME = "cart_prefs";
    private static final String KEY_CART_COUNT = "cart_count";

    private Cart currentCart;
    private Context context;
    private DatabaseReference databaseRef;
    private FirebaseAuth firebaseAuth;
    private List<CartUpdateListener> listeners;

    // Private constructor cho Singleton
    private CartManager() {
        this.listeners = new ArrayList<>();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    // ================== SINGLETON INSTANCE ==================
    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    /**
     * Khởi tạo CartManager với context
     */
    public void initialize(Context context) {
        this.context = context.getApplicationContext();
        loadCart();
    }

    // ================== INTERFACE FOR LISTENERS ==================
    public interface CartUpdateListener {
        void onCartUpdated(Cart cart);
        void onCartItemAdded(CartItem item);
        void onCartItemRemoved(String productId);
        void onCartItemQuantityChanged(String productId, int newQuantity);
        void onCartCleared();
    }

    public void addCartUpdateListener(CartUpdateListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeCartUpdateListener(CartUpdateListener listener) {
        listeners.remove(listener);
    }

    private void notifyCartUpdated() {
        for (CartUpdateListener listener : listeners) {
            listener.onCartUpdated(currentCart);
        }
        saveCartCountToPrefs();
    }

    private void notifyItemAdded(CartItem item) {
        for (CartUpdateListener listener : listeners) {
            listener.onCartItemAdded(item);
        }
    }

    private void notifyItemRemoved(String productId) {
        for (CartUpdateListener listener : listeners) {
            listener.onCartItemRemoved(productId);
        }
    }

    private void notifyQuantityChanged(String productId, int newQuantity) {
        for (CartUpdateListener listener : listeners) {
            listener.onCartItemQuantityChanged(productId, newQuantity);
        }
    }

    private void notifyCartCleared() {
        for (CartUpdateListener listener : listeners) {
            listener.onCartCleared();
        }
    }

    // ================== CART OPERATIONS ==================

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    public void addToCart(String productId, Product product, int quantity) {
        if (currentCart == null) {
            loadCart();
        }

        CartItem cartItem = new CartItem(productId, product, quantity);
        boolean isNewItem = !currentCart.containsItem(productId);

        if (currentCart.addItem(cartItem)) {
            syncToFirebase();
            notifyCartUpdated();

            if (isNewItem) {
                notifyItemAdded(cartItem);
            }

            if (context != null) {
                String message = isNewItem ?
                        "Đã thêm " + product.getName() + " vào giỏ hàng" :
                        "Đã cập nhật số lượng " + product.getName();
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Thêm sản phẩm với thông tin cơ bản
     */
    public void addToCart(String productId, String productName, int price, int quantity, int imageRes) {
        Product tempProduct = new Product(productName, price, imageRes, "all", "unknown", "");
        addToCart(productId, tempProduct, quantity);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void removeFromCart(String productId) {
        if (currentCart == null || !currentCart.containsItem(productId)) {
            return;
        }

        CartItem removedItem = currentCart.getItem(productId);
        if (currentCart.removeItem(productId)) {
            syncToFirebase();
            notifyCartUpdated();
            notifyItemRemoved(productId);

            if (context != null && removedItem != null) {
                Toast.makeText(context, "Đã xóa " + removedItem.getProductName() + " khỏi giỏ hàng",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Cập nhật số lượng sản phẩm
     */
    public void updateQuantity(String productId, int newQuantity) {
        if (currentCart == null) {
            return;
        }

        if (currentCart.updateItemQuantity(productId, newQuantity)) {
            syncToFirebase();
            notifyCartUpdated();
            notifyQuantityChanged(productId, newQuantity);
        }
    }

    /**
     * Tăng số lượng sản phẩm
     */
    public void increaseQuantity(String productId) {
        if (currentCart != null && currentCart.increaseItemQuantity(productId)) {
            syncToFirebase();
            notifyCartUpdated();

            CartItem item = currentCart.getItem(productId);
            if (item != null) {
                notifyQuantityChanged(productId, item.getQuantity());
            }
        }
    }

    /**
     * Giảm số lượng sản phẩm
     */
    public void decreaseQuantity(String productId) {
        if (currentCart != null && currentCart.decreaseItemQuantity(productId)) {
            syncToFirebase();
            notifyCartUpdated();

            CartItem item = currentCart.getItem(productId);
            if (item != null) {
                notifyQuantityChanged(productId, item.getQuantity());
            } else {
                // Item đã bị xóa vì quantity = 0
                notifyItemRemoved(productId);
            }
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart() {
        if (currentCart != null) {
            currentCart.clearCart();
            syncToFirebase();
            notifyCartUpdated();
            notifyCartCleared();

            if (context != null) {
                Toast.makeText(context, "Đã xóa toàn bộ giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ================== CART DATA ACCESS ==================

    /**
     * Lấy giỏ hàng hiện tại
     */
    public Cart getCurrentCart() {
        if (currentCart == null) {
            loadCart();
        }
        return currentCart;
    }

    /**
     * Lấy danh sách CartItem
     */
    public List<CartItem> getCartItems() {
        if (currentCart == null) {
            return new ArrayList<>();
        }
        return currentCart.getItemsList();
    }

    /**
     * Lấy số lượng tổng sản phẩm
     */
    public int getTotalItemCount() {
        if (currentCart == null) {
            return 0;
        }
        return currentCart.getTotalItemCount();
    }

    /**
     * Lấy số loại sản phẩm khác nhau
     */
    public int getTotalUniqueItems() {
        if (currentCart == null) {
            return 0;
        }
        return currentCart.getTotalUniqueItems();
    }

    /**
     * Lấy tổng tiền
     */
    public int getTotalAmount() {
        if (currentCart == null) {
            return 0;
        }
        return currentCart.getTotalAmount();
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không
     */
    public boolean isCartEmpty() {
        return currentCart == null || currentCart.isEmpty();
    }

    /**
     * Kiểm tra sản phẩm có trong giỏ hàng không
     */
    public boolean isProductInCart(String productId) {
        return currentCart != null && currentCart.containsItem(productId);
    }

    /**
     * Lấy CartItem theo productId
     */
    public CartItem getCartItem(String productId) {
        if (currentCart == null) {
            return null;
        }
        return currentCart.getItem(productId);
    }

    // ================== FIREBASE OPERATIONS ==================

    /**
     * Load giỏ hàng từ Firebase hoặc tạo mới
     */
    private void loadCart() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // User đã đăng nhập, load từ Firebase
            loadCartFromFirebase(currentUser.getUid());
        } else {
            // User chưa đăng nhập, tạo cart local
            currentCart = new Cart("guest_" + System.currentTimeMillis());
        }
    }

    /**
     * Load giỏ hàng từ Firebase
     */
    private void loadCartFromFirebase(String userId) {
        DatabaseReference cartRef = databaseRef.child("users").child(userId).child("cart");

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    try {
                        Cart firebaseCart = dataSnapshot.getValue(Cart.class);
                        if (firebaseCart != null && firebaseCart.isValid()) {
                            currentCart = firebaseCart;
                            currentCart.setUserId(userId);
                        } else {
                            currentCart = new Cart(userId);
                        }
                    } catch (Exception e) {
                        currentCart = new Cart(userId);
                    }
                } else {
                    currentCart = new Cart(userId);
                }
                notifyCartUpdated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                currentCart = new Cart(userId);
                notifyCartUpdated();
            }
        });
    }

    /**
     * Sync giỏ hàng lên Firebase
     */
    public void syncToFirebase() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null && currentCart != null) {
            String userId = currentUser.getUid();
            currentCart.setUserId(userId);

            DatabaseReference cartRef = databaseRef.child("users").child(userId).child("cart");
            cartRef.setValue(currentCart)
                    .addOnSuccessListener(aVoid -> {
                        // Sync thành công - không cần thông báo
                    })
                    .addOnFailureListener(e -> {
                        if (context != null) {
                            Toast.makeText(context, "Lỗi đồng bộ giỏ hàng: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Xử lý khi user login
     */
    public void onUserLogin(String userId) {
        Cart oldCart = currentCart;
        loadCartFromFirebase(userId);

        // Nếu có cart cũ, merge với cart mới
        if (oldCart != null && !oldCart.isEmpty()) {
            // Merge logic có thể implement sau
        }
    }

    /**
     * Xử lý khi user logout
     */
    public void onUserLogout() {
        currentCart = new Cart("guest_" + System.currentTimeMillis());
        clearCartCountFromPrefs();
        notifyCartUpdated();
    }

    // ================== SHARED PREFERENCES ==================

    /**
     * Lưu số lượng cart vào SharedPreferences để hiển thị badge
     */
    private void saveCartCountToPrefs() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putInt(KEY_CART_COUNT, getTotalItemCount()).apply();
        }
    }

    /**
     * Lấy số lượng cart từ SharedPreferences
     */
    public int getCartCountFromPrefs() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            return prefs.getInt(KEY_CART_COUNT, 0);
        }
        return 0;
    }

    /**
     * Xóa cart count từ SharedPreferences
     */
    private void clearCartCountFromPrefs() {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().remove(KEY_CART_COUNT).apply();
        }
    }

    // ================== UTILITY METHODS ==================

    /**
     * Tạo productId từ thông tin sản phẩm
     */
    public static String generateProductId(String productName, int price) {
        return productName.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase() + "_" + price;
    }

    /**
     * Format currency
     */
    public static String formatCurrency(int amount) {
        return String.format("%,d VND", amount);
    }

    /**
     * Debug method - in thông tin cart
     */
    public void printCartInfo() {
        if (currentCart != null) {
            System.out.println("=== CART INFO ===");
            System.out.println(currentCart.toString());
            System.out.println("Items: " + currentCart.getItemsList().size());
            for (CartItem item : currentCart.getItemsList()) {
                System.out.println("- " + item.toString());
            }
            System.out.println("==================");
        }
    }
}