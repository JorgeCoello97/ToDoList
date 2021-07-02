package com.jorch.todolist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddTaskDialogFragment extends DialogFragment {

    private EditText editText;
    private TextView textView;
    private Button button;
    public AddTaskDialogListener callback;

    public interface AddTaskDialogListener{
        void createNewTask(String value);
    }

    public AddTaskDialogFragment() {
    }


    public static AddTaskDialogFragment newInstance() {
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_task_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = view.findViewById(R.id.editTextNewTask);
        textView = view.findViewById(R.id.textViewNewTask);
        button = view.findViewById(R.id.buttonNewTask);

        button.setOnClickListener(v->{
            String value = editText.getText().toString();
            if (value.isEmpty()){
                Toast.makeText(getContext(), "Please enter a value for your new task.", Toast.LENGTH_LONG).show();
            }else{
                value = textView.getText()+" "+value;
                callback.createNewTask(value);
                dismiss();
            }
        });
    }

    public void setAddTaskDialogListener(AddTaskDialogListener callback) {
        this.callback = callback;
    }
}