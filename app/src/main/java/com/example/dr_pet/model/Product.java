//package com.example.dr_pet.Model;
//
//public class Product {
//    private String name;
//    private int price;
//    private int imageRes;
//    private String category;
//    private String type;
//    private String description;
//    private boolean isAvailable;
//
//    public Product(String name, int price, int imageRes, String category, String type, String description) {
//        this.name = name;
//        this.price = price;
//        this.imageRes = imageRes;
//        this.category = category;
//        this.type = type;
//        this.description = description;
//        this.isAvailable = true;
//    }
//
//    public Product(String name, int price, int imageRes, String category, String type) {
//        this(name, price, imageRes, category, type, "");
//    }
//
//    // Getters
//    public String getName() {
//        return name;
//    }
//
//    public int getPrice() {
//        return price;
//    }
//
//    public int getImageRes() {
//        return imageRes;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public boolean isAvailable() {
//        return isAvailable;
//    }
//
//    // Setters
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setPrice(int price) {
//        this.price = price;
//    }
//
//    public void setImageRes(int imageRes) {
//        this.imageRes = imageRes;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setAvailable(boolean available) {
//        isAvailable = available;
//    }
//
//
//    public String getFormattedPrice() {
//        return String.format("%,d VND", price);
//    }
//
//    public boolean matchesCategory(String filterCategory) {
//        return filterCategory.equals("all") || this.category.equals(filterCategory);
//    }
//
//    public boolean matchesSearch(String searchQuery) {
//        if (searchQuery == null || searchQuery.trim().isEmpty()) {
//            return true;
//        }
//        return name.toLowerCase().contains(searchQuery.toLowerCase()) ||
//                description.toLowerCase().contains(searchQuery.toLowerCase());
//    }
//
//    @Override
//    public String toString() {
//        return "Product{" +
//                "name='" + name + '\'' +
//                ", price=" + price +
//                ", category='" + category + '\'' +
//                ", type='" + type + '\'' +
//                '}';
//    }
//}

package com.example.dr_pet.model;

public class Product {
    private String name;
    private int price;
    private int imageRes;
    private String category; // "all", "dog", "cat"
    private String type; // "food", "accessory"
    private String description;

    public Product(String name, int price, int imageRes, String category, String type, String description) {
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.category = category;
        this.type = type;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public String getDescription() { return description; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setImageRes(int imageRes) { this.imageRes = imageRes; }
    public void setCategory(String category) { this.category = category; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }

    // Helper methods
    public boolean matchesCategory(String filterCategory) {
        if ("all".equals(filterCategory)) {
            return true;
        }
        return this.category.equals(filterCategory) || "all".equals(this.category);
    }

    public boolean matchesSearch(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return true;
        }

        String query = searchQuery.toLowerCase().trim();
        return this.name.toLowerCase().contains(query) ||
                this.description.toLowerCase().contains(query);
    }

    public boolean matchesType(String filterType) {
        return this.type.equals(filterType);
    }
}