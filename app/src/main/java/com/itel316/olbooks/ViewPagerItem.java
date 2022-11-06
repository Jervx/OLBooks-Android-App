package com.itel316.olbooks;

import com.itel316.olbooks.models.Book;

public class ViewPagerItem {

    Book book;

    public ViewPagerItem(Book book) {
        this.book = book;
    }


    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
