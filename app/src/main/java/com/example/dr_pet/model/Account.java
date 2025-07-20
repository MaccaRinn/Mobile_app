package com.example.dr_pet.model;


public class Account {


    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private int accUrl;

    private String Role;


    public void setAccUrl(int accUrl) {
        this.accUrl = accUrl;
    }

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



    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public Account() {
    }

    public Account(String email, String firstName, String lastName, String address, String role, int accUrl, String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        Role = role;
        this.accUrl = accUrl;
        this.phoneNumber = phoneNumber;
    }
}
