package com.example.myevents.models;

public class Note {

    private String groupId;
    private String id;
    private String name;
    private String date;
    private boolean urgent;



    public Note() {
    }

    public Note(String groupId, String id, String name, String date, boolean urgent) {
        this.groupId = groupId;
        this.id = id;
        this.name = name;
        this.date = date;
        this.urgent = urgent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
