package com.example.dr_pet.model;

public class GroomingService {
    private String name;
    private String desc;
    private int imageResId;
    private int price;
    private String date;
    private String note;
    private String pet;
    private String id;

    public GroomingService(String name, String desc, int imageResId, int price) {
        this.name = name;
        this.desc = desc;
        this.imageResId = imageResId;
        this.price = price;
        this.date = date;
        this.note = note;
        this.pet = pet;
        this.id = id;
    }
    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getImageResId() { return imageResId; }
    public int getPrice() { return price; }
}
