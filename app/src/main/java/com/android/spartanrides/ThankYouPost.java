package com.android.spartanrides;

/**
 * Created by venka on 7/21/2017.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ThankYouPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thank_you);
        TextView textView = (TextView)findViewById(R.id.thankYouText);
        Typeface typeface;
        try {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
            textView.setTypeface(typeface);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ImageView v = (ImageView) findViewById(R.id.thankYouLogo);
        Bitmap blurredBitmap = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurredBitmap = BlurBitmap.blur( getApplicationContext(), BitmapFactory.decodeResource(getResources(), R.drawable.spartanlogo));
            v.setImageBitmap(blurredBitmap);
        }
    }

    public void returnHome(View view)
    {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}