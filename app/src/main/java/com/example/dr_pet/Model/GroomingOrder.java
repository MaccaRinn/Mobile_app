package com.example.dr_pet.Model;

public class GroomingOrder {
    private String name;
    private String date;
    private String note;
    private String pet;
    private String price;
    private String id;

    public GroomingOrder() {
        // Required for Firebase
    }

    public GroomingOrder(String name, String date, String note, String pet, String price, String id) {
        this.name = name;
        this.date = date;
        this.note = note;
        this.pet = pet;
        this.price = price;
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPet() { return pet; }
    public void setPet(String pet) { this.pet = pet; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
