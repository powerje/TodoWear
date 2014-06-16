package com.powerje.todo.receivers;

import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.powerje.todo.data.Todo;
import com.powerje.todo.data.TodoProvider;
import com.powerje.todo.reminders.TodoNotificationUtils;

import static com.powerje.todo.data.TodoProvider.TODO_URI;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by jep on 6/7/14.
 */
public class TodoReceiver extends BroadcastReceiver {

    public static final String ACTION_TODO_TOGGLED = "com.powerje.todo.intent.action.TOGGLED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            Long todoId = intent.getExtras().getLong("todoId");
            Uri todoUri = ContentUris.withAppendedId(TODO_URI, todoId);
            Todo todo = cupboard().withContext(context).get(todoUri, Todo.class);
            todo.toggleChecked();
            TodoProvider.updateTodo(todo, context);

            TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(context), context);
            Toast.makeText(context, "Just toggled '" + todo.getText() + "' " + todo.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}
