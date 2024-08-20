package com.example.myevents.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myevents.DialogFullNoteParticipants;
import com.example.myevents.FirebaseHelper;
import com.example.myevents.Participant;
import com.example.myevents.R;
import com.example.myevents.models.Actions;
import com.example.myevents.models.Belongs;
import com.example.myevents.models.Group;
import com.example.myevents.models.Members;
import com.example.myevents.models.Note;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesRecycleAdapter extends RecyclerView.Adapter<NotesRecycleAdapter.MyViewHolder> {

    private ArrayList<Note> notes;
    Context context;
    private RecycleViewClickListener listener;

    public NotesRecycleAdapter(ArrayList<Note> notes, RecycleViewClickListener listener, Context context) {
        this.notes = notes;
        this.context = context;
        this.listener = listener;
    }


    public interface OnChooseGroup {
        void onChooseGroup(String name);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*1 define all views*/
        private LinearLayout group1LL, group2LL;
        private TextView groupName1H, groupName2H;
        private androidx.gridlayout.widget.GridLayout membersGL1, membersGL2;

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
        Note note1 = notes.get(position * 2);

        //first group


        holder.groupName1H.setTag((position * 2) + "");
        holder.groupName1H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFullNoteParticipants d = new DialogFullNoteParticipants(context, note1.getId());
                d.show();

                d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        if (context instanceof OnRefresh) {
                            ((OnRefresh) context).onRefresh(note1.getGroupId());

                        }
                    }
                });
            }
        });


        String name = note1.getName();
        String date = note1.getDate();
        holder.groupName1H.setText(name + "\n" + date);
        if (note1.isUrgent())
            holder.group1LL.setBackgroundResource(R.drawable.border_red);
        else
            holder.group1LL.setBackgroundResource(R.drawable.border_black);


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


            holder.membersGL1.addView(p);

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
                holder.membersGL1.addView(checkBox);


            } else
                holder.membersGL1.addView(imageView);


        }


        //second group
        if (position * 2 + 1 < notes.size()) {
            Note note2 = notes.get(position * 2 + 1);


            holder.groupName2H.setTag((position * 2 + 1) + "");
            holder.groupName2H.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFullNoteParticipants d = new DialogFullNoteParticipants(context, note2.getId());
                    d.show();

                    d.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (context instanceof OnRefresh) {
                                ((OnRefresh) context).onRefresh(note1.getGroupId());

                            }
                        }
                    });
                }
            });


            name = note2.getName();
            date = note2.getDate();
            holder.groupName2H.setText(name + "\n" + date);
            if (note2.isUrgent())
                holder.group2LL.setBackgroundResource(R.drawable.border_red);
            else
                holder.group2LL.setBackgroundResource(R.drawable.border_black);


            belongs = Belongs.findByGroup(note2.getGroupId());
            for (int i = 0; i < belongs.size(); i++) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                String phone = belongs.get(i).getPhone();
                String action = Actions.getAction(note2.getId(), phone);

                ImageView imageView = new ImageView(context);

                imageView.setImageResource(R.drawable.blank);

                if (action.equals("watch")) {
                    imageView.setImageResource(R.drawable.watch);
                } else if (action.equals("done")) {
                    imageView.setImageResource(R.drawable.check);

                } else if (action.equals("") && mAuth.getCurrentUser().getPhoneNumber().equals(phone)) {
                    action(note2.getId(), "watch");

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


                holder.membersGL2.addView(p);

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
                                action(note2.getId(), "done");
                            else
                                action(note2.getId(), "watch");

                        }
                    });
                    holder.membersGL2.addView(checkBox);


                } else
                    holder.membersGL2.addView(imageView);


            }


        } else {
            holder.group2LL.setVisibility(View.INVISIBLE);
            holder.group2LL.setClickable(false);
        }


    }

    @Override
    public int getItemCount() {
        return notes.size() / 2 + notes.size() % 2;
    }

    public interface RecycleViewClickListener {
        void onClick(View v, int position);
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


    public interface OnRefresh {
        void onRefresh(String id);

    }


}
