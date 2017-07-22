package com.android.spartanrides;

/**
 * Created by venka on 07/20/17.
 *
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class spartan_post extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static final String PLACES_TAG = "Google Places";

    protected GoogleApiClient mGoogleApiClient;
    protected GoogleApiClient mGoogleApiClient2;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private static final LatLngBounds BOUNDS_INDIA2 = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private EditText mAutocompleteViewSource;
    private EditText mAutocompleteViewDest;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private LinearLayoutManager mLinearLayoutManager;
    private LinearLayoutManager mLinearLayoutManager2;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter2;
    private CharSequence textData = "";

    ImageView delete;
    ImageView delete2;
    Button button;

    public String sourceVal="";
    public String destVal = "";
    public String dateVal = "";
    public String timeVal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        buildGoogleApiClient2();
        setContentView(R.layout.activity_post);

        final TextView postView = (EditText) findViewById(R.id.post_source);
        final TextView destView = (EditText) findViewById(R.id.post_destination);

        button = (Button) findViewById(R.id.submit_post);

        mAutocompleteViewSource = (EditText)findViewById(R.id.post_source);
        mAutocompleteViewDest = (EditText)findViewById(R.id.post_destination);
        delete=(ImageView)findViewById(R.id.cross_post);
        delete2=(ImageView)findViewById(R.id.cross_dest);

        mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.search_row,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mAutoCompleteAdapter2 =  new PlacesAutoCompleteAdapter(this, R.layout.search_row,
                mGoogleApiClient2, BOUNDS_INDIA2, null);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView2=(RecyclerView)findViewById(R.id.recyclerView2);
        mLinearLayoutManager=new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager2=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView2.setLayoutManager(mLinearLayoutManager2);
        mRecyclerView.setAdapter(mAutoCompleteAdapter);
        mRecyclerView2.setAdapter(mAutoCompleteAdapter2);
        button.setOnClickListener(this);
        delete.setOnClickListener(this);
        delete2.setOnClickListener(this);

        /*OnClick Listener for Source Field*/
        mAutocompleteViewSource.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    Log.e(PLACES_TAG,API_NOT_CONNECTED);
                }
                clearOtherBoxes(postView);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });


        /*OnClick Listener for Destination Field*/
        mAutocompleteViewDest.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient2.isConnected()) {mAutoCompleteAdapter2.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient2.isConnected()){
                    Toast.makeText(getApplicationContext(), API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    Log.e(PLACES_TAG,API_NOT_CONNECTED);
                }
                clearOtherBoxes(destView);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
                mRecyclerView2.setVisibility(View.VISIBLE);
            }
        });

        /*OnTouch Listener for Selection of Source*/
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);

                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */
                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

                        textData = item.description;
                        mAutocompleteViewSource.setVisibility(View.INVISIBLE);
                        mAutocompleteViewDest.setVisibility(View.INVISIBLE);
                        sourceVal = textData.toString();
                        showAllBoxes();
                    }
                })
        );

        /*OnTouch Listener for Selection of Destination*/
        mRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter2.getItem(position);
                        final String placeId = String.valueOf(item.placeId);
                        Log.i("TAG", "Autocomplete item selected: " + item.description);

                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */
                        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                .getPlaceById(mGoogleApiClient2, placeId);
                        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if(places.getCount()==1){
                                    //Do the things here on Click.....
                                    Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getLatLng()),Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Log.i("TAG", "Clicked: " + item.description);
                        Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);

                        textData = item.description;
                        mAutocompleteViewSource.setVisibility(View.INVISIBLE);
                        mAutocompleteViewDest.setVisibility(View.INVISIBLE);
                        destVal = textData.toString();
                        showAllBoxes();
                    }
                })
        );


    }



    public void showAllBoxes()
    {
        TextView postText = (EditText) findViewById(R.id.post_source);
        TextView destText = (EditText) findViewById(R.id.post_destination);
        TextView dateText = (EditText) findViewById(R.id.post_date);
        TextView timeText = (EditText) findViewById(R.id.post_time);
        postText.setText(sourceVal);
        destText.setText(destVal);

        ImageView crossPost = (ImageView) findViewById(R.id.cross_post);
        ImageView crossDate = (ImageView) findViewById(R.id.cross_date);
        ImageView crossTime = (ImageView) findViewById(R.id.cross_time);
        ImageView crossDest = (ImageView) findViewById(R.id.cross_dest);
        Button button = (Button) findViewById(R.id.submit_post);
        postText.setVisibility(View.VISIBLE);
        destText.setVisibility(View.VISIBLE);
        dateText.setVisibility(View.VISIBLE);
        timeText.setVisibility(View.VISIBLE);
        crossDest.setVisibility(View.VISIBLE);
        crossDate.setVisibility(View.VISIBLE);
        crossTime.setVisibility(View.VISIBLE);
        crossPost.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRecyclerView2.setVisibility(View.INVISIBLE);
    }

    public void clearOtherBoxes(TextView textView)
    {
        TextView postText = (EditText) findViewById(R.id.post_source);
        TextView destText = (EditText) findViewById(R.id.post_destination);
        TextView dateText = (EditText) findViewById(R.id.post_date);
        TextView timeText = (EditText) findViewById(R.id.post_time);
        ImageView crossPost = (ImageView) findViewById(R.id.cross_post);
        ImageView crossDate = (ImageView) findViewById(R.id.cross_date);
        ImageView crossTime = (ImageView) findViewById(R.id.cross_time);
        ImageView crossDest = (ImageView) findViewById(R.id.cross_dest);

        Button button = (Button) findViewById(R.id.submit_post);
        destText.setVisibility(View.INVISIBLE);
        dateText.setVisibility(View.INVISIBLE);
        timeText.setVisibility(View.INVISIBLE);
        postText.setVisibility(View.INVISIBLE);
        crossDest.setVisibility(View.INVISIBLE);
        crossDate.setVisibility(View.INVISIBLE);
        crossTime.setVisibility(View.INVISIBLE);
        crossPost.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        if(textView.getId()==R.id.post_destination)
            postText.setVisibility(View.VISIBLE);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    protected synchronized void buildGoogleApiClient2() {
        mGoogleApiClient2 = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        Toast.makeText(this, API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if(v==button) {
            try {
                convertToJSON();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(v==delete && v.getId()==R.id.cross_post){
            mAutocompleteViewSource.setText("");
        }
        if(v==delete2 && v.getId()==R.id.cross_dest){
            mAutocompleteViewDest.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
        if (!mGoogleApiClient2.isConnected() && !mGoogleApiClient2.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient2.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
        if(mGoogleApiClient2.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient2.disconnect();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
