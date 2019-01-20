package com.example.androidlabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //the first function is onCreate, it is equal to java main method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
