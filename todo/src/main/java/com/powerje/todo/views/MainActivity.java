package com.powerje.todo.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;
import com.powerje.todo.data.TodoProvider;
import com.powerje.todo.data.adapters.TodoCursorAdapter;
import com.powerje.todo.reminders.TodoNotificationUtils;

import java.util.HashSet;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.timroes.android.listview.EnhancedListView;

public class MainActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.list) EnhancedListView list;

    private TodoCursorAdapter todoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        todoCursorAdapter = new TodoCursorAdapter(this);
        list.setAdapter(todoCursorAdapter);
        list.setMultiChoiceModeListener(multiChoiceModeListener);

        getLoaderManager().initLoader(R.id.list, null, this);

        TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(this), this);
        setupUndo();
    }

    private void setupUndo() {
        list.setUndoStyle(EnhancedListView.UndoStyle.MULTILEVEL_POPUP);
        list.setDismissCallback(new EnhancedListView.OnDismissCallback() {

            @Override
            public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

                Cursor c = (Cursor) todoCursorAdapter.getItem(position);
                final Todo todo = TodoProvider.getTodo(c);

                todoCursorAdapter.putPendingDismiss(todo.getId());
                todoCursorAdapter.notifyDataSetChanged();

                TodoProvider.removeTodo(todo, getApplicationContext());

                return new EnhancedListView.Undoable() {
                    @Override public void undo() { TodoProvider.addTodo(todo, getApplicationContext()); }
                    @Override public String getTitle() { return "Deleted '" + todo.getText() + "'"; }
                    @Override public void discard() { }
                };
            }
        });
        list.enableSwipeToDismiss();
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
                showAddTodoDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // UI callbacks

    @OnItemClick(R.id.list)
    void onItemClick(int position) {
        Cursor c = (Cursor) todoCursorAdapter.getItem(position);
        Todo todo = TodoProvider.getTodo(c);
        todo.toggleChecked();
    }

    private void showAddTodoDialog() {
        FragmentManager fm = getFragmentManager();
        NewTodoDialogFragment dialog = NewTodoDialogFragment.newInstance();
        dialog.show(fm, "fragment_edit_name");
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
            selected = new HashSet<>();

            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context, menu);
            return true;
        }

        @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    deleteSelectedItems();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override public void onDestroyActionMode(ActionMode mode) { selected = null; }

        private void deleteSelectedItems() {
            for (Todo todo : selected) {
                TodoProvider.removeTodo(todo, MainActivity.this);
            }
        }
    };
}
