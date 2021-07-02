package com.jorch.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jorch.todolist.data.ToDoListContract;
import com.jorch.todolist.data.ToDoListCursorAdapter;
import com.jorch.todolist.data.ToDoListDbHelper;

import static com.jorch.todolist.data.ToDoListContract.ToDoListEntry.TABLE;
import static com.jorch.todolist.data.ToDoListCursorAdapter.TASK_DONE;
import static com.jorch.todolist.data.ToDoListCursorAdapter.TASK_TODO;

public class MainActivity
        extends AppCompatActivity
        implements AddTaskDialogFragment.AddTaskDialogListener,
                    ToDoListCursorAdapter.RemoveTask {
    private ToDoListCursorAdapter horchataAdapter;
    private ListView listView;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewTasks);
        floatingActionButton = findViewById(R.id.floatButtonNewTask);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshTasks);


        floatingActionButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            AddTaskDialogFragment addTaskDialogFragment = AddTaskDialogFragment.newInstance();
            addTaskDialogFragment.setAddTaskDialogListener(this);
            addTaskDialogFragment.show(fragmentManager, "FRAGMENT_ADD_TASK");
        });

        swipeRefreshLayout.setOnRefreshListener(this::renderTasks);

        renderTasks();
    }

    private void renderTasks() {

        swipeRefreshLayout.setRefreshing(true);
        ToDoListDbHelper dbHelper = new ToDoListDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                ToDoListContract.ToDoListEntry.TABLE,null,null,null,
                null,null,null);
        horchataAdapter = new ToDoListCursorAdapter(this, cursor);
        listView.setAdapter(horchataAdapter);
        horchataAdapter.setRemoveTask(this);

        if (cursor.getCount() == 0){
            showMessage("Empty TO DO List");
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void createNewTask(String value) {
        ToDoListDbHelper dbHelper = new ToDoListDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ToDoListEntry.COLUMN_NAME, value);
        values.put(ToDoListContract.ToDoListEntry.COLUMN_DONE, TASK_TODO);

        long newRowId = db.insert(ToDoListContract.ToDoListEntry.TABLE, null, values);
        if (newRowId != -1){
            showMessage("Create new task successfully");
            renderTasks();
        }else{
            showMessage("Could not to create the task");
        }
    }

    @Override
    public void removeTask(String id) {
        ToDoListDbHelper dbHelper = new ToDoListDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = ToDoListContract.ToDoListEntry._ID+"=?";
        String[] whereArgs = {id};

        long rowsAffected = db.delete(ToDoListContract.ToDoListEntry.TABLE, whereClause, whereArgs);
        if (rowsAffected != 0){
            showMessage("Remove task successfully");
            renderTasks();
        }else{
            showMessage("Could not to remove the task");
        }
    }

    @Override
    public void completedTask(String id) {
        ToDoListDbHelper dbHelper = new ToDoListDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ToDoListEntry.COLUMN_DONE, TASK_DONE);
        String whereClause = ToDoListContract.ToDoListEntry._ID+"=?";
        String[] whereArgs = {id};

        long rowsAffected = db.update(ToDoListContract.ToDoListEntry.TABLE,
                values, whereClause, whereArgs);

        if (rowsAffected != -1){
            showMessage("Task completed!");
            renderTasks();
        }else{
            showMessage("Could not to complete the task");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.removeAll:
                if (listView.getCount() != 0 ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are your sure that you want remove all entries?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        removeAllTasks();
                        dialog.dismiss();
                    });
                    builder.setNegativeButton("No", ((dialog, which) -> {
                        dialog.dismiss();
                    }));
                    builder.create().show();
                }
                else {
                    showMessage("Empty TO DO List");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void removeAllTasks() {
        ToDoListDbHelper dbHelper = new ToDoListDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long newRowId = db.delete(ToDoListContract.ToDoListEntry.TABLE, null, null);
        if (newRowId != 0){
            showMessage("Remove all tasks successfully");
            renderTasks();
        }else{
            showMessage("Could not to remove the task");
        }
    }

    public void showMessage(String message){
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

}