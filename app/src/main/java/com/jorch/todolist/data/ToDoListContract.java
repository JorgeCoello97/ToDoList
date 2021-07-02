package com.jorch.todolist.data;

import android.provider.BaseColumns;

public class ToDoListContract {

    private ToDoListContract() {}

    public static class ToDoListEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DONE = "done";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ToDoListEntry.TABLE + " (" +
                    ToDoListEntry._ID + " INTEGER PRIMARY KEY," +
                    ToDoListEntry.COLUMN_NAME + " TEXT," +
                    ToDoListEntry.COLUMN_DONE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ToDoListEntry.TABLE;
}
