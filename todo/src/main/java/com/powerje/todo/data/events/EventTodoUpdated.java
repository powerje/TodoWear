package com.powerje.todo.data.events;

import com.powerje.todo.data.Todo;

/**
 * Created by jep on 6/19/14.
 */
public class EventTodoUpdated {
    public final Todo todo;
    public EventTodoUpdated(Todo todo) { this.todo = todo; }
}
