package com.example.myevents;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myevents.adapters.GroupsRecycleAdapter;
import com.example.myevents.adapters.NotesRecycleAdapter;
import com.example.myevents.fragment.AddGroupFragment;
import com.example.myevents.fragment.AddNoteFragment;
import com.example.myevents.fragment.GroupFragment;
import com.example.myevents.fragment.HomeFragment;
import com.example.myevents.fragment.SettingFragment;
import com.example.myevents.models.Action;
import com.example.myevents.models.Actions;
import com.example.myevents.models.Belong;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Group;
import com.example.myevents.models.Groups;
import com.example.myevents.models.Member;
import com.example.myevents.models.Members;
import com.example.myevents.models.Note;
import com.example.myevents.models.Notes;
import com.example.myevents.Participant;


import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity implements AddGroupFragment.OnAddingGroup, GroupsRecycleAdapter.OnChooseGroup, GroupFragment.OnMoveToAddNote, AddNoteFragment.OnAddingNote, NotesRecycleAdapter.OnRefresh {

    private TextView accountName;
    private TextView avatarInitials;
    private String moveTo ="addGroup";
    private String groupId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialize UI components
        accountName = findViewById(R.id.accountName);
        avatarInitials = findViewById(R.id.avatarInitials);

        changeHeightFragment();
        createData();
        initMenu();
    }

    void initMenu() {
        LinearLayout addGroupMenu = findViewById(R.id.addGroupMenu);
        addGroupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moveTo.equals("addGroup")) {
                    initAddGroupFragment();
                } else if (moveTo.equals("addNote")) {
                    Group group = Groups.getInstance().findGroupById(groupId);
                    onMoveToAddNote(group);
                }
            }
        });

        LinearLayout homeMenu = findViewById(R.id.homeMenu);
        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initHomeFragment();
            }
        });

        LinearLayout settigMenu = findViewById(R.id.settigMenu);
        settigMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initSettingFragment();
                moveTo = "addGroup";
            }
        });
    }

    void initAddGroupFragment() {
        AddGroupFragment addGroupFragment = new AddGroupFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, addGroupFragment);
        fragmentTransaction.commit();
    }

    void initHomeFragment() {
        moveTo = "addGroup";
        HomeFragment homeFragment = new HomeFragment(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, homeFragment);
        fragmentTransaction.commit();
    }

    void initSettingFragment() {
        SettingFragment settingFragment = new SettingFragment(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, settingFragment);
        fragmentTransaction.commit();
    }

    void changeHeightFragment() {
        FragmentContainerView fragmentContainerView = findViewById(R.id.bodyFragment);
        int heightF = fragmentContainerView.getHeight();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomMenu);
        int heightM = constraintLayout.getHeight();
        heightF = heightF - heightM;
        ViewGroup.LayoutParams params = fragmentContainerView.getLayoutParams();
        params.height = heightF;
        fragmentContainerView.setLayoutParams(params);
    }

    void createData() {
        getMembers();
        FirebaseHelper.fetchData("belongs", Belong.class, new FirebaseHelper.DataCallback<Belong>() {
            @Override
            public void onSuccess(List<Belong> data) {
                Belongs belongs = Belongs.getInstance();
                for (Belong belong : data) {
                    belongs.add(belong);
                }
                ArrayList<String> groupsId = Belongs.groupsIBelongTo(Member.getCurrent());
                getGroups(groupsId);
                getNotes(groupsId);
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("info", "Error getting documents.", e);
            }
        });
    }

    void getGroups(ArrayList<String> groupsId) {
        FirebaseHelper.fetchData("groups", Group.class, new FirebaseHelper.DataCallback<Group>() {
            @Override
            public void onSuccess(List<Group> data) {
                Groups groups = Groups.getInstance();
                for (Group group : data) {
                    if (groupsId.contains(group.getId()))
                        groups.add(group);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("info", "Error getting documents.", e);
            }
        });
    }

    void getNotes(ArrayList<String> groupsId) {
        FirebaseHelper.fetchData("notes", Note.class, new FirebaseHelper.DataCallback<Note>() {
            @Override
            public void onSuccess(List<Note> data) {
                Notes notes = Notes.getInstance();
                for (Note note : data) {
                    if (groupsId.contains(note.getGroupId()))
                        notes.add(note);
                }
                getActions();
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("info", "Error getting documents.", e);
            }
        });
    }

    void getMembers() {
        FirebaseHelper.fetchData("members", Member.class, new FirebaseHelper.DataCallback<Member>() {
            @Override
            public void onSuccess(List<Member> data) {
                Members members = Members.getInstance();
                for (Member member : data) {
                    members.add(member);
                }
                updateAccountInfo(Member.getCurrent());
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("info", "Error getting documents.", e);
            }
        });
    }

    void updateAccountInfo(String phone) {
        Member member = Members.getInstance().getMember(phone);
        if (member != null) {
            String name = member.getName();
            accountName.setText(name);
            avatarInitials.setText(getInitials(name));

            // Debugging log statement
            Log.d("MainMenuActivity", "Account Name: " + name);
        } else {
            Log.d("MainMenuActivity", "Member not found for phone: " + phone);
        }
    }



    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ""; // Return an empty string or a default value if the name is empty or null
        }

        String[] nameParts = name.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (String part : nameParts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0));
            }
        }

        return initials.toString().toUpperCase();
    }

    void getActions() {
        FirebaseHelper.fetchData("actions", Action.class, new FirebaseHelper.DataCallback<Action>() {
            @Override
            public void onSuccess(List<Action> data) {
                Actions actions = Actions.getInstance();
                for (Action action : data) {
                    if (Notes.contains(action.getIdNote()))
                        actions.add(action);
                }
                initHomeFragment();
            }

            @Override
            public void onFailure(Exception e) {
                Log.w("info", "Error getting documents.", e);
            }
        });
    }

    @Override
    public void onAddingGroup() {
        initHomeFragment();
    }

    @Override
    public void onChooseGroup(String id) {
        moveTo = "addNote";
        this.groupId = id;

        GroupFragment groupFragment = new GroupFragment(this, id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, groupFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMoveToAddNote(Group group) {
        AddNoteFragment addNoteFragment = new AddNoteFragment(this, group);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.bodyFragment, addNoteFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onAddingNote(String id) {
        onChooseGroup(id);
    }

    @Override
    public void onRefresh(String id) {
        onChooseGroup(id);
    }
}

