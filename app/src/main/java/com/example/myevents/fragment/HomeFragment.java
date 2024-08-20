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

import com.example.myevents.R;
import com.example.myevents.adapters.GroupsRecycleAdapter;
import com.example.myevents.models.Groups;


public class HomeFragment extends Fragment {

    GroupsRecycleAdapter.RecycleViewClickListener listener;

    Context context;


    public HomeFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        initRecycleView(view);
        return view;
    }


    void initRecycleView(View view) {
        RecyclerView recycleView;

        recycleView = view.findViewById(R.id.groupsRV);

        itemClick();
        GroupsRecycleAdapter adapter = new GroupsRecycleAdapter(Groups.getInstance(), listener, context);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setAdapter(adapter);

    }




    void itemClick() {

        listener = new GroupsRecycleAdapter.RecycleViewClickListener() {

            @Override
            public void onClick(View v, int position) {

            }


        };
    }
}