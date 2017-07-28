package com.android.spartanrides;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class spartan_chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        ImageView v =  (ImageView) findViewById(R.id.chatLogo);
//
//        Bitmap blurredBitmap = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            blurredBitmap = BlurBitmap.blur( this, BitmapFactory.decodeResource(getResources(), R.drawable.spartanlogo) );
//            v.setImageBitmap(blurredBitmap);
//        }

    }
}
