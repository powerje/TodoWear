package com.powerje.todo.views;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;
import com.powerje.todo.data.TodoProvider;
import com.powerje.todo.data.adapters.TodoCursorAdapter;
import com.powerje.todo.reminders.TodoNotificationUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.list) ListView list;

    private TodoCursorAdapter todoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        todoCursorAdapter = new TodoCursorAdapter(this);
        list.setAdapter(todoCursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) todoCursorAdapter.getItem(position);
                Todo todo = TodoProvider.getTodo(c);
                todo.toggleChecked();
                TodoProvider.updateTodo(todo, getApplicationContext());
            }
        });
        getLoaderManager().initLoader(R.id.list, null, this);

        TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(this), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_plus) {
            TodoProvider.addTodo(new Todo("foo"), this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(TodoProvider.TODO_URI);
        return loader;
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) { todoCursorAdapter.swapCursor(cursor); }

    @Override public void onLoaderReset(Loader<Cursor> loader) { todoCursorAdapter.swapCursor(null); }
}
