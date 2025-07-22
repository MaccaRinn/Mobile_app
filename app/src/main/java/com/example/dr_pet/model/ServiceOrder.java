package com.example.dr_pet.model;

public class ServiceOrder {
    public String serviceOrderId;
    public String name;
    public String date;
    public String hour; // giờ hẹn
    public String note;
    public String petId;
    public String price;
    public String status; // pending, missed, completed
    public ServiceOrder() {}
    public ServiceOrder(String serviceOrderId, String name, String date, String hour, String note, String pet, String price, String status) {
        this.serviceOrderId = serviceOrderId;
        this.name = name;
        this.date = date;
        this.hour = hour;
        this.note = note;
        this.petId = pet;
        this.price = price;
        this.status = status;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getHour() { return hour; }
    public void setHour(String hour) { this.hour = hour; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getPetId() { return petId; }
    public void setPetId(String petId) { this.petId = petId; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getId() { return serviceOrderId; }
    public void setId(String id) { this.serviceOrderId = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
