package com.example.dr_pet.Model;


import com.google.firebase.auth.ActionCodeResult;


public class Account {


    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String accUrl;

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

    public String getAccUrl() {
        return accUrl;
    }

    public void setAccUrl(String accUrl) {
        this.accUrl = accUrl;
    }

    public Account() {
    }

    public Account(String email, String lastName, String firstName, String address, String accUrl) {
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.accUrl = accUrl;
    }
}
