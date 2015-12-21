package com.codepath.apps.awesometwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.awesometwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.awesometwitter.fragments.TweetsListFragment;
import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.models.User;
import com.codepath.apps.awesometwitter.network.TwitterClient;

public class TimelineActivity extends AppCompatActivity {

    private TweetsListFragment fragmentTweetsList;
    private TwitterClient client;
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.twitter_circle_filled);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Get the ViewPager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the ViewPager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the Pager sliding tabs
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the Pager Tabs to the ViewPager
        tabStrip.setViewPager(vpPager);


        client = TwitterApp.getRestClient();

        if (savedInstanceState == null){
            fragmentTweetsList = new HomeTimelineFragment();
        }

//        populateTimeline(1, client, fragmentTweetsList.getAdapter());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    // Append more data into the adapter
//    public static void customLoadMoreDataFromApi(int offset, TweetsArrayAdapter adapter ) {
//        // This method probably sends out a network request and appends new data items to your adapter.
//        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
//        // Deserialize API response and then construct new objects to append to the adapter
////        TwitterClient client = TwitterApp.getRestClient();
////        populateTimeline(offset, client, adapter);
//    }



//    private static void populateTimeline(int page, TwitterClient client, final TweetsArrayAdapter adapter){
////        int offset;
////        if (page != 1) {
////            offset = (page-1)*25;
////        } else{
////            offset = 1;
////        }
////        client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
////            @Override
////            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//////                super.onSuccess(statusCode, headers, response);
////                Log.d("DEBUG", response.toString());
////                // Deserialize JSON
////                // Create the Model and add them to the Adapter
////                // Load the model data in to listview
////                adapter.addAll(Tweet.fromJsonArray(response));
////            }
////
////            @Override
////            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
////                Log.d("DEBUG", errorResponse.toString());
////            }
////        });
//    }





    public void onProfileView(MenuItem mi){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }


    // Return the order of fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter{
        private String tabTitles[] = { "Home", "Mentions"};

        // This is how adapter gets the manager which is used to insert / remove fragments to / from the activity
        public TweetsPagerAdapter(android.support.v4.app.FragmentManager fm){
            super(fm);
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HomeTimelineFragment();
            } else if (position == 1){
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        
        // Returns how many fragments are available
        @Override
        public int getCount() {
            return tabTitles.length;
        }

        // Return the tab titles
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}
