package com.codepath.apps.awesometwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.awesometwitter.R;
import com.codepath.apps.awesometwitter.managers.TwitterApp;
import com.codepath.apps.awesometwitter.network.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by s.srinivas2 on 12/13/15.
 */
public class ComposeActivity  extends AppCompatActivity {

    private Button btTweet, btCancel;
    private EditText etTweetMessage;
    private TextView tvComposeFullName;
    private TextView tvComposeUserName, tvCharacterCount;
    private TwitterClient client;
    private ImageView ivComposeProfilePic;
    private String strTweetMessage;
    private String strFullName, strUserName, strPicUrl, strAccountUrl;
    Long id, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        strFullName = getIntent().getStringExtra("fullName");
        strUserName = getIntent().getStringExtra("userName");
        strPicUrl = getIntent().getStringExtra("picUrl");
        strAccountUrl = getIntent().getStringExtra("accountUrl");
        id = getIntent().getLongExtra("id", 0);
        btTweet = (Button) findViewById(R.id.btTweet);
        btCancel = (Button) findViewById(R.id.btCancel);
        etTweetMessage = (EditText) findViewById(R.id.etTweetMessage);
        tvComposeFullName = (TextView) findViewById(R.id.tvComposeFullName);
        tvComposeUserName = (TextView) findViewById(R.id.tvComposeUserName);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);
        ivComposeProfilePic = (ImageView) findViewById(R.id.ivComposeProfilePic);
        tvComposeFullName.setText(strFullName);
        tvComposeUserName.setText("@" + strUserName);
        ivComposeProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getApplicationContext()).load(strPicUrl).into(ivComposeProfilePic);

        client = TwitterApp.getRestClient();

        etTweetMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String countText;
                int charLength = etTweetMessage.getText().length();
                countText = charLength + " / 140";
                tvCharacterCount.setText(countText);
                if (charLength > 140) {
                    tvCharacterCount.setTextColor(Color.parseColor("#FF0000"));
                    btTweet.setBackgroundResource(android.R.color.darker_gray);
                    btTweet.setEnabled(false);
                }
                if (charLength <= 140 && !btTweet.isEnabled()) {
                    tvCharacterCount.setTextColor(Color.parseColor("#000000"));
                    btTweet.setBackgroundResource(android.R.color.holo_blue_light);
                    btTweet.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTweetMessage = etTweetMessage.getText().toString();

                client.postTweet(strTweetMessage, new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        try {
                            uid = response.getJSONObject("user").getLong("id");
                            id = response.getLong("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent data = new Intent();
                        data.putExtra("fullName", strFullName);
                        data.putExtra("userName", strUserName);
                        data.putExtra("picUrl", strPicUrl);
                        data.putExtra("accountUrl", strTweetMessage);
                        data.putExtra("id", id);
                        data.putExtra("uid", uid);
                        data.putExtra("body", strTweetMessage);
                        Toast.makeText(getApplicationContext(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, data);
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        finish();
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });
            }
        });
     }
}
