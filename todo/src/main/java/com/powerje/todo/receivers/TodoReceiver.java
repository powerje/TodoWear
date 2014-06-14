package com.powerje.todo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jep on 6/7/14.
 */
public class TodoReceiver extends BroadcastReceiver {

    public static final String ACTION_TODO_TOGGLED = "com.powerje.todo.intent.action.TOGGLED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Long todoId = intent.getExtras().getLong("todoId");
        // TODO: pull todo from database

//        Todo todo = intent.getExtras().getParcelable("todo");
//        Toast.makeText(context, "GOT AN INTENT, todo #" + todo.getId(), Toast.LENGTH_SHORT).show();
    }
}
