package com.itel316.olbooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Activity_Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
    }
}