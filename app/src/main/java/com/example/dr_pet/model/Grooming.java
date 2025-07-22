package com.example.dr_pet.model;

public class Grooming {
    private String date;
    private String id;
    private String name;
    private String note;
    private String price;

    private String serviceOrderId;

    private String status;

    public Grooming(String date, String status, String serviceOrderId, String price, String note, String name, String id) {
        this.date = date;
        this.status = status;
        this.serviceOrderId = serviceOrderId;
        this.price = price;
        this.note = note;
        this.name = name;
        this.id = id;
    }

    public Grooming() {
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
