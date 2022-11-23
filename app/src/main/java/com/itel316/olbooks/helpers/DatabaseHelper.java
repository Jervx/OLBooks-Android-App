package com.itel316.olbooks.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.itel316.olbooks.models.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String checkUserTable = "CREATE TABLE IF NOT EXISTS user ( userId INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, fname TEXT, lname TEXT, password TEXT, role TEXT, isLoggedIn INTEGER, datejoined TEXT, liked TEXT, img TEXT );";
        String checkBookTable = "CREATE TABLE IF NOT EXISTS book ( photo TEXT, ISBN_10 TEXT PRIMARY KEY, ISBN_13 TEXT, title TEXT, author TEXT, category TEXT, description TEXT, pubDate TEXT, dateAdded TEXT, pdfFile TEXT, save_count INTEGER, like_count INTEGER);";
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

    public boolean truncateDbs(SQLiteDatabase db, String[] dbNames) {
        for (String dbName : dbNames)
            db.execSQL(String.format("DELETE FROM %s", dbName));
        db.close();
        return true;
    }

    public Boolean insertUser(String email, String fname, String lname, String password, int isLoggedIn, int role, Date dateJoined) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundUser = getUser(String.format("SELECT * FROM user WHERE email='%s';", email), null);

            if (foundUser.getCount() > 0)
                return false;

            ContentValues values = new ContentValues();

            values.put("email", email);
            values.put("fname", fname);
            values.put("lname", lname);
            values.put("password", password);
            values.put("role", role);
            values.put("isLoggedIn", isLoggedIn);
            values.put("dateJoined", formatDateTime(dateJoined));
            values.put("liked", "");
            values.put("img", "");

            db.insert("user", null, values);
            db.close();
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
            db.close();
//            Cursor users = db.rawQuery("SELECT * FROM user where userId = "+userId, null);
        } catch (Exception e) {
        }
    }

    public Cursor getUser(String query, String[] args) {
        Cursor result = null;
        try {
            result = execRawQuery(query, args);
        } catch (Exception e) { System.out.println("ERR : "+e); }
        return result;
    }

    // BOOKS
    public Boolean insertBook(String photo, String isbn_10, String isbn_13, String title, String author, String category, String description, String pubDate, String dateAdded, String pdfFile, int save_count, int like_count) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor foundBook = getUser(String.format("SELECT * FROM book WHERE isbn_10='%s';", isbn_10), null);

            if (foundBook.getCount() > 0) {
                return false;
            }

            ContentValues values = new ContentValues();

//            isbn_10, isbn_13, title, author , category, description, pubDate, dateAdded, pdfFile, save_count, like_count
            values.put("photo", photo);
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
            db.close();
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
                    bookResult.getString(9),
                    bookResult.getInt(10),
                    bookResult.getInt(11)
            );
            x++;
        }

        return books;
    }

    public Book [] getBooksByTag(String [] tags){

        String likes = "";
        System.out.println(likes);
        int counter = 0;
        for(String tag : tags){
            System.out.println(tag);
            if(tag == null) break;
            String fields = "title like '%<~>%' or category like '%<~>%' or author like '%<~>%'";
            likes += fields.replaceAll("<~>", tag);
            if(counter != tags.length - 1) likes += " or ";
            counter ++;
        }

        String prepared = "SELECT * FROM book";

        if(tags.length > 0) prepared = String.format("SELECT * FROM book where %s", likes);
        System.out.println("PREP : "+ prepared);
        Cursor bookResult = execRawQuery(prepared, null);
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
                    bookResult.getString(9),
                    bookResult.getInt(10),
                    bookResult.getInt(11)
            );
            x++;
        }

        return books;
    }

    public ArrayList<Book> getSuggestedBook( int count ){
        SQLiteDatabase db = getWritableDatabase();

        String statement = "SELECT * FROM book ORDER BY RANDOM() LIMIT "+count+";";

        ArrayList<Book> bk = new ArrayList<>();

        Cursor bookResult = execRawQuery(statement, null);

        if(bookResult.getCount() > 0){
            while(bookResult.moveToNext()) {
                bk.add(new Book(
                        bookResult.getString(0),
                        bookResult.getString(1),
                        bookResult.getString(2),
                        bookResult.getString(3),
                        bookResult.getString(4),
                        bookResult.getString(5),
                        bookResult.getString(6),
                        bookResult.getString(7),
                        bookResult.getString(8),
                        bookResult.getString(9),
                        bookResult.getInt(10),
                        bookResult.getInt(11)
                ));
            }
        }

        return bk;
    }

    public ArrayList<Book> getMostViews( int count ){
        SQLiteDatabase db = getWritableDatabase();

        String statement = "SELECT * FROM book ORDER BY like_count DESC LIMIT "+count+";";

        ArrayList<Book> bk = new ArrayList<>();

        Cursor bookResult = execRawQuery(statement, null);

        if(bookResult.getCount() > 0){
            while(bookResult.moveToNext()) {
                bk.add(new Book(
                        bookResult.getString(0),
                        bookResult.getString(1),
                        bookResult.getString(2),
                        bookResult.getString(3),
                        bookResult.getString(4),
                        bookResult.getString(5),
                        bookResult.getString(6),
                        bookResult.getString(7),
                        bookResult.getString(8),
                        bookResult.getString(9),
                        bookResult.getInt(10),
                        bookResult.getInt(11)
                ));
            }
        }

        return bk;
    }

    public ArrayList<Book> getMostSaved( int count ){
        SQLiteDatabase db = getWritableDatabase();

        String statement = "SELECT * FROM book ORDER BY save_count DESC LIMIT "+count+";";

        ArrayList<Book> bk = new ArrayList<>();

        Cursor bookResult = execRawQuery(statement, null);

        if(bookResult.getCount() > 0){
            while(bookResult.moveToNext()) {
                bk.add(new Book(
                        bookResult.getString(0),
                        bookResult.getString(1),
                        bookResult.getString(2),
                        bookResult.getString(3),
                        bookResult.getString(4),
                        bookResult.getString(5),
                        bookResult.getString(6),
                        bookResult.getString(7),
                        bookResult.getString(8),
                        bookResult.getString(9),
                        bookResult.getInt(10),
                        bookResult.getInt(11)
                ));
            }
        }

        return bk;
    }


    public Book [] getBooksBySearch(String search){

        String likes = "";

        String fields = "title like '%<~>%%' or category like '%<~>%' or author like '%<~>%'";
        likes += fields.replaceAll("<~>", search);

        String prepared = String.format("SELECT * FROM book where %s", likes);

        Cursor bookResult = execRawQuery(prepared, null);
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
                    bookResult.getString(9),
                    bookResult.getInt(10),
                    bookResult.getInt(11)
            );
            x++;
        }

        return books;
    }


    // BOOKLIST
    //dateAdded, userId, isbn_10, isbn_13
    public Boolean insertToBookList(int newValue, String dateAdded, int userId, String isbn_10, String isbn_13) {
        try {
            this.getWritableDatabase().execSQL(String.format("UPDATE book set save_count=%d where isbn_10='%s'", newValue,isbn_10));

            System.out.printf("Added %s to booklist for user %d\n", isbn_10,userId);
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor foundBook = getUser(String.format("SELECT * FROM booklist WHERE userId='%d' AND isbn_10='%s' AND isbn_13='%s';", userId, isbn_10, isbn_13), null);

            if (foundBook.getCount() > 0) {
                return false;
            }

            ContentValues values = new ContentValues();

//          dateAdded, userId, isbn_10, isbn_13
            values.put("dateAdded", dateAdded);
            values.put("userId", userId);
            values.put("isbn_10", isbn_10);
            values.put("isbn_13", isbn_13);

            db.insert("booklist", null, values);
            db.close();
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean removeFromBookList(int newValue, int userId, String isbn_10){
        System.out.printf("Remove %s from booklist for user %d\n", isbn_10,userId);
        this.getWritableDatabase().execSQL(String.format("DELETE FROM booklist where isbn_10='%s' and userId=%d", isbn_10, userId));
        this.getWritableDatabase().execSQL(String.format("UPDATE book set save_count=%d where isbn_10='%s'", newValue,isbn_10));
        return true;
    }

    public Boolean likeBook( int newValue, String isbn_10){
        System.out.printf("Liked %s from booklist for user\n", isbn_10);
        this.getWritableDatabase().execSQL(String.format("UPDATE book set like_count=%d where isbn_10='%s'", newValue,isbn_10));
        return true;
    }

    public Cursor execRawQuery(String query, String[] args) {
        Cursor result = null;
        try {
            result = this.getWritableDatabase().rawQuery(query, args);
        } catch (Exception e) {
        }
        return result;
    }

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String ISO = sdf.format(date);
        return ISO;
    }
}