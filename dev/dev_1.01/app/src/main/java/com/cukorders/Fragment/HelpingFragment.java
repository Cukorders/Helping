package com.cukorders.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cukorders.Adapter.PageAdapter_Mycalling;
import com.cukorders.Adapter.PostAdapter;
import com.cukorders.helping.InitPost;
import com.cukorders.helping.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class HelpingFragment extends Fragment {
    private RecyclerView MyCallingPostListsView;
    private PageAdapter_Mycalling mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<InitPost> mPost=new ArrayList<InitPost>();
    //DB에서  post 연결
    private DatabaseReference postRef;
    public HelpingFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.helping_mission,container,false);
    }
}

