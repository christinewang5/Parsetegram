package com.codepath.parsetegram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.adapter.GridViewAdapter;
import com.codepath.parsetegram.model.Post;
import com.codepath.parsetegram.utils.CameraUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileFragment extends Fragment {
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    //@BindView(R.id.gridview) GridView gridView;
    GridView gridView;
    private GridViewAdapter gridViewAdapter;
    List<Post> myPosts;
    public final static int PICK_PHOTO_CODE = 1046;
    String profileImageName = "profile.png";
    File profileImage;

    // Required empty public constructor
    public ProfileFragment() { }

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        gridView = view.findViewById(R.id.gridview);
        String current_user = ParseUser.getCurrentUser().getUsername();
        tvUsername.setText(current_user);
        gridView.setAdapter(gridViewAdapter);
        // inflate the layout for this fragment
        return view;
    }

    private List<Post> loadMyPosts() {
        myPosts = new ArrayList<Post>();
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        Post post = objects.get(i);
                        if (post.getUser() == ParseUser.getCurrentUser()) {
                            myPosts.add(0, post);
                        }
                    }
                } else {
                    Log.e("FeedFragment", e.toString());
                    e.printStackTrace();
                }
            }
        });
        return myPosts;
    }

    @OnClick(R.id.fabUpload)
    public void onUpload() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // create a File reference

        profileImage = getPhotoFileUri(profileImageName);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri photoUri = data.getData();
            // Do something with the photo based on Uri
            Bitmap selectedImage = null;
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                Bitmap rotatedImage = CameraUtils.rotateBitmapOrientation(profileImage.getAbsolutePath());
                // Load the selected image into a preview
                ivProfileImage.setImageBitmap(rotatedImage);
                CameraUtils.writeBitmapToFile(profileImage, rotatedImage);
                /*User user = (User) ParseUser.getCurrentUser();
                user.setProfileImage(new ParseFile(profileImage));*/
                ParseUser.getCurrentUser().put("profileImage", new ParseFile(profileImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public File getPhotoFileUri(String fileName) {
        Log.d("HomeActivity", "in getPhotoFileUri");

        // get directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "ProfileFragment");

        // create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("ProfileFragment", "failed to create directory");
        }

        // return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }
}
