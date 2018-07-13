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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.codepath.parsetegram.utils.BitmapScaler;
import com.codepath.parsetegram.utils.DeviceDimensionsHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CreatePost extends AppCompatActivity {
    public final String APP_TAG = "Parstegram";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    // TODO - photoFileName is "o"?
    public String photoFileName = "photo.jpg";
    File photoFile;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.ivPreview) ImageView ivPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        final String description = etDescription.getText().toString();
        final ParseUser user = ParseUser.getCurrentUser();

        // ensure that we cannot post without photoFile and description
        if (photoFile!=null && !description.isEmpty()) {
            final ParseFile parseFile = new ParseFile(photoFile);
            createPost(description, parseFile, user);
        } else {
            Toast.makeText(this, "No picture or description to upload", Toast.LENGTH_SHORT).show();
        }
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
        Uri fileProvider = FileProvider.getUriForFile(CreatePost.this, "com.codepath.fileprovider", photoFile);
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

            // get height or width of screen at runtime
            int screenWidth = DeviceDimensionsHelper.getDisplayWidth(this);
            Bitmap resizedBitmap = BitmapScaler.scaleToFitWidth(takenImage, screenWidth);

            // load the taken image into a preview
            ivPreview.setImageBitmap(resizedBitmap);
        } else {
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }
}
