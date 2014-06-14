package com.powerje.todo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.powerje.todo.data.models.Todo;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by jep on 6/13/14.
 */

public class CupboardSqlLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    static {
        // register our models
        cupboard().register(Todo.class);
    }

    public CupboardSqlLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.db = db;
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work
    }

    public void addTodo(Todo todo) { cupboard().withDatabase(db).put(todo); }
    public void removeTodo(Todo todo) { cupboard().withDatabase(db).delete(todo); }}