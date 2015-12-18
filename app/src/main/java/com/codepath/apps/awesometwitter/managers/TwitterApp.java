package com.codepath.apps.awesometwitter.managers;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.codepath.apps.awesometwitter.models.Tweet;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterClient client = TwitterApp.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApp extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApp.context = this;
	}

	public static TwitterClient getRestClient() {
		return (TwitterClient) TwitterClient.getInstance(TwitterClient.class, TwitterApp.context);
	}


}