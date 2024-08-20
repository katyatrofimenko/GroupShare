package com.example.myevents.models;

import java.util.ArrayList;

public class Actions extends ArrayList<Action> {

    private static Actions actions ;

    private Actions() {
    }

    public static Actions getInstance() {
        if (actions == null)
            actions = new Actions();

        return actions;
    }


    public static String getAction(String idNote, String phone )
    {

        for(Action action: actions)
        {
            if(action.getIdNote().equals(idNote) && action.getPhone().equals(phone))
                return action.getAction();
        }

        return "";
    }

    public static void change (String id, String phone,String act)
    {
        for(Action action: actions)
        {
            if(action.getPhone().equals(phone) && action.getIdNote().equals(id))
                action.setAction(act);
        }

    }


}
