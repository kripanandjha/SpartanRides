package com.android.spartanrides;

/**
 * Created by venka on 8/4/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONRequest {
    public static String[] ids;
    public static String[] usernames;
    public static String[] sources;
    public static String[] destinations;
    public static String[] dates;
    public static String[] times;
    public static String[] facebookID;
    public static String[] photoURL;

    public static final String KEY_ID = "0";
    public static final String JSON_ARRAY = "post_data";
    public static final String USERNAME = "username";
    public static final String DESTINATION = "destination";
    public static final String SOURCE = "source";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String FACEBOOKID = "FBID";
    public static final String PHOTOURL = "ph_url";


    private JSONArray users = null;

    private String json;

    public JSONRequest(String json){
        this.json = json;
    }

    protected void parseJSON(){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[users.length()];
            usernames = new String[users.length()];
            sources = new String[users.length()];
            destinations = new String[users.length()];
            dates = new String[users.length()];
            times = new String[users.length()];
            facebookID = new String[users.length()];
            photoURL = new String[users.length()];

            for(int i=0;i<users.length();i++){
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                usernames[i] = jo.getString(USERNAME);
                sources[i] = jo.getString(SOURCE);
                destinations[i] = jo.getString(DESTINATION);
                dates[i] = jo.getString(DATE);
                times[i] = jo.getString(TIME);
                facebookID[i] = jo.getString(FACEBOOKID);
                photoURL[i] = jo.getString(PHOTOURL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
