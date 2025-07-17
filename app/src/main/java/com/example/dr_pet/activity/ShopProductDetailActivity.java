package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dr_pet.R;

public class ShopProductDetailActivity extends AppCompatActivity {

    private int quantity = 1;
    private TextView txtQuantity;
    private Button btnDecrease, btnIncrease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_product_detail);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Initialize main product views
            ImageView imgProduct = findViewById(R.id.imgShopOrder);
            TextView txtName = findViewById(R.id.txtShopOrderName);
            TextView txtPrice = findViewById(R.id.txtShopOrderPrice);
            TextView txtDescription = findViewById(R.id.txtShopOrderDescription);
            TextView txtStockStatus = findViewById(R.id.txtStockStatus);

            // Initialize quantity controls
            txtQuantity = findViewById(R.id.txtQuantity);
            btnDecrease = findViewById(R.id.btnDecrease);
            btnIncrease = findViewById(R.id.btnIncrease);

            // Initialize header buttons
            ImageButton btnBack = findViewById(R.id.btnBack);
            ImageButton btnCart = findViewById(R.id.btnCart);

            // Initialize action buttons
            Button btnCancel = findViewById(R.id.btn_cancel);
            Button btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

            // Get data from Intent
            String name = getIntent().getStringExtra("service_name");
            int price = getIntent().getIntExtra("service_price", 0);
            int imgRes = getIntent().getIntExtra("service_img", 0);
            String description = getIntent().getStringExtra("service_description");

            // Display product information
            txtName.setText(name != null ? name : "Product Name");
            txtPrice.setText(String.format("%,d VND", price));
            txtDescription.setText(description != null ? description : "No description available");

            if (imgRes != 0) {
                imgProduct.setImageResource(imgRes);
            }

            // Set stock status (you can make this dynamic based on your data)
            txtStockStatus.setText("In Stock");
            txtStockStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            // Header button handlers
            btnBack.setOnClickListener(v -> {
                finish();
            });

            btnCart.setOnClickListener(v -> {
                Toast.makeText(this, "View Cart clicked", Toast.LENGTH_SHORT).show();
                // Add navigation to cart activity
                // Intent intent = new Intent(this, CartActivity.class);
                // startActivity(intent);
            });

            // Quantity control handlers
            btnDecrease.setOnClickListener(v -> {
                if (quantity > 1) {
                    quantity--;
                    txtQuantity.setText(String.valueOf(quantity));
                }
            });

            btnIncrease.setOnClickListener(v -> {
                if (quantity < 99) { // Set maximum quantity
                    quantity++;
                    txtQuantity.setText(String.valueOf(quantity));
                }
            });

            // Action button handlers
            btnCancel.setOnClickListener(v -> {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                finish();
            });

            btnConfirmOrder.setOnClickListener(v -> {
                Toast.makeText(this, String.format("Added %d item(s) to cart", quantity), Toast.LENGTH_SHORT).show();
                // Add logic to add product to cart
                // CartManager.addToCart(productId, quantity);
                finish();
            });

            // Initialize related products
            initializeRelatedProducts();

        } catch (Exception e) {
            Toast.makeText(this, "UI Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeRelatedProducts() {
        // Get current product type to show similar products
        String currentProductType = getIntent().getStringExtra("product_type");

        // View All button
        findViewById(R.id.btnViewAllSimilar).setOnClickListener(v -> {
            Toast.makeText(this, "View all similar products", Toast.LENGTH_SHORT).show();
            // Intent to shop with category filter
            // Intent intent = new Intent(this, ShopActivity.class);
            // intent.putExtra("category", currentProductType);
            // startActivity(intent);
        });

        // Load similar products based on type
        loadSimilarProducts(currentProductType);

        // Set click listeners for similar products
        findViewById(R.id.similarProduct1).setOnClickListener(v -> {
            openProductDetail("Premium Cat Food 5kg", 450000, R.drawable.hatmeo,
                    "High-quality cat food with premium ingredients", "food");
        });

        findViewById(R.id.similarProduct2).setOnClickListener(v -> {
            openProductDetail("Tuna Pate for Cats 160g", 45000, R.drawable.pate,
                    "Delicious tuna pate for cats", "food");
        });

        findViewById(R.id.similarProduct3).setOnClickListener(v -> {
            openProductDetail("Organic Cat Treats 200g", 85000, R.drawable.ic_launcher_background,
                    "Healthy organic treats for cats", "food");
        });

        findViewById(R.id.similarProduct4).setOnClickListener(v -> {
            openProductDetail("Pet Leash Strong Pink", 90000, R.drawable.daylung,
                    "Durable pet leash in pink color", "accessory");
        });

        findViewById(R.id.similarProduct5).setOnClickListener(v -> {
            openProductDetail("Double Pet Food Bowl Pink", 50000, R.drawable.bat,
                    "Double bowl for pet food and water", "accessory");
        });
    }

    private void loadSimilarProducts(String productType) {
        // Show products based on current product type
        if ("food".equals(productType)) {
            // Show food products
            findViewById(R.id.similarProduct1).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct2).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct3).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct4).setVisibility(View.GONE);
            findViewById(R.id.similarProduct5).setVisibility(View.GONE);
        } else if ("accessory".equals(productType)) {
            // Show accessory products
            findViewById(R.id.similarProduct1).setVisibility(View.GONE);
            findViewById(R.id.similarProduct2).setVisibility(View.GONE);
            findViewById(R.id.similarProduct3).setVisibility(View.GONE);
            findViewById(R.id.similarProduct4).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct5).setVisibility(View.VISIBLE);
        } else {
            // Show mixed products
            findViewById(R.id.similarProduct1).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct2).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct3).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct4).setVisibility(View.VISIBLE);
            findViewById(R.id.similarProduct5).setVisibility(View.VISIBLE);
        }
    }

    private void openProductDetail(String name, int price, int imageRes, String description, String type) {
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra("service_name", name);
        intent.putExtra("service_price", price);
        intent.putExtra("service_img", imageRes);
        intent.putExtra("service_description", description);
        intent.putExtra("product_type", type);
        startActivity(intent);
    }
}