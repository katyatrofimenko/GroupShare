package com.example.myevents.models;

public class Belong {

    private String id;
    private String phone;


    public Belong() {

    }
    public Belong(String id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
