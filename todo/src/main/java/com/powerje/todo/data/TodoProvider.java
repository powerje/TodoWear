package com.powerje.todo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.powerje.todo.data.models.Todo;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by jep on 6/13/14.
 */
public class TodoProvider extends ContentProvider {
    public static String AUTHORITY = "com.powerje.todo.provider";
    public static final Uri TODO_URI = Uri.parse("content://" + AUTHORITY + "/todo");
    public static final Uri LIST_URI = Uri.parse("content://" + AUTHORITY + "/list");
    private static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private CupboardSqlLiteOpenHelper mDatabaseHelper;
    private SQLiteDatabase db;

    private static final int TODO = 0;
    private static final int LIST = 1;

    static {
        sMatcher.addURI(AUTHORITY, "book/#", TODO);
//        sMatcher.addURI(AUTHORITY, "list/#", LIST);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new CupboardSqlLiteOpenHelper(getContext());
        return mDatabaseHelper != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        switch (sMatcher.match(uri)) {
            case TODO:
                return cupboard().withDatabase(db).query(Todo.class).
                        byId(ContentUris.parseId(uri)).
                        getCursor();
        }
        return null;
    }

    @Override public String getType(Uri uri) { return null; }
    @Override public Uri insert(Uri uri, ContentValues values) { return null; }
    @Override public int delete(Uri uri, String selection, String[] selectionArgs) { return 0; }
    @Override public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) { return 0; }

    public void addTodo(Todo todo) { cupboard().withDatabase(db).put(todo); }
    public void removeTodo(Todo todo) { cupboard().withDatabase(db).delete(todo); }
}
