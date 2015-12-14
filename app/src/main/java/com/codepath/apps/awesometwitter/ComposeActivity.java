package com.codepath.apps.awesometwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

/**
 * Created by s.srinivas2 on 12/13/15.
 */
public class ComposeActivity  extends AppCompatActivity {

    private Button btTweet;
    private EditText etTweetMessage;
    private TwitterClient client;
    private String strTweetMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        String t = getIntent().getStringExtra("Twitter");
        btTweet = (Button) findViewById(R.id.btTweet);
        etTweetMessage = (EditText) findViewById(R.id.etTweetMessage);
        strTweetMessage = etTweetMessage.getText().toString();
        client = TwitterApp.getRestClient();



        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.postTweet(strTweetMessage , new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.d("DEBUG", response.toString());
                        Toast.makeText(getApplicationContext(),"Posted successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }
        });
     }
}
