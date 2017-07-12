package com.android.spartanrides;

import android.content.SyncStatusObserver;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by venka on 7/12/2017.
 */

public class ServerTask extends AsyncTask<String, Void, String> {

    JSONObject jsonObject;

    public void sendRequest(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("Background task","success");
        OutputStream os = null;
        InputStream is = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://spartanrides.me/post.php");
            String message = jsonObject.toString();

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //HHTP Header
            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.connect();

            //setup send
            os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            os.flush();
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //clean up
            try {
                if (os != null) {
                    os.close();
                    is.close();
                }}
            catch(IOException e){
                e.printStackTrace();
            }
        }

        conn.disconnect();
        return null;
    }
}
