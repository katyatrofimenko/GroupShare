package com.example.myevents.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myevents.Participant;
import com.example.myevents.R;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Group;
import com.example.myevents.models.Members;

import java.util.ArrayList;

public class GroupsRecycleAdapter extends RecyclerView.Adapter<GroupsRecycleAdapter.MyViewHolder> {

    private ArrayList<Group> groups;
    Context context;
    private RecycleViewClickListener listener;

    public GroupsRecycleAdapter(ArrayList<Group> groups, RecycleViewClickListener listener, Context context) {
        this.groups = groups;
        this.context = context;
        this.listener = listener;
    }



    public interface OnChooseGroup {
        void onChooseGroup(String name);

    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*1 define all views*/
        private LinearLayout group1LL,group2LL;
        private TextView groupName1H,groupName2H;
        private androidx.gridlayout.widget.GridLayout membersGL1,membersGL2;

        public MyViewHolder(final View view) {
            super(view);

            /*2 find all views*/
            group1LL = view.findViewById(R.id.group1LL);
            group2LL = view.findViewById(R.id.group2LL);
            groupName1H = view.findViewById(R.id.groupName1H);
            groupName2H = view.findViewById(R.id.groupName2H);
            membersGL1 = view.findViewById(R.id.membersGL1);
            membersGL2 = view.findViewById(R.id.membersGL2);


            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        /* 3 change the xml source  "R.layout.item_list"  */
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_memner_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group group1 = groups.get(position*2);

        //first group



        holder.group1LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof OnChooseGroup) {
                    ((OnChooseGroup) context).onChooseGroup(group1.getId());

                }
            }
        });

        String groupName = group1.getName();
        holder.groupName1H.setText(groupName);


        Belongs belongs = Belongs.findByGroup(group1.getId());
        for(int i=0; i<belongs.size();i++)
        {
            String memberName = Members.findNameByPhone(belongs.get(i).getPhone());
            Participant p = new Participant(context);

            String name = "";
            if(memberName.length()>0)
            {
            name+=memberName.charAt(0);
            }
            if(memberName.length()>1)
                name+=memberName.charAt(1);

            p.setText(name);

            holder.membersGL1.addView(p);

        }

        //second group
        if(position*2+1<groups.size())
        {

            Group group2 = groups.get(position*2+1);



            holder.group2LL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context instanceof OnChooseGroup) {
                        ((OnChooseGroup) context).onChooseGroup(group2.getId());

                    }
                }
            });

             groupName = group2.getName();
            holder.groupName2H.setText(groupName);


             belongs = Belongs.findByGroup(group2.getId());
            for(int i=0; i<belongs.size();i++)
            {
                String memberName = Members.findNameByPhone(belongs.get(i).getPhone());
                Participant p = new Participant(context);

                String name = "";
                if(memberName.length()>0)
                {
                    name+=memberName.charAt(0);
                }
                if(memberName.length()>1)
                    name+=memberName.charAt(1);

                p.setText(name);

                holder.membersGL2.addView(p);

            }

        }
        else {
            holder.group2LL.setVisibility(View.INVISIBLE);
            holder.group2LL.setClickable(false);
        }


        /* 4 change the information */
    //    holder.tVName.setText(user.getName());

    }

    @Override
    public int getItemCount() {
        return groups.size()/2+groups.size()%2;
    }

    public interface RecycleViewClickListener {
        void onClick(View v, int position);
    }


}
