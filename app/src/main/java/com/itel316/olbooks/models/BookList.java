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

        // select bs.bookId as b_id, BS.title as title, BS.author as author from booklist as bs right join book as BS on BS.bookId = bs.bookId where bs.userId = 1;
        // pubDate, dateAdded, pdfFile, int save_count, like_count

        Cursor myList = dbHelper.execRawQuery(String.format(
                "select bs.bookId as bookId, BS.isbn_10 as isbn_10, BS.isbn_13 as isbn_13," +
                        " BS.title as title, BS.author as author, BS.category as category, " +
                        "BS.description as description, BS.pubDate as pubDate, BS.dateAdded as dateAdded, " +
                        "BS.pdfFile as pdfFile, BS.save_count as save_count, BS.save_count as like_count " +
                        "from booklist as bs inner join book as BS on BS.bookId = bs.bookId where bs.userId=%d",
                userId
        ), null);

        this.books = new Book[myList.getCount()];

        for (int x = 0; x < this.books.length; x++) {
            this.books[x] = new Book(
                    myList.getString(1),
                    myList.getString(2),
                    myList.getString(3),
                    myList.getString(4),
                    myList.getString(5),
                    myList.getString(6),
                    myList.getString(7),
                    myList.getString(8),
                    myList.getString(9),
                    myList.getInt(10),
                    myList.getInt(11)
            );
        }
    }
}
