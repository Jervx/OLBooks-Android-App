package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
//      dbhelper.dropDbs(dbhelper.getWritableDatabase(), new String [] {"user"});
        dbhelper.checkTableExist();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        // INSERT BOOK
        try {
            dbhelper.insertBook("img_java_programming_8th_edition_2015_joyce", "1285856910", "978-1285856919", "Java Programming 8th Edition", "Joyce Farrell", "Coding<~>Programming<~>Java<~>Code", "Discover the power of Java for developing applications with the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 8E. With this book, even first-time programmers can quickly develop useful programs while learning the basic principles of structured and object-oriented programming.<~> The text incorporates the latest version of Java with a reader-friendly presentation and meaningful real-world exercises that highlight new Java strengths. Updated Programming Exercises and a wealth of case problems help you build skills critical for ongoing programming success.<~> You can find additional tools to strengthen your Java programming success with the optional CourseMate that includes a wealth of interactive teaching and learning tools and unique Video Quizzes created by the book's author.", OlbookUtils.toISODateString(format.parse("23/01/2015")), OlbookUtils.toISODateString(new Date()), "pdf_java_programming_8th_edition_2015_joyce.pdf", new Random().nextInt(40000), /*new Random().nextInt(40000)*/ 0);
            dbhelper.insertBook("processing1251278501uigt1hg8912", "0123736021", "978-0123736024", "Learning Processing: A Beginner's Guide to Programming Images, Animation, and Interaction", "Daniel Shiffman", "Programming<~>Coding<~>Processing<~>Graphics<~>Animation<~>Pixels<~>Patterns", "The free, open-source Processing programming language environment was created at MIT for people who want to develop images, animation, and sound. Based on the ubiquitous Java, it provides an alternative to daunting languages and expensive proprietary software.<~> This book gives graphic designers, artists and illustrators of all stripes a jump start to working with processing by providing detailed information on the basic principles of programming with the language, followed by careful, step-by-step explanations of select advanced techniques.<~> The author teaches computer graphics at NYU's Tisch School of the Arts, and his book has been developed with a supportive learning experience at its core. From algorithms and data mining to rendering and debugging, it teaches object-oriented programming from the ground up within the fascinating context of interactive visual media.<~> Previously announced as \"Pixels, Patterns, and Processing\"", OlbookUtils.toISODateString(format.parse("02/09/2008")), OlbookUtils.toISODateString(new Date()), "processing3y1t98h1th19ga.pdf", 4521, 3234);

            //TODO HERE INSERT BOOKS
//            dbhelper.insertBook("", "", "", "", "", "NAT GEO<~>Nature<~>Bird", "Description here", OlbookUtils.toISODateString(format.parse("31/04/2018")), OlbookUtils.toISODateString(new Date()), "pdffilename.pdf", 61400, 7234);
        } catch (Exception e) {
            System.out.println("ERR " + e);
        }

        Cursor result = dbhelper.getUser("select * from user where isLoggedIn = 1", null);

        if (result != null && result.getCount() > 0)
            handler.postDelayed(() -> {
                Intent homeIntent = new Intent(getApplicationContext(), Activity_Home.class);
                result.moveToNext();
                User user = new User(result.getString(1), dbhelper);
                System.out.println("CUR USER :"+user.toString());
                homeIntent.putExtra("CURRENT_USER", user);
                startActivity(homeIntent);
                finish();
            }, 2000);
        else
            handler.postDelayed(() -> {
                Intent SignInIntent = new Intent(getApplicationContext(), Activity_Sign_In.class);
                startActivity(SignInIntent);
                finish();
            }, 2000);
    }
}