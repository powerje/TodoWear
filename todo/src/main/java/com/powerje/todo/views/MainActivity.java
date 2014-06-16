package com.powerje.todo.views;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;
import com.powerje.todo.data.TodoProvider;
import com.powerje.todo.data.adapters.TodoCursorAdapter;
import com.powerje.todo.reminders.TodoNotificationUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.list) ListView list;

    private TodoCursorAdapter todoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        todoCursorAdapter = new TodoCursorAdapter(this);
        list.setAdapter(todoCursorAdapter);
        list.setMultiChoiceModeListener(multiChoiceModeListener);

        list.setOnItemClickListener(itemClickListener);
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
        switch (id) {
            case R.id.action_plus:
                TodoProvider.addTodo(new Todo("foo"), this);
                return true;
            case R.id.action_edit:
                startActionMode(multiChoiceModeListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // LoaderCallbacks

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = new CursorLoader(this);
        loader.setUri(TodoProvider.TODO_URI);
        return loader;
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) { todoCursorAdapter.swapCursor(cursor); }
    @Override public void onLoaderReset(Loader<Cursor> loader) { todoCursorAdapter.swapCursor(null); }


    // Contextual Action Bar

    private AbsListView.MultiChoiceModeListener multiChoiceModeListener = new AbsListView.MultiChoiceModeListener() {
        private Set<Todo> selected;

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            // Never called when startActionMode called on Activity
            Todo todo = TodoProvider.getTodo(todoCursorAdapter.getItemId(position), MainActivity.this);
            if (selected.contains(todo)) {
                selected.remove(todo);
            } else {
                selected.add(todo);
            }
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Called both when startActionMode is called on Activity and when user long presses listview
            selected = new HashSet<>();
            //list.setOnItemClickListener(null);

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            // Here you can perform updates to the CAB due to
            // an invalidate() request
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            // This does get called whether startActionMode is called on Activity
            // or via user long press
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    deleteSelectedItems();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            // Here you can make any necessary updates to the activity when
            // the CAB is removed. By default, selected items are deselected/unchecked.
            selected = null;
            //list.setOnItemClickListener(itemClickListener);
        }

        private void deleteSelectedItems() {
            for (Todo todo : selected) {
                TodoProvider.removeTodo(todo, MainActivity.this);
            }
        }
    };

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor c = (Cursor) todoCursorAdapter.getItem(position);
            Todo todo = TodoProvider.getTodo(c);
            todo.toggleChecked();
            TodoProvider.updateTodo(todo, getApplicationContext());
        }
    };
}
