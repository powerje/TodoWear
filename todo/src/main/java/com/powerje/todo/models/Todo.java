package com.powerje.todo.models;

import com.powerje.todo.reminders.TodoNotificationUtils;

/**
 * Created by jep on 6/7/14.
 */
public class Todo implements TodoNotificationUtils.TodoNotification {
    private boolean checked;
    private String text;

    public Todo(String text) { this.text = text; }

    public boolean isChecked() { return checked; }
    public void toggleChecked() { checked = !checked; }

    public String getText() { return text; }
}
