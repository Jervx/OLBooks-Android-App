package com.itel316.olbooks.models;

import com.itel316.olbooks.helpers.*;

import android.database.Cursor;

import java.io.Serializable;

public class BookList implements Serializable {

    int userId;
    Book books[];

    public BookList(int userId, DatabaseHelper dbHelper) {
        this.userId = userId;
        loadBooks(userId, dbHelper);
    }

    public void loadBooks(int userId, DatabaseHelper dbHelper) {
        Cursor myList = dbHelper.execRawQuery(String.format("SELECT B.isbn_10, B.isbn_13, B.title, B.author , B.category, B.description, B.pubDate, B.dateAdded, B.pdfFile, B.save_count, B.like_count from booklist as BL INNER JOIN book as B on BL.isbn_10 = B.isbn_10 WHERE BL.userId=%d", userId), null);

        int foundBooksCount = myList.getCount();

        if(foundBooksCount > 0) this.books = new Book[foundBooksCount];
        else this.books = new Book[0];

        int x = 0;
        while (myList.moveToNext()) {
            this.books[x] = new Book(
                    myList.getString(0),
                    myList.getString(1),
                    myList.getString(2),
                    myList.getString(3),
                    myList.getString(4),
                    myList.getString(5),
                    myList.getString(6),
                    myList.getString(7),
                    myList.getString(8),
                    myList.getInt(9),
                    myList.getInt(10)
            );
            x++;
        }
    }
}
