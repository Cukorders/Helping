package com.cukorders.helping;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    String s1[], s2[];
    int images[] = {R.drawable.logo_charcter,R.drawable.logo_charcter,R.drawable.logo_charcter,R.drawable.logo_charcter,R.drawable.logo_charcter,R.drawable.logo_charcter};

    @Override
    protected void onCreate(Bundle savedInStanceState) {
        super.onCreate(savedInStanceState);
        setContentView(R.layout.notice);

        recyclerView = findViewById(R.id.notice_recyclerview);

        s1 = getResources().getStringArray(R.array.notice);
        s2 = getResources().getStringArray(R.array.notice_date);

        NoticeAdapter noticeAdapter = new NoticeAdapter(this, s1, s2, images);

        recyclerView.setAdapter(noticeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
