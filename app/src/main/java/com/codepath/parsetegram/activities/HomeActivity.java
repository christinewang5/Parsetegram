package com.codepath.parsetegram.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.parsetegram.R;
import com.codepath.parsetegram.fragments.FeedFragment;
import com.codepath.parsetegram.fragments.PostFragment;
import com.codepath.parsetegram.fragments.ProfileFragment;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private FeedFragment feedFragment;
    private PostFragment postFragment;
    private ProfileFragment profileFragment;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        Log.d("HomeActivity", "Current user is " + ParseUser.getCurrentUser());

        // find and set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.nav_logo_whiteout);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        if (feedFragment == null) {
            feedFragment = FeedFragment.newInstance();
        }
        if (postFragment == null) {
            postFragment = postFragment.newInstance();
        }
        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();
        }

        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    default:
                    case 0:
                        return feedFragment;
                    case 1:
                        return postFragment;
                    case 2:
                        return profileFragment;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        // set on item selected listener for bottom navigation view
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            default:
                            // TODO - change bottom navigation
                            case R.id.action_home:
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_feed, feedFragment).commit();
                                return true;
                            // TODO - bottom navigation view with fragments
                            case R.id.action_new_post:
                            case R.id.action_profile:

                        }
                        return true;
                    }
                });
        viewPager.setAdapter(pagerAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            default:
            case R.id.action_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.action_new_post:
                viewPager.setCurrentItem(1);
                break;
            case R.id.action_profile:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Log.d("HomeActivity", "Logout. Current user is " + ParseUser.getCurrentUser());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}

