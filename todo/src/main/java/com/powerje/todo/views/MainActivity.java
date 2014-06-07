package com.powerje.todo.views;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.powerje.todo.R;
import com.powerje.todo.models.Todo;
import com.powerje.todo.receivers.TodoReceiver;
import com.powerje.todo.reminders.TodoNotificationUtils;
import com.powerje.todo.views.adapters.TodoAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;


public class MainActivity extends Activity {

    @InjectView(R.id.list) ListView list;

    final TodoAdapter todoAdapter = new TodoAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        todoAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                // Fire off notification stuff
                Timber.d("Sending notifications!");
                /*
                TodoNotificationUtils.setupNotifications(todoAdapter.getTodos(),
                        MainActivity.this);
                        */
            }
        });

        todoAdapter.addTodo(new Todo(0, "Eat pizza"));
        todoAdapter.addTodo(new Todo(1, "Acquire WiFi"));
        todoAdapter.addTodo(new Todo(2, "Make feature branch"));
        list.setAdapter(todoAdapter);

        TodoNotificationUtils.setupNotifications(todoAdapter.getTodos(),
                getApplicationContext());

        Intent intent = new Intent(TodoReceiver.ACTION_TODO_TOGGLED);
        intent.setClass(this, TodoReceiver.class);
        sendBroadcast(intent, TodoReceiver.ACTION_TODO_TOGGLED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_plus) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
