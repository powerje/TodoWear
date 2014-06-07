package com.powerje.todo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import timber.log.Timber;

/**
 * Created by jep on 6/7/14.
 */
public class TodoReceiver extends BroadcastReceiver {

    public static final String ACTION_TODO_TOGGLED = "com.powerje.todo.intent.action.TOGGLED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "GOT AN INTENT", Toast.LENGTH_SHORT).show();

        Timber.e("Received intent!");
    }
}
