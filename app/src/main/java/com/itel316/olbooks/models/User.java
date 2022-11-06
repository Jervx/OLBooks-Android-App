package com.itel316.olbooks.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import com.itel316.olbooks.helpers.*;
import com.itel316.olbooks.helpers.DatabaseHelper;

public class User implements Serializable {

    private int userId;
    private String email;
    private String fname;
    private String lname;
    private String password;
    private int role;
    private int isLoggedIn;
    private Date dateJoined;
    private BookList bookList;

    public User(String email, DatabaseHelper dbHelper) {
        this.email = email;
        fetchSelf(dbHelper);
    }

    public void fetchSelf(DatabaseHelper dbhelper) {
        Cursor findUser = dbhelper.getUser(String.format("SELECT * FROM user WHERE email = '%s';", email), null);
        if (findUser == null || findUser.getCount() == 0 || !findUser.moveToNext()) return;
        setUserId(findUser.getInt(0));
        setEmail(findUser.getString(1));
        setFname(findUser.getString(2));
        setLname(findUser.getString(3));
        setPassword(findUser.getString(4));
        setIsLoggedIn(findUser.getInt(5));
        this.bookList = getBookList(dbhelper);
    }

    public BookList getBookList(DatabaseHelper dbHelper) {
        return new BookList(this.userId, dbHelper);
    }

    public String getEmail() {
        return email;
    }

    public int getUserId() {
        return userId;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getPassword() {
        return password;
    }

    public int getRole() {
        return role;
    }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }


}
