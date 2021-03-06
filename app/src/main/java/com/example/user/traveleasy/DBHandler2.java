package com.example.user.traveleasy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;



public class DBHandler2 extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "eventsInfo";
    // Contacts table name
    private static final String TABLE_EVENTS = "events";
    // Trips Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_DATE = "event_date";
    private static final String KEY_EVENT_TEXT = "event_text";
    private static final String KEY_EVENT_TIME = "event_time";
    public DBHandler2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EVENT_DATE + " TEXT,"
                + KEY_EVENT_TEXT + " TEXT," + KEY_EVENT_TIME + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
// Creating tables again
        onCreate(db);
    }
    // Adding new event
    public void addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_DATE, event.getDate()); // Event date
        values.put(KEY_EVENT_TEXT, event.getText()); // Event text
        values.put(KEY_EVENT_TIME, event.getTime()); // Event text

// Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }
    // Getting one event
    public Event getEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EVENTS, new String[] { KEY_ID,
                        KEY_EVENT_DATE, KEY_EVENT_TEXT, KEY_EVENT_TIME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Event contact = new Event(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));
// return event
        return contact;
    }
    // Getting All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setDate(cursor.getString(1));
                event.setText(cursor.getString(2));
                event.setTime(cursor.getString(3));

// Adding contact to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }
// return contact list
        return eventList;
    }
    // Getting events Count
    public int getEventsCount() {
        String countQuery = "SELECT * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }
    // Updating a event
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_DATE, event.getDate());
        values.put(KEY_EVENT_TEXT, event.getText());
        values.put(KEY_EVENT_TIME, event.getTime());
// updating row
        return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }
    // Deleting a event
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID + " = ?",
                new String[] { String.valueOf(event.getId()) });
        db.close();
    }
}