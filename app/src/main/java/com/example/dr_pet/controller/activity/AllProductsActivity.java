package com.example.dr_pet.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dr_pet.R;
import com.example.dr_pet.Model.Product;
import com.example.dr_pet.controller.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private EditText etSearch;
    private ImageButton btnClearSearch, btnGridView, btnListView;
    private TextView txtTitle, txtSortBy, txtResultsCount;
    private LinearLayout layoutEmptyState, layoutSortOptions;

    // TẠM THỜI COMMENT OUT CATEGORY FILTER
    // private TextView txtCategoryFilter;
    // private LinearLayout layoutCategoryFilter;

    private List<Product> allProducts;
    private List<Product> filteredProducts;

    private String productType;
    private String currentCategory = "all";
    private String currentSortBy = "name";
    private boolean isGridView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        // Nhận dữ liệu từ Intent
        productType = getIntent().getStringExtra("product_type");
        String categoryFromIntent = getIntent().getStringExtra("current_category");
        if (categoryFromIntent != null) {
            currentCategory = categoryFromIntent;
        }

        initializeViews();
        initProducts();
        setupEventListeners();
        setupRecyclerView();
        updateTitle();
        // updateCategoryFilter(); // TẠM THỜI VÔ HIỆU HÓA
        filterProducts("");
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        btnGridView = findViewById(R.id.btnGridView);
        btnListView = findViewById(R.id.btnListView);
        txtTitle = findViewById(R.id.txtTitle);
        txtSortBy = findViewById(R.id.txtSortBy);
        txtResultsCount = findViewById(R.id.txtResultsCount);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        layoutSortOptions = findViewById(R.id.layoutSortOptions);

        // TẠM THỜI COMMENT OUT CATEGORY FILTER
        // txtCategoryFilter = findViewById(R.id.txtCategoryFilter);
        // layoutCategoryFilter = findViewById(R.id.layoutCategoryFilter);

        // Setup RecyclerView with grid layout by default
        setupRecyclerView();
    }

    private void setupEventListeners() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Cart button
        findViewById(R.id.btnCart).setOnClickListener(v -> {
            Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show();
            // Navigate to cart
            // Intent intent = new Intent(this, CartActivity.class);
            // startActivity(intent);
        });

        // Search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());

                // Show/hide clear button
                if (s.length() > 0) {
                    btnClearSearch.setVisibility(View.VISIBLE);
                } else {
                    btnClearSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear search
        btnClearSearch.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.clearFocus();
        });

        // TẠM THỜI VÔ HIỆU HÓA CATEGORY FILTER
        // layoutCategoryFilter.setOnClickListener(v -> showCategoryFilterDialog());

        // Sort options
        layoutSortOptions.setOnClickListener(v -> showSortOptionsDialog());

        // View toggle
        btnGridView.setOnClickListener(v -> {
            if (!isGridView) {
                isGridView = true;
                updateViewToggle();
                setupRecyclerView();
            }
        });

        btnListView.setOnClickListener(v -> {
            if (isGridView) {
                isGridView = false;
                updateViewToggle();
                setupRecyclerView();
            }
        });
    }

    private void setupRecyclerView() {
        if (isGridView) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        adapter = new ProductAdapter(filteredProducts, this::openProductDetail);
        recyclerView.setAdapter(adapter);
    }

    private void updateTitle() {
        if ("food".equals(productType)) {
            txtTitle.setText("Pet Food");
        } else if ("accessory".equals(productType)) {
            txtTitle.setText("Pet Accessories");
        } else {
            txtTitle.setText("All Products");
        }
    }

    private void initProducts() {
        allProducts = new ArrayList<>();

        // Copy logic khởi tạo sản phẩm từ code gốc
        // =============  THỨC ĂN CHO MÈO  =============
        allProducts.add(new Product(
                "Cat Food Catsrang 5kg",
                460000,
                R.drawable.hatmeo,
                "cat",
                "food",
                "High-quality dry food for cats"
        ));

        allProducts.add(new Product(
                "Tuna Pate for Cats Nutri Plan 160g",
                45000,
                R.drawable.pate,
                "cat",
                "food",
                "Nutritious tuna pate for cats"
        ));

        allProducts.add(new Product(
                "Royal Canin Kitten Food 400g",
                185000,
                R.drawable.royal_canin_kitten,
                "cat",
                "food",
                "Food for kittens under 12 months"
        ));

        allProducts.add(new Product(
                "Whiskas Temptations Cat Snacks 85g",
                35000,
                R.drawable.whiskas_snack,
                "cat",
                "food",
                "Salmon flavor treats for cats"
        ));

        // =============  THỨC ĂN CHO CHÓ  =============
        allProducts.add(new Product(
                "Pedigree Adult Dog Food 10kg",
                580000,
                R.drawable.pedigree_adult,
                "dog",
                "food",
                "Complete food for adult dogs"
        ));

        allProducts.add(new Product(
                "Cesar Classic Dog Pate 100g",
                55000,
                R.drawable.cesar_pate,
                "dog",
                "food",
                "Beef and vegetables pate for dogs"
        ));

        allProducts.add(new Product(
                "Royal Canin Medium Adult Dog 4kg",
                720000,
                R.drawable.royal_canin_dog,
                "dog",
                "food",
                "Food for medium breed dogs"
        ));

        allProducts.add(new Product(
                "Pedigree DentaStix Dog Treats 110g",
                45000,
                R.drawable.dentastix,
                "dog",
                "food",
                "Dental care treats for dogs"
        ));

        // =============  PHỤ KIỆN CHO MÈO  =============
        allProducts.add(new Product(
                "Pink Cat Collar with Bell",
                25000,
                R.drawable.cat_collar,
                "cat",
                "accessory",
                "Safety collar for cats with bell"
        ));

        allProducts.add(new Product(
                "Gray Fabric Cat House",
                280000,
                R.drawable.cat_house1,
                "cat",
                "accessory",
                "Cozy sleeping house for cats"
        ));

        allProducts.add(new Product(
                "Cactus-shaped Cat Scratching Post",
                350000,
                R.drawable.cat_scratch,
                "cat",
                "accessory",
                "Beautiful scratching post for cats"
        ));

        allProducts.add(new Product(
                "Covered Cat Litter Box",
                180000,
                R.drawable.cat_litter_box,
                "cat",
                "accessory",
                "Private litter box for cats"
        ));

        // =============  PHỤ KIỆN CHO CHÓ  =============
        allProducts.add(new Product(
                "Strong Pink Pet Leash",
                90000,
                R.drawable.daylung,
                "dog",
                "accessory",
                "Durable leash for dogs"
        ));

        allProducts.add(new Product(
                "Blue Hoodie for Small Dogs",
                120000,
                R.drawable.dog_hoodie,
                "dog",
                "accessory",
                "Fashionable warm hoodie for dogs"
        ));

        allProducts.add(new Product(
                "Rubber Ball Toy for Dogs",
                35000,
                R.drawable.dog_ball,
                "dog",
                "accessory",
                "Safe rubber ball for dogs"
        ));

        allProducts.add(new Product(
                "Dog Bed Size M",
                450000,
                R.drawable.dog_bed,
                "dog",
                "accessory",
                "Comfortable bed for medium dogs"
        ));

        // =============  PHỤ KIỆN CHUNG  =============
        allProducts.add(new Product(
                "Double Pet Food Bowl Pink",
                50000,
                R.drawable.bat,
                "all",
                "accessory",
                "Convenient double bowl for pets"
        ));

        allProducts.add(new Product(
                "Pet Grooming Brush",
                75000,
                R.drawable.pet_brush,
                "all",
                "accessory",
                "Soft grooming brush for pets"
        ));

        allProducts.add(new Product(
                "Food Storage Bag with Zip Lock",
                30000,
                R.drawable.food_container,
                "all",
                "accessory",
                "Keep pet food fresh and tasty"
        ));

        filteredProducts = new ArrayList<>();
    }

    private void filterProducts(String query) {
        filteredProducts.clear();

        for (Product product : allProducts) {
            // Lọc theo loại sản phẩm (food/accessory) nếu có
            boolean matchesType = productType == null || product.getType().equals(productType);

            // Lọc theo search query
            boolean matchesSearch = query.isEmpty() ||
                    product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(query.toLowerCase());

            // VẪN GIỮ LỌCTHEO CATEGORY (dog/cat) - chỉ ẩn dialog category filter
            boolean matchesCategory = currentCategory.equals("all") ||
                    product.getCategory().equals(currentCategory) ||
                    product.getCategory().equals("all");

            if (matchesType && matchesSearch && matchesCategory) {
                filteredProducts.add(product);
            }
        }

        sortProducts();
        adapter.notifyDataSetChanged();
        updateResultsCount();
        updateEmptyState();
    }

    private void sortProducts() {
        switch (currentSortBy) {
            case "name":
                Collections.sort(filteredProducts, (p1, p2) ->
                        p1.getName().compareToIgnoreCase(p2.getName()));
                break;
            case "price_low":
                Collections.sort(filteredProducts, (p1, p2) ->
                        Integer.compare(p1.getPrice(), p2.getPrice()));
                break;
            case "price_high":
                Collections.sort(filteredProducts, (p1, p2) ->
                        Integer.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "rating":
                // Có thể thêm rating vào Product model sau
                Collections.sort(filteredProducts, (p1, p2) ->
                        p1.getName().compareToIgnoreCase(p2.getName()));
                break;
        }
    }

    // TẠM THỜI VÔ HIỆU HÓA CATEGORY FILTER DIALOG - chỉ ẩn phần chọn categories
    // VẪN GIỮ LỌCTHEO currentCategory cho dog/cat từ Intent
    /*
    private void showCategoryFilterDialog() {
        String[] categories = {"All Categories", "Pet Food", "Pet Accessories", "Pet Toys", "Pet Care"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Filter by Category");
        builder.setItems(categories, (dialog, which) -> {
            // TẠM THỜI COMMENT OUT VIỆC THAY ĐỔI CATEGORY
            // switch (which) {
            //     case 0: currentCategory = "all"; break;
            //     case 1: currentCategory = "food"; break;
            //     case 2: currentCategory = "accessory"; break;
            //     case 3: currentCategory = "toy"; break;
            //     case 4: currentCategory = "care"; break;
            // }
            // updateCategoryFilter();
            // filterProducts(etSearch.getText().toString());
        });
        builder.show();
    }
    */

    private void showSortOptionsDialog() {
        String[] sortOptions = {"Name (A-Z)", "Price (Low to High)", "Price (High to Low)", "Rating (High to Low)"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Sort by");
        builder.setItems(sortOptions, (dialog, which) -> {
            switch (which) {
                case 0: currentSortBy = "name"; txtSortBy.setText("Sort by Name"); break;
                case 1: currentSortBy = "price_low"; txtSortBy.setText("Price: Low to High"); break;
                case 2: currentSortBy = "price_high"; txtSortBy.setText("Price: High to Low"); break;
                case 3: currentSortBy = "rating"; txtSortBy.setText("Sort by Rating"); break;
            }
            filterProducts(etSearch.getText().toString());
        });
        builder.show();
    }

    // TẠM THỜI VÔ HIỆU HÓA CATEGORY FILTER UPDATE
    /*
    private void updateCategoryFilter() {
        switch (currentCategory) {
            case "all": txtCategoryFilter.setText("All Categories"); break;
            case "food": txtCategoryFilter.setText("Pet Food"); break;
            case "accessory": txtCategoryFilter.setText("Pet Accessories"); break;
            case "toy": txtCategoryFilter.setText("Pet Toys"); break;
            case "care": txtCategoryFilter.setText("Pet Care"); break;
        }
    }
    */

    private void updateViewToggle() {
        if (isGridView) {
            btnGridView.setBackgroundResource(R.drawable.toggle_selected);
            btnListView.setBackgroundResource(R.drawable.toggle_unselected);
        } else {
            btnGridView.setBackgroundResource(R.drawable.toggle_unselected);
            btnListView.setBackgroundResource(R.drawable.toggle_selected);
        }
    }

    private void updateResultsCount() {
        int count = filteredProducts.size();

        if (count == 0) {
            txtResultsCount.setText("No products found");
        } else if (count == 1) {
            txtResultsCount.setText("Showing 1 product");
        } else {
            txtResultsCount.setText(String.format("Showing %d products", count));
        }
    }

    private void updateEmptyState() {
        boolean isEmpty = filteredProducts.isEmpty();

        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ShopProductDetailActivity.class);
        intent.putExtra("service_name", product.getName());
        intent.putExtra("service_price", product.getPrice());
        intent.putExtra("service_img", product.getImageRes());
        intent.putExtra("service_description", product.getDescription());
        intent.putExtra("product_type", product.getType());
        startActivity(intent);
    }
}