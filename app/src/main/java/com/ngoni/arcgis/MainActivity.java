package com.ngoni.arcgis;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = findViewById(R.id.arc_map);
        setUpMap();
    }

    private void setUpMap() {
        ArcGISRuntimeEnvironment.setApiKey(BuildConfig.API_KEY);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_MIDCENTURY);
        mMapView.setMap(map);
        mMapView.setViewpoint(new Viewpoint(-17.824858, 31.053028, 72000.0));
        /*-17.8577368116804, 31.038344197312732*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }
}