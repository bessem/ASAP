package com.pirana.aka.asap.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aka on 11/30/15.
 */
public class TodoOperations extends SQLiteOpenHelper {

    final static String TODO_DATA_BASE = "TODOS";
    final static String TODO_TABLE = "TODOS";
    final public String ID = "ID";
    final public String ABOUT = "ABOUT";
    final public String LONGITUDE = "LONGITUDE";
    final public String LATITUDE = "LATITUDE";
    public static final int VERSION = 1;
    public String CREATE_QUERY = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY   AUTOINCREMENT, " + ABOUT + " TEXT, "
            + LONGITUDE + " TEXT, " + LATITUDE + " TEXT );";

    public TodoOperations(Context context) {
        super(context, TODO_DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        System.out.println("data Base Operation:Table Created successfully ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    // Adding new Todo
    public boolean addTodo(Todo todo) {
        ContentValues values = new ContentValues();
        values.put(ABOUT, todo.getAbout());
        values.put(LATITUDE, todo.getLocation().getLatitude());
        values.put(LONGITUDE, todo.getLocation().getLongitude());
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TODO_TABLE, null, values);
            return true;
        } catch (SQLiteException e) {
            return false;
        }
    }

    // Getting single Todo
    public Todo getTodo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TODO_TABLE, new String[]{ID,
                        ABOUT, LONGITUDE, LATITUDE}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Location location = new Location("");
        location.setLatitude(Double.parseDouble(cursor.getString(2)));
        location.setLongitude(Double.parseDouble(cursor.getString(3)));
        Todo contact = new Todo(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), location);
        // return contact
        return contact;
    }

    // Getting All Todos
    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<Todo>();
        String selectQuery = "SELECT  * FROM " + TODO_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                Location location = new Location("");
                todo.set_id(Integer.parseInt(cursor.getString(0)));
                todo.setAbout((cursor.getString(1)));
                String longitude = (cursor.getString(2));
                String latitude = (cursor.getString(3));
                location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));
                todo.setLocation(location);
                todos.add(todo);
            } while (cursor.moveToNext());
        }
        return todos;
    }

    // Getting Todos Count
    public int getTodosCount() {
        String countQuery = "SELECT  * FROM " + TODO_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    // Updating single Todo
    public int updateTodo(Todo todo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(ABOUT, todo.getAbout());
            values.put(LATITUDE, todo.getLocation().getLatitude());
            values.put(LONGITUDE, todo.getLocation().getLongitude());

            // updating row
            return db.update(TODO_TABLE, values, ID + " = ?",
                    new String[]{String.valueOf(todo.get_id())});
        } catch (SQLiteException e) {
            return 0;
        }
    }

    // Deleting
    public void deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TODO_TABLE, ID + " = ?",
                new String[]{String.valueOf(todo.get_id())});
        db.close();
    }
}
