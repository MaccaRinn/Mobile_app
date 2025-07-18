package com.example.dr_pet.model;

import java.io.Serializable;

public class Pet implements Serializable{

    private  String petId;

    private  String name;
    private  String species;
    private  float  weight;
    private  int petUrl;
    private  String gender;
    private String brithDate;
    private String note;


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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getPetUrl() {
        return petUrl;
    }

    public void setPetUrl(int petUrl) {
        this.petUrl = petUrl;
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

    public void setPetId(String petId){
        this.petId = petId;
    }


    public String getPetId() {
        return  petId;
    }
}
