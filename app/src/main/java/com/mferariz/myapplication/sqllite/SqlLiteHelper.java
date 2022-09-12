package com.mferariz.myapplication.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String  DATABASE_NAME = "dbPractice";
    private static final String TABLE_SESSION = "session";

    public static String getTableSession() {
        return TABLE_SESSION;
    }

    public SqlLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_SESSION + "(" +
                "id integer primary key autoincrement," +
                "email text not null," +
                "password text not null," +
                "token text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_SESSION);
        onCreate(sqLiteDatabase);
    }
}
