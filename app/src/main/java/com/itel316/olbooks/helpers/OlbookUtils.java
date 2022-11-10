package com.itel316.olbooks.helpers;

import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

public class OlbookUtils {

    public static String toISODateString(Date date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String ISO = sdf.format(date);
        return ISO;
    }

    public static Date fromIoDateStringToDate(String ISODateString) {
        return Date.from(ZonedDateTime.parse(ISODateString).toInstant());
    }

    public static String shortenNumber(int number){
        String given = String.format("%d", number);
        String ret = "";
        if(given.length() >= 10){
            int end = given.length() - 1;
            ret += given.substring(0, given.length() - 9) +"."+given.substring(end, end + 1);
            ret += "B";
        }else if(given.length() >= 7){
            int end = given.length() - 6;
            ret +=  given.substring(0, given.length() - 6) +"."+given.substring(end, end + 1);
            ret += "M";
        }else if(given.length() >= 4){
            int end = given.length() - 3;
            ret +=  given.substring(0, given.length() - 3) +"."+given.substring(end, end+1);
            ret += "K";
        }else{
            ret = given;
        }
        return ret;
    }

    public static boolean doesBookAdded(User user, Book book){
        for(Book BOOK : user.bookList.getBooks())
            if( BOOK.getIsbn_10().equals(book.getIsbn_10()) ) return true;
        return false;
    }
}
