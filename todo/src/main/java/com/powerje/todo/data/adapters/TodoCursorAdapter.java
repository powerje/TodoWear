package com.powerje.todo.data.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.powerje.todo.R;
import com.powerje.todo.data.Todo;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by jep on 6/14/14.
 */
public class TodoCursorAdapter extends BaseSwipableCursorAdapter {

    static class ViewHolder {
        @InjectView(R.id.text) CheckedTextView name;
        public ViewHolder(View view) { ButterKnife.inject(this, view); }
    }

    public TodoCursorAdapter(Context context) { super(context, null, false); }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        Todo todo = cupboard().withCursor(cursor).get(Todo.class);
        holder.name.setText(todo.getText());
        holder.name.setChecked(todo.isChecked());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.todo_cell, parent, false);
        ButterKnife.inject(this, view);
        view.setTag(new ViewHolder(view));

        return view;
    }

}
