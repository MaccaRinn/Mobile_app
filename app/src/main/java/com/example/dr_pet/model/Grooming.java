package com.example.dr_pet.model;

public class Grooming {
    private String date;
    private String id;
    private String name;
    private String note;
    private String price;

    private String phone;

    private String serviceOrderId;

    private String status;

    public Grooming() {
    }

    public Grooming(String date, String id, String name, String note, String price, String phone, String status, String serviceOrderId) {
        this.date = date;
        this.id = id;
        this.name = name;
        this.note = note;
        this.price = price;
        this.phone = phone;
        this.status = status;
        this.serviceOrderId = serviceOrderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getServiceOrderId() {
        return serviceOrderId;
    }

    public void setServiceOrderId(String serviceOrderId) {
        this.serviceOrderId = serviceOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
