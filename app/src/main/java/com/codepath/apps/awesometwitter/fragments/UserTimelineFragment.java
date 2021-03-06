package com.codepath.apps.awesometwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.models.Tweet;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by s.srinivas2 on 12/20/15.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline(1);
    }

    public static UserTimelineFragment newInstance(String screen_name) {
        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        populateTimeline(offset);
    }

    private void populateTimeline(int page){
        String screen_name = getArguments().getString("screen_name");
        int offset;
        if (page != 1) {
            offset = (page-1)*25;
        } else{
            offset = 1;
        }
        client.getUserTimeline(screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                addAll(Tweet.fromJsonArray(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
