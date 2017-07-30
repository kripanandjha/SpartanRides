package com.android.spartanrides;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class user_info extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private TextView profileUsername, emailid;
    private String name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        findViewById(R.id.signoutButton).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        googleApiClient.connect();
        super.onStart();

        profileUsername = (TextView)findViewById(R.id.username);
        emailid = (TextView)findViewById(R.id.emailid);

        Intent i = this.getIntent();
        name = i.getStringExtra("PROFILE_DISPLAY_NAME");
        email =i.getStringExtra("USER_PROFILE_EMAIL");

        profileUsername.setText("Name: "+name);
        emailid.setText("EMAIL_ID: "+email);
    }

    private void SignOut() {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent i = new Intent(user_info.this,LoginActivity.class);
                        startActivity(i);
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signoutButton:
                SignOut();
                break;

           /* case R.id.sign_out_button:
                SignOut();
                break;
        */}
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
