package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itel316.olbooks.helpers.AuthHelper;
import com.itel316.olbooks.helpers.DatabaseHelper;
import com.itel316.olbooks.helpers.JavaMailAPI;
import com.itel316.olbooks.helpers.OlbookUtils;
import com.itel316.olbooks.helpers.Validator;
import com.itel316.olbooks.models.User;

import java.util.Date;
import java.util.Random;

public class Activity_Sign_Up extends AppCompatActivity {

    private EditText name, lastname, email, password, re_password;
    private TextView toSignUp;
    private Button btn_sign_up;
    private DatabaseHelper dbhelper;
    private String genKey, i_email, i_name, i_lastname, i_password, i_rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();

        dbhelper = new DatabaseHelper(this);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        lastname = findViewById(R.id.lastname);
        password = findViewById(R.id.password);
        re_password = findViewById(R.id.re_password);
        toSignUp = findViewById(R.id.sign_in_instead);

        btn_sign_up = findViewById(R.id.btn_sign_up);

        btn_sign_up.setOnClickListener(e -> {
            i_email = email.getText().toString();
            i_name = name.getText().toString();
            i_lastname = lastname.getText().toString();
            i_password = password.getText().toString();
            i_rePassword = re_password.getText().toString();

            String val_email = Validator.multiValidate(i_email, new String[] {"validateIsNotEmpty", "validateIsEmail"});
            String val_name = Validator.multiValidate(i_name, new String[] {"validateIsNotEmpty"});
            String val_lastname = Validator.multiValidate(i_lastname, new String[] {"validateIsNotEmpty"});
            String val_password = Validator.multiValidate(i_password, new String[] {"validateIsNotEmpty"});
            String val_rePassword = Validator.multiValidate(i_rePassword, new String[] {"validateIsNotEmpty"});


            Cursor findUser = dbhelper.getUser(String.format("SELECT * FROM user WHERE email = '%s';", i_email), null);
            if(findUser.getCount() >= 1) {
                email.setError("Email Already Used");
                return;
            }

            if(!val_email.equals("valid")){
                email.setError(val_email);
                return;
            }

            if(!val_name.equals("valid")){
                name.setError(val_name);
                return;
            }

            if(!val_lastname.equals("valid")){
                lastname.setError(val_lastname);
                return;
            }

            if(!val_password.equals("valid")){
                password.setError(val_password);
                return;
            }

            if(!val_rePassword.equals("valid")){
                re_password.setError(val_rePassword);
                return;
            }

            if(!i_rePassword.equals(i_password)){
                re_password.setError("Password Doesn't Match!");
                return;
            }

            genKey = OlbookUtils.randomKey(6);

            String buildMsg = OlbookUtils.HTML_NewVerification.replace("${name}", i_name);
            buildMsg = buildMsg.replace("${content}", "Thank you for signing up to OlBooks, to complete your sign up, here is your verification code.");
            buildMsg = buildMsg.replace("${verification}", genKey);

            JavaMailAPI javaMailAPI = new JavaMailAPI(this, i_email, "OLBook Verification", buildMsg);
            javaMailAPI.execute();
            showConfirmDialog();

        });

        toSignUp.setOnClickListener(e -> {
            Intent SignIn = new Intent(getApplicationContext(), Activity_Sign_In.class);
            startActivity(SignIn);
            finish();
        });
    }

    public void showConfirmDialog(){
        Dialog verify = new Dialog(this);
        verify.setContentView(R.layout.verificationcode);
        ImageView close = (ImageView) verify.findViewById(R.id.close);
        Button verifyBtn  = (Button) verify.findViewById(R.id.verify);
        verifyBtn.setOnClickListener(e->{
            String code = ((EditText) verify.findViewById(R.id.verification)).getText().toString();
            if(!code.equals(genKey)){
                Toast.makeText(this,"You Entered Invalid Code",Toast.LENGTH_LONG).show();
                ((EditText) verify.findViewById(R.id.verification)).setError("Invalid Code");
            }else{
                Toast.makeText(this,"Verified",Toast.LENGTH_LONG).show();
                dbhelper.insertUser(i_email, i_name, i_lastname, AuthHelper.hashPassword(i_password), 1, 0, new Date());
                Cursor result = dbhelper.getUser(String.format("select * from user where email = '%s'", i_email), null);

                Intent homeIntent = new Intent(getApplicationContext(), Activity_Home.class);
                result.moveToNext();
                User user = new User(result.getString(1), dbhelper);
                dbhelper.updateUserLoginState(user.getUserId(), 1);
                homeIntent.putExtra("CURRENT_USER", user);
                startActivity(homeIntent);
                finish();
            }
        });
        close.setOnClickListener(e -> {
            verify.dismiss();
        });
        verify.show();
    }
}