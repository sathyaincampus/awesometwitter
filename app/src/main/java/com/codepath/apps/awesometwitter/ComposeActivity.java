package com.codepath.apps.awesometwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by s.srinivas2 on 12/13/15.
 */
public class ComposeActivity  extends AppCompatActivity {

    private Button btTweet, btCancel;
    private EditText etTweetMessage;
    private TextView tvComposeFullName;
    private TextView tvComposeUserName;
    private TwitterClient client;
    private ImageView ivComposeProfilePic;
    private String strTweetMessage;
    private String strFullName, strUserName, strPicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        strFullName = getIntent().getStringExtra("fullName");
        strUserName = "@" + getIntent().getStringExtra("userName");
        strPicUrl = getIntent().getStringExtra("picUrl");
        btTweet = (Button) findViewById(R.id.btTweet);
        btCancel = (Button) findViewById(R.id.btCancel);
        etTweetMessage = (EditText) findViewById(R.id.etTweetMessage);
        tvComposeFullName = (TextView) findViewById(R.id.tvComposeFullName);
        tvComposeUserName = (TextView) findViewById(R.id.tvComposeUserName);
        ivComposeProfilePic = (ImageView) findViewById(R.id.ivComposeProfilePic);
        tvComposeFullName.setText(strFullName);
        tvComposeUserName.setText(strUserName);
        ivComposeProfilePic.setImageResource(android.R.color.transparent);
        Picasso.with(getApplicationContext()).load(strPicUrl).into(ivComposeProfilePic);

        client = TwitterApp.getRestClient();

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        etTweetMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    strTweetMessage = etTweetMessage.getText().toString();
//                    if (strTweetMessage.equals(getString(R.string.text_share_status))){
//                        strTweetMessage = "";
//                    }
//                }
//            }
//        });

//        etTweetMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTweetMessage = etTweetMessage.getText().toString();

                client.postTweet(strTweetMessage, new JsonHttpResponseHandler() {


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        Intent data = new Intent();
                        data.putExtra("index", 0);
//        Toast.makeText(this, ("value : " + etCurrentItem.getText().toString()), Toast.LENGTH_SHORT).show();
                        data.putExtra("fullName", strFullName);
                        data.putExtra("userName", strUserName);
                        data.putExtra("picUrl", strPicUrl);
                        data.putExtra("body", strTweetMessage);
//                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Posted successfully!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, data);
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
