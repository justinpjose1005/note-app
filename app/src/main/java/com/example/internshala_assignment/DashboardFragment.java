package com.example.internshala_assignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {
    TextView title, new_note_btn, empty_message;
    ImageButton logout;
    RecyclerView notes_recyclerView;
    LinearLayout listview;
    List<NotesData> notesList = new ArrayList<>();
    MyDbHelper myDbHelper;
    static final String status = "add_note";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize_variables(view);
        initialize_onClicks();
        print_google_users_name();
        initialize_sqlite_database();
        initialize_recyclerView();
    }

    private void initialize_variables(View view) {
        title = view.findViewById(R.id.title);
        logout = view.findViewById(R.id.logout);
        listview = view.findViewById(R.id.listview);
        new_note_btn = view.findViewById(R.id.new_note_btn);
        empty_message = view.findViewById(R.id.empty_message);
        notes_recyclerView = view.findViewById(R.id.notes_recyclerView);
    }

    private void initialize_onClicks() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logs out google user
                FirebaseAuth.getInstance().signOut();
                Fragment fragment = new LoginFragment();
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
        new_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add a new note
                Fragment fragment = new NoteFragment(status, null);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
    }

    private void print_google_users_name() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (signInAccount != null) {
            String gName = signInAccount.getDisplayName();
            title.setText(gName);
        }
    }

    private void initialize_sqlite_database() {
        myDbHelper = new MyDbHelper(requireActivity(), "notes_db", null, 1);
        retrieve_data_from_database();
    }

    private void retrieve_data_from_database() {
        notesList = myDbHelper.retrieveData();
//        myDbHelper.dropTable();
    }

    private void initialize_recyclerView() {
        if (notesList.isEmpty()) {
            empty_message.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireActivity());
            notes_recyclerView.setLayoutManager(linearLayoutManager2);
            NotesAdapter notesAdapter = new NotesAdapter(requireActivity(), notesList);
            notes_recyclerView.setAdapter(notesAdapter);
            listview.setVisibility(View.VISIBLE);
        }
    }
}