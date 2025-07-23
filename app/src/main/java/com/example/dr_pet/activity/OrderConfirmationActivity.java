package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dr_pet.R;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView txtOrderId, txtCustomerName, txtTotalAmount, txtEstimatedDelivery;
    private TextView txtOrderStatus, txtThankYouMessage;
    private Button btnBackToHome, btnViewOrderDetails, btnContinueShopping;

    private String orderId;
    private String customerName;
    private int totalAmount;
    private String estimatedDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Get data from intent
        getDataFromIntent();

        initViews();
        displayOrderInfo();
        setupClickListeners();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderId = intent.getStringExtra("order_id");
        customerName = intent.getStringExtra("customer_name");
        totalAmount = intent.getIntExtra("total_amount", 0);
        estimatedDelivery = intent.getStringExtra("estimated_delivery");

        // Set default values if missing
        if (orderId == null) orderId = "N/A";
        if (customerName == null) customerName = "Khách hàng";
        if (estimatedDelivery == null) estimatedDelivery = "2-3 ngày làm việc";
    }

    private void initViews() {
        txtThankYouMessage = findViewById(R.id.txtThankYouMessage);
        txtOrderId = findViewById(R.id.txtOrderId);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        txtEstimatedDelivery = findViewById(R.id.txtEstimatedDelivery);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);

        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnViewOrderDetails = findViewById(R.id.btnViewOrderDetails);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
    }

    private void displayOrderInfo() {
        // Thank you message
        txtThankYouMessage.setText(String.format("Cảm ơn %s đã đặt hàng!", customerName));

        // Order information
        txtOrderId.setText(orderId);
        txtCustomerName.setText(customerName);
        txtTotalAmount.setText(String.format("%,d VND", totalAmount));
        txtEstimatedDelivery.setText(estimatedDelivery);
        txtOrderStatus.setText("Chờ xác nhận");
        txtOrderStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    private void setupClickListeners() {
        // Back to home
        btnBackToHome.setOnClickListener(v -> {
            goToHome();
        });

        // View order details (TODO: implement when have order history)
        btnViewOrderDetails.setOnClickListener(v -> {
            // TODO: Navigate to OrderDetailsActivity
//             Intent intent = new Intent(this, OrderDetailsActivity.class);
//             intent.putExtra("order_id", orderId);
//             startActivity(intent);

            // For now, just show a message
            android.widget.Toast.makeText(this,
                    "Chi tiết đơn hàng: " + orderId,
                    android.widget.Toast.LENGTH_LONG).show();
        });

        // Continue shopping
        btnContinueShopping.setOnClickListener(v -> {
            goToShop();
        });
    }

    private void goToHome() {
        // Navigate to main activity or home
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToShop() {
        // Navigate to shop activity
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent going back to checkout
        goToHome();
    }

    // Auto-close activity after some time (optional)
    private void scheduleAutoClose() {
        new android.os.Handler().postDelayed(() -> {
            if (!isFinishing()) {
                goToHome();
            }
        }, 30000); // 30 seconds
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Optional: Schedule auto-close
        // scheduleAutoClose();
    }
}