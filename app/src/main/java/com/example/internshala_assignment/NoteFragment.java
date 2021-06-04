package com.example.internshala_assignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteFragment extends Fragment {
    ImageButton back;
    TextView confirm, header;
    EditText note_title, note_description;
    String timestamp, title, description, lastModified;
    NotesData note;
    String status;
    Date date;
    MyDbHelper myDbHelper;
    Toast toast;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final String sharedPrefName = "active_fragment";
    static final String sharedPrefKey = "fragment_name";
    static final String status_add = "add_note";
    static final String status_edit = "edit_note";

    private note_interface mInterface;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mInterface = (note_interface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement note_interface");
        }
    }

    public NoteFragment(String status, NotesData dbNote) {
        this.status = status;
        this.note = dbNote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize_variables(view);
        initialize_sqlite_database();
        initialize_onClicks();
        initialize_sharedPref();
        initialize_noteData();
        initialize_title();
        mInterface.pass_note(note);
    }

    private void initialize_title() {
        if (status.equals(status_add)) {
            // add note function
            header.setText("NEW NOTE");
            confirm.setText("ADD");
        } else if (status.equals(status_edit)) {
            // edit note function
            header.setText("EDIT NOTE");
            confirm.setText("SAVE");
        }
    }

    private void initialize_noteData() {
        if (status.equals(status_edit)) {
            note_title.setText(note.getTitle());
            note_description.setText(note.getDescription());
        }
    }

    private void initialize_variables(View view) {
        back = view.findViewById(R.id.back);
        header = view.findViewById(R.id.title);
        confirm = view.findViewById(R.id.confirm);
        note_title = view.findViewById(R.id.note_title);
        note_description = view.findViewById(R.id.note_description);
    }

    private void initialize_sqlite_database() {
        myDbHelper = new MyDbHelper(requireActivity(), "notes_db", null, 1);
    }

    private void initialize_onClicks() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals(status_add)) {
                    // back btn navigates to DashboardFragment
                    call_dashboard_fragment();
                } else if (status.equals(status_edit)) {
                    // back btn navigates to ViewNoteFragment
                    call_view_note_fragment(note);
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals(status_add)) {
                    insert_into_sqlite_database();
                } else if (status.equals(status_edit)) {
                    edit_sqlite_database();
                }
            }
        });
    }

    private void call_view_note_fragment(NotesData note) {
        Fragment fragment = new ViewNoteFragment(this.note);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void call_dashboard_fragment() {
        Fragment fragment = new DashboardFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        editor.putString(sharedPrefKey, null);
        editor.apply();
    }

    private void insert_into_sqlite_database() {
        compute_input();
        date = java.util.Calendar.getInstance().getTime();
        timestamp = date.toString();
        if (!title.isEmpty()) {
            if (!description.isEmpty()) {
                note = new NotesData(timestamp,title,description,lastModified);
                myDbHelper.insertData(note);
                toast = Toast.makeText(requireActivity(), "Note Created", Toast.LENGTH_SHORT);
                center_toast();
                call_dashboard_fragment();
            } else {
                toast = Toast.makeText(requireActivity(), "Description field is empty", Toast.LENGTH_SHORT);
                center_toast();
                note_description.requestFocus();
            }
        } else {
            toast = Toast.makeText(requireActivity(), "Title field is empty", Toast.LENGTH_SHORT);
            center_toast();
            note_title.requestFocus();
        }
    }

    private void edit_sqlite_database() {
        compute_input();
        timestamp = note.getTime_stamp();
        if (!title.isEmpty()) {
            if (!description.isEmpty()) {
                note = new NotesData(timestamp,title,description,lastModified);
                myDbHelper.editData(note);
                toast = Toast.makeText(requireActivity(), "Note Edited", Toast.LENGTH_SHORT);
                center_toast();
                call_view_note_fragment(note);
            } else {
                toast = Toast.makeText(requireActivity(), "Description field is empty", Toast.LENGTH_SHORT);
                center_toast();
                note_description.requestFocus();
            }
        } else {
            toast = Toast.makeText(requireActivity(), "Title field is empty", Toast.LENGTH_SHORT);
            center_toast();
            note_title.requestFocus();
        }
    }

    private void compute_input() {
        title = note_title.getText().toString();
        description = note_description.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        date = new Date();
        lastModified = formatter.format(date);
    }

    private void initialize_sharedPref() {
        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (status.equals(status_add)) {
            editor.putString(sharedPrefKey, "AddNoteFragment");
        } else if (status.equals(status_edit)) {
            editor.putString(sharedPrefKey, "EditNoteFragment");
        }
        editor.apply();
    }

    private void center_toast() {
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}