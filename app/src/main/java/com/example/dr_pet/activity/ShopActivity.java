package com.example.dr_pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.KeyEvent;
import android.widget.Toast;
import android.view.inputmethod.EditorInfo;

import com.example.dr_pet.R;
import com.example.dr_pet.manager.CartManager;
import com.example.dr_pet.model.Cart;
import com.example.dr_pet.model.CartItem;
import com.example.dr_pet.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity implements CartManager.CartUpdateListener {

    private LinearLayout btnCategoryAll, btnCategoryDog, btnCategoryCat;
    private LinearLayout foodProduct1, foodProduct2;
    private LinearLayout accessoryProduct1, accessoryProduct2;
    private ImageButton btnBack, btnCart, btnSearch;
    private EditText etSearch;
    private TextView btnSeeAllFood, btnSeeAllAccessories;
    private TextView txtCartBadge; // Badge for cart

    // Product ImageViews và TextViews
    private ImageView imgFoodProduct1, imgFoodProduct2;
    private ImageView imgAccessoryProduct1, imgAccessoryProduct2;
    private TextView txtFoodProduct1Name, txtFoodProduct1Price;
    private TextView txtFoodProduct2Name, txtFoodProduct2Price;
    private TextView txtAccessoryProduct1Name, txtAccessoryProduct1Price;
    private TextView txtAccessoryProduct2Name, txtAccessoryProduct2Price;

    // Danh sách sản phẩm
    private List<Product> allProducts;
    private List<Product> filteredProducts;
    private String currentCategory = "all";

    // CartManager
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize CartManager
        cartManager = CartManager.getInstance();
        cartManager.addCartUpdateListener(this);

        initViews();
        initProducts();
        setupClickListeners();
        updateProductDisplay();

        // Update cart badge on start
        updateCartBadge();
    }

    private void initViews() {
        // Header buttons
        btnBack = findViewById(R.id.btnBack);
        btnCart = findViewById(R.id.btnCart);
        btnSearch = findViewById(R.id.btnSearch);

        // Cart badge (thêm vào layout nếu chưa có)
        txtCartBadge = findViewById(R.id.txtCartBadge); // Có thể null nếu chưa add vào layout

        // Search
        etSearch = findViewById(R.id.etSearch);

        // Category buttons
        btnCategoryAll = findViewById(R.id.btnCategoryAll);
        btnCategoryDog = findViewById(R.id.btnCategoryDog);
        btnCategoryCat = findViewById(R.id.btnCategoryCat);

        // Product items
        foodProduct1 = findViewById(R.id.foodProduct1);
        foodProduct2 = findViewById(R.id.foodProduct2);
        accessoryProduct1 = findViewById(R.id.accessoryProduct1);
        accessoryProduct2 = findViewById(R.id.accessoryProduct2);

        // Product ImageViews
        imgFoodProduct1 = findViewById(R.id.imgFoodProduct1);
        imgFoodProduct2 = findViewById(R.id.imgFoodProduct2);
        imgAccessoryProduct1 = findViewById(R.id.imgAccessoryProduct1);
        imgAccessoryProduct2 = findViewById(R.id.imgAccessoryProduct2);

        // Product TextViews
        txtFoodProduct1Name = findViewById(R.id.txtFoodProduct1Name);
        txtFoodProduct1Price = findViewById(R.id.txtFoodProduct1Price);
        txtFoodProduct2Name = findViewById(R.id.txtFoodProduct2Name);
        txtFoodProduct2Price = findViewById(R.id.txtFoodProduct2Price);
        txtAccessoryProduct1Name = findViewById(R.id.txtAccessoryProduct1Name);
        txtAccessoryProduct1Price = findViewById(R.id.txtAccessoryProduct1Price);
        txtAccessoryProduct2Name = findViewById(R.id.txtAccessoryProduct2Name);
        txtAccessoryProduct2Price = findViewById(R.id.txtAccessoryProduct2Price);

        // See all buttons
        btnSeeAllFood = findViewById(R.id.btnSeeAllFood);
        btnSeeAllAccessories = findViewById(R.id.btnSeeAllAccessories);
    }

    private void initProducts() {
        allProducts = new ArrayList<>();

        // =============  THỨC ĂN CHO MÈO  =============
        allProducts.add(new Product(
                "Hạt mèo Catsrang túi 5kg",
                460000,
                R.drawable.hatmeo,
                "cat",
                "food",
                "Thức ăn khô cho mèo chất lượng cao"
        ));

        allProducts.add(new Product(
                "Pate cho mèo Nutri Plan Cá ngừ 160g",
                45000,
                R.drawable.pate,
                "cat",
                "food",
                "Pate dinh dưỡng cho mèo vị cá ngừ"
        ));

        allProducts.add(new Product(
                "Thức ăn mèo Royal Canin Kitten 400g",
                185000,
                R.drawable.royal_canin_kitten,
                "cat",
                "food",
                "Thức ăn cho mèo con dưới 12 tháng"
        ));

        allProducts.add(new Product(
                "Snack mèo Whiskas Temptations 85g",
                35000,
                R.drawable.whiskas_snack,
                "cat",
                "food",
                "Bánh thưởng cho mèo vị cá hồi"
        ));

        // =============  THỨC ĂN CHO CHÓ  =============
        allProducts.add(new Product(
                "Hạt chó Pedigree Adult 10kg",
                580000,
                R.drawable.pedigree_adult,
                "dog",
                "food",
                "Thức ăn cho chó trưởng thành"
        ));

        allProducts.add(new Product(
                "Pate chó Cesar Classic 100g",
                55000,
                R.drawable.cesar_pate,
                "dog",
                "food",
                "Pate chó vị thịt bò và rau củ"
        ));

        allProducts.add(new Product(
                "Thức ăn chó Royal Canin Medium Adult 4kg",
                720000,
                R.drawable.royal_canin_dog,
                "dog",
                "food",
                "Thức ăn cho chó giống trung bình"
        ));

        allProducts.add(new Product(
                "Snack chó Pedigree DentaStix 110g",
                45000,
                R.drawable.dentastix,
                "dog",
                "food",
                "Bánh thưởng làm sạch răng cho chó"
        ));

        // =============  PHỤ KIỆN CHO MÈO  =============
        allProducts.add(new Product(
                "Vòng cổ mèo có chuông màu hồng",
                25000,
                R.drawable.cat_collar,
                "cat",
                "accessory",
                "Vòng cổ an toàn cho mèo với chuông"
        ));

        allProducts.add(new Product(
                "Nhà mèo bằng vải màu xám",
                280000,
                R.drawable.cat_house1,
                "cat",
                "accessory",
                "Nhà ngủ ấm áp cho mèo"
        ));

        allProducts.add(new Product(
                "Cào móng mèo hình cây xương rồng",
                350000,
                R.drawable.cat_scratch,
                "cat",
                "accessory",
                "Cây cào móng đẹp mắt cho mèo"
        ));

        allProducts.add(new Product(
                "Khay vệ sinh mèo có nắp đậy",
                180000,
                R.drawable.cat_litter_box,
                "cat",
                "accessory",
                "Khay vệ sinh kín đáo cho mèo"
        ));

        // =============  PHỤ KIỆN CHO CHÓ  =============
        allProducts.add(new Product(
                "Dây dắt thú cưng cường lực màu hồng",
                90000,
                R.drawable.daylung,
                "dog",
                "accessory",
                "Dây dắt chắc chắn cho chó"
        ));

        allProducts.add(new Product(
                "Áo hoodie cho chó nhỏ màu xanh",
                120000,
                R.drawable.dog_hoodie,
                "dog",
                "accessory",
                "Áo ấm thời trang cho chó"
        ));

        allProducts.add(new Product(
                "Đồ chơi bóng cao su cho chó",
                35000,
                R.drawable.dog_ball,
                "dog",
                "accessory",
                "Bóng cao su an toàn cho chó"
        ));

        allProducts.add(new Product(
                "Giường nằm cho chó size M",
                450000,
                R.drawable.dog_bed,
                "dog",
                "accessory",
                "Giường êm ái cho chó trung bình"
        ));

        // =============  PHỤ KIỆN CHUNG  =============
        allProducts.add(new Product(
                "Bát ăn/uống thú cưng kép màu hồng",
                50000,
                R.drawable.bat,
                "all",
                "accessory",
                "Bát ăn đôi tiện dụng cho thú cưng"
        ));

        allProducts.add(new Product(
                "Lược chải lông thú cưng",
                75000,
                R.drawable.pet_brush,
                "all",
                "accessory",
                "Lược chải lông mềm mại"
        ));

        allProducts.add(new Product(
                "Túi đựng thức ăn có khóa zip",
                30000,
                R.drawable.food_container,
                "all",
                "accessory",
                "Túi bảo quản thức ăn tươi ngon"
        ));

        filteredProducts = new ArrayList<>(allProducts);
    }


    private void setupClickListeners() {
        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Cart button - Navigate to CartActivity
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });

        // Search button
        btnSearch.setOnClickListener(v -> {
            String searchQuery = etSearch.getText().toString();
            performSearch(searchQuery);
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String searchQuery = etSearch.getText().toString();
                performSearch(searchQuery);
                return true;
            }
            return false;
        });

        // Category buttons
        btnCategoryAll.setOnClickListener(v -> selectCategory("all"));
        btnCategoryDog.setOnClickListener(v -> selectCategory("dog"));
        btnCategoryCat.setOnClickListener(v -> selectCategory("cat"));

        // See all buttons
        btnSeeAllFood.setOnClickListener(v -> {
            openAllProductsActivity("food");
        });

        btnSeeAllAccessories.setOnClickListener(v -> {
            openAllProductsActivity("accessory");
        });
    }

    private void selectCategory(String category) {
        currentCategory = category;

        // Reset all category backgrounds
        btnCategoryAll.setBackgroundResource(R.drawable.category_background);
        btnCategoryDog.setBackgroundResource(R.drawable.category_background);
        btnCategoryCat.setBackgroundResource(R.drawable.category_background);

        // Set selected category background
        switch (category) {
            case "all":
                btnCategoryAll.setBackgroundResource(R.drawable.category_selected_background);
                break;
            case "dog":
                btnCategoryDog.setBackgroundResource(R.drawable.category_selected_background);
                break;
            case "cat":
                btnCategoryCat.setBackgroundResource(R.drawable.category_selected_background);
                break;
        }

        // Filter products
        filterProducts();
    }

    private void performSearch(String searchQuery) {
        filteredProducts.clear();

        for (Product product : allProducts) {
            if (product.matchesCategory(currentCategory) &&
                    product.matchesSearch(searchQuery)) {
                filteredProducts.add(product);
            }
        }

        if (filteredProducts.isEmpty() && !searchQuery.trim().isEmpty()) {
            Toast.makeText(this, "Không tìm thấy sản phẩm phù hợp với từ khóa: \"" + searchQuery + "\"",
                    Toast.LENGTH_SHORT).show();
        } else if (!searchQuery.trim().isEmpty()) {
            Toast.makeText(this, "Tìm thấy " + filteredProducts.size() + " sản phẩm",
                    Toast.LENGTH_SHORT).show();
        }

        updateProductDisplay();
    }

    private void filterProducts() {
        filteredProducts.clear();
        String searchQuery = etSearch.getText().toString();

        for (Product product : allProducts) {
            if (product.matchesCategory(currentCategory) &&
                    product.matchesSearch(searchQuery)) {
                filteredProducts.add(product);
            }
        }

        updateProductDisplay();
    }

    private void updateProductDisplay() {
        foodProduct1.setVisibility(View.GONE);
        foodProduct2.setVisibility(View.GONE);
        accessoryProduct1.setVisibility(View.GONE);
        accessoryProduct2.setVisibility(View.GONE);

        List<Product> foodProducts = new ArrayList<>();
        List<Product> accessoryProducts = new ArrayList<>();

        for (Product product : filteredProducts) {
            if ("food".equals(product.getType())) {
                foodProducts.add(product);
            } else if ("accessory".equals(product.getType())) {
                accessoryProducts.add(product);
            }
        }

        // Hiển thị tối đa 2 sản phẩm food
        if (foodProducts.size() > 0) {
            foodProduct1.setVisibility(View.VISIBLE);
            updateProductContent(foodProducts.get(0), imgFoodProduct1, txtFoodProduct1Name, txtFoodProduct1Price);
        }
        if (foodProducts.size() > 1) {
            foodProduct2.setVisibility(View.VISIBLE);
            updateProductContent(foodProducts.get(1), imgFoodProduct2, txtFoodProduct2Name, txtFoodProduct2Price);
        }

        if (accessoryProducts.size() > 0) {
            accessoryProduct1.setVisibility(View.VISIBLE);
            updateProductContent(accessoryProducts.get(0), imgAccessoryProduct1, txtAccessoryProduct1Name, txtAccessoryProduct1Price);
        }
        if (accessoryProducts.size() > 1) {
            accessoryProduct2.setVisibility(View.VISIBLE);
            updateProductContent(accessoryProducts.get(1), imgAccessoryProduct2, txtAccessoryProduct2Name, txtAccessoryProduct2Price);
        }

        int totalProducts = filteredProducts.size();
        int displayedProducts = Math.min(foodProducts.size(), 2) + Math.min(accessoryProducts.size(), 2);

        if (totalProducts > displayedProducts && totalProducts > 0) {
//            Toast.makeText(this, "Tìm thấy " + totalProducts + " sản phẩm. Hiển thị " +
//                    displayedProducts + " sản phẩm đầu tiên.", Toast.LENGTH_SHORT).show();
        }


        updateProductClickListeners(foodProducts, accessoryProducts);
    }

    private void updateProductContent(Product product, ImageView imageView, TextView nameView, TextView priceView) {
        imageView.setImageResource(product.getImageRes());
        nameView.setText(product.getName());
        priceView.setText(String.format("%,dđ", product.getPrice()));

        // Add visual indicator if product is in cart
        if (cartManager.isProductInCart(CartManager.generateProductId(product.getName(), product.getPrice()))) {
            // You can add a small icon or change text color to indicate product is in cart
            nameView.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        } else {
            nameView.setTextColor(getResources().getColor(android.R.color.black));
        }
    }


    private void updateProductClickListeners(List<Product> foodProducts, List<Product> accessoryProducts) {
        // Food products click listeners
        if (foodProducts.size() > 0) {
            final Product food1 = foodProducts.get(0);
            foodProduct1.setOnClickListener(v -> openProductOrder(food1));
        }
        if (foodProducts.size() > 1) {
            final Product food2 = foodProducts.get(1);
            foodProduct2.setOnClickListener(v -> openProductOrder(food2));
        }

        // Accessory products click listeners
        if (accessoryProducts.size() > 0) {
            final Product accessory1 = accessoryProducts.get(0);
            accessoryProduct1.setOnClickListener(v -> openProductOrder(accessory1));
        }
        if (accessoryProducts.size() > 1) {
            final Product accessory2 = accessoryProducts.get(1);
            accessoryProduct2.setOnClickListener(v -> openProductOrder(accessory2));
        }
    }

    private void openProductOrder(Product product) {
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra("service_name", product.getName());
        intent.putExtra("service_price", product.getPrice());
        intent.putExtra("service_img", product.getImageRes());
        intent.putExtra("service_description", product.getDescription());
        intent.putExtra("product_type", product.getType());
        intent.putExtra("product_category", product.getCategory());
        startActivity(intent);
    }

    private void openAllProductsActivity(String productType) {
        Intent intent = new Intent(this, AllProductsActivity.class);
        intent.putExtra("product_type", productType);
        intent.putExtra("current_category", currentCategory);
        startActivity(intent);
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

    @Override
    public void onCartUpdated(Cart cart) {
        runOnUiThread(() -> {
            updateCartBadge();
            updateProductDisplay(); // Refresh product colors if in cart
        });
    }

    @Override
    public void onCartItemAdded(CartItem item) {
        runOnUiThread(() -> {
            updateCartBadge();
            updateProductDisplay();
            Toast.makeText(this, "Đã thêm " + item.getProductName() + " vào giỏ hàng",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onCartItemRemoved(String productId) {
        runOnUiThread(() -> {
            updateCartBadge();
            updateProductDisplay();
        });
    }

    @Override
    public void onCartItemQuantityChanged(String productId, int newQuantity) {
        runOnUiThread(() -> {
            updateCartBadge();
        });
    }

    @Override
    public void onCartCleared() {
        runOnUiThread(() -> {
            updateCartBadge();
            updateProductDisplay();
            Toast.makeText(this, "Đã xóa toàn bộ giỏ hàng", Toast.LENGTH_SHORT).show();
        });
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
        updateProductDisplay();
    }
}



