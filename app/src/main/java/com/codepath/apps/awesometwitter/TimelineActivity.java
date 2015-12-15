package com.codepath.apps.awesometwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.awesometwitter.models.Tweet;
import com.codepath.apps.awesometwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private User currentUser;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timline);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        lvTweets = (ListView) findViewById(R.id.lvTweets);



        // Create ArrayList
        tweets = new ArrayList<Tweet>();
        // Construct Adapter from the datasource
        aTweets = new TweetsArrayAdapter(this, tweets);
        // Connect Adapater with the ListView
        lvTweets.setAdapter(aTweets);
        client = TwitterApp.getRestClient();

        // Attach the listener to the AdapterView onCreate
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });

        populateTimeline(1);



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
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        populateTimeline(offset);
    }

    private void populateTimeline(int page){
        int offset;
        if (page != 1) {
            offset = (page-1)*25;
        } else{
            offset = 1;
        }
        client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                super.onSuccess(statusCode, headers, response);
                Log.d("DEBUG", response.toString());
                // Deserialize JSON
                // Create the Model and add them to the Adapter
                // Load the model data in to listview
                aTweets.addAll(Tweet.fromJsonArray(response));
                Log.d("DEBUG", aTweets.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_timeline, menu);
        MenuItem composeItem = menu.findItem(R.id.action_compose);
        composeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                client.getUserInfo(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        currentUser = User.fromJson(response);
                        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                        i.putExtra("fullName", currentUser.getName());
                        i.putExtra("userName", currentUser.getScreenName());
                        i.putExtra("picUrl", currentUser.getProfilePicUrl());
                        i.putExtra("accountUrl", currentUser.getAccountUrl());
                        i.putExtra("id", currentUser.getId());
                        startActivityForResult(i, REQUEST_CODE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            int index = data.getExtras().getInt("index", 0);
            String strFullName = data.getExtras().getString("fullName");
            String strUserName = data.getExtras().getString("userName");
            String strPicUrl = data.getExtras().getString("picUrl");
            String strAccountUrl = data.getExtras().getString("accountUrl");
            Long id = data.getExtras().getLong("id", 0);
            Long uid = data.getExtras().getLong("uid", 0);
            String strTweetMessage = data.getExtras().getString("body");
//            Toast.makeText(this, ("body : " + strTweetMessage), Toast.LENGTH_SHORT).show();
            User u = new User(strFullName, strUserName, uid, strPicUrl, strAccountUrl);

            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);
            String currentDateandTime = sf.format(new Date());

//            Log.d("DEBUG", "current time : " + currentDateandTime);
            Tweet myTweet = new Tweet(id, u, strTweetMessage, currentDateandTime);
            aTweets.insert(myTweet, 0);
            aTweets.notifyDataSetChanged();
        }
    }

}
