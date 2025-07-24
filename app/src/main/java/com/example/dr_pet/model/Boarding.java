package com.example.dr_pet.model;

public class Boarding {
    private String date;
    private String id;
    private String name;
    private String note;
    private String price;

    private String phone;


    public Boarding() {
    }

    public Boarding(String date, String phone, String price, String name, String note, String id) {
        this.date = date;
        this.phone = phone;
        this.price = price;
        this.name = name;
        this.note = note;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
