package com.codepath.parsetegram.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostDetailActivity extends AppCompatActivity {
    @BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.tvCaption) TextView tvCaption;
    @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        Post post = getIntent().getParcelableExtra(Post.class.getSimpleName());
        // set post's text, image, created at
        tvUsername.setText(post.getUser().getUsername());
        post.getImage().getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // Decode the Byte[] into
                    // Bitmap
                    Bitmap bmp = BitmapFactory
                            .decodeByteArray(
                                    data, 0,
                                    data.length);
                    // set the Bitmap into the ivImage
                    ivImage.setImageBitmap(bmp);

                }
                else {
                    Log.d("PostAdapter","Problem load image the data.");
                }
            }
        });
        tvCreatedAt.setText(post.getRelativeTimeAgo());
        tvCaption.setText(post.getDescription());
    }
}
