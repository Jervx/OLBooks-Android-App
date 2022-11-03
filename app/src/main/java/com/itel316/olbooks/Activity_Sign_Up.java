package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Activity_Sign_Up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        TextView toSignUp = findViewById(R.id.sign_in_instead);
        toSignUp.setOnClickListener(e -> {
            Intent SignIn = new Intent(getApplicationContext(), Activity_Sign_In.class);
            startActivity(SignIn);
            finish();
        });
    }
}