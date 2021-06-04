package com.example.internshala_assignment;

public class NotesData {
    String time_stamp;
    String title;
    String description;
    String last_modified;

    public NotesData(String time_stamp, String title, String description, String last_modified) {
        this.time_stamp = time_stamp;
        this.title = title;
        this.description = description;
        this.last_modified = last_modified;
    }

    public NotesData() {
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }
}
