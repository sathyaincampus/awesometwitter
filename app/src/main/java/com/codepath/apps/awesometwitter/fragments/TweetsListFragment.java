package com.codepath.apps.awesometwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.managers.EndlessScrollListener;
import com.codepath.apps.awesometwitter.models.Tweet;
import com.codepath.apps.awesometwitter.views.adapters.TweetsArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s.srinivas2 on 12/18/15.
 */
public class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
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
        // Create ArrayList
        tweets = new ArrayList<Tweet>();
        // Construct Adapter from the datasource
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
//        TwitterClient client = TwitterApp.getRestClient();
//        populateTimeline(offset);
    }

    private void populateTimeline(int page){
//        int offset;
//        if (page != 1) {
//            offset = (page-1)*25;
//        } else{
//            offset = 1;
//        }
//        client.getHomeTimeline(offset, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
////                super.onSuccess(statusCode, headers, response);
//                Log.d("DEBUG", response.toString());
//                // Deserialize JSON
//                // Create the Model and add them to the Adapter
//                // Load the model data in to listview
//                addAll(Tweet.fromJsonArray(response));
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
    }

    public TweetsArrayAdapter getAdapter(){
        return aTweets;
    }
}
