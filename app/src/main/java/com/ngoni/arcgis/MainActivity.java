package com.ngoni.arcgis;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import com.ngoni.arcgis.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MapView mMapView;
    private Location mLocation;
    private GraphicsOverlay mGraphicsOverlay;
    private LocatorTask mLocatorTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mMapView = mBinding.arcMap;
        mLocation = new Location(MainActivity.this);
        mGraphicsOverlay = new GraphicsOverlay();
        mLocatorTask = new LocatorTask("https://geocode.arcgis.com/arcgis/rest/services/World/GeocodeServer");
        setUpMap();
        checkPermissions();
    }

    private void setUpMap() {
        ArcGISRuntimeEnvironment.setApiKey(BuildConfig.API_KEY);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_COMMUNITY);
        mMapView.setMap(map);
        mMapView.setViewpoint(new Viewpoint(mLocation.getLatitude(), mLocation.getLongitude(), 72000.0));
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

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

    private void checkPermissions() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchViewListener() {
        SearchView searchView;
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                geoCode(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void geoCode(String query) {
        GeocodeParameters geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*");
        geocodeParameters.setMaxResults(1);
        geocodeParameters.setOutputSpatialReference(mMapView.getSpatialReference());
        ListenableFuture listenableFuture = mLocatorTask.geocodeAsync(query, geocodeParameters);
        listenableFuture.addDoneListener(() -> {

            try {

                //todo: continue here
                if ("".isEmpty()) {
                } else {
                    Toast.makeText(this, "no result found", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Log.e(MainActivity.class.getSimpleName(), "Error getting result" + e.getLocalizedMessage());

            }

        });

    }

}