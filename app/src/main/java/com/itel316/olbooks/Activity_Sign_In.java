package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.Validator;
import com.itel316.olbooks.models.User;

public class Activity_Sign_In extends AppCompatActivity {

    private TextView toSignUp;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());

        toSignUp = findViewById(R.id.sign_up_instead);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button signIn = findViewById(R.id.btn_sign_up);

        signIn.setOnClickListener(e -> {
            String email_val = email.getText().toString();
            String password_val = password.getText().toString();

            String emailValidation = Validator.multiValidate(email_val, new String[] {"validateIsNotEmpty", "validateIsEmail"});

            if(!emailValidation.contains("valid")){
                email.setError(emailValidation);
                return;
            }

            if(!Validator.validateNotEmpty(password_val)){
                password.setError("Wrong Password");
                return;
            }

            Cursor findUser = dbhelper.getUser(String.format("SELECT * FROM user WHERE email = '%s';", email_val), null);
            if(findUser == null || findUser.getCount() == 0) {
                email.setError("User Not Found!");
                return;
            }

            findUser.moveToNext();
            User user = new User(findUser.getString(1), dbhelper);
            if(!AuthHelper.comparePassword(password_val, user.getPassword())){
                password.setError("Wrong Password!");
                return;
            }

            Intent homeIntent = new Intent(getApplicationContext(), Activity_Home.class);
            homeIntent.putExtra("CURRENT_USER", user);
            startActivity(homeIntent);
            finish();
        });

        toSignUp.setOnClickListener(e -> {
            Intent SignUp = new Intent(getApplicationContext(), Activity_Sign_Up.class);
            startActivity(SignUp);
            finish();
        });
    }
}