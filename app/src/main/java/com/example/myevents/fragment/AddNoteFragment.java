package com.example.myevents.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myevents.FirebaseHelper;
import com.example.myevents.R;
import com.example.myevents.models.Group;
import com.example.myevents.models.Note;
import com.example.myevents.models.Notes;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class AddNoteFragment extends Fragment {

    private Context context;
    private Group group;
    private View view;

    public AddNoteFragment(Context context, Group group) {
        this.context = context;
        this.group = group;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);

        this.view = view;
        initDatePicker();
        initButton();

        return view;
    }


    void initButton() {
        Button btnCreateNote = view.findViewById(R.id.btnCreateNote);


        btnCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean urgent = false;

                TextView tVChosenDate = view.findViewById(R.id.tVChosenDate);
               String date =  tVChosenDate.getText().toString();
               if(date.equals("chosen date"))
               {
                   Toast.makeText(context, "Please choose date", Toast.LENGTH_SHORT).show();
                   return;
               }

                EditText eTNameNote = view.findViewById(R.id.eTNameNote);

                String name  = eTNameNote.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(context, "Please insert name of note", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioGroup urgencyGroup = view.findViewById(R.id.urgencyGroup);
                int selectedId = urgencyGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(selectedId);
                if (radioButton.getText().toString().equals("urgent")) {
                    urgent = true;
                }

                Random rnd = new Random();

                String id = group.getId()+name+(rnd.nextInt(1000)+50);
                createNote( name,date,urgent,id);

                Note note = new Note(group.getId(),id,name,date,urgent);
                Notes.getInstance().add(note);


                watch( id);

            }
        });


    }


    void initDatePicker()
    {

        Button pickDateButton = view.findViewById(R.id.pickDateButton);

        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // קבלת התאריך הנוכחי
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // יצירת DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker v, int selectedYear, int selectedMonth, int selectedDay) {
                                // month מתחיל מ-0, לכן נוסיף 1 כדי לקבל את החודש הנכון
                                selectedMonth += 1;

                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(selectedYear, selectedMonth - 1, selectedDay);

                                // בדיקה האם התאריך שנבחר הוא עתידי
                                if (selectedDate.after(Calendar.getInstance())) {
                                    String selectedDateString = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                    Toast.makeText(context, "Chosen date: " + selectedDateString, Toast.LENGTH_SHORT).show();
                                    TextView tVChosenDate = view.findViewById(R.id.tVChosenDate);
                                    tVChosenDate.setText(selectedDateString.toString());
                                } else {
                                    Toast.makeText(context, "Please select a future date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);

                // הגדרת מינימום תאריך לבחירה - התאריך הנוכחי
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                // הצגת ה-Dialog
                datePickerDialog.show();
            }
        });
    }



    void watch(String id) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String phone = mAuth.getCurrentUser().getPhoneNumber();
        String customDocumentId = mAuth.getCurrentUser().getPhoneNumber();

        Map<String, Object> data = new HashMap<>();
        data.put("idNote", id);
        data.put("phone", phone);
        data.put("action", "watch");


        firestoreHelper.setDocument("actions", phone+id, data);

    }


    void createNote(String name, String date, boolean urgent ,String id) {
        FirebaseHelper firestoreHelper = new FirebaseHelper();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String phone = mAuth.getCurrentUser().getPhoneNumber();
        String customDocumentId = mAuth.getCurrentUser().getPhoneNumber();

        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("groupId", group.getId());
        data.put("date", date);
        data.put("urgent", urgent);
        data.put("id", id);


        firestoreHelper.setDocument("notes", id, data);



        if (getActivity() instanceof OnAddingNote) {
            ((OnAddingNote) getActivity()).onAddingNote(group.getId());
        } else {
            Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
        }

    }


    public interface OnAddingNote {
        void onAddingNote( String id );

    }


}