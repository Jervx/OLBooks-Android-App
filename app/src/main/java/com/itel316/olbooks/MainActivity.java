package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.models.User;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button btn_sign_in, btn_sign_up;
    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        btn_sign_in.setVisibility(View.GONE);
        btn_sign_up.setVisibility(View.GONE);

        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());

//        dbhelper.dropDbs(dbhelper.getWritableDatabase(), new String[]{"user"});
          dbhelper.checkTableExist();
          dbhelper.insertUser("jsparagas1@gmail.com", "Jerbee", "Paragas", AuthHelper.hashPassword("helloworld"), 0, 0, new Date());
        Cursor result = dbhelper.getUser("select * from user where isLoggedIn=1", null);
        if (result != null && result.getCount() > 0)
            handler.postDelayed(() -> {
                Intent homeIntent = new Intent(getApplicationContext(), Activity_Home.class);
                result.moveToNext();
                User user = new User(result.getInt(0), result.getString(1), result.getString(2), result.getString(3), result.getString(4),result.getInt(5), User.fromIoDateStringToDate(result.getString(7)));
                homeIntent.putExtra("CURRENT_USER", user);
                startActivity(homeIntent);
                finish();
            }, 2000);
        else
            handler.postDelayed(() -> {
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