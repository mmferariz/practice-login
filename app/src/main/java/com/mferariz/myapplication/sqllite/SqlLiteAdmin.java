package com.mferariz.myapplication.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mferariz.myapplication.model.Session;

public class SqlLiteAdmin {

    private SQLiteDatabase db;

    public SqlLiteAdmin(Context context) {
        SqlLiteHelper sql = new SqlLiteHelper(context);
        db = sql.getWritableDatabase();
    }

    public Long saveSession(String email, String password, String token){
        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("password", password);
        values.put("token", token);

        Long idRes = db.insert(SqlLiteHelper.getTableSession(), "id", values);
        return idRes;
    }

    public int clearSession(){
        return db.delete(SqlLiteHelper.getTableSession(), "", null);
    }

    public Session getSession() {
        Cursor cursor = db.rawQuery("select * from " + SqlLiteHelper.getTableSession(), null);

        if(cursor.getCount() == 1){
            if(cursor.moveToFirst()){
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String token = cursor.getString(cursor.getColumnIndexOrThrow("token"));
                return new Session(id, email, password, token);
            }
        }
        return null;
    }
}
