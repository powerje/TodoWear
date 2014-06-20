package com.powerje.todo.data;

import com.powerje.todo.data.events.EventTodoUpdated;

import de.greenrobot.event.EventBus;

/**
 * Created by jep on 6/7/14.
 */
public class Todo {
    private Long _id;
    private boolean checked;
    private String text;

    public Todo() {} // Used by Cupboard
    public Todo(String text) { this.text = text; }


    public void toggleChecked() {
        checked = !checked;
        EventBus.getDefault().post(new EventTodoUpdated(this));
    }

    public Long getId() { return _id; }
    public boolean isChecked() { return checked; }
    public String getText() { return text; }

    public int hashCode() { return (getText() + getId()).hashCode(); }

    public boolean equals(Object obj) {
        if (!(obj instanceof Todo))
            return false;
        if (obj == this)
            return true;

        Todo other = (Todo) obj;
        return other.getId() == getId() &&
                other.isChecked() == isChecked() &&
                other.getText().equals(getText());
    }
}
