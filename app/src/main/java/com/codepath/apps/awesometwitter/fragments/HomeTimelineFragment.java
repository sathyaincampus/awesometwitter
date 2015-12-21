package com.codepath.apps.awesometwitter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.activities.ComposeActivity;
import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.models.Tweet;
import com.codepath.apps.awesometwitter.models.User;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by s.srinivas2 on 12/20/15.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    private User currentUser;
    private final int REQUEST_CODE = 20;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
//        populateTimeline(1, client, fragmentTweetsList.getAdapter());
        populateTimeline(1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_home_timeline, menu);
        MenuItem composeItem = menu.findItem(R.id.action_compose);
        composeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                getUserInfoAndInvokeIntent();
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


    public void getUserInfoAndInvokeIntent(){
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                currentUser = User.fromJson(response);
                Intent i = new Intent(getActivity(), ComposeActivity.class);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            int index = data.getExtras().getInt("index", 0);
            String strFullName = data.getExtras().getString("fullName");
            String strUserName = data.getExtras().getString("userName");
            String strPicUrl = data.getExtras().getString("picUrl");
            String strAccountUrl = data.getExtras().getString("accountUrl");
            Long id = data.getExtras().getLong("id", 0);
            Long uid = data.getExtras().getLong("uid", 0);
            String strTweetMessage = data.getExtras().getString("body");
            User u = new User(strFullName, strUserName, uid, strPicUrl, strAccountUrl);

            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);
            String currentDateandTime = sf.format(new Date());

            Tweet myTweet = new Tweet(id, u, strTweetMessage, currentDateandTime);
            getAdapter().insert(myTweet, 0);
            getAdapter().notifyDataSetChanged();
        }
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
                addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
