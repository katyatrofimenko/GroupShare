package com.example.myevents.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myevents.FirebaseHelper;
import com.example.myevents.R;
import com.example.myevents.models.Belong;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Group;
import com.example.myevents.models.Groups;
import com.example.myevents.models.Member;
import com.example.myevents.models.Members;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddGroupFragment extends Fragment {

    private Context context;
    private View view;

    private ArrayList<Member> members = new ArrayList<Member>();
    private int indexMember = 0;

    public AddGroupFragment(Context context) {
        this.context = context;

    }


    public interface OnAddingGroup {
        void onAddingGroup();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_group, container, false);

        this.view = view;

        //addMember();

        createGroup();
        initSearch();
        return view;
    }

    void initSearch() {


        EditText etPhoneNumberSerach = view.findViewById(R.id.etPhoneNumberSerach);
        Button btnFindByPhone = view.findViewById(R.id.btnFindByPhone);

        btnFindByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = etPhoneNumberSerach.getText().toString();

                if (phone.equals("")) {
                    Toast.makeText(context, "Please insert phone", Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = Members.findNameByPhone(phone);
                if (name.equals("")) {
                    Toast.makeText(context, "Cannot find number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Member member = Members.findMember(phone);


                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String myPhone = mAuth.getCurrentUser().getPhoneNumber();


                if (phone.equals(myPhone)) {
                    Toast.makeText(context, "You are already in!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!members.contains(member)) {
                    members.add(member);
                    addMember();
                }
            }
        });

    }

    void createGroup() {

        Button btnCreate = view.findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText eTGroupName = view.findViewById(R.id.eTGroupName);
                String groupName = eTGroupName.getText().toString();

                if (groupName.isEmpty()) {
                    Toast.makeText(context, "Please insert group name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (countMembersInGroup() == 0) {
                    Toast.makeText(context, "You have to insert at least one member!", Toast.LENGTH_SHORT).show();
                    return;
                }


                String id = generateId();

                //firebase
                createGroup(groupName, id);

                //locally
                Groups groups = Groups.getInstance();
                groups.add(new Group(groupName, id));


                androidx.gridlayout.widget.GridLayout gridLayout = view.findViewById(R.id.addingMembersGL);


                Belongs belongs = Belongs.getInstance();
                for (int i = 0; i < gridLayout.getChildCount(); i++) {
                    View child = gridLayout.getChildAt(i);
                    androidx.gridlayout.widget.GridLayout.LayoutParams params = (androidx.gridlayout.widget.GridLayout.LayoutParams) child.getLayoutParams();

                    if (params.columnSpec.equals(androidx.gridlayout.widget.GridLayout.spec(0))) {
                        // This child is in the first column
                        // Do something with the child view

                        if (child instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) child;
                            if (checkBox.isChecked()) {
                                String phone = checkBox.getTag().toString();
                                belongs.add(new Belong(id, phone));

                                addBelongs(phone, id);
                            }

                        }
                    }
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String myPhone = mAuth.getCurrentUser().getPhoneNumber();

                belongs.add(new Belong(id,myPhone));
                addBelongs(myPhone,id);

                if (getActivity() instanceof OnAddingGroup) {
                    ((OnAddingGroup) getActivity()).onAddingGroup();
                } else {
                    Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void addMember() {
        androidx.gridlayout.widget.GridLayout gridLayout = view.findViewById(R.id.addingMembersGL);

        for (int i = indexMember; i < members.size(); i++) {
            String name = members.get(i).getName();
            String phone = members.get(i).getPhone();

            CheckBox checkBox = new CheckBox(context);
            checkBox.setText(name);
            checkBox.setTag(phone);

            TextView textView = new TextView(context);
            textView.setText(phone);


            gridLayout.addView(checkBox);
            gridLayout.addView(textView);

        }
        indexMember = members.size();
    }


    public static String generateId() {
        Random random = new Random();
        int number = random.nextInt(9000000) + 1000000; // ייצר מספר בין 1000000 ל-9999999
        return String.valueOf(number);
    }

    void createGroup(String name, String id) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("id", id);


        firestoreHelper.setDocument("groups", id, data);

    }


    void addBelongs(String phone, String id) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        Map<String, Object> data = new HashMap<>();
        data.put("phone", phone);
        data.put("id", id);


        firestoreHelper.setDocument("belongs", id + phone, data);

    }



    int countMembersInGroup()
    {
        int counter=0;
        androidx.gridlayout.widget.GridLayout gridLayout = view.findViewById(R.id.addingMembersGL);


        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            androidx.gridlayout.widget.GridLayout.LayoutParams params = (androidx.gridlayout.widget.GridLayout.LayoutParams) child.getLayoutParams();

            if (params.columnSpec.equals(androidx.gridlayout.widget.GridLayout.spec(0))) {

                if (child instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) child;
                    if (checkBox.isChecked()) {
                        counter++;
                    }

                }
            }
        }
        return counter;
    }

}