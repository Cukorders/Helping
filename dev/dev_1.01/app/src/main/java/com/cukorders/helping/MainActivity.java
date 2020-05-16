package com.cukorders.helping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Todo Phone Auth
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private Button mLogoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mLogoutBtn = (Button) findViewById(R.id.LogoutBtn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mAuth.signOut();
                sendToAuth();
            }
        });
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // if not logged in we should go to login page
        if(currentUser==null){
            Intent authIntent = new Intent(MainActivity.this, AuthActivity.class);
            authIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            authIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(authIntent);
            finish();
        }
    }
    //when logout, go to AuthActivity page again
    private void sendToAuth() {
        Intent authIntent = new Intent (MainActivity.this, AuthActivity.class);
        startActivity(authIntent);
        finish();
    }


}
