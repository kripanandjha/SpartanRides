package com.android.spartanrides;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by venka on 6/22/2017.
 */

public class MyFirebaseInstanceID extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {
        String recentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN,recentToken);

    }
}
