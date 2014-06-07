package com.powerje.todo.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.powerje.todo.R;
import com.powerje.todo.models.Todo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jep on 6/7/14.
 */
public class TodoAdapter extends BaseAdapter {

    private List<Todo> todos = new ArrayList<>();

    public void addTodo(Todo todo) {
        todos.add(todo);
        this.notifyDataSetChanged();
    }

    public List<Todo> getTodos() {
        // TODO: defensively copy this so as not to be a caveman
        return todos;
    }

    @Override
    public int getCount() { return todos.size(); }

    @Override
    public Todo getItem(int position) { return todos.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.todo_cell, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        Todo todo = getItem(position);
        holder.name.setText(todo.getText());

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.text)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
