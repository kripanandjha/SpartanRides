package com.android.spartanrides;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by venka on 7/24/2017.
 */

public class HelpActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    public String convertToJSON(String rating, String suggestion) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Activity", "Suggestions");
        jsonObject.put("Username", UserDetails.username);
        jsonObject.put("Rating", rating);
        jsonObject.put("Suggestion", suggestion);
        return jsonObject.toString();
    }
}


