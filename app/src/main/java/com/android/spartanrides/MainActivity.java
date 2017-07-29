package com.android.spartanrides;

import android.app.ProgressDialog;
import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Demonstrate Firebase Authentication using a Facebook access token.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "FacebookLogin";

    private TextView mStatusTextView;
    private TextView mDetailTextView;


    //Chat details
    String firebaseUser;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        // Views
//        mStatusTextView = (TextView) findViewById(R.id.status);
//        mDetailTextView = (TextView) findViewById(R.id.detail);
//        findViewById(R.id.button_facebook_signout).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                loginButton.setVisibility(View.GONE);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_activity_result]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    private void updateUI(FirebaseUser user) {
//        hideProgressDialog();
        if (user != null) {
//            mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
//            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
            Log.d("LOGIN MSG","Logged in Successfully");
            findViewById(R.id.login_button).setVisibility(View.GONE);
            updateUserInfo(user);
            addUser();
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            startActivity(intent);
//            findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
//            mStatusTextView.setText(R.string.signed_out);
//            mDetailTextView.setText(null);
            Log.d("LOGIN MSG","Login Failed");
            findViewById(R.id.login_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
//        if (i == R.id.button_facebook_signout) {
//            signOut();
//        }
    }

    private void addUser(){
        {
//            final ProgressDialog pd = new ProgressDialog(getApplicationContext());
//            pd.setMessage("Loading...");
//            pd.show();
            Firebase.setAndroidContext(getApplicationContext());

            String url = "https://spartanride-173019.firebaseio.com/users.json";

            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://spartanride-173019.firebaseio.com/users");

                    if(s.equals("null")) {
                        reference.child(firebaseUser).child("password").setValue("qwerty");
                        UserDetails.username = firebaseUser;
//                        Toast.makeText(getApplicationContext(), "registration successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);
                            if (!obj.has(firebaseUser)) {
                                reference.child(firebaseUser).child("password").setValue("qwerty");
                                UserDetails.username = firebaseUser;
//                                Toast.makeText(getApplicationContext(), "registration successful", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    pd.dismiss();
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
//                    pd.dismiss();
                }
            });

            RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
            rQueue.add(request);
        }
    }

    public void updateUserInfo(FirebaseUser user){
        firebaseUser = user.getDisplayName();
        UserDetails.username = firebaseUser;
        UserDetails.fbUserID = user.getUid();
        UserDetails.emailID = user.getEmail();
        UserDetails.photoURL = user.getPhotoUrl().toString();
        UserDetails.accessToken = AccessToken.getCurrentAccessToken().getToken();
    }
}