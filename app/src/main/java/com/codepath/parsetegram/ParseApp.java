package com.codepath.parsetegram;

import android.app.Application;

import com.codepath.parsetegram.model.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        final Parse.Configuration configuration = new Parse.Configuration.Builder(this)
                .applicationId("christine-instagram")
                .clientKey("happy123")
                .server("http://christine-instagram.herokuapp.com/parse")
                .build();

        Parse.initialize(configuration);
    }
}
