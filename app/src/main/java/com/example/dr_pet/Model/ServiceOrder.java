package com.example.dr_pet.Model;

public class ServiceOrder {
    public String serviceOrderId;
    public String name;
    public String date;
    public String note;
    public String pet;
    public String price;
    public ServiceOrder() {}
    public ServiceOrder(String serviceOrderId, String name, String date, String note, String pet, String price) {
        this.serviceOrderId = serviceOrderId;
        this.name = name;
        this.date = date;
        this.note = note;
        this.pet = pet;
        this.price = price;
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

    public String getId() { return serviceOrderId; }
    public void setId(String id) { this.serviceOrderId = id; }
}
