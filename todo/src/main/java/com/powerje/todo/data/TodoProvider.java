package com.powerje.todo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.powerje.todo.data.events.EventTodoUpdated;
import com.powerje.todo.reminders.TodoNotificationUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by jep on 6/13/14.
 */
public class TodoProvider extends ContentProvider {
    public static String AUTHORITY = "com.powerje.todo.provider";
    public static final Uri TODO_URI = Uri.parse("content://" + AUTHORITY + "/todo");
    private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static TodoHelper mDatabaseHelper;

    private static final int TODO = 0;
    private static final int TODOS = 1;

    private static final Object LOCK = new Object();

    static {
        sMatcher.addURI(AUTHORITY, "todo/#", TODO);
        sMatcher.addURI(AUTHORITY, "todo", TODOS);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new TodoHelper(getContext());
        EventBus.getDefault().register(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        synchronized (LOCK) {
            Cursor c;
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            switch (sMatcher.match(uri)) {
                case TODO:
                    c = cupboard().withDatabase(db).query(Todo.class).
                            byId(ContentUris.parseId(uri)).
                            getCursor();
                    break;
                case TODOS:
                    // this is the full query syntax, most of the time you can leave out projection etc
                    // if the content provider returns a fixed set of data
                    c = cupboard().withDatabase(db).query(Todo.class).
                            withProjection(projection).
                            withSelection(selection, selectionArgs).
                            orderBy(sortOrder).
                            getCursor();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }
    }

    @Override public String getType(Uri uri) { return null; }

    @Override public Uri insert(Uri uri, ContentValues values) {
        synchronized (LOCK) {
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            Class clz;
            long id = Long.getLong(uri.getLastPathSegment(), 0);

            switch (sMatcher.match(uri)) {
                case TODO:
                case TODOS:
                    clz = Todo.class;
                    if (id == 0) {
                        id = cupboard().withDatabase(db).put(clz, values);
                    } else {
                        id = cupboard().withDatabase(db).update(clz, values);
                    }
                    Uri insert = Uri.parse(TODO_URI + "/" + id);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return insert;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (LOCK) {
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            switch (sMatcher.match(uri)) {
                case TODO:
                case TODOS:
                    int delete = cupboard().withDatabase(db).delete(Todo.class, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return delete;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (LOCK) {
            SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
            Class clz;
            switch (sMatcher.match(uri)) {
                case TODO:
                case TODOS:
                    clz = Todo.class;
                    int update = cupboard().withDatabase(db).update(clz, values, selection, selectionArgs);
                    getContext().getContentResolver().notifyChange(uri, null);
                    return update;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
        }
    }

    public void onEvent(EventTodoUpdated event) {
        ContentValues values = cupboard().withEntity(Todo.class).toContentValues(event.todo);
        if (getContext() != null) {
            getContext().getContentResolver().update(TODO_URI, values, "_id = ?", new String[]{"" + event.todo.getId()});
            TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(getContext()), getContext());
        }
    }

    public static void addTodo(Todo todo, Context context) {
       cupboard().withContext(context).put(TODO_URI, todo);
        TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(context), context);
    }

    public static void removeTodo(Todo todo, Context context)  {
        context.getContentResolver().delete(TODO_URI, "_id = ?", new String[]{"" + todo.getId()});
        TodoNotificationUtils.setupNotifications(TodoProvider.getTodos(context), context);
    }

    public static Todo getTodo(Long id, Context context) {
        Uri todoUri = ContentUris.withAppendedId(TODO_URI, id);
        return cupboard().withContext(context).get(todoUri, Todo.class);
    }

    public static Todo getTodo(Cursor c) {
        return cupboard().withCursor(c).get(Todo.class);
    }

    public static List<Todo> getTodos(Context context) {
        return cupboard().withDatabase(mDatabaseHelper.getWritableDatabase()).query(Todo.class).list();
    }
}
