package com.example.raunaktiwari.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 0;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    FloatingActionButton fab;
    private FirebaseAuth auth = FirebaseAuth.getInstance();


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
                displayChatMessage();
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
            //Load the content
            displayChatMessage();
        }

    }



    //FirebaseUI has a very handy class called FirebaseListAdapter, which greatly reduces the effort required to populate
    //a Listview using data present in the Firebase real-time database.
    private void displayChatMessage() {
        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,FirebaseDatabase.getInstance().getReference())
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
        listOfMessage.setAdapter(adapter);

    }
}
