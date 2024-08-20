package com.example.myevents.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Belongs extends ArrayList<Belong> {

    private static Belongs belongs;

    private Belongs() {
    }


    public static Belongs getInstance() {
        if (belongs == null) {
            // Synchronize to make it thread-safe
            synchronized (Belongs.class) {
                if (belongs == null) {
                    belongs = new Belongs();
                }
            }
        }
        return belongs;
    }


    //find all belongs by name group
    public static Belongs findByGroup(String id) {
        Belongs b = new Belongs();

        for (int i = 0; i < belongs.size(); i++) {
            if (belongs.get(i).getId().equals(id))
                b.add(belongs.get(i));
        }

        return b;
    }


    public static ArrayList<String> groupsIBelongTo(String phone)

    {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < belongs.size(); i++) {
            if (belongs.get(i).getPhone().equals(phone))
                set.add(belongs.get(i).getId());

        }
        ArrayList<String> list = new ArrayList<String>(set);
        return list;
    }

    //TODO is group name exist

}
