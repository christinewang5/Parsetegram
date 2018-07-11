package com.codepath.parsetegram.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {
    static String imagePath = "src/IMG_0007.jpg";
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.btnCreate) Button btnCreate;
    @BindView(R.id.btnRefresh) Button btnRefresh;
    @BindView(R.id.btnLogout) Button btnLogout;
    @BindView(R.id.btnPost) Button btnPost;
    @BindView(R.id.ivPreview) ImageView ivPreview;

    public final String APP_TAG = "Parstegram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    // TODO - photoFileName is "o"?
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Log.d("HomeActivity", "Current user is " + ParseUser.getCurrentUser());

    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        final String description = etDescription.getText().toString();
        final ParseUser user = ParseUser.getCurrentUser();
        final ParseFile parseFile = new ParseFile(photoFile);

        // ensure that we cannot post without photoFile and description
        if (photoFile.exists() && !description.isEmpty()) {
            createPost(description, parseFile, user);
        } else {
            Toast.makeText(this, "No picture or description to upload", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btnRefresh)
    public void onRefresh() {
        loadTopPosts();
    }

    @OnClick(R.id.btnLogout)
    public void onLogout() {
        ParseUser.logOut();
        Log.d("HomeActivity", "Logout. Current user is " + ParseUser.getCurrentUser());
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        // TODO - stop user from constantly create same post
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

    @OnClick(R.id.btnCreate)
    public void onLaunchCamera(View view) {
        Log.d("HomeActivity", "in onLaunchCamera");

        // create intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a File reference
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(HomeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // start intent when activity component is not null
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        Log.d("HomeActivity", "in getPhotoFileUri");

        // get directory for photos
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }

        // return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("HomeActivity", "in onActivityResult");

            // camera photo already on disk!
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

            // TODO - resize bitmap

            // load the taken image into a preview
            ivPreview.setImageBitmap(takenImage);
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
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