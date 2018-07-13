package com.codepath.parsetegram.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.Collections.reverse;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    List<Post> posts;
    Context context;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Post post = posts.get(position);
        Log.d("PostAdapter", post.getUser().getUsername()+" posted.");

        // set post's text, image, created at
        viewHolder.tvUsername.setText(post.getUser().getUsername());
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
                    Log.d("PostAdapter", "Byte count for my pic " + bmp.getByteCount());
                    // set the Bitmap into the ivImage
                    viewHolder.ivImage.setImageBitmap(bmp);

                }
                else {
                    Log.d("PostAdapter","Problem load image the data.");
                }
            }
        });
        viewHolder.tvCreatedAt.setText(post.getRelativeTimeAgo());
        viewHolder.tvCaption.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void setItems(List<Post> posts) {
        reverse(posts);
        this.posts = posts;
        notifyDataSetChanged();
    }
    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
        @BindView(R.id.tvCaption) TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
