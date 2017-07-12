package com.android.spartanrides;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

public class spartan_post extends AppCompatActivity {

    String sourceVal="";
    String destVal = "";
    String dateVal = "";
    String timeVal = "";

    public String getSourceVal() {
        return sourceVal;
    }

    public void setSourceVal(String sourceVal) {
        this.sourceVal = sourceVal;
    }

    public String getDestVal() {
        return destVal;
    }

    public void setDestVal(String destVal) {
        this.destVal = destVal;
    }

    public String getDateVal() {
        return dateVal;
    }

    public void setDateVal(String dateVal) {
        this.dateVal = dateVal;
    }

    public String getTimeVal() {
        return timeVal;
    }

    public void setTimeVal(String timeVal) {
        this.timeVal = timeVal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
    }
    public JSONObject convertToJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Source", sourceVal);
        jsonObject.put("Destination", destVal);
        jsonObject.put("Date", dateVal);
        jsonObject.put("Time", timeVal);
        JSONActivity.printJSON(jsonObject);
        return jsonObject;
    }
}
