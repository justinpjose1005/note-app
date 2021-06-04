package com.example.internshala_assignment;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {
    //declare variables
    private final List<NotesData> notesList;
    private final Activity context;

    public NotesAdapter(Activity context, List<NotesData> notesList) {
        this.notesList = notesList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_notes, parent, false);
        return new NotesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NotesHolder holder, int position) {
        //initialize notes data
        NotesData note = notesList.get(position);
        String title = note.getTitle();
        String last_modified = note.getLast_modified();
        holder.notes_title.setText((position+1) + ". " + title);
        holder.notes_time.setText("Last Modified: " + last_modified);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewNoteFragment(note);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class NotesHolder extends RecyclerView.ViewHolder {
        //declare variables
        TextView notes_title, notes_time;
        LinearLayout item;

        public NotesHolder(View view) {
            super(view);
            //assign variables
            notes_title = view.findViewById(R.id.notes_title);
            notes_time = view.findViewById(R.id.notes_time);
            item = view.findViewById(R.id.item);
        }
    }
}
