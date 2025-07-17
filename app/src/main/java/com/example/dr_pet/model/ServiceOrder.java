package com.example.dr_pet.model;

public class ServiceOrder {
    public String serviceOrderId;
    public String name;
    public String date;
    public String note;
    public String petId;
    public String price;
    public ServiceOrder() {}
    public ServiceOrder(String serviceOrderId, String name, String date, String note, String pet, String price) {
        this.serviceOrderId = serviceOrderId;
        this.name = name;
        this.date = date;
        this.note = note;
        this.petId = pet;
        this.price = price;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPetId() { return petId; }
    public void setPetId(String petId) { this.petId = petId; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getId() { return serviceOrderId; }
    public void setId(String id) { this.serviceOrderId = id; }
}
