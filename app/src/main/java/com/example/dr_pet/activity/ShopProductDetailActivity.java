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
import com.example.dr_pet.manager.CartManager;
import com.example.dr_pet.model.Product;

public class ShopProductDetailActivity extends AppCompatActivity implements CartManager.CartUpdateListener {

    private int quantity = 1;
    private TextView txtQuantity;
    private Button btnDecrease, btnIncrease;
    private TextView txtCartBadge;

    // Product data
    private String productName;
    private int productPrice;
    private int productImageRes;
    private String productDescription;
    private String productType;
    private String productCategory;
    private String productId;

    private CartManager cartManager;

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

            // Initialize CartManager
            cartManager = CartManager.getInstance();
            cartManager.addCartUpdateListener(this);

            // Initialize views
            initializeViews();

            // Get product data from Intent
            getProductDataFromIntent();

            // Display product information
            displayProductInfo();

            // Setup click listeners
            setupClickListeners();

            // Update cart badge
            updateCartBadge();

            // Initialize related products
            initializeRelatedProducts();

        } catch (Exception e) {
            Toast.makeText(this, "UI Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        // Main product views
        findViewById(R.id.imgShopOrder);
        findViewById(R.id.txtShopOrderName);
        findViewById(R.id.txtShopOrderPrice);
        findViewById(R.id.txtShopOrderDescription);
        findViewById(R.id.txtStockStatus);

        // Quantity controls
        txtQuantity = findViewById(R.id.txtQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);

        // Header buttons
        findViewById(R.id.btnBack);
        findViewById(R.id.btnCart);

        // Cart badge (nếu có trong layout)
        txtCartBadge = findViewById(R.id.txtCartBadge); // Có thể null nếu chưa có

        // Action buttons
        findViewById(R.id.btn_cancel);
        findViewById(R.id.btnConfirmOrder);
    }

    private void getProductDataFromIntent() {
        Intent intent = getIntent();
        productName = intent.getStringExtra("service_name");
        productPrice = intent.getIntExtra("service_price", 0);
        productImageRes = intent.getIntExtra("service_img", 0);
        productDescription = intent.getStringExtra("service_description");
        productType = intent.getStringExtra("product_type");
        productCategory = intent.getStringExtra("product_category");

        // Generate unique product ID
        if (productName != null && productPrice > 0) {
            productId = CartManager.generateProductId(productName, productPrice);
        }

        // Set default values if missing
        if (productName == null) productName = "Product Name";
        if (productDescription == null) productDescription = "No description available";
        if (productType == null) productType = "unknown";
        if (productCategory == null) productCategory = "all";
    }

    private void displayProductInfo() {
        // Display product information
        ImageView imgProduct = findViewById(R.id.imgShopOrder);
        TextView txtName = findViewById(R.id.txtShopOrderName);
        TextView txtPrice = findViewById(R.id.txtShopOrderPrice);
        TextView txtDescription = findViewById(R.id.txtShopOrderDescription);
        TextView txtStockStatus = findViewById(R.id.txtStockStatus);

        txtName.setText(productName);
        txtPrice.setText(String.format("%,d VND", productPrice));
        txtDescription.setText(productDescription);

        if (productImageRes != 0) {
            imgProduct.setImageResource(productImageRes);
        }

        // Set stock status
        txtStockStatus.setText("In Stock");
        txtStockStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        // Check if product is already in cart and set initial quantity
        if (cartManager.isProductInCart(productId)) {
            quantity = cartManager.getCartItem(productId).getQuantity();
            txtQuantity.setText(String.valueOf(quantity));
        } else {
            txtQuantity.setText(String.valueOf(quantity));
        }
    }

    private void setupClickListeners() {
        // Header button handlers
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            // Navigate to CartActivity
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
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
        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            finish();
        });

        Button btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnConfirmOrder.setOnClickListener(v -> {
            addToCart();

            // Optional: Show option to go to cart or continue shopping
            showAddToCartSuccess();
        });
    }

    /**
     * Show success dialog after adding to cart
     */
    private void showAddToCartSuccess() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Thêm vào giỏ hàng thành công!")
                .setMessage("Sản phẩm đã được thêm vào giỏ hàng. Bạn muốn làm gì tiếp theo?")
                .setPositiveButton("Xem giỏ hàng", (dialog, which) -> {
                    Intent intent = new Intent(this, CartActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Tiếp tục mua sắm", (dialog, which) -> {
                    finish(); // Go back to shop
                })
                .setNeutralButton("Ở lại", null)
                .show();
    }

    private void addToCart() {
        if (productId == null) {
            Toast.makeText(this, "Lỗi: Không thể xác định sản phẩm", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create Product object
            Product product = new Product(
                    productName,
                    productPrice,
                    productImageRes,
                    productCategory,
                    productType,
                    productDescription
            );

            // Add to cart using CartManager
            cartManager.addToCart(productId, product, quantity);

            // Show success message
            String message = String.format("Đã thêm %d %s vào giỏ hàng", quantity, productName);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            // Optionally finish activity or reset quantity
            // finish(); // Uncomment if you want to go back after adding

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi thêm vào giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartBadge() {
        int cartCount = cartManager.getTotalItemCount();

        if (txtCartBadge != null) {
            if (cartCount > 0) {
                txtCartBadge.setVisibility(View.VISIBLE);
                txtCartBadge.setText(cartCount > 99 ? "99+" : String.valueOf(cartCount));
            } else {
                txtCartBadge.setVisibility(View.GONE);
            }
        }
    }

    // ================== CartUpdateListener Implementation ==================
    @Override
    public void onCartUpdated(com.example.dr_pet.model.Cart cart) {
        runOnUiThread(() -> {
            updateCartBadge();
        });
    }

    @Override
    public void onCartItemAdded(com.example.dr_pet.model.CartItem item) {
        runOnUiThread(() -> {
            updateCartBadge();
            // You can add more UI updates here if needed
        });
    }

    @Override
    public void onCartItemRemoved(String productId) {
        runOnUiThread(() -> {
            updateCartBadge();
        });
    }

    @Override
    public void onCartItemQuantityChanged(String productId, int newQuantity) {
        runOnUiThread(() -> {
            updateCartBadge();
            // Update quantity display if this is the same product
            if (this.productId != null && this.productId.equals(productId)) {
                quantity = newQuantity;
                txtQuantity.setText(String.valueOf(quantity));
            }
        });
    }

    @Override
    public void onCartCleared() {
        runOnUiThread(() -> {
            updateCartBadge();
        });
    }

    // ================== Related Products (unchanged) ==================
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
                    "High-quality cat food with premium ingredients", "food", "cat");
        });

        findViewById(R.id.similarProduct2).setOnClickListener(v -> {
            openProductDetail("Tuna Pate for Cats 160g", 45000, R.drawable.pate,
                    "Delicious tuna pate for cats", "food", "cat");
        });

        findViewById(R.id.similarProduct3).setOnClickListener(v -> {
            openProductDetail("Organic Cat Treats 200g", 85000, R.drawable.ic_launcher_background,
                    "Healthy organic treats for cats", "food", "cat");
        });

        findViewById(R.id.similarProduct4).setOnClickListener(v -> {
            openProductDetail("Pet Leash Strong Pink", 90000, R.drawable.daylung,
                    "Durable pet leash in pink color", "accessory", "dog");
        });

        findViewById(R.id.similarProduct5).setOnClickListener(v -> {
            openProductDetail("Double Pet Food Bowl Pink", 50000, R.drawable.bat,
                    "Double bowl for pet food and water", "accessory", "all");
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

    private void openProductDetail(String name, int price, int imageRes, String description, String type, String category) {
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra("service_name", name);
        intent.putExtra("service_price", price);
        intent.putExtra("service_img", imageRes);
        intent.putExtra("service_description", description);
        intent.putExtra("product_type", type);
        intent.putExtra("product_category", category);
        startActivity(intent);
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
        // Update cart badge when returning to this activity
        updateCartBadge();
    }
}

