package com.android.spartanrides;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private LinearLayout Prof_section;
    private Button Signout;
    private SignInButton SignIn;
    private TextView Name, Email;
    private ImageView Prof_pic;
    private TextView mStatusTextView;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;

    public static final String PROFILE_USER_ID = "USER_ID";
    public static final String PROFILE_DISPLAY_NAME = "PROFILE_DISPLAY_NAME";
    public static final String PROFILE_USER_EMAIL = "USER_PROFILE_EMAIL";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.signinButton).setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.signinButton:
                SignIn();
                break;

           /* case R.id.sign_out_button:
                SignOut();
                break;
        */}

    }

    private void SignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);


    }

    private void SignOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mStatusTextView.setText("Sign in ");
                    }
                }
        );

    }

    private void handleResult(GoogleSignInResult result) {

        if(result.isSuccess())
        {

            GoogleSignInAccount acct = result.getSignInAccount();
            //if (acct.getEmail().indexOf("@sjsu.edu") != -1)
            {
                googleApiClient.connect();
                // update UI
               //Intent i = new Intent(MainActivity.this,user_info.class);
               Intent i = new Intent(MainActivity.this,Main2Activity.class);
               i.putExtra(PROFILE_USER_EMAIL,acct.getEmail());
               i.putExtra(PROFILE_USER_ID,acct.getId());
               i.putExtra(PROFILE_DISPLAY_NAME,acct.getDisplayName());
               i.putExtra(PROFILE_IMAGE_URL,acct.getPhotoUrl());
               startActivity(i);
            }
/*
            else
            {
                //signed in with email which is not sjsu domain
                SignOut();

            }
            */
        }
        else
        {
            mStatusTextView.setText("Sign in Failed!");
            // set update UI false
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    public GoogleApiClient getApiClient()
    {
        return googleApiClient;
    }

}
