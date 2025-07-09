package com.example.dr_pet.Model;

import java.time.LocalDate;

public class Pet {
    private  String name;
    private  String species;
    private  float  weight;
    private  int petUrl;
    private  String gender;
    private LocalDate brithDate;
    private String note;

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
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

    public LocalDate getBrithDate() {
        return brithDate;
    }

    public void setBrithDate() {
        this.brithDate = brithDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Pet() {
    }

    public Pet(String name, String species, int petUrl, float weight, String gender, LocalDate brithDate, String note) {
        this.name = name;
        this.species = species;
        this.petUrl = petUrl;
        this.weight = weight;
        this.gender = gender;
        this.brithDate = brithDate;
        this.note = note;
    }
}
