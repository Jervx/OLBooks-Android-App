package com.itel316.olbooks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itel316.olbooks.models.Book;

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
        String checkBookList = "CREATE TABLE IF NOT EXISTS booklist ( bookListId INTEGER PRIMARY KEY AUTOINCREMENT, dateAdded TEXT, userId INTEGER, isbn_10 TEXT, isbn_13 TEXT );";
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

    public boolean dropDbs(SQLiteDatabase db, String[] dbNames) {
        for (String dbName : dbNames)
            db.execSQL(String.format("drop Table if exists %s", dbName));
        return true;
    }

    public Boolean insertUser(String email, String fname, String lname, String password, int isLoggedIn, int role, Date dateJoined) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundUser = getUser(String.format("SELECT * FROM user WHERE email='%s';", email), null);

            if (foundUser.getCount() > 0) {
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
            ContentValues values = new ContentValues();
            values.put("isLoggedIn",toState);
            db.update("user", values, "userId = "+userId, null);
            Cursor users = db.rawQuery("SELECT * FROM user where userId = "+userId, null);
        } catch (Exception e) {
        }
    }

    public Cursor getUser(String query, String[] args) {
        Cursor result = null;
        try {
            result = this.getWritableDatabase().rawQuery(query, args);
        } catch (Exception e) {
            System.out.println("ERR SQL GET USER: " + e.toString());
        }
        return result;
    }

    // BOOKS

    public Boolean insertBook(String isbn_10, String isbn_13, String title, String author, String category, String description, String pubDate, String dateAdded, String pdfFile, int save_count, int like_count) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundBook = getUser(String.format("SELECT * FROM book WHERE isbn_10='%s';", isbn_10), null);

            if (foundBook.getCount() > 0) {
                System.out.println("Book already exist");
                return false;
            }

            ContentValues values = new ContentValues();

//            isbn_10, isbn_13, title, author , category, description, pubDate, dateAdded, pdfFile, save_count, like_count
            values.put("isbn_10", isbn_10);
            values.put("isbn_13", isbn_13);
            values.put("title", title);
            values.put("author", author);
            values.put("category", category);
            values.put("description", description);
            values.put("pubDate", pubDate);
            values.put("dateAdded", dateAdded);
            values.put("pdfFile", pdfFile);
            values.put("save_count", save_count);
            values.put("like_count", like_count);

            db.insert("book", null, values);

            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public Book [] getBooks(){
        Cursor bookResult = execRawQuery("SELECT * FROM book", null);
        Book [] books = new Book[bookResult.getCount()];

        int x = 0;
        while (bookResult.moveToNext()) {
            books[x] = new Book(
                    bookResult.getString(0),
                    bookResult.getString(1),
                    bookResult.getString(2),
                    bookResult.getString(3),
                    bookResult.getString(4),
                    bookResult.getString(5),
                    bookResult.getString(6),
                    bookResult.getString(7),
                    bookResult.getString(8),
                    bookResult.getInt(9),
                    bookResult.getInt(10)
            );
            x++;
        }

        return books;
    }

    // BOOKLIST
    //dateAdded, userId, isbn_10, isbn_13
    public Boolean insertToBookList(String dateAdded, int userId, String isbn_10, String isbn_13) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundBook = getUser(String.format("SELECT * FROM booklist WHERE userId='%d' AND isbn_10='%s' AND isbn_13='%s';", userId, isbn_10, isbn_13), null);

            if (foundBook.getCount() > 0) {
                System.out.println("Book already added");
                return false;
            }

            ContentValues values = new ContentValues();

//          dateAdded, userId, isbn_10, isbn_13
            values.put("dateAdded", dateAdded);
            values.put("userId", userId);
            values.put("isbn_10", isbn_10);
            values.put("isbn_13", isbn_13);

            db.insert("booklist", null, values);

            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public Cursor execRawQuery(String query, String[] args) {
        Cursor result = null;
        try {
            result = this.getWritableDatabase().rawQuery(query, args);
        } catch (Exception e) {
            System.out.println("Err Execute Query : " + e.toString());
        }
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