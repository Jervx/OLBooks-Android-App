package com.itel316.olbooks.models;

import android.database.Cursor;

import com.itel316.olbooks.helpers.DatabaseHelper;

import java.io.Serializable;

public class Book implements Serializable {

    String photo, isbn_10, isbn_13, title, author, category, description, pubDate, dateAdded, pdfFile;
    int save_count, like_count;

    public Book(String photo, String isbn_10, String isbn_13, String title, String author, String category, String description, String pubDate, String dateAdded, String pdfFile, int save_count, int like_count) {
        this.photo = photo;
        this.isbn_10 = isbn_10;
        this.isbn_13 = isbn_13;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        this.pubDate = pubDate;
        this.dateAdded = dateAdded;
        this.pdfFile = pdfFile;
        this.save_count = save_count;
        this.like_count = like_count;
    }

    public String getPhoto() {
        return photo;
    }

    public void fetchSelf(DatabaseHelper dbHelper){
        Cursor myList = dbHelper.execRawQuery(String.format("SELECT B.photo, B.isbn_10, B.isbn_13, B.title, B.author , B.category, B.description, B.pubDate, B.dateAdded, B.pdfFile, B.save_count, B.like_count from book as B WHERE B.isbn_10='%s'", this.getIsbn_10()), null);

        while (myList.moveToNext()) {
            this.photo = myList.getString(0);
            this.isbn_10 = myList.getString(1);
            this.isbn_13 = myList.getString(2);
            this.title = myList.getString(3);
            this.author = myList.getString(4);
            this.category = myList.getString(5);
            this.description = myList.getString(6);
            this.pubDate = myList.getString(7);
            this.dateAdded = myList.getString(8);
            this.pdfFile = myList.getString(9);
            this.save_count = myList.getInt(10);
            this.like_count = myList.getInt(11);
            System.out.println("FOUND "+this.toString());
        }
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIsbn_10() {
        return isbn_10;
    }

    public String getIsbn_13() {
        return isbn_13;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public int getSave() {
        return save_count;
    }

    public int getLikes() {
        return like_count;
    }

    @Override
    public String toString() {
        return "Book{" +
                "photo='" + photo + '\'' +
                ", isbn_10='" + isbn_10 + '\'' +
                ", isbn_13='" + isbn_13 + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                ", pdfFile='" + pdfFile + '\'' +
                ", save_count=" + save_count +
                ", like_count=" + like_count +
                '}';
    }
}
