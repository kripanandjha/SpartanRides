package com.android.spartanrides;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;

import java.lang.reflect.Array;

public class PostsearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postsearch);
        displayData();
        PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        // listen refresh event
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // get data from server and display the list
                displayData();

            }
        });
    }

        private void displayData(){
        Bundle b = getIntent().getExtras();
        String message = "";
        String[] postList;
        if(b != null)
            message = b.getString("message");
        if(!message.equals(""))
            postList = new String[]{message, "test"};
        else
            postList = new String[]{"test"};
        ListView listView = (ListView) findViewById(R.id.postList);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,postList);
        listView.setAdapter(arrayAdapter);
    }



}
