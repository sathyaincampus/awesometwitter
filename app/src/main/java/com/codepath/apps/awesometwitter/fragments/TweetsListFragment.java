package com.codepath.apps.awesometwitter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.activities.ComposeActivity;
import com.codepath.apps.awesometwitter.activities.ProfileActivity;
import com.codepath.apps.awesometwitter.activities.TimelineActivity;
import com.codepath.apps.awesometwitter.managers.EndlessScrollListener;
import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.models.Tweet;
import com.codepath.apps.awesometwitter.models.User;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.codepath.apps.awesometwitter.views.adapters.TweetsArrayAdapter;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by s.srinivas2 on 12/18/15.
 */
public class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    private ImageView ivProfilePic;

    private TwitterClient client;
    private User currentUser;
    private final int REQUEST_CODE = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        ivProfilePic = (ImageView) v.findViewById(R.id.ivProfilePic);
        // Connect Adapater with the ListView
        lvTweets.setAdapter(aTweets);

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

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        // Create ArrayList
        tweets = new ArrayList<Tweet>();
        // Construct Adapter from the datasource
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_home_timeline, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.miProfile:
                onProfileView();
                return true;
            case R.id.action_compose:
                getUserInfoAndInvokeIntent();
                return true;
            case R.id.action_home:
                onHomeView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onHomeView(){
        Intent i = new Intent(getActivity(), TimelineActivity.class);
        startActivity(i);
    }

    public void onProfileView(){
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        startActivity(i);
    }

    public void onProfileView(MenuItem mi){
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        startActivity(i);
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
    }

    private void populateTimeline(int page){

    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public TweetsArrayAdapter getAdapter(){
        return aTweets;
    }
}
