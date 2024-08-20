package com.example.myevents.models;

public class Member {


    private static String current ="";
    private String phone;
    private String name;


    public Member( ) {
    }

    public Member( String phone, String name) {
        this.phone = phone;
        this.name = name;
    }

    public static String getCurrent() {
        return current;
    }

    public static void setCurrent(String current) {
        Member.current = current;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
