package com.android.spartanrides;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

public class Main2Activity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
  /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.chat);
        tabLayout.getTabAt(1).setIcon(R.drawable.ride);
        tabLayout.getTabAt(2).setIcon(R.drawable.prf);
        tabLayout.getTabAt(3).setIcon(R.drawable.help);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

        if(id== R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(mViewPager,"You have been signed out.",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseListAdapter<ChatMessage> adapter;
        FloatingActionButton fab;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_chat, container, false);

            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1: {
                    rootView = inflater.inflate(R.layout.activity_main, container, false);
                    final ListView listOfMessage = rootView.findViewById(R.id.list_of_message);
                    displayChatMessage(listOfMessage);
                    scrollListViewToBottom(listOfMessage);
                    fab = (FloatingActionButton)rootView.findViewById(R.id.fab);

                    final View finalRootView = rootView;
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Getting the DatabaseReference object using the getReference() method of the FirebaseDatabase class
                           EditText input = (EditText) finalRootView.findViewById(R.id.input);
                            FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                            input.setText("");
                            listOfMessage.smoothScrollToPosition(adapter.getCount()-1);
                            scrollListViewToBottom(listOfMessage);
                        }
                    });

                    break;
                }
                case 2: {
                     /*
                    Post data acepting User Inputs
                    */
                    rootView = inflater.inflate(R.layout.search_or_post, container, false);
                    TextView textView = rootView.findViewById(R.id.searchPostText);
                    Typeface typeface;

                    ImageView v =  rootView.findViewById(R.id.searchPostLogo);
                    blur(v);

                    try {
                        typeface = Typeface.createFromAsset(container.getContext().getAssets(), "fonts/Pacifico.ttf");
                        textView.setTypeface(typeface);
                        textView.setTextColor(Color.parseColor("#000000"));
                        textView.setTextSize(30);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                }
                case 3:
                {
                    rootView = inflater.inflate(R.layout.fragment_main2, container, false);
                    TextView name = (TextView)rootView.findViewById(R.id.username);
                    TextView email = (TextView)rootView.findViewById(R.id.emailid);
                    Button signout = (Button)rootView.findViewById(R.id.signoutButton);
                    break;
                }
                case 4:
                {
                    rootView = inflater.inflate(R.layout.activity_help, container, false);
                    ImageView v =  rootView.findViewById(R.id.helpLogo);
                    blur(v);
                    break;
                }
            }
            return rootView;
        }

        private void blur(ImageView v)
        {
            Bitmap blurredBitmap = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                blurredBitmap = BlurBitmap.blur( getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.spartanlogo) );
                v.setImageBitmap(blurredBitmap);
            }
        }

        public void displayChatMessage(ListView view) {

            adapter = new FirebaseListAdapter<ChatMessage>(getActivity(),ChatMessage.class,R.layout.list_item, FirebaseDatabase.getInstance().getReference())
            {
                //FirebaseListAdapter is an abstract class and has an abstract method populateView(), which must be overriden.
                //populateView() method is used to populate the views of each list item.
                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    //Get references to the views of the list_item.xml
                    TextView messageText,messageUser,messageTime;
                    messageText = (TextView) v.findViewById(R.id.message_text);
                    messageUser = (TextView) v.findViewById(R.id.message_user);
                    messageTime = (TextView) v.findViewById(R.id.message_time);

                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
                }
            };
            view.setAdapter(adapter);
            view.smoothScrollToPosition(adapter.getCount()-1);
            scrollListViewToBottom(view);

        }

        private void scrollListViewToBottom(final ListView myListView)
        {
            myListView.post(new Runnable() {
                @Override
                public void run()
                { // Select the last row so it will scroll into view...
                    myListView.setSelection(adapter.getCount() - 1);
                }
            });
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    public void doPost(View view)
    {
        Intent intent = new Intent(this, spartan_post.class);
        startActivity(intent);
    }

    public void doSearch(View view)
    {
        Intent intent = new Intent(this, PostsearchActivity.class);
        startActivity(intent);
    }

    public void submitSuggestion(View view)
    {
        String ratingBar =  String.valueOf(((RatingBar)findViewById(R.id.ratingBar2)).getRating());
        String suggestionText =  ((TextView)findViewById(R.id.suggestionText)).getText().toString();
        InputMethodManager imm = (InputMethodManager)this.getSystemService(this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (!ratingBar.equals("0.0")) {
            HelpActivity helpActivity = new HelpActivity();
            try {
                Snackbar.make(view, "Thank you for your feedback!", Snackbar.LENGTH_INDEFINITE).show();
                new JSONActivity().JSONTransmitter(helpActivity.convertToJSON("venkat", ratingBar, suggestionText));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            Snackbar.make(view, "Oops! Seems like you you forgot to rate us.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
