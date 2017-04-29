package com.example.sourabmangrulkar.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sourab Mangrulkar on 1/15/2017.
 */

public class NotebookDBAdapter {

    private static final String DATABASE_NAME = "notebook.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOTE_TABLE = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";

    private String[] allColumns = new String[]{COLUMN_ID , COLUMN_TITLE , COLUMN_MESSAGE , COLUMN_CATEGORY , COLUMN_DATE};

    public static final String CREATE_NOTE_TABLE = "CREATE TABLE " +  NOTE_TABLE +" ( "
                                + COLUMN_ID + " integer primary key autoincrement, "
                                + COLUMN_TITLE + " text not null, "
                                + COLUMN_MESSAGE + " text not null, "
                                + COLUMN_CATEGORY + " text not null, "
                                + COLUMN_DATE + " );";

    private SQLiteDatabase sqlDB;
    private Context context;
    private NotebookDBHelper notebookDBHelper;

    public NotebookDBAdapter(Context ctx) {
        context = ctx;
    }

    public NotebookDBAdapter open() throws android.database.SQLException {
        notebookDBHelper = new NotebookDBHelper(context);
        sqlDB = notebookDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        notebookDBHelper.close();
    }

    public Note createNote(String title, String message, Note.Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_CATEGORY, category.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis());

        long index_id = sqlDB.insert(NOTE_TABLE, null, values);

        Cursor cursor=sqlDB.query(NOTE_TABLE, allColumns, COLUMN_ID + " = " + index_id, null,null,null,null);
        cursor.moveToFirst();
        Note newNote = cursorToNote(cursor);
        cursor.close();
        return newNote;
    }

    public long updateNote(long idToUpdate, String title, String message, Note.Category category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_MESSAGE, message);
        values.put(COLUMN_CATEGORY, category.name());
        values.put(COLUMN_DATE, Calendar.getInstance().getTimeInMillis());

        return sqlDB.update(NOTE_TABLE, values, COLUMN_ID + " = " + idToUpdate, null);
    }

    public long deleteNote(long idToDelete) {
        return sqlDB.delete(NOTE_TABLE, COLUMN_ID + " = " + idToDelete, null);
    }

    public ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();

        Cursor cursor = sqlDB.query(NOTE_TABLE , allColumns , null , null , null , null , null);

        for(cursor.moveToLast();!cursor.isBeforeFirst();cursor.moveToPrevious()) {
            Note  note = cursorToNote(cursor);
            notes.add(note);
        }
        cursor.close();
        return notes;
    }

    private Note cursorToNote(Cursor cursor)
    {
        Note new_note = new Note(cursor.getString(1),cursor.getString(2),
                Note.Category.valueOf(cursor.getString(3)),cursor.getLong(0),cursor.getLong(4));

        return new_note;

    }

    private static class NotebookDBHelper extends SQLiteOpenHelper{

        NotebookDBHelper(Context ctx) {
            super(ctx , DATABASE_NAME , null , DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqlDB){
            //create note table here
            sqlDB.execSQL(CREATE_NOTE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqlDB , int oldVersion , int newVersion) {
            //drop the note table and call onCreate()
            sqlDB.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
            onCreate(sqlDB);

        }


    }

//end of this file
}
