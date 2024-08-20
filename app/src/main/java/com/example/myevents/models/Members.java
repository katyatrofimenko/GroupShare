package com.example.myevents.models;

import java.util.ArrayList;

public class Members extends ArrayList<Member> {

    private static Members members;

    private Members() {
    }

    public static Members getInstance() {
        if (members == null)
            members = new Members();

            return members;
    }


    public static String findNameByPhone(String phone)
    {
        for(int i=0; i<members.size();i++)
        {
            if(members.get(i).getPhone().equals(phone))
                return members.get(i).getName();
        }

        return "";
    }

    public static Member findMember(String phone)
    {
        for(int i=0; i<members.size();i++)
        {
            if(members.get(i).getPhone().equals(phone))
                return members.get(i);
        }

        return null;
    }

    public static Member getMember(String phone) {
        return findMember(phone);
    }
}
