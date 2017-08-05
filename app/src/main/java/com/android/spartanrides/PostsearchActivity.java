package com.android.spartanrides;

import android.content.Intent;
import android.graphics.Color;
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
import com.baoyz.widget.PullRefreshLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener  {

    public PullRefreshLayout swipeRefreshLayout;
    public ListView listView;

    int[] IMAGES = {R.drawable.prf,R.drawable.spartanlogo,R.drawable.a};
    public static final String JSON_URL = "https://spartanrides.me/getpost.php";
    Geocoder geocoder;
    private AdapterView.OnItemClickListener onItemClickListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);
        listView = (ListView) findViewById(R.id.travellersList);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendRequest();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
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
        final CustomAdapter cl = new CustomAdapter(JSONRequest.ids,JSONRequest.usernames,JSONRequest.sources,JSONRequest.destinations,JSONRequest.dates,JSONRequest.times, JSONRequest.facebookID, JSONRequest.photoURL);
        listView.setAdapter(cl);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3)
            {
                Log.i("TEST", "Testing");
                ObjectContainer item = (ObjectContainer)adapterView.getItemAtPosition(position);
                listView.getSelectedItem();
//                ((ListView) adapterView).getItemAtPosition(position);
                String url = "https://www.facebook.com/" + item.facebookID;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("TEST", "Testing");
                ObjectContainer item = (ObjectContainer)adapterView.getItemAtPosition(i);
                listView.getSelectedItem();
                UserDetails.chatWith = item.username;
                startActivity(new Intent(getApplicationContext(), Chat.class));
                return true;
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
        }, 2000);
    }

    class ObjectContainer
    {
        String username;
        String source;
        String destination;
        String facebookID;
        String photoURL;
        public ObjectContainer (String username, String source, String destination, String facebookID, String photoURL)
        {
            this.username = username;
            this.source = source;
            this.destination = destination;
            this.facebookID = facebookID;
            this.photoURL = photoURL;
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
        private String[] photoURLs;

        public CustomAdapter(String[] ids, String[] usernames, String[] sources, String[] destinations, String[] dates, String[] times, String[] facebookIDs, String[] photoURLs){
            this.ids = ids;
            this.usernames = usernames;
            this.sources = sources;
            this.destinations = destinations;
            this.dates = dates;
            this.times = times;
            this.facebookIDs = facebookIDs;
            this.photoURLs = photoURLs;
        }

        @Override
        public int getCount() {
            return ids.length;
        }

        @Override
        public Object getItem(int i) {
            ObjectContainer objectContainer = new ObjectContainer(usernames[i],sources[i],destinations[i],facebookIDs[i],photoURLs[i]);
            return objectContainer;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout,null);
//            ImageView imageView = view.findViewById(R.id.imageView);
            CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.postName);
            TextView source = view.findViewById(R.id.postSource);
            TextView timeView = view.findViewById(R.id.postTime);

            name.setFocusable(false);
            name.setClickable(false);
            name.setCursorVisible(false);

            source.setFocusable(false);
            source.setClickable(false);
            source.setCursorVisible(false);

            timeView.setFocusable(false);
            timeView.setClickable(false);
            timeView.setCursorVisible(false);
            circleImageView.setBorderColor(Color.parseColor("#b2b2ff"));
            if(!photoURLs[i].equals("")){
                Picasso.with((PostsearchActivity.this).getApplicationContext()).load(photoURLs[i]).into(circleImageView);
            }
            else{
                Picasso.with((PostsearchActivity.this).getApplicationContext()).load(R.drawable.ic_face_black_24dp).into(circleImageView);
            }
            name.setText(usernames[i]+" will be driving from");
            if(null!=sources[i] && null!=destinations[i])
            source.setText(getLocation(sources[i])+" to "+getLocation(destinations[i]));
            timeView.setText(" on "+dates[i]+" at "+times[i]);
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
