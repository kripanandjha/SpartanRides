package com.android.spartanrides;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 0;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public static final String PROFILE_USER_ID = "USER_ID";
    public static final String PROFILE_DISPLAY_NAME = "PROFILE_DISPLAY_NAME";
    public static final String PROFILE_USER_EMAIL = "USER_PROFILE_EMAIL";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main,"You have been signed out.",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    //Once the user has signed in, MainActivity will recieve a result in the form of an intent.
    //To handle it, we are overriding the onActivityResult() method.
    //If the result's code is RESULT_OK, it means the user has successfully logged in.
    //If so then we will call the displayChatMessage method again or else will call the finish() method to close the app.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
            {
            if(resultCode == RESULT_OK)
                {
                Snackbar.make(activity_main,"Successfully signed in. Welcome!",Snackbar.LENGTH_SHORT).show();
                    Intent i = new Intent(this.getApplicationContext(),Main2Activity.class);
                    i.putExtra(PROFILE_USER_EMAIL,FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    i.putExtra(PROFILE_USER_ID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                    i.putExtra(PROFILE_DISPLAY_NAME,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    i.putExtra(PROFILE_IMAGE_URL,FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    startActivity(i);
                }
            else
                {
                    Snackbar.make(activity_main,"We couldn't sign you in. Please try again later.",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            }

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            activity_main = (RelativeLayout)findViewById(R.id.activity_main);
            fab = (FloatingActionButton)findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Getting the DatabaseReference object using the getReference() method of the FirebaseDatabase class
                    EditText input = (EditText)findViewById(R.id.input);
                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                    input.setText("");
                }


            });


            //Check if not signed in the navigate to sign-in page
            if(auth.getCurrentUser() == null)
            {
                //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(AuthUI.FACEBOOK_PROVIDER,AuthUI.EMAIL_PROVIDER).build(),1);
            }
            else
            {
                Snackbar.make(activity_main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
                Intent i = new Intent(this.getApplicationContext(),Main2Activity.class);
                i.putExtra(PROFILE_USER_EMAIL,FirebaseAuth.getInstance().getCurrentUser().getEmail());
                i.putExtra(PROFILE_USER_ID,FirebaseAuth.getInstance().getCurrentUser().getUid());
                i.putExtra(PROFILE_DISPLAY_NAME,FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                i.putExtra(PROFILE_IMAGE_URL,FirebaseAuth.getInstance().getCurrentUser().getEmail());
                startActivity(i);
                finish();
            }
        }



        //FirebaseUI has a very handy class called FirebaseListAdapter, which greatly reduces the effort required to populate
        //a Listview using data present in the Firebase real-time database.

}