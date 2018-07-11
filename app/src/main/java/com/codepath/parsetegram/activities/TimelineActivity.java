package com.codepath.parsetegram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.codepath.parsetegram.PostAdapter;
import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.List;

import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {
    List<Post> posts;
    PostAdapter postAdapter;
    //@BindView(R.id.swipeContainer) private SwipeRefreshLayout swipeContainer;
    //@BindView(R.id.rvPosts) RecyclerView rvPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
    }

    private void loadTop20Posts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    posts = objects.subList(0,20);
                    Log.d("PostAdapter", "Loaded 20 posts");
                } else {
                    e.printStackTrace();
                    Log.d("PostAdapter", "Failed loading 20 posts");
                }
            }
        });
    }
}
