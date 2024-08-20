package com.example.myevents;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.example.myevents.models.Actions;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Members;
import com.example.myevents.models.Note;
import com.example.myevents.models.Notes;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myevents.databinding.DialogFullNoteParticipantsBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class DialogFullNoteParticipants extends Dialog {


   private String noteId;
   private Context context;



    private androidx.gridlayout.widget.GridLayout gridLayout;
    public DialogFullNoteParticipants(@NonNull Context context, String noteId) {
        super(context);
        this.noteId = noteId;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_full_note_participants);


        initDialog();

    }

    void initDialog() {

        gridLayout = findViewById(R.id.gLDialog);

        Note note1 = Notes.getInstance().findNote(noteId);



        //first group

        String name;




        Belongs belongs = Belongs.findByGroup(note1.getGroupId());
        for (int i = 0; i < belongs.size(); i++) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            String phone = belongs.get(i).getPhone();
            String action = Actions.getAction(note1.getId(), phone);

            ImageView imageView = new ImageView(context);

            imageView.setImageResource(R.drawable.blank);

            if (action.equals("watch")) {
                imageView.setImageResource(R.drawable.watch);
            } else if (action.equals("done")) {
                imageView.setImageResource(R.drawable.check);

            } else if (action.equals("") && mAuth.getCurrentUser().getPhoneNumber().equals(phone)) {
                action(note1.getId(), "watch");

            }


            String memberName = Members.findNameByPhone(belongs.get(i).getPhone());
            Participant p = new Participant(context);

            name = "";
            if (memberName.length() > 0) {
                name += memberName.charAt(0);
            }
            if (memberName.length() > 1)
                name += memberName.charAt(1);

            p.setText(name);


            gridLayout.addView(p);

            if (phone.equals(mAuth.getCurrentUser().getPhoneNumber())) {
                CheckBox checkBox = new CheckBox(context);

                if (action.equals("done"))
                    checkBox.setChecked(true);
                else
                    checkBox.setChecked(false);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (checkBox.isChecked())
                            action(note1.getId(), "done");
                        else
                            action(note1.getId(), "watch");

                    }
                });
                gridLayout.addView(checkBox);


            } else
                gridLayout.addView(imageView);


        }
    }



    void action(String id, String action) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String phone = mAuth.getCurrentUser().getPhoneNumber();
        String customDocumentId = mAuth.getCurrentUser().getPhoneNumber();

        Map<String, Object> data = new HashMap<>();
        data.put("idNote", id);
        data.put("phone", phone);
        data.put("action", action);


        firestoreHelper.setDocument("actions", phone + id, data);


        Actions.change(id, phone, action);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }



}