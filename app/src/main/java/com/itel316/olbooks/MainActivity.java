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
//        dbhelper.dropDbs(dbhelper.getWritableDatabase(), new String[]{""});
//        dbhelper.truncateDbs(dbhelper.getWritableDatabase(), new String[]{"user"});

        dbhelper.checkTableExist();

        // CREATE USER (dummy)
//        dbhelper.insertUser("jsparagas1@gmail.com", "Jerbee", "Paragas", AuthHelper.hashPassword("helloworld"), 0, 0, new Date());

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        // INSERT BOOK
        try{
            dbhelper.insertBook("img_java_programming_8th_edition_2015_joyce","1285856910", "978-1285856919", "Java Programming 8th Edition", "Joyce Farrell", "Coding<~>Programming<~>Java<~>Code", "Discover the power of Java for developing applications with the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 8E. With this book, even first-time programmers can quickly develop useful programs while learning the basic principles of structured and object-oriented programming.<~> The text incorporates the latest version of Java with a reader-friendly presentation and meaningful real-world exercises that highlight new Java strengths. Updated Programming Exercises and a wealth of case problems help you build skills critical for ongoing programming success.<~> You can find additional tools to strengthen your Java programming success with the optional CourseMate that includes a wealth of interactive teaching and learning tools and unique Video Quizzes created by the book's author.", OlbookUtils.toISODateString(format.parse("23/01/2015")), OlbookUtils.toISODateString(new Date()), "pdf_java_programming_8th_edition_2015_joyce.pdf", new Random().nextInt(40000), /*new Random().nextInt(40000)*/ 0);
            dbhelper.insertBook("img_java_programming_8th_edition_2015_joyce","1285856911", "978-1285856920", "Java Programming 7th Edition", "Joyce Farrell", "Coding<~>Programming<~>Java<~>Code", "Discover the power of Java for developing applications with the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 8E. With this book, even first-time programmers can quickly develop useful programs while learning the basic principles of structured and object-oriented programming.<~> The text incorporates the latest version of Java with a reader-friendly presentation and meaningful real-world exercises that highlight new Java strengths. Updated Programming Exercises and a wealth of case problems help you build skills critical for ongoing programming success.<~> You can find additional tools to strengthen your Java programming success with the optional CourseMate that includes a wealth of interactive teaching and learning tools and unique Video Quizzes created by the book's author.", OlbookUtils.toISODateString(format.parse("23/01/2015")), OlbookUtils.toISODateString(new Date()), "pdf_java_programming_8th_edition_2015_joyce.pdf", new Random().nextInt(40000), /*new Random().nextInt(40000)*/ 0);
//            dbhelper.insertBook("anmlwrld.jpg","0000000002", "0000000000002", "Animal World", "Jason Yu", "Animals<~>Nature<~>Forest<~>Sea", "Discover the wonders of nature that God gave us.", OlbookUtils.toISODateString(new Date()), OlbookUtils.toISODateString(new Date()), "AnimalWorld.pdf", new Random().nextInt(40000), new Random().nextInt(40000));
        }catch (Exception e){ System.out.println("ERR "+e);}

        // ADD BOOK TO USER BOOKLIST
//        dbhelper.insertToBookList(OlbookUtils.toISODateString(new Date()), 1, "0000000002", "0000000000002");
//        dbhelper.insertToBookList(OlbookUtils.toISODateString(new Date()), 1, "0000000002", "0000000000002");

        Cursor result = dbhelper.getUser("select * from user where isLoggedIn = 1", null);

        if (result != null && result.getCount() > 0)
            handler.postDelayed(() -> {
                Intent homeIntent = new Intent(getApplicationContext(), Activity_Home.class);
                result.moveToNext();
                User user = new User(result.getString(1), dbhelper);
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