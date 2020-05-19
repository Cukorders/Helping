package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        String[] myDataset={"test1","test2","test3"};
        mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_out) {
            Toast.makeText(this, "out", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_block) {
            Toast.makeText(this, "검색 클릭", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.action_notify) {
            Toast.makeText(this, "액션버튼 클릭", Toast.LENGTH_SHORT).show();

        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {
    }
}

