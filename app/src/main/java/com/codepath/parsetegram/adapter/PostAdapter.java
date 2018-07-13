package com.codepath.parsetegram.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.codepath.parsetegram.activities.PostDetailActivity;
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
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
        @BindView(R.id.tvUsername) TextView tvUsername;
        @BindView(R.id.ivImage) ImageView ivImage;
        @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
        @BindView(R.id.tvCaption) TextView tvCaption;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("PostAdapter", "Clicked on post");
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Post post = posts.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, PostDetailActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Post.class.getSimpleName(), post);
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}
