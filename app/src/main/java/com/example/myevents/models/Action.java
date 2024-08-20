package com.example.myevents.models;

public class Action {

    private String idNote;
    private String phone;
    private String action; // watch / done


    public Action() {

    }

    public Action(String idNote, String phone, String action) {
        this.idNote = idNote;
        this.phone = phone;
        this.action = action;
    }

    public String getIdNote() {
        return idNote;
    }

    public void setIdNote(String idNote) {
        this.idNote = idNote;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
