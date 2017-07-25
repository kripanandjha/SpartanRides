package com.android.spartanrides;

/**
 * Created by venka on 07/20/17.
 *
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class spartan_post extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static final String PLACES_TAG = "Google Places";

    protected GoogleApiClient mGoogleApiClient,mGoogleApiClient2;

    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private static final LatLngBounds BOUNDS_INDIA2 = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));

    private EditText mAutocompleteViewSource, mAutocompleteViewDest;
    private RecyclerView mRecyclerView, mRecyclerView2;
    private LinearLayoutManager mLinearLayoutManager,mLinearLayoutManager2;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter, mAutoCompleteAdapter2;
    private CharSequence textData = "";

    ImageView delete, delete2;
    Button submit, cancel;
    TextView sourceView, destView;
    EditText dateView, timeView;

    public String sourceVal= "";
    public String destVal  = "";
    public String dateVal  = "";
    public String timeVal  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        buildGoogleApiClient2();
        setContentView(R.layout.activity_post);

        ImageView v = (ImageView) findViewById(R.id.postLogo);
        Bitmap blurredBitmap;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurredBitmap = BlurBitmap.blur( getApplicationContext(), BitmapFactory.decodeResource(getResources(), R.drawable.spartanlogo));
            v.setImageBitmap(blurredBitmap);
        }

        sourceView = (EditText) findViewById(R.id.post_source);
        destView = (EditText) findViewById(R.id.post_destination);
        submit = (Button) findViewById(R.id.submit_post);
        cancel = (Button) findViewById(R.id.cancel_post);
        dateView = (EditText) findViewById(R.id.post_date);
        timeView = (EditText) findViewById(R.id.post_time);
        delete = (ImageView)findViewById(R.id.cross_post);
        delete2 = (ImageView)findViewById(R.id.cross_dest);


        /* Set onClick Listeners various events in PostActivity*/
        dateView.setOnClickListener(this);
        timeView.setOnClickListener(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        delete.setOnClickListener(this);
        delete2.setOnClickListener(this);

        /*Initializing values for Location Autocomplete*/
        mAutocompleteViewSource = (EditText)findViewById(R.id.post_source);
        mAutocompleteViewDest = (EditText)findViewById(R.id.post_destination);

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

        /*OnClick Listener for Source Field*/
        mAutocompleteViewSource.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {mAutoCompleteAdapter.getFilter().filter(s.toString());
                }else if(!mGoogleApiClient.isConnected()){
                    Toast.makeText(getApplicationContext(), API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                    Log.e(PLACES_TAG,API_NOT_CONNECTED);
                }
                clearOtherBoxes(sourceView);
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

                mRecyclerView.setVisibility(View.VISIBLE);
                sourceView.requestFocus();
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
                destView.requestFocus();
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

    private void showAllBoxes()
    {
        TextView postText = (EditText) findViewById(R.id.post_source);
        TextView destText = (EditText) findViewById(R.id.post_destination);
        ImageView crossPost = (ImageView) findViewById(R.id.cross_post);
        ImageView crossDate = (ImageView) findViewById(R.id.cross_date);
        ImageView crossDest = (ImageView) findViewById(R.id.cross_dest);
        View dateText = findViewById(R.id.textLayout);
        postText.setText(sourceVal);
        destText.setText(destVal);
        View button = findViewById(R.id.buttonLayout);
        postText.setVisibility(View.VISIBLE);
        destText.setVisibility(View.VISIBLE);
        dateText.setVisibility(View.VISIBLE);
        crossDate.setVisibility(View.VISIBLE);
        crossPost.setVisibility(View.VISIBLE);
        crossDest.setVisibility(View.VISIBLE);
        button.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRecyclerView2.setVisibility(View.INVISIBLE);
    }

    private void clearOtherBoxes(TextView textView)
    {
        TextView postText = (EditText) findViewById(R.id.post_source);
        TextView destText = (EditText) findViewById(R.id.post_destination);
        View dateText = findViewById(R.id.textLayout);
        ImageView crossPost = (ImageView) findViewById(R.id.cross_post);
        ImageView crossDest = (ImageView) findViewById(R.id.cross_dest);
        ImageView crossDate = (ImageView) findViewById(R.id.cross_date);

        View button = findViewById(R.id.buttonLayout);
        destText.setVisibility(View.INVISIBLE);
        dateText.setVisibility(View.INVISIBLE);
        postText.setVisibility(View.INVISIBLE);
        crossDate.setVisibility(View.INVISIBLE);
        crossPost.setVisibility(View.INVISIBLE);
        crossDest.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        if(textView.getId()==R.id.post_destination)
            postText.setVisibility(View.VISIBLE);
    }

    private JSONObject convertToJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Source", sourceVal);
        jsonObject.put("Destination", destVal);
        jsonObject.put("Date", dateVal);
        jsonObject.put("Time", timeVal);
        JSONActivity.printJSON(jsonObject);
        return jsonObject;
    }

    private String getMessage(){
        String message = "Venkat"+" "+"would be driving to "+destVal+
                " from "+sourceVal+" on "+dateVal+" at "+timeVal;
        return message;
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


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        TextView dateView = (TextView) findViewById(R.id.post_date);
        dateView.setText(sdf.format(myCalendar.getTime()));
        dateVal = sdf.format(myCalendar.getTime()).toString();
    }

    @Override
    public void onClick(View v) {
        if(v==submit) {
            if(!sourceView.getText().toString().isEmpty() && !destView.getText().toString().isEmpty()
                    && !dateView.getText().toString().isEmpty() && !timeView.getText().toString().isEmpty()) {
                try {
                    convertToJSON();
                    Intent intent = new Intent(this, PostsearchActivity.class);
                    Bundle b = new Bundle();
                    b.putString("message", getMessage());
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{

                Snackbar.make(v,"Please enter all the values before posting",Snackbar.LENGTH_SHORT).show();
            }
        }

        if(v==findViewById(R.id.post_date)) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
            datePickerDialog.show();
        }

        if(v==findViewById(R.id.post_time))
        {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    timeView.setText( selectedHour + ":" + selectedMinute);
                    timeVal = (selectedHour + ":" + selectedMinute).toString();
                }
            }, hour, minute, false);
            mTimePicker.show();
        }

        if(v==delete && v.getId()==R.id.cross_post && !mAutocompleteViewSource.getText().toString().equals("")){
            mAutocompleteViewSource.setText("");
        }
        if(v==delete2 && v.getId()==R.id.cross_dest && !mAutocompleteViewDest.getText().toString().equals("")){
            mAutocompleteViewDest.setText("");
        }

        if(v==cancel)
        {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
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
