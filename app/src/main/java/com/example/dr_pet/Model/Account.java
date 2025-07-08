package com.example.dr_pet.Model;


import com.google.firebase.auth.ActionCodeResult;


public class Account {

    private String firstName;
    private String lastName;
    private String address;


    public Account(String email, String userName, String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Account() {
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
}
