package com.example.internshala_assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {
    //declare variables
    String table_name = "notes";
    String column_1 = "note_timestamp";
    String column_2 = "note_title";
    String column_3 = "note_description";
    String column_4 = "note_modified";

    public MyDbHelper(Context context, String db_name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, db_name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + "( " + column_1 + " TEXT, " + column_2 + " TEXT, " + column_3 + " TEXT, " + column_4 + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertData(NotesData note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_1, note.getTime_stamp());
        contentValues.put(column_2, note.getTitle());
        contentValues.put(column_3, note.getDescription());
        contentValues.put(column_4, note.getLast_modified());
        db.insert(table_name, null, contentValues);
        db.close();
    }


    public List<NotesData> retrieveData() {
        List<NotesData> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + table_name,null);
        while (cursor.moveToNext()) {
            String timestamp = cursor.getString(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String lastModified = cursor.getString(3);
            NotesData data = new NotesData(timestamp,title,description,lastModified);
            dataList.add(data);
        }
        cursor.close();
        db.close();
        return dataList;
    }


    public void delete(String dbTimestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table_name + " WHERE " + column_1 + " = '" + dbTimestamp + "' ");
        db.close();
    }

    public void editData(NotesData note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(column_2, note.getTitle());
        contentValues.put(column_3, note.getDescription());
        contentValues.put(column_4, note.getLast_modified());
        db.update(table_name,contentValues,column_1 + "=?",new String[]{note.getTime_stamp()});
        db.close();
    }

    public void dropTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        db.close();
    }
}
