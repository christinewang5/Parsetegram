package com.codepath.parsetegram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.parsetegram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.btnLogin) Button btnLogin;
    @BindView(R.id.btnCreateAccount) Button btnCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            Log.d("LoginActivity", "Already logged in, current user is " + currentUser);
            // navigate to home activity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("LoginActivity", "Need to log in");
            setContentView(R.layout.activity_login);
            ButterKnife.bind(this);

        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin() {
        if (ParseUser.getCurrentUser() != null ) {
            ParseUser.logOut();
        }

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        login(username, password);
    }

    @OnClick(R.id.btnCreateAccount)
    public void onCreateAccount() {
        if (ParseUser.getCurrentUser() != null ) {
            ParseUser.logOut();
        }
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {
                    Log.d("LoginActivity", "Login successful");
                    final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}


