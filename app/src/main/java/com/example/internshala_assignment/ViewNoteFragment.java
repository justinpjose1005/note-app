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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNoteFragment extends Fragment {
    //declare variables
    ImageButton back;
    TextView note_title, note_description, note_lastModified;
    TextView edit_btn, delete_btn;
    TextView confirm_btn, cancel_btn;
    RelativeLayout dialog_box;
    String dbTimestamp, dbTitle, dbDescription, dbLastModified;
    NotesData dbNote;
    MyDbHelper myDbHelper;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final String sharedPrefName = "active_fragment";
    static final String sharedPrefKey = "fragment_name";
    static final String status = "edit_note";

    public ViewNoteFragment(NotesData note) {
        this.dbNote = note;
        this.dbTimestamp = note.getTime_stamp();
        this.dbTitle = note.getTitle();
        this.dbDescription = note.getDescription();
        this.dbLastModified = note.getLast_modified();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize_variables(view);
        initialize_onClick();
        print_note();
        initialize_sharedPref();
        initialize_sqlite_database();
    }

    private void initialize_variables(View view) {
        back = view.findViewById(R.id.back);
        edit_btn = view.findViewById(R.id.edit_btn);
        delete_btn = view.findViewById(R.id.delete_btn);
        confirm_btn = view.findViewById(R.id.confirm_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        note_title = view.findViewById(R.id.note_title);
        note_description = view.findViewById(R.id.note_description);
        note_lastModified = view.findViewById(R.id.note_lastModified);
        dialog_box = view.findViewById(R.id.dialog_box);
    }

    private void initialize_onClick() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back btn navigates to dashboard
                call_dashboard_fragment();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm_with_the_user();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // edits the current note by navigating to
                Fragment fragment = new NoteFragment(status,dbNote);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user confirms to delete the current note
                myDbHelper.delete(dbTimestamp);
                Toast toast = Toast.makeText(requireActivity(), "Note Deleted", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                call_dashboard_fragment();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user does not wish to delete the note
                dialog_box.setVisibility(View.GONE);
            }
        });
    }

    private void confirm_with_the_user() {
        dialog_box.setVisibility(View.VISIBLE);
    }

    private void call_dashboard_fragment() {
        Fragment fragment = new DashboardFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        editor.putString(sharedPrefKey,null);
        editor.apply();
    }

    private void print_note() {
        // display selected note details
        note_title.setText(dbTitle);
        note_description.setText(dbDescription);
        note_lastModified.setText("Last Modified: " + dbLastModified);
    }

    private void initialize_sharedPref() {
        sharedPreferences = requireActivity().getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(sharedPrefKey,"ViewNoteFragment");
        editor.apply();
    }

    private void initialize_sqlite_database() {
        myDbHelper = new MyDbHelper(requireActivity(), "notes_db", null, 1);
    }
}