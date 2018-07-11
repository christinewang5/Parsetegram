package com.codepath.parsetegram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.codepath.parsetegram.R;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    /** First activity to launch, which automatically signs in or prompts user to login */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        View view = findViewById(R.id.main_view);
        // find the root view
        View root = view.getRootView();
        // set the color
        root.setBackgroundColor(R.drawable.bg_splash);

        ParseUser currentUser = ParseUser.getCurrentUser();

        Log.d("MainActivity", "Current user is " + currentUser);

        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
