package com.example.dr_pet.model;

import java.io.Serializable;

public class Pet implements Serializable{

    private  String petId;

    private  String name;
    private  String species;
    private  double weight;
    private  int petUrl;
    private  String gender;
    private String brithDate;
    private String note;

    private double height;

    public Pet(String name, String species, double weight, double height, int petUrl, String gender, String brithDate, String note) {
        this.name = name;
        this.species = species;
        this.weight = weight;
        this.petUrl = petUrl;
        this.gender = gender;
        this.brithDate = brithDate;
        this.note = note;
        this.height = height;
    }

    public Pet() {
    }

    public Pet(String name, String species, float weight, int petUrl, String gender, String brithDate, String note) {
        this.name = name;
        this.species = species;
        this.weight = weight;
        this.petUrl = petUrl;
        this.gender = gender;
        this.brithDate = brithDate;
        this.note = note;

    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getPetUrl() {
        return petUrl;
    }

    public void setPetUrl(int petUrl) {
        this.petUrl = petUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBrithDate() {
        return brithDate;
    }

    public void setBrithDate(String brithDate) {
        this.brithDate = brithDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
