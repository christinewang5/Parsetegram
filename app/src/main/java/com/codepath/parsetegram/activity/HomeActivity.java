package com.codepath.parsetegram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private static String imagePath = "src/IMG_0007.jpg";
    private EditText etDescription;
    private Button btnCreate;
    private Button btnRefresh;
    private Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        etDescription = findViewById(R.id.etDescription);
        btnCreate = findViewById(R.id.btnCreate);
        btnRefresh = findViewById(R.id.btnRefresh);
        btnLogout = findViewById(R.id.btnLogout);

        Log.d("HomeActivity", "Current user is " + ParseUser.getCurrentUser());

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String description = etDescription.getText().toString();
                final ParseUser user = ParseUser.getCurrentUser();
                // TODO - ask user to select file or take a photo
                final File file = new File(imagePath);
                final ParseFile parseFile = new ParseFile(file);
                createPost(description, parseFile, user);
                /*parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (null == e) {
                            createPost(description, parseFile, user);
                        } else {
                            Log.e("HomeActivity", "Failed to save parse file");
                            e.printStackTrace();
                        }
                    }
                });*/
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTopPosts();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Log.d("HomeActivity", "Logout. Current user is " + ParseUser.getCurrentUser());
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        // newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("HomeActivity", "Done");
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                } else {
                    Log.e("HomeActivity", "Create post failed");
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadTopPosts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); ++i) {
                        Log.d("HomeActivity", "Post["+i+"] = "
                                + objects.get(i).getDescription()
                                + "\nusername = "+objects.get(i).getUser().getUsername());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}

/*
Intent MediaStore.Action_image_capture);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
    fragment.startActivityForResult(intent, requestCode);
    return path

    // TODO - General idea of photo submitting
    take photo create new intent
    create temporary file
    get absolute path
    convert to uri so that intent can understand
    output directory
    update ui
    submit, building parse object, saving remotely in parse

*/