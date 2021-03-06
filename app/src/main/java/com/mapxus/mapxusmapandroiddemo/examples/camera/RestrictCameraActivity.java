package com.mapxus.mapxusmapandroiddemo.examples.camera;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.mapxusmapandroiddemo.R;


/**
 * Restrict the map camera to certain bounds.
 */
public class RestrictCameraActivity extends AppCompatActivity implements OnMapReadyCallback {


    private MapView mapView;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private LatLngBounds latLngBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera_restrict);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(RestrictCameraActivity.this,mapView);
        mapView.getMapAsync(this);

        LatLng southweast = new LatLng(22.3702127,114.110572);
        LatLng northeast = new LatLng(22.3710112,114.1116603);
        latLngBounds = LatLngBounds.from(northeast.getLatitude(),northeast.getLongitude(),southweast.getLatitude(),southweast.getLongitude());
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));//

        // Set bounds
        mapboxMap.setLatLngBoundsForCameraTarget(latLngBounds);
        mapboxMap.setMinZoomPreference(17);
        mapboxMap.setMaxZoomPreference(19);

        // Visualise bounds area
//        showBoundsArea();
//
//        showCrosshair();

    }

    private void showBoundsArea() {
        PolygonOptions boundsArea = new PolygonOptions();

        boundsArea.add(latLngBounds.getNorthWest());
        boundsArea.add(latLngBounds.getNorthEast());
        boundsArea.add(latLngBounds.getSouthEast());
        boundsArea.add(latLngBounds.getSouthWest());

        boundsArea.fillColor(getResources().getColor(R.color.bound_polygon_color));
        mapboxMap.addPolygon(boundsArea);
    }

    private void showCrosshair() {
        View crosshair = new View(this);
        crosshair.setLayoutParams(new FrameLayout.LayoutParams(15, 15, Gravity.CENTER));
        crosshair.setBackgroundColor(Color.GREEN);
        mapView.addView(crosshair);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}