package com.android.spartanrides;

import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostsearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    public SwipeRefreshLayout swipeRefreshLayout;
    public ListView listView;

    int[] IMAGES = {R.drawable.prf,R.drawable.spartanlogo,R.drawable.a};
    String[] from = {"firsS", "SecondS", "thirdS"};
    String[] to = {"firsD", "SecondD", "thirdD"};
    public static final String JSON_URL = "https://spartanrides.me/getpost.php";
    Geocoder geocoder;
    private AdapterView.OnItemClickListener onItemClickListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);
        listView = (ListView) findViewById(R.id.travellersList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        geocoder= new Geocoder(getApplicationContext());
        sendRequest();
    }
    private void sendRequest(){
        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("FBUserID", UserDetails.fbUserID);
                params.put("AccessToken", UserDetails.accessToken);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String json){
        Log.d("JSON Received", json);
        JSONRequest jsonRequest = new JSONRequest(json);
        jsonRequest.parseJSON();
        final CustomAdapter cl = new CustomAdapter(JSONRequest.ids,JSONRequest.usernames,JSONRequest.sources,JSONRequest.destinations,JSONRequest.dates,JSONRequest.times, JSONRequest.facebookID);
        listView.setAdapter(cl);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3)
            {
                Log.i("TEST", "Testing");
                ObjectContainer item = (ObjectContainer)adapterView.getItemAtPosition(position);
                listView.getSelectedItem();
                ((ListView) adapterView).getItemAtPosition(position);
                String url = "https://www.facebook.com/" + item.facebookID;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }



    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                sendRequest();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }

    class ObjectContainer
    {
        String username;
        String source;
        String destination;
        String facebookID;
        public ObjectContainer (String username, String source, String destination, String facebookID)
        {
            this.username = username;
            this.source = source;
            this.destination = destination;
            this.facebookID = facebookID;
        }
    }

    class CustomAdapter extends BaseAdapter{

        private String[] ids;
        private String[] usernames;
        private String[] sources;
        private String[] destinations;
        private String[] dates;
        private String[] times;
        private String[] facebookIDs;

        public CustomAdapter(String[] ids, String[] usernames, String[] sources, String[] destinations, String[] dates, String[] times, String[] facebookIDs){
            this.ids = ids;
            this.usernames = usernames;
            this.sources = sources;
            this.destinations = destinations;
            this.dates = dates;
            this.times = times;
            this.facebookIDs = facebookIDs;
        }

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int i) {
            ObjectContainer objectContainer = new ObjectContainer(usernames[i],sources[i],destinations[i],facebookIDs[i]);
            return objectContainer;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout,null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.postName);
            TextView source = view.findViewById(R.id.postSource);

            name.setFocusable(false);
            name.setClickable(false);
            name.setCursorVisible(false);

            source.setFocusable(false);
            source.setClickable(false);
            source.setCursorVisible(false);

            imageView.setImageResource(IMAGES[i]);
            name.setText(usernames[i]);
            if(null!=sources[i] && null!=destinations[i])
            source.setText(getLocation(sources[i])+" to "+getLocation(destinations[i]));
            return view;
        }

        public String getLocation(String data)
        {
            String address = "";
            String position = data.substring(data.indexOf("(") + 1, data.indexOf(")"));
            String[] latlong =  position.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            try {
                address = (geocoder.getFromLocation(latitude,longitude,1)).get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return address;
        }

    }
}
