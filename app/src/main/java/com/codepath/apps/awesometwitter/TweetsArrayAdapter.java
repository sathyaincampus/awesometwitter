package com.codepath.apps.awesometwitter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.awesometwitter.models.Tweet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;

import java.util.List;

/**
 * Created by s.srinivas2 on 12/12/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{
    public TweetsArrayAdapter(Context context, List<Tweet> tweets){
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        // Get Tweet
        Tweet tweet = getItem(position);
        // Find or inflate the Template
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // Find subviews to fill data in the template
        ImageView ivProfilePic = (ImageView) convertView.findViewById(R.id.ivProfilePic);
        TextView tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvPostBody = (TextView) convertView.findViewById(R.id.tvPostBody);
        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);
        // Populate data in to subviews
        tvFullName.setText(tweet.getUser().getName());
        tvUserName.setText("@"+tweet.getUser().getScreenName());
        tvPostBody.setText(tweet.getBody());

//        Long createdTime = Long.parseLong(tweet.getCreatedAt()) * 1000;
//        String createdAt = DateUtils.getRelativeTimeSpanString(createdTime).toString();
        String RelativeTime = Tweet.getRelativeTimeAgo(tweet.getCreatedAt());
        RelativeTime = RelativeTime.replace(" ago", "");
        RelativeTime = RelativeTime.replace(" hours", "h").replace(" minutes", "m").replace(" seconds", "s");
        RelativeTime = RelativeTime.replace(" hour", "h").replace(" minute", "m").replace(" second", "s");
        tvCreatedAt.setText(RelativeTime);

        // Clear old image
        ivProfilePic.setImageResource(android.R.color.transparent);
//        Picasso.with(getContext()).load(tweet.getUser().getProfilePicUrl()).into(ivProfilePic);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext())
                .load(tweet.getUser().getProfilePicUrl())
                .resize(150, 0)
                .transform(transformation)
                .into(ivProfilePic);

        // return view to insert in to the list
        return convertView;
    }


}
