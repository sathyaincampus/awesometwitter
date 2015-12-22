package com.codepath.apps.awesometwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.models.User;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by s.srinivas2 on 12/21/15.
 */
public class UserHeaderFragment extends Fragment{
    private TwitterClient client;
    User user;
    TextView tvName;
    TextView tvTagline;
    TextView tvFollowers;
    TextView tvFollowing;
    ImageView ivProfileImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        populateHeader();
    }

    public static UserHeaderFragment newInstance(String screen_name) {
        UserHeaderFragment userFragment = new UserHeaderFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    private void populateHeader(){
        client = TwitterApp.getRestClient();

        String screen_name = getArguments().getString("screen_name");
        if (screen_name != null){
            client.getOtherUserInfo(screen_name, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            });
        } else{
            client.getUserInfo(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJson(response);
                    ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("@" + user.getScreenName());
                    populateProfileHeader(user);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_userheader, container, false);
        tvName = (TextView) v.findViewById(R.id.tvName);
        tvTagline = (TextView) v.findViewById(R.id.tvTagline);
        tvFollowers = (TextView) v.findViewById(R.id.tvFollowers);
        tvFollowing = (TextView) v.findViewById(R.id.tvFollowing);
        ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);

        return v;
    }

    private void populateProfileHeader(User user) {

        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");
        Picasso.with(getContext()).load(user.getProfilePicUrl()).into(ivProfileImage);
    }
}
