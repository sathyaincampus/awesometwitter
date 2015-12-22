package com.codepath.apps.awesometwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.fragments.UserHeaderFragment;
import com.codepath.apps.awesometwitter.fragments.UserTimelineFragment;

public class ProfileActivity extends AppCompatActivity {
//    TwitterClient client;
//    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_circle_filled);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        // Get Screen name
        String screen_name = getIntent().getStringExtra("screen_name");
        if (savedInstanceState == null){
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screen_name);
            UserHeaderFragment fragmentUserHeader = UserHeaderFragment.newInstance(screen_name);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserHeader, fragmentUserHeader);
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }



}
