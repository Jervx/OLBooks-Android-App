package com.itel316.olbooks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "LibMa.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void checkTableExist() {
        SQLiteDatabase db = this.getWritableDatabase();
        String checkUserTable = "CREATE TABLE IF NOT EXISTS user ( userId INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, fname TEXT, lname TEXT, password TEXT, role TEXT, isLoggedIn INTEGER, datejoined TEXT );";
        String checkBookTable = "CREATE TABLE IF NOT EXISTS book ( ISBN_10 TEXT PRIMARY KEY, ISBN_13 TEXT, title TEXT, author TEXT, category TEXT, description TEXT, pubDate TEXT, dateAdded TEXT, pdfFile TEXT, save_count INTEGER, like_count INTEGER);";
        String checkBookList = "CREATE TABLE IF NOT EXISTS booklist ( bookListId INTEGER PRIMARY KEY AUTOINCREMENT, dateAdded TEXT, userId INTEGER, ISBN TEXT );";
        String checkRecover = "CREATE TABLE IF NOT EXISTS recovery( recoveryId INTEGER PRIMARY KEY AUTOINCREMENT, requestDate TEXT, userId INTEGER );";

        db.execSQL(checkUserTable);
        db.execSQL(checkBookTable);
        db.execSQL(checkBookList);
        db.execSQL(checkRecover);
    }

    public boolean dropDb(SQLiteDatabase db, String dbName) {
        db.execSQL(String.format("drop Table if exists %s", dbName));
        return true;
    }

    public boolean dropDbs(SQLiteDatabase db, String [] dbNames) {
        for(String dbName : dbNames)
            db.execSQL(String.format("drop Table if exists %s", dbName));
        return true;
    }

    public Boolean insertUser(String email, String fname, String lname, String password, int isLoggedIn, int role, Date dateJoined) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundUser = getUser(String.format("SELECT * FROM user WHERE email='%s';", email), null);

            if(foundUser.getCount() > 0) {
                System.out.println("User already exist");
                return false;
            }

            ContentValues values = new ContentValues();

            values.put("email", email);
            values.put("fname", fname);
            values.put("lname", lname);
            values.put("password", password);
            values.put("role", role);
            values.put("isLoggedIn", isLoggedIn);
            values.put("dateJoined", formatDateTime(dateJoined));

            db.insert("user", null, values);

            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public void updateUserLoginState(int userId, int toState) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(String.format("update user set isLoggedIn = %d where userId = %d", toState, userId));
        }catch (Exception e){  }
    }

    public Cursor getUser(String query, String[] args) {
        Cursor result = null;
        try{
            result = this.getWritableDatabase().rawQuery(query, args);
        }catch (Exception e){ System.out.println("ERR SQL GET USER: "+e.toString()); }
        return result;
    }

    public Cursor execRawQuery(String query, String[] args) {
        Cursor result = null;
        try{
            result = this.getWritableDatabase().rawQuery(query, args);
        }catch (Exception e){ System.out.println("Err Execute Query : "+e.toString()); }
        return result;
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String ISO = sdf.format(date);
        System.out.println(ISO);
        return ISO;
    }
}