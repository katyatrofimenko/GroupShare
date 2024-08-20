package com.example.myevents.models;

import java.util.ArrayList;

public class Groups extends ArrayList<Group> {

    private static Groups groups;

    private Groups() {
    }

    public static Groups getInstance() {
        if (groups == null)
            groups = new Groups();

        return groups;
    }


    public Group findGroupById(String id)
    {
        for(int i=0; i<groups.size();i++)
        {
            if(groups.get(i).getId().equals(id))
                return groups.get(i);
        }
        return null;
    }
}
