package com.example.dr_pet.model;

public class Medical {
    private String appointmentId;
    private String date;

    private String name;

    private String phone;

    private String service;

    private String status;


    public Medical() {
    }

    public Medical(String appointmentId, String status, String service, String phone, String name, String date) {
        this.appointmentId = appointmentId;
        this.status = status;
        this.service = service;
        this.phone = phone;
        this.name = name;
        this.date = date;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
