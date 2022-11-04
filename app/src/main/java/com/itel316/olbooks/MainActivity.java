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
import com.itel316.olbooks.models.User;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btn_sign_in, btn_sign_up;
    private LinearProgressIndicator progress;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        progress = findViewById(R.id.loader);

        btn_sign_in.setVisibility(View.GONE);
        btn_sign_up.setVisibility(View.GONE);

        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
        dbhelper.dropDbs(dbhelper.getWritableDatabase(), new String[]{});

        dbhelper.checkTableExist();

        dbhelper.insertUser("jsparagas1@gmail.com", "Jerbee", "Paragas", AuthHelper.hashPassword("helloworld"), 0, 0, new Date());
//        dbhelper.insertBook("0000000001", "0000000000001", "Java Programming", "Joyce Farrell", "Coding<~>Programming<~>Java", "Discover the power of Java� for developing applications today when you trust the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 9E. Even if you're a first-time programmer, JAVA PROGRAMMING can show you how to quickly start developing useful programs, all while still mastering the basic principles of structured and object-oriented programming. Unique, reader-friendly explanations and meaningful programming exercises emphasize business applications and game creation while useful debugging exercises and contemporary case problems further expand your understanding. Additional digital learning resources within MindTap provide interactive learning tools as well as coding IDE (Integrated Development Environment) labs for practicing and expanding your skills.", User.toISODateString(new Date()), User.toISODateString(new Date()), "javajoyce.pdf", new Random().nextInt(40000), new Random().nextInt(40000));
//        dbhelper.insertBook("0000000002", "0000000000002", "Java Programming 2", "Joyce Farrell", "Coding<~>Programming<~>Java", "Discover the power of Java� for developing applications today when you trust the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 9E. Even if you're a first-time programmer, JAVA PROGRAMMING can show you how to quickly start developing useful programs, all while still mastering the basic principles of structured and object-oriented programming. Unique, reader-friendly explanations and meaningful programming exercises emphasize business applications and game creation while useful debugging exercises and contemporary case problems further expand your understanding. Additional digital learning resources within MindTap provide interactive learning tools as well as coding IDE (Integrated Development Environment) labs for practicing and expanding your skills.", User.toISODateString(new Date()), User.toISODateString(new Date()), "javajoyce.pdf", new Random().nextInt(40000), new Random().nextInt(40000));
//        dbhelper.insertBook("0000000003", "0000000000003", "Java Programming 3", "Joyce Farrell", "Coding<~>Programming<~>Java", "Discover the power of Java� for developing applications today when you trust the engaging, hands-on approach in Farrell's JAVA PROGRAMMING, 9E. Even if you're a first-time programmer, JAVA PROGRAMMING can show you how to quickly start developing useful programs, all while still mastering the basic principles of structured and object-oriented programming. Unique, reader-friendly explanations and meaningful programming exercises emphasize business applications and game creation while useful debugging exercises and contemporary case problems further expand your understanding. Additional digital learning resources within MindTap provide interactive learning tools as well as coding IDE (Integrated Development Environment) labs for practicing and expanding your skills.", User.toISODateString(new Date()), User.toISODateString(new Date()), "javajoyce.pdf", new Random().nextInt(40000), new Random().nextInt(40000));
//        dbhelper.insertToBookList(User.toISODateString(new Date()), 1, "0000000002", "0000000000002");
//        dbhelper.insertToBookList(User.toISODateString(new Date()), 1, "0000000002", "0000000000002");

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
                progress.setVisibility(View.GONE);
                btn_sign_in.setVisibility(View.VISIBLE);
                btn_sign_up.setVisibility(View.VISIBLE);
            }, 2000);


        btn_sign_in.setOnClickListener(e -> {
            Intent SignInIntent = new Intent(getApplicationContext(), Activity_Sign_In.class);
            startActivity(SignInIntent);
            finish();
        });

        btn_sign_up.setOnClickListener(e -> {
            Intent signupIntent = new Intent(getApplicationContext(), Activity_Sign_Up.class);
            startActivity(signupIntent);
            finish();
        });

    }
}