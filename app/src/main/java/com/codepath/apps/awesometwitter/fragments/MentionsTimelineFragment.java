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
public class MentionsTimelineFragment extends TweetsListFragment{
    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApp.getRestClient();
        populateTimeline(1);
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
        client.getMentionsTimeline(offset, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
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
