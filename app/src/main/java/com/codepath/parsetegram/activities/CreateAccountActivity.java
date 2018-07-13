package com.codepath.parsetegram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.parsetegram.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateAccountActivity extends AppCompatActivity {
    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;
    @BindView(R.id.etEmail) EditText etEmail;
    @BindView(R.id.btnCreateAccount) Button btnCreateAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnCreateAccount)
    public void onCreateAccount() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        final String email = etEmail.getText().toString();
        createNewUser(username, password, email);
    }

    public void createNewUser(final String username, final String password, String email){//}, String phoneNumber) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("CreateAccountActivity", "Create account success");
                    login(username, password);
                } else {
                    Log.e("CreateAccountActivity", "Create account failure");
                    e.printStackTrace();
                }
            }
        });
    }

    private void login(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("CreateAccountActivity", "Login successful");
                    final Intent intent = new Intent(CreateAccountActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("CreateAccountActivity", "Login failure");
                    e.printStackTrace();
                }
            }
        });
    }
}
