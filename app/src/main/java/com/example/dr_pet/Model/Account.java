package com.example.dr_pet.Model;


import com.google.firebase.auth.ActionCodeResult;


public class Account {

    private String userName;
    private String firstName;
    private String lastName;
    private String address;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Account() {
    }

    public Account(String accountId, String userName, String firstName, String address, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.address = address;
        this.lastName = lastName;
    }


}
