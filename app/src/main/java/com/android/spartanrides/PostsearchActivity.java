package com.android.spartanrides;

import android.app.ListActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;

import java.lang.reflect.Array;

public class PostsearchActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_postsearch);
//        displayData();
//        PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        // listen refresh event
//        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // get data from server and display the list
//                displayData();
//
//            }
//        });
//    }
//
//        private void displayData(){
//        Bundle b = getIntent().getExtras();
//        String message = "";
//        String[] postList;
//        if(b != null)
//            message = b.getString("message");
//        if(!message.equals(""))
//            postList = new String[]{message, "test"};
//        else
//            postList = new String[]{"test"};
//        ListView listView = (ListView) findViewById(R.id.postList);
//        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,postList);
//        listView.setAdapter(arrayAdapter);
//    }


    int[] IMAGES = {R.drawable.prf,R.drawable.spartanlogo,R.drawable.a};
    String[] from = {"firsS", "SecondS", "thirdS"};
    String[] to = {"firsD", "SecondD", "thirdD"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear_layout);
        ListView listView = (ListView) findViewById(R.id.travellersList);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

    }
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return IMAGES.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.custom_layout,null);
            ImageView imageView = view.findViewById(R.id.imageView);
            TextView name = view.findViewById(R.id.postName);
            TextView source = view.findViewById(R.id.postSource);

            name.setFocusable(false);
            name.setClickable(false);
            name.setCursorVisible(false);

            source.setFocusable(false);
            source.setClickable(false);
            source.setCursorVisible(false);

            imageView.setImageResource(IMAGES[i]);
            name.setText(from[i]);
            source.setText(to[i]);
            return view;
        }
    }
}
