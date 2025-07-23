package com.example.dr_pet.model;

public class CartItem {
    private String productId;
    private String productName;
    private int price;
    private int quantity;
    private int imageRes;
    private String category;
    private String type;
    private String description;
    private long addedDate;


    public CartItem() {
    }


    public CartItem(String productId, String productName, int price, int quantity,
                    int imageRes, String category, String type, String description) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imageRes = imageRes;
        this.category = category;
        this.type = type;
        this.description = description;
        this.addedDate = System.currentTimeMillis();
    }

    // Constructor từ Product (tiện lợi khi add to cart)
    public CartItem(String productId, Product product, int quantity) {
        this.productId = productId;
        this.productName = product.getName();
        this.price = product.getPrice();
        this.quantity = quantity;
        this.imageRes = product.getImageRes();
        this.category = product.getCategory();
        this.type = product.getType();
        this.description = product.getDescription();
        this.addedDate = System.currentTimeMillis();
    }

    // ================== GETTERS ==================
    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getAddedDate() {
        return addedDate;
    }

    // ================== SETTERS ==================
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    // ================== HELPER METHODS ==================

    /**
     * Tính tổng tiền cho item này (giá x số lượng)
     */
    public int getTotalPrice() {
        return price * quantity;
    }

    /**
     * Lấy giá đã format
     */
    public String getFormattedPrice() {
        return String.format("%,d VND", price);
    }

    /**
     * Lấy tổng tiền đã format
     */
    public String getFormattedTotalPrice() {
        return String.format("%,d VND", getTotalPrice());
    }

    /**
     * Tăng số lượng
     */
    public void increaseQuantity() {
        this.quantity++;
    }

    /**
     * Giảm số lượng (không để âm)
     */
    public void decreaseQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }

    /**
     * Cập nhật số lượng với giá trị cụ thể
     */
    public void updateQuantity(int newQuantity) {
        if (newQuantity > 0) {
            this.quantity = newQuantity;
        }
    }

    /**
     * Kiểm tra xem có phải cùng sản phẩm không
     */
    public boolean isSameProduct(String productId) {
        return this.productId != null && this.productId.equals(productId);
    }

    /**
     * Convert thành Product object (nếu cần)
     */
    public Product toProduct() {
        return new Product(productName, price, imageRes, category, type, description);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + getTotalPrice() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CartItem cartItem = (CartItem) obj;
        return productId != null ? productId.equals(cartItem.productId) : cartItem.productId == null;
    }

    @Override
    public int hashCode() {
        return productId != null ? productId.hashCode() : 0;
    }
}