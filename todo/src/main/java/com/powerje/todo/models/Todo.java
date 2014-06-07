package com.powerje.todo.models;

import com.powerje.todo.reminders.TodoNotificationUtils;

/**
 * Created by jep on 6/7/14.
 */
public class Todo implements TodoNotificationUtils.TodoNotification {
    private int id;
    private boolean checked;
    private String text;

    public Todo(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public void toggleChecked() { checked = !checked; }

    public int getId() { return id; }
    public boolean isChecked() { return checked; }
    public String getText() { return text; }
}
