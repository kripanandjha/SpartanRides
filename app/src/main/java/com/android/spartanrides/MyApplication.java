package com.android.spartanrides;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by jhakr on 02-Jul-2017.
 */


public class MyApplication extends Application {
//    private GoogleApiClient mGoogleApiClient;
//    private GoogleSignInOptions gso;
//    public AppCompatActivity activity;
//    public GoogleSignInOptions getGoogleSignInOptions(){
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        return gso;
//    }
//    public GoogleApiClient getGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
//        this.activity = activity;
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this.activity, listener)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
//                .build();
//        return mGoogleApiClient;
//    }
//
//    private void SignOut() {
//
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        //Intent i = new Intent(Main2Activity.this, MainActivity.class);
//                        //startActivity(i);
//                    }
//                }
//        );
//    }
}