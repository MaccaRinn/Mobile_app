package com.example.dr_pet.Model;


import com.google.firebase.auth.ActionCodeResult;


public class Account {


    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private int accUrl;

    private String role;

    private String phoneNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAccUrl() {
        return accUrl;
    }

    public void setAccUrl(int accUrl) {
        this.accUrl = accUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Account() {
    }

    public Account(String email, String lastName, String firstName, String address, int accUrl, String role, String phoneNumber) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.accUrl = accUrl;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }
}
