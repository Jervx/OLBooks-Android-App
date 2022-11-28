package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.JavaMailAPI;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.helpers.Validator;
import com.itel316.olbooks.models.User;

public class Activity_Sign_In extends AppCompatActivity {

    private TextView toSignUp, forgot;
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
        forgot = findViewById(R.id.forgot);

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
                password.setError("Empty Password");
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
            findUser.close();
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

        forgot.setOnClickListener(e -> {
            Dialog recover = new Dialog(this);
            recover.setContentView(R.layout.recover);

            ImageView close = recover.findViewById(R.id.close);
            Button btn_send_new_pass = recover.findViewById(R.id.btn_send_new_pass);
            EditText email_input = recover.findViewById(R.id.email_input);

            btn_send_new_pass.setOnClickListener( ev -> {
                String i_em = email_input.getText().toString();

                if(!Validator.validateEmail(i_em)){
                    email_input.setError("Invalid Email");
                    return;
                }

                Cursor findUser = dbhelper.getUser(String.format("SELECT * FROM user WHERE email = '%s';", i_em), null);
                if(findUser == null || findUser.getCount() == 0) {
                    email_input.setError("User Email Is Not Registered Here!");
                    return;
                }

                findUser.moveToNext();
                User user = new User(findUser.getString(1), dbhelper);

                String genKey = OlbookUtils.randomKey(6);

                user.setPassword(AuthHelper.hashPassword(genKey));
                user.saveState(dbhelper);

                String buildMsg = OlbookUtils.HTML_Forgot.replace("${name}", "beloved user");
                buildMsg = buildMsg.replace("${content}", "You have requested a temporary password. You can use this to login to your account, just remember to change it afterwards in your account settings.");
                buildMsg = buildMsg.replace("${verification}", genKey);

                JavaMailAPI javaMailAPI = new JavaMailAPI(this, i_em, "OLBook Temporary Password", buildMsg);
                javaMailAPI.execute();
                findUser.close();
                recover.dismiss();
            });

            close.setOnClickListener(ev -> { recover.dismiss(); });
            recover.show();
        });

    }
}