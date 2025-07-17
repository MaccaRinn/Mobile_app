package com.example.dr_pet.model;

public class HotelOption {
    private String name;
    private String desc;
    private int price;
    private int imageResId;

    public HotelOption(String name, String desc, int price, int imageResId) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getPrice() { return price; }
    public int getImageResId() { return imageResId; }
}
