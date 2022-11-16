package com.itel316.olbooks.helpers;

import com.itel316.olbooks.models.Book;
import com.itel316.olbooks.models.User;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class OlbookUtils {

    public static final String EMAIL = "itelrecov01@gmail.com";
    public static final String APPMAILPASS = "asexzlpeuxuefbai";
    public static final String HTML_NewVerification = "<!DOCTYPE html><html lang=\"en\">  <head>    <meta charset=\"UTF-8\" />    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />    <title>Document</title>    <style>        .font-lato {            font-family: 'Lato', sans-serif;        }        .font-montserrat{            font-family: 'Montserrat', sans-serif        }        .text-col{             color : #473766        }        .justify{            text-align: justify;        }    </style>    <link    href=\"https://fonts.googleapis.com/css?family=Lato:400,700&display=swap\"    rel=\"stylesheet\"    type=\"text/css\"  />  <link    href=\"https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap\"    rel=\"stylesheet\"    type=\"text/css\"  />  </head>  <body style=\"background-color: rgb(249, 244, 251);\">    <center>        <div style=\"background-color: white; padding : 1rem; border-radius: 1rem; width: calc(560px);\">            <div style=\"background-color: rgb(247, 249, 250); padding:1rem; border-radius: 1rem; width : calc(520px);\">                <table        cellpadding=\"0\"        cellspacing=\"0\"        border=\"0\"        width=\"100%\"        style=\"          width: calc(500px);          border-radius: 1rem;          border-collapse: collapse;          margin: 0 auto;          background-color: white;          padding: 0;          overflow: hidden;        \"      >        <tbody>          <tr style=\"border-collapse: collapse; margin: 0; padding: 0\">            <td              width=\"100%\"              style=\"border-collapse: collapse; margin: 0; padding: 0\"            >              <table                width=\"100%\"                cellpadding=\"0\"                cellspacing=\"0\"                border=\"0\"                style=\"                  min-width: 100%;                  border-collapse: collapse;                  margin: 0;                  padding: 0;                \"              >                <tbody>                  <tr style=\"font-family: helvetica, sans-serif;                  font-weight: 100;                  text-decoration: none;                  color: #333333;                  font-size: 24px;                  border-collapse: collapse; margin: 0; padding: 0\">                    <td                      width=\"100%\"                      style=\"                        min-width: 100%;                        border-collapse: collapse;                        margin: 0;                        padding: 0;                      \"                    >                        <div style=\"margin: 0px 0px 30px 0px\">                            <div style=\"margin: 0px 0px 30px 0px\">                                <img src=\"https://cdn.discordapp.com/attachments/955281529481883729/1042356186672009246/logo.png\"                                 style=\"width: 100%;\" ></img>                            </div>                            <div style=\"margin : 0px 20px 0px 20px\">                                <p class=\"text-col font-lato\" style=\"font-size: 1.5rem; font-weight: bold; \"><span style=\"font-weight: lighter;\">Hi ${name},</p>                                <p class=\"text-col  font-monsterrat justify\" style=\"font-size: 1rem; line-height: 2rem; font-weight: 500;\">Thank you for signing up to OlBooks, to complete your sign up, here is your verification code. </p>                                <div class=\"font-monsterrat\" style=\"background-color:rgb(237, 237, 237); padding : 1rem; font-size: 1rem; font-weight: 400;\">                                    <p>${verification}</p>                                </div>                                <p class=\"text-col font-monsterrat justify\" style=\"font-size: 1rem; line-height: 2rem; font-weight: 500;\">Have a great day!</p>                            </div>                        </div>                    </td>                  </tr>                </tbody>              </table>            </td>          </tr>        </tbody>                </table>                <center>                    <table                      cellpadding=\"0\"                      cellspacing=\"0\"                      style=\"                        border-collapse: collapse;                        font-size: 13px;                        font-family: helvetica, sans-serif;                        line-height: 30px;                        border-spacing: 0px;                        width: 100%;                        margin-top: 2rem;                        width: calc(340px);                        padding: 0;                      \"                    >                      <tbody>                        <tr                          style=\"                            border-collapse: collapse;                            margin: 0;                          \"                        >                          <td                            align=\"center\"                            style=\"                              border-collapse: collapse;                              margin: 0;                              padding: 0;                            padding: 5;                            border-radius: 2%;                            \"                          >                                                          <p                                style=\"                                  font-family: helvetica, sans-serif;                                  text-decoration: none;                                  color: #383838;                                  font-size: 13px;                                  opacity: 0.7;                                  line-height: 1.5rem;                                  margin: 0;                                  padding: 0;                                \"                              >                              </p>                                                            </p>                          </td>                        </tr>                        <tr                          style=\"                            border-collapse: collapse;                            margin: 0;                            padding: 0;                          \"                        >                          <td                            align=\"center\"                            width=\"50\"                            height=\"50\"                            style=\"                              border-collapse: collapse;                              margin: 0;                              padding: 0;                            \"                          >                            <img                              src=\"https://cdn.discordapp.com/attachments/955281529481883729/1042356186672009246/logo.png\"                              alt=\"hero-image\"                              width=\"200\"                            />                          </td>                        </tr>                        <tr                          style=\"                            border-collapse: collapse;                            margin: 0;                          \"                        >                          <td                            align=\"center\"                            style=\"                              border-collapse: collapse;                              margin: 0;                              padding: 0;                            padding: 5;                            border-radius: 2%;                            \"                          >                                                          <p                              class=\"text-col font-lato\"                                style=\"                                  text-decoration: none;                                  font-size: 13px;                                  opacity : 0.7;                                  font-weight: 500;                                  color : #333333;                                  margin: 0;                                  padding: 0;                                \"                              >                              OlBooks • Metro Manila, Philippines</p>                                                            </p>                          </td>                        </tr>                                                                      </tbody>                    </table>                </center>            </div>        </div>    </center>  </body></html>";

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

    public static String shortenNumber(int number) {
        String given = String.format("%d", number);
        String ret = "";
        if (given.length() >= 10) {
            int end = given.length() - 1;
            ret += given.substring(0, given.length() - 9) + "." + given.substring(end, end + 1);
            ret += "B";
        } else if (given.length() >= 7) {
            int end = given.length() - 6;
            ret += given.substring(0, given.length() - 6) + "." + given.substring(end, end + 1);
            ret += "M";
        } else if (given.length() >= 4) {
            int end = given.length() - 3;
            ret += given.substring(0, given.length() - 3) + "." + given.substring(end, end + 1);
            ret += "K";
        } else {
            ret = given;
        }
        return ret;
    }

    public static boolean doesBookAdded(User user, Book book) {
        for (Book BOOK : user.bookList.getBooks())
            if (BOOK.getIsbn_10().equals(book.getIsbn_10())) return true;
        return false;
    }

    public static String randomKey(int length) {
        String genCars = "";

        int nums[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        while (length > 0) {
            genCars += nums[randomNum()];
            length--;
        }

        return genCars;
    }

    public static int randomNum() {
        return new Random().nextInt(9);
    }
}
