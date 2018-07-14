package com.codepath.parsetegram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public void setProfileImage(ParseFile image) {
        put("profileImage", image);
    }
    public ParseFile getProfileImage(ParseFile image) {
        return getParseFile("profileImage");
    }
}
