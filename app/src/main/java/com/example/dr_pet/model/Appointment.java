package com.example.dr_pet.model;

public class Appointment {
    private String appointmentId;
    private String name;
    private String phone;
    private String date;
    private String service;
    private String petId;
    private String status;

    public Appointment() { }

    public Appointment(String name,
                       String phone,
                       String date,
                       String service,
                       String petId) {
        this.name          = name;
        this.phone         = phone;
        this.date          = date;
        this.service       = service;
        this.petId         = petId;
        this.status        = "pending";
    }

    // getters & setters
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public String getPetId() { return petId; }
    public void setPetId(String petId) { this.petId = petId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
