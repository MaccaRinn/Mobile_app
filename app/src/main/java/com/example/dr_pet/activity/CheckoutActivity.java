package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.adapter.CheckoutAdapter;
import com.example.dr_pet.manager.CartManager;
import com.example.dr_pet.model.CartItem;
import com.example.dr_pet.model.ShopOrder;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etCustomerName, etPhoneNumber, etAddress, etNotes;
    private RadioGroup rgPaymentMethod;
    private RadioButton rbCashOnDelivery, rbBankTransfer, rbCreditCard;
    private RecyclerView recyclerViewOrderItems;
    private TextView txtOrderSummary, txtTotalAmount, txtShippingFee, txtFinalTotal;
    private Button btnPlaceOrder;
    private ImageButton btnBack;

    private CheckoutAdapter checkoutAdapter;
    private CartManager cartManager;
    private List<CartItem> orderItems;
    private int totalAmount = 0;
    private int shippingFee = 30000; // Fixed shipping fee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        cartManager = CartManager.getInstance();
        orderItems = cartManager.getCartItems();

        if (orderItems.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupRecyclerView();
        calculateTotal();
        setupClickListeners();
        loadCustomerInfo();
    }

    private void initViews() {
        // Header
        btnBack = findViewById(R.id.btnBack);

        // Customer Information
        etCustomerName = findViewById(R.id.etCustomerName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        etNotes = findViewById(R.id.etNotes);

        // Payment Method
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rbCashOnDelivery = findViewById(R.id.rbCashOnDelivery);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        rbCreditCard = findViewById(R.id.rbCreditCard);

        // Order Summary
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        txtOrderSummary = findViewById(R.id.txtOrderSummary);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtShippingFee = findViewById(R.id.txtShippingFee);
        txtFinalTotal = findViewById(R.id.txtFinalTotal);

        // Action Button
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
    }

    private void setupRecyclerView() {
        checkoutAdapter = new CheckoutAdapter(this, orderItems);
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrderItems.setAdapter(checkoutAdapter);
    }

    private void calculateTotal() {
        totalAmount = 0;
        for (CartItem item : orderItems) {
            totalAmount += item.getTotalPrice();
        }

        updateOrderSummary();
    }

    private void updateOrderSummary() {
        int itemCount = 0;
        for (CartItem item : orderItems) {
            itemCount += item.getQuantity();
        }

        txtOrderSummary.setText(String.format("Đơn hàng (%d sản phẩm)", itemCount));
        txtTotalAmount.setText(CartManager.formatCurrency(totalAmount));
        txtShippingFee.setText(CartManager.formatCurrency(shippingFee));
        txtFinalTotal.setText(CartManager.formatCurrency(totalAmount + shippingFee));
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnPlaceOrder.setOnClickListener(v -> validateAndPlaceOrder());
    }

    private void loadCustomerInfo() {
        // TODO: Load customer info from SharedPreferences or Firebase
        // For now, set default values
        etCustomerName.setHint("Nhập họ tên");
        etPhoneNumber.setHint("Nhập số điện thoại");
        etAddress.setHint("Nhập địa chỉ giao hàng");
        etNotes.setHint("Ghi chú (không bắt buộc)");

        // Set default payment method
        rbCashOnDelivery.setChecked(true);
    }

    private void validateAndPlaceOrder() {
        // Validate customer information
        String customerName = etCustomerName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (customerName.isEmpty()) {
            etCustomerName.setError("Vui lòng nhập họ tên");
            etCustomerName.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Vui lòng nhập số điện thoại");
            etPhoneNumber.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            etAddress.setError("Vui lòng nhập địa chỉ giao hàng");
            etAddress.requestFocus();
            return;
        }

        // Validate phone number format
        if (!isValidPhoneNumber(phoneNumber)) {
            etPhoneNumber.setError("Số điện thoại không hợp lệ");
            etPhoneNumber.requestFocus();
            return;
        }

        // Get selected payment method
        String paymentMethod = getSelectedPaymentMethod();

        // Show order confirmation dialog
        showOrderConfirmation(customerName, phoneNumber, address, notes, paymentMethod);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Simple Vietnamese phone number validation
        return phoneNumber.matches("^(0|\\+84)[0-9]{9,10}$");
    }

    private String getSelectedPaymentMethod() {
        int selectedId = rgPaymentMethod.getCheckedRadioButtonId();

        if (selectedId == R.id.rbCashOnDelivery) {
            return "Thanh toán khi nhận hàng (COD)";
        } else if (selectedId == R.id.rbBankTransfer) {
            return "Chuyển khoản ngân hàng";
        } else if (selectedId == R.id.rbCreditCard) {
            return "Thẻ tín dụng/Thẻ ATM";
        }

        return "Thanh toán khi nhận hàng (COD)"; // Default
    }

    private void showOrderConfirmation(String customerName, String phoneNumber,
                                       String address, String notes, String paymentMethod) {

        String confirmationMessage = String.format(
                "Xác nhận đặt hàng\n\n" +
                        "Khách hàng: %s\n" +
                        "Điện thoại: %s\n" +
                        "Địa chỉ: %s\n" +
                        "Thanh toán: %s\n\n" +
                        "Tổng tiền: %s\n" +
                        "Phí vận chuyển: %s\n" +
                        "Thành tiền: %s",
                customerName, phoneNumber, address, paymentMethod,
                CartManager.formatCurrency(totalAmount),
                CartManager.formatCurrency(shippingFee),
                CartManager.formatCurrency(totalAmount + shippingFee)
        );

        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đặt hàng")
                .setMessage(confirmationMessage)
                .setPositiveButton("Đặt hàng", (dialog, which) -> {
                    processOrder(customerName, phoneNumber, address, notes, paymentMethod);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void processOrder(String customerName, String phoneNumber,
                              String address, String notes, String paymentMethod) {

        // Create ShopOrder object
        ShopOrder shopOrder = new ShopOrder(
                cartManager.getCurrentCart().getUserId(),
                customerName,
                phoneNumber,
                address,
                notes,
                paymentMethod,
                orderItems,
                totalAmount,
                shippingFee,
                totalAmount + shippingFee
        );

        // TODO: Save order to Firebase
        // ShopOrderManager.saveOrder(shopOrder);

        // Clear cart after successful order
        cartManager.clearCart();

        // Navigate to order confirmation
        Intent intent = new Intent(this, OrderConfirmationActivity.class);
        intent.putExtra("order_id", shopOrder.getOrderId());
        intent.putExtra("customer_name", customerName);
        intent.putExtra("total_amount", totalAmount + shippingFee);
        intent.putExtra("estimated_delivery", "2-3 ngày làm việc");

        startActivity(intent);

        // Close checkout and cart activities
        setResult(RESULT_OK);
        finish();

        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();
    }

    private void saveCustomerInfo(String customerName, String phoneNumber, String address) {
        // TODO: Save customer info to SharedPreferences for next time
        getSharedPreferences("customer_info", MODE_PRIVATE)
                .edit()
                .putString("name", customerName)
                .putString("phone", phoneNumber)
                .putString("address", address)
                .apply();
    }
}