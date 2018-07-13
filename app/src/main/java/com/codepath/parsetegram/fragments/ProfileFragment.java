package com.codepath.parsetegram.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.parsetegram.R;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    @BindView(R.id.tvUsername) TextView tvUsername;

    // Required empty public constructor
    public ProfileFragment() { }

    public static ProfileFragment newInstance() { return new ProfileFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        String current_user = ParseUser.getCurrentUser().getUsername();
        tvUsername.setText(current_user);
        // inflate the layout for this fragment
        return view;
    }
}
