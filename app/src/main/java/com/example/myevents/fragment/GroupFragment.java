package com.example.myevents.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myevents.R;
import com.example.myevents.adapters.NotesRecycleAdapter;
import com.example.myevents.models.Group;
import com.example.myevents.models.Groups;
import com.example.myevents.models.Notes;


public class GroupFragment extends Fragment {

    private Context context;
    private String id;
    private View view;

    private Group group;

    NotesRecycleAdapter.RecycleViewClickListener listener;


    public GroupFragment(Context context,String id) {
        this.context = context;
        this.id = id;
        group = Groups.getInstance().findGroupById(id);

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        this.view = view;

        initRecycleView();


        init();

        return view;
    }



    public interface OnMoveToAddNote {
        void onMoveToAddNote(Group group);

    }




    void init()
    {

        TextView tVGroupName = this.view.findViewById(R.id.tVGroupName);
        tVGroupName.setText(group.getName());


        /* ImageView btnMoveAddNote = view.findViewById(R.id.btnMoveAddNote);
        btnMoveAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getActivity() instanceof OnMoveToAddNote) {
                    ((OnMoveToAddNote) getActivity()).onMoveToAddNote(group);
                } else {
                    Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }





    void initRecycleView() {
        RecyclerView recycleView;

        recycleView = view.findViewById(R.id.notesRV);

        itemClick();
        NotesRecycleAdapter adapter = new NotesRecycleAdapter(Notes.findNotesByGroup(group.getId()), listener, context);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(adapter);

    }




    void itemClick() {

        listener = new NotesRecycleAdapter.RecycleViewClickListener() {

            @Override
            public void onClick(View v, int position) {

            }


        };
    }


}