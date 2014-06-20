package com.powerje.todo.views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;
import com.powerje.todo.data.TodoProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by jep on 6/19/14.
 */
public class NewTodoDialogFragment extends DialogFragment {
    @InjectView(R.id.todo_edit_text) EditText todoEditText;

    public static NewTodoDialogFragment newInstance() {
        NewTodoDialogFragment frag = new NewTodoDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_todo, container);
        ButterKnife.inject(this, view);

        getDialog().setTitle(R.string.new_todo_prompt);
        // Show soft keyboard automatically
        todoEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    private void addTodo() {
        TodoProvider.addTodo(new Todo(todoEditText.getText().toString()), getActivity());
        dismiss();
    }

    @OnClick(R.id.new_todo_button) void donePressed() { addTodo(); }

    @OnEditorAction(R.id.todo_edit_text)
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            addTodo();
            return true;
        }
        return false;
    }
}
