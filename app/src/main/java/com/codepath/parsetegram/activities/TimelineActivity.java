package com.codepath.parsetegram.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.parsetegram.PostAdapter;
import com.codepath.parsetegram.R;
import com.codepath.parsetegram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {
    List<Post> posts;
    PostAdapter postAdapter;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.rvPosts) RecyclerView rvPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        // initialize all vars
        posts =  new ArrayList<>();
        postAdapter = new PostAdapter(posts);

        // setup recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(this));
        rvPosts.setAdapter(postAdapter);

        // setup container view
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postAdapter.clear();
                loadTop20Posts();
                swipeContainer.setRefreshing(false);
            }
        });

        loadTop20Posts();
    }

    private void loadTop20Posts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for(int i = 0; i<objects.size(); i++) {
                        posts.add(objects.get(i));
                        postAdapter.notifyItemInserted(posts.size()-1);
                    }
                    /*posts = objects;
                    postAdapter.notifyDataSetChanged();*/
                    Log.d("TimelineActivity", "Loaded 20 posts");
                } else {
                    e.printStackTrace();
                    Log.d("TimelineActivity", "Failed loading 20 posts");
                }
            }
        });
    }
}