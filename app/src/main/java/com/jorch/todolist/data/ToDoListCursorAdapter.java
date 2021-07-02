package com.jorch.todolist.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jorch.todolist.R;

import static android.provider.BaseColumns._ID;
import static com.jorch.todolist.data.ToDoListContract.ToDoListEntry.COLUMN_DONE;
import static com.jorch.todolist.data.ToDoListContract.ToDoListEntry.COLUMN_NAME;

public class ToDoListCursorAdapter extends CursorAdapter {
    public static final int TASK_TODO = 0;
    public static final int TASK_DONE = 1;

    public RemoveTask callback;

    public interface RemoveTask{
        void removeTask(String id);
        void completedTask(String id);
    }

    public ToDoListCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CheckBox checkBox = view.findViewById(R.id.checkBoxTask);
        TextView textView = view.findViewById(R.id.textViewTask);
        ImageView imageView = view.findViewById(R.id.imageViewRemoveTask);

        String id = cursor.getString(cursor.getColumnIndexOrThrow(_ID));

        String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
        textView.setText(name);

        int status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DONE));
        Log.d("TASK_STATUS",""+ status);
        switch (status){
            case TASK_TODO:
                checkBox.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                break;
            case TASK_DONE:
                checkBox.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                break;
        }
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                checkBox.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                callback.completedTask(id);
            }
        });
        imageView.setOnClickListener(v ->{
            callback.removeTask(id);
        });
    }

    public void setRemoveTask(RemoveTask callback) {
        this.callback = callback;
    }

}
