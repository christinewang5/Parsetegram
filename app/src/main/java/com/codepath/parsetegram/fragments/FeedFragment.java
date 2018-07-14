package com.codepath.parsetegram.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.adapter.PostAdapter;
import com.codepath.parsetegram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment {


    public static FeedFragment newInstance() {
        return new FeedFragment();
    }

    // required empty public constructor
    public FeedFragment() {}

    // public interface FeedCallbacks { }
    // private FeedCallbacks listener;
    private PostAdapter postAdapter;
    private LinearLayoutManager layoutManager;

    private FragmentActivity listener;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @BindView(R.id.rvPosts) RecyclerView rvPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.bind(this, view);
        // initialize all vars
        if (postAdapter == null) {
            postAdapter = new PostAdapter(new ArrayList<Post>());
        }

        // setup recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPosts.setAdapter(postAdapter);

        // setup container view for refresh function
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postAdapter.clear();
                loadTop20Posts();
                swipeContainer.setRefreshing(false);
            }
        });



        loadTop20Posts();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void loadTop20Posts() {
        final Post.Query postsQuery = new Post.Query();
        postsQuery.getTop().withUser();
        postsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    postAdapter.setItems(objects);
                    Log.d("FeedFragment", "Loaded 20 posts");
                } else {
                    Log.e("FeedFragment", e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

}