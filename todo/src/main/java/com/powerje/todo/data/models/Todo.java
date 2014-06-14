package com.powerje.todo.data.models;

import android.os.Parcel;

import com.powerje.todo.reminders.TodoNotificationUtils;

/**
 * Created by jep on 6/7/14.
 */
public class Todo implements TodoNotificationUtils.TodoNotification, android.os.Parcelable {
    private Long _id;
    private boolean checked;
    private String text;

    public Todo(String text) {
        this.text = text;
    }

    public void toggleChecked() { checked = !checked; }

    public Long getId() { return _id; }
    public boolean isChecked() { return checked; }
    public String getText() { return text; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this._id);
        dest.writeByte(checked ? (byte) 1 : (byte) 0);
        dest.writeString(this.text);
    }

    private Todo(Parcel in) {
        this._id = in.readLong();
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
