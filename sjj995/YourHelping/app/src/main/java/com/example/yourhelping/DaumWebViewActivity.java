package com.example.yourhelping;

import android.os.Bundle;
import net.daum.mf.map.api.MapView;

//TODO kakao map view or kakao doromyung juso search or both


public class DaumWebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_myarea);

        MapView mapView = new MapView(this);
        ViewGroup mapViewContainer = findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
    }
}