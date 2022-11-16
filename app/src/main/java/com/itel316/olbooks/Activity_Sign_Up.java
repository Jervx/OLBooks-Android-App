package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_Sign_Up extends AppCompatActivity {

    private EditText name, lastname, email, password, re_password;
    private TextView toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        toSignUp = findViewById(R.id.sign_in_instead);

        toSignUp.setOnClickListener(e -> {
            Intent SignIn = new Intent(getApplicationContext(), Activity_Sign_In.class);
            startActivity(SignIn);
            finish();
        });
    }
}