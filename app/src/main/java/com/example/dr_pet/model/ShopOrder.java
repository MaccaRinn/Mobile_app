package com.example.dr_pet.model;

import java.util.ArrayList;
import java.util.List;

public class ShopOrder {
    private String orderId;
    private String userId;
    private String customerName;
    private String phoneNumber;
    private String address;
    private String notes;
    private String paymentMethod;
    private String orderStatus;
    private List<CartItem> orderItems;
    private int subtotal;
    private int shippingFee;
    private int totalAmount;
    private long orderDate;
    private long estimatedDeliveryDate;

    // Constructor mặc định (cho Firebase)
    public ShopOrder() {
        this.orderItems = new ArrayList<>();
        this.orderDate = System.currentTimeMillis();
        this.orderStatus = "PENDING"; // PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
    }

    // Constructor đầy đủ
    public ShopOrder(String userId, String customerName, String phoneNumber, String address,
                     String notes, String paymentMethod, List<CartItem> orderItems,
                     int subtotal, int shippingFee, int totalAmount) {
        this();
        this.orderId = generateOrderId();
        this.userId = userId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.notes = notes;
        this.paymentMethod = paymentMethod;
        this.orderItems = new ArrayList<>(orderItems);
        this.subtotal = subtotal;
        this.shippingFee = shippingFee;
        this.totalAmount = totalAmount;
        this.estimatedDeliveryDate = calculateEstimatedDelivery();
    }

    // ================== GETTERS & SETTERS ==================

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public long getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(long estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    // ================== HELPER METHODS ==================

    /**
     * Tạo ID đơn hàng unique
     */
    private String generateOrderId() {
        return "ORDER_" + System.currentTimeMillis() + "_" +
                (int)(Math.random() * 1000);
    }

    /**
     * Tính ngày giao hàng dự kiến (2-3 ngày làm việc)
     */
    private long calculateEstimatedDelivery() {
        // Thêm 3 ngày (3 * 24 * 60 * 60 * 1000 milliseconds)
        return orderDate + (3L * 24 * 60 * 60 * 1000);
    }

    /**
     * Lấy tổng số lượng sản phẩm
     */
    public int getTotalItemCount() {
        int total = 0;
        for (CartItem item : orderItems) {
            total += item.getQuantity();
        }
        return total;
    }

    /**
     * Lấy số loại sản phẩm khác nhau
     */
    public int getTotalUniqueItems() {
        return orderItems.size();
    }

    /**
     * Format giá tiền
     */
    public String getFormattedSubtotal() {
        return String.format("%,d VND", subtotal);
    }

    public String getFormattedShippingFee() {
        return String.format("%,d VND", shippingFee);
    }

    public String getFormattedTotalAmount() {
        return String.format("%,d VND", totalAmount);
    }

    /**
     * Format ngày đặt hàng
     */
    public String getFormattedOrderDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(new java.util.Date(orderDate));
    }

    /**
     * Format ngày giao hàng dự kiến
     */
    public String getFormattedEstimatedDelivery() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new java.util.Date(estimatedDeliveryDate));
    }

    /**
     * Lấy màu status
     */
    public int getStatusColor() {
        switch (orderStatus) {
            case "PENDING":
                return android.R.color.holo_orange_dark;
            case "CONFIRMED":
                return android.R.color.holo_blue_dark;
            case "SHIPPING":
                return android.R.color.holo_purple;
            case "DELIVERED":
                return android.R.color.holo_green_dark;
            case "CANCELLED":
                return android.R.color.holo_red_dark;
            default:
                return android.R.color.darker_gray;
        }
    }

    /**
     * Lấy text status tiếng Việt
     */
    public String getStatusText() {
        switch (orderStatus) {
            case "PENDING":
                return "Chờ xác nhận";
            case "CONFIRMED":
                return "Đã xác nhận";
            case "SHIPPING":
                return "Đang giao hàng";
            case "DELIVERED":
                return "Đã giao hàng";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return "Không xác định";
        }
    }

    /**
     * Kiểm tra đơn hàng có thể hủy không
     */
    public boolean canBeCancelled() {
        return "PENDING".equals(orderStatus) || "CONFIRMED".equals(orderStatus);
    }

    /**
     * Validate order data
     */
    public boolean isValid() {
        return orderId != null && !orderId.isEmpty() &&
                userId != null && !userId.isEmpty() &&
                customerName != null && !customerName.isEmpty() &&
                phoneNumber != null && !phoneNumber.isEmpty() &&
                address != null && !address.isEmpty() &&
                !orderItems.isEmpty() &&
                totalAmount > 0;
    }

    /**
     * Tạo order summary text
     */
    public String getOrderSummary() {
        return String.format("Đơn hàng %s\n%d sản phẩm - %s\nTrạng thái: %s",
                orderId, getTotalItemCount(), getFormattedTotalAmount(), getStatusText());
    }

    @Override
    public String toString() {
        return "ShopOrder{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", totalAmount=" + totalAmount +
                ", orderStatus='" + orderStatus + '\'' +
                ", itemCount=" + getTotalItemCount() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ShopOrder shopOrder = (ShopOrder) obj;
        return orderId != null ? orderId.equals(shopOrder.orderId) : shopOrder.orderId == null;
    }

    @Override
    public int hashCode() {
        return orderId != null ? orderId.hashCode() : 0;
    }
}