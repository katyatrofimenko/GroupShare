package com.example.myevents.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myevents.FirebaseHelper;
import com.example.myevents.MainActivity;
import com.example.myevents.R;
import com.example.myevents.models.Actions;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Groups;
import com.example.myevents.models.Members;
import com.example.myevents.models.Notes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;


public class SettingFragment extends Fragment {

    private Context context;
    private View view;

    public SettingFragment(Activity context) {
        this.context = context;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        this.view = view;
        initButton();

        initButton();
        return view;
    }


    void initButton() {
        Button btnUpdateName = view.findViewById(R.id.btnUpdateName);
        btnUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText eTNameEdit = view.findViewById(R.id.eTNameEdit);
                String name = eTNameEdit.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(context, "Please insert name", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateName(name);

            }
        });


        Button btnLogout = view.findViewById(R.id.btnLogOutSetting);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Members.getInstance().clear();
                Notes.getInstance().clear();
                Belongs.getInstance().clear();
                Groups.getInstance().clear();
                Actions.getInstance().clear();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                context.startActivity(new Intent(context, MainActivity.class));

            }
        });


    }


    void updateName(String name) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String phone = mAuth.getCurrentUser().getPhoneNumber();
        String customDocumentId = mAuth.getCurrentUser().getPhoneNumber();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("phone", customDocumentId);


        firestoreHelper.setDocument("members", phone, data);

    }
}