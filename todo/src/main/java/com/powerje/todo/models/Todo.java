package com.powerje.todo.models;

import android.os.Parcel;

import com.powerje.todo.reminders.TodoNotificationUtils;

/**
 * Created by jep on 6/7/14.
 */
public class Todo implements TodoNotificationUtils.TodoNotification, android.os.Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.text);
    }

    private Todo(Parcel in) {
        this.id = in.readInt();
        this.checked = in.readByte() != 0;
        this.text = in.readString();
    }

    public static Creator<Todo> CREATOR = new Creator<Todo>() {
        public Todo createFromParcel(Parcel source) {
            return new Todo(source);
        }

        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };
}
