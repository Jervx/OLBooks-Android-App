package com.itel316.olbooks.models;

public class Book {

    String isbn_10, isbn_13, title, author, category, description, pubDate, dateAdded, pdfFile;
    int save_count, like_count;

    public Book(String isbn_10, String isbn_13, String title, String author, String category, String description, String pubDate, String dateAdded, String pdfFile, int save_count, int like_count) {
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
}
