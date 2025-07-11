package com.example.dr_pet.Model;

public class GroomingService {
    private String name;
    private String desc;
    private int imageResId;
    private int price;

    public GroomingService(String name, String desc, int imageResId, int price) {
        this.name = name;
        this.desc = desc;
        this.imageResId = imageResId;
        this.price = price;
    }
    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getImageResId() { return imageResId; }
    public int getPrice() { return price; }
}
