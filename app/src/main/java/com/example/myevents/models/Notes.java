package com.example.myevents.models;

import java.util.ArrayList;

public class Notes extends ArrayList<Note> {

    private static Notes notes;

    private Notes() {
    }

    public static Notes getInstance() {
        if (notes == null)
            notes = new Notes();

        return notes;
    }

    public static boolean contains(String id)
    {
        for(Note note :notes){

            if(note.getId().equals(id))
                return true;
        }
        return false;
    }

    public static Notes findNotesByGroup(String groupId)
    {
        Notes notes1 = new Notes();

        for(Note note: notes)
        {
            if(note.getGroupId().equals(groupId))
                notes1.add(note);
        }

        return  notes1;
    }


    public static Note findNote(String noteId)
    {

        for(Note note: notes)
        {
            if(note.getId().equals(noteId))
                return note;
        }

        return  null;
    }




}
