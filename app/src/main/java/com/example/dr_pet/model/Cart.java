package com.example.dr_pet.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private String userId;                          // ID người dùng
    private Map<String, CartItem> items;            // Map productId -> CartItem
    private long lastUpdated;                       // Thời gian cập nhật cuối
    private String cartId;                          // ID của cart (có thể dùng cho Firebase)

    // Constructor mặc định (cần cho Firebase)
    public Cart() {
        this.items = new HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
    }

    // Constructor với userId
    public Cart(String userId) {
        this.userId = userId;
        this.items = new HashMap<>();
        this.lastUpdated = System.currentTimeMillis();
        this.cartId = "cart_" + userId;
    }

    // ================== GETTERS & SETTERS ==================
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, CartItem> getItems() {
        return items;
    }

    public void setItems(Map<String, CartItem> items) {
        this.items = items != null ? items : new HashMap<>();
        updateLastModified();
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    // ================== CART OPERATIONS ==================

    /**
     * Thêm sản phẩm vào giỏ hàng
     * Nếu đã có thì tăng số lượng, nếu chưa thì thêm mới
     */
    public boolean addItem(CartItem cartItem) {
        if (cartItem == null || cartItem.getProductId() == null) {
            return false;
        }

        String productId = cartItem.getProductId();

        if (items.containsKey(productId)) {
            // Sản phẩm đã có trong cart, tăng số lượng
            CartItem existingItem = items.get(productId);
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
        } else {
            // Sản phẩm mới, thêm vào cart
            items.put(productId, cartItem);
        }

        updateLastModified();
        return true;
    }

    /**
     * Thêm sản phẩm từ Product với số lượng
     */
    public boolean addItem(String productId, Product product, int quantity) {
        CartItem cartItem = new CartItem(productId, product, quantity);
        return addItem(cartItem);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public boolean removeItem(String productId) {
        boolean removed = items.remove(productId) != null;
        if (removed) {
            updateLastModified();
        }
        return removed;
    }

    /**
     * Cập nhật số lượng sản phẩm
     */
    public boolean updateItemQuantity(String productId, int newQuantity) {
        if (!items.containsKey(productId)) {
            return false;
        }

        if (newQuantity <= 0) {
            return removeItem(productId);
        }

        CartItem item = items.get(productId);
        item.setQuantity(newQuantity);
        updateLastModified();
        return true;
    }

    /**
     * Tăng số lượng sản phẩm
     */
    public boolean increaseItemQuantity(String productId) {
        if (!items.containsKey(productId)) {
            return false;
        }

        CartItem item = items.get(productId);
        item.increaseQuantity();
        updateLastModified();
        return true;
    }

    /**
     * Giảm số lượng sản phẩm
     */
    public boolean decreaseItemQuantity(String productId) {
        if (!items.containsKey(productId)) {
            return false;
        }

        CartItem item = items.get(productId);
        if (item.getQuantity() <= 1) {
            return removeItem(productId);
        }

        item.decreaseQuantity();
        updateLastModified();
        return true;
    }

    /**
     * Lấy CartItem theo productId
     */
    public CartItem getItem(String productId) {
        return items.get(productId);
    }

    /**
     * Kiểm tra xem sản phẩm có trong cart không
     */
    public boolean containsItem(String productId) {
        return items.containsKey(productId);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart() {
        items.clear();
        updateLastModified();
    }

    // ================== CART CALCULATIONS ==================

    /**
     * Lấy danh sách tất cả CartItem
     */
    public List<CartItem> getItemsList() {
        return new ArrayList<>(items.values());
    }

    /**
     * Tính tổng số lượng sản phẩm trong cart
     */
    public int getTotalItemCount() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Tính tổng số loại sản phẩm khác nhau
     */
    public int getTotalUniqueItems() {
        return items.size();
    }

    /**
     * Tính tổng tiền của toàn bộ giỏ hàng
     */
    public int getTotalAmount() {
        int total = 0;
        for (CartItem item : items.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    /**
     * Lấy tổng tiền đã format
     */
    public String getFormattedTotalAmount() {
        return String.format("%,d VND", getTotalAmount());
    }

    /**
     * Kiểm tra giỏ hàng có rỗng không
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    // ================== FILTER & SEARCH ==================

    /**
     * Lấy sản phẩm theo category
     */
    public List<CartItem> getItemsByCategory(String category) {
        List<CartItem> filteredItems = new ArrayList<>();
        for (CartItem item : items.values()) {
            if (category.equals("all") || category.equals(item.getCategory())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    /**
     * Lấy sản phẩm theo type
     */
    public List<CartItem> getItemsByType(String type) {
        List<CartItem> filteredItems = new ArrayList<>();
        for (CartItem item : items.values()) {
            if (type.equals(item.getType())) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    // ================== HELPER METHODS ==================

    /**
     * Cập nhật thời gian sửa đổi
     */
    private void updateLastModified() {
        this.lastUpdated = System.currentTimeMillis();
    }

    /**
     * Tạo summary text cho giỏ hàng
     */
    public String getCartSummary() {
        if (isEmpty()) {
            return "Giỏ hàng trống";
        }
        return String.format("Có %d sản phẩm (%d loại) - Tổng: %s",
                getTotalItemCount(), getTotalUniqueItems(), getFormattedTotalAmount());
    }

    /**
     * Validate cart data
     */
    public boolean isValid() {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra tất cả items có hợp lệ không
        for (CartItem item : items.values()) {
            if (item.getProductId() == null || item.getQuantity() <= 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "userId='" + userId + '\'' +
                ", itemCount=" + getTotalUniqueItems() +
                ", totalQuantity=" + getTotalItemCount() +
                ", totalAmount=" + getTotalAmount() +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}