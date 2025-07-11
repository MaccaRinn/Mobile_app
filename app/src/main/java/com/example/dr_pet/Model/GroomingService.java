package com.example.dr_pet.Model;

public class GroomingService {
    private String name;
    private String desc;
    private int imageResId;

    public GroomingService(String name, String desc, int imageResId) {
        this.name = name;
        this.desc = desc;
        this.imageResId = imageResId;
    }
    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getImageResId() { return imageResId; }
}
