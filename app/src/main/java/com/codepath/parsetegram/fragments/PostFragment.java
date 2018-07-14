package com.codepath.parsetegram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.codepath.parsetegram.utils.CameraUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    public final String APP_TAG = "PostFragment";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    // TODO - photoFileName is "o"?
    public String photoFileName = "photo.jpg";
    File photoFile;
    @BindView(R.id.etCaption) EditText etCaption;
    @BindView(R.id.ivPhoto) ImageView ivPhoto;


    // Required empty public constructor
    public PostFragment() { }

    public static PostFragment newInstance() { return new PostFragment(); }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @OnClick(R.id.fabTakePhoto)
    public void onLaunchCamera(View view) {
        Log.d("HomeActivity", "in onLaunchCamera");

        // create intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a File reference
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // start intent when activity component is not null
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        Log.d("HomeActivity", "in getPhotoFileUri");

        // get directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

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

            // camera photo already on disk! resize and rotate!
            //Bitmap takenImage = BitmapFactory.decodeFile(rawPhotoFile.getAbsolutePath());
            Bitmap rotatedImage = CameraUtils.rotateBitmapOrientation(photoFile.getAbsolutePath());
            Bitmap resizedImage = CameraUtils.scaleToFitWidth(rotatedImage, (int) (CameraUtils.getDisplayWidth(getContext())*0.8));
            CameraUtils.writeBitmapToFile(photoFile, resizedImage);
            // load the taken image into a preview
            ivPhoto.setImageBitmap(resizedImage);
        } else {
            Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPost(String description, ParseFile imageFile, ParseUser user) {
        final Post newPost = new Post();
        newPost.setDescription(description);
        newPost.setImage(imageFile);
        newPost.setUser(user);
        newPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("HomeActivity", "Done");
                if (e == null) {
                    Log.d("HomeActivity", "Create post success");
                    Toast.makeText(getContext(), "Posted!", Toast.LENGTH_LONG);
                } else {
                    Log.e("HomeActivity", "Create post failed");
                    Toast.makeText(getContext(), "Failed to post, try again.", Toast.LENGTH_LONG);
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnPost)
    public void onPost() {
        String caption = etCaption.getText().toString();
        if (!caption.isEmpty() && photoFile != null){
            ParseFile photoParseFile = new ParseFile(photoFile);
            createPost(caption, photoParseFile, ParseUser.getCurrentUser());
            // clear the current screens
            ivPhoto.setImageResource(R.drawable.bg_splash);
            etCaption.setText("");
        }
        else {
            Toast.makeText(getContext(), "No photo or caption to post", Toast.LENGTH_LONG);
        }
    }


}
