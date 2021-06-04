package com.example.internshala_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements note_interface {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static final String sharedPrefName = "active_fragment";
    static final String sharedPrefKey = "fragment_name";
    static final String sharedPrefNote = "note_data";
    static String currentFragment = null;
    NotesData note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize_frameLayout();
    }

    private void initialize_frameLayout() {
        //calls login fragment
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new LoginFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        initialize_sharedPref();
        if (currentFragment == null) {
            super.onBackPressed();
        } else if (currentFragment.equals("AddNoteFragment") || currentFragment.equals("ViewNoteFragment")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new DashboardFragment()).commit();
            editor.putString(sharedPrefKey,null);
            editor.apply();
        } else if (currentFragment.equals("EditNoteFragment")) {
//            NotesData note = sharedPreferences.getString(sharedPrefNote,null);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ViewNoteFragment(note)).commit();
            editor.putString(sharedPrefKey,null);
            editor.apply();
        }
    }

    private void initialize_sharedPref() {
        sharedPreferences = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        currentFragment = sharedPreferences.getString(sharedPrefKey,null);
    }

    @Override
    public void pass_note(NotesData note) {
        // retrieves note data from unedited note
        // in-case user goes back using onBackPressed
        this.note = note;
    }
}