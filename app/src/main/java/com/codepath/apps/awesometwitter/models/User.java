package com.codepath.apps.awesometwitter.models;

/**
 * Created by s.srinivas2 on 12/12/15.
 */

import org.json.JSONException;
import org.json.JSONObject;

/** Sample User Data
 "user": {
 "name": "OAuth Dancer",
 "profile_sidebar_fill_color": "DDEEF6",
 "profile_background_tile": true,
 "profile_sidebar_border_color": "C0DEED",
 "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 "location": "San Francisco, CA",
 "follow_request_sent": false,
 "id_str": "119476949",
 "is_translator": false,
 "profile_link_color": "0084B4",
 "entities": {
 "url": {
 "urls": [
 {
 "expanded_url": null,
 "url": "http://bit.ly/oauth-dancer",
 "indices": [
 0,
 26
 ],
 "display_url": null
 }
 ]
 },
 "description": null
 },
 "default_profile": false,
 "url": "http://bit.ly/oauth-dancer",
 "contributors_enabled": false,
 "favourites_count": 7,
 "utc_offset": null,
 "profile_image_url_https": "https://si0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "id": 119476949,
 "listed_count": 1,
 "profile_use_background_image": true,
 "profile_text_color": "333333",
 "followers_count": 28,
 "lang": "en",
 "protected": false,
 "geo_enabled": true,
 "notifications": false,
 "description": "",
 "profile_background_color": "C0DEED",
 "verified": false,
 "time_zone": null,
 "profile_background_image_url_https": "https://si0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "statuses_count": 166,
 "profile_background_image_url": "http://a0.twimg.com/profile_background_images/80151733/oauth-dance.png",
 "default_profile_image": false,
 "friends_count": 14,
 "following": false,
 "show_all_inline_media": false,
 "screen_name": "oauth_dancer"
 },
 */
public class User {

    public User(){

    }

    public User(String name, String screenName, long id, String profilePicUrl, String accountUrl){
        this.name = name;
        this.screenName = screenName;
        this.id = id;
        this.profilePicUrl = profilePicUrl;
        this.accountUrl = accountUrl;
    }

//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setScreenName(String screenName) {
//        this.screenName = screenName;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public void setProfilePicUrl(String profilePicUrl) {
//        this.profilePicUrl = profilePicUrl;
//    }
//
//    public void setAccountUrl(String accountUrl) {
//        this.accountUrl = accountUrl;
//    }

    private String name;
    private String screenName;
    private long id;
    private String profilePicUrl;
    private String accountUrl;

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getId() {
        return id;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public String getAccountUrl() {
        return accountUrl;
    }

    // Deserialize the JSON
    public static User fromJson(JSONObject jsonObject) {
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.id = jsonObject.getLong("id");
            user.accountUrl = jsonObject.getString("url");
            user.profilePicUrl = jsonObject.getString("profile_image_url");
            user.screenName = jsonObject.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

}
