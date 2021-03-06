package com.mapxus.mapxusmapandroiddemo.examples.searchservices;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.model.LatLng;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.api.map.model.SelectorPosition;
import com.mapxus.map.mapxusmap.api.map.model.overlay.MapxusMarker;
import com.mapxus.map.mapxusmap.api.services.RoutePlanning;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningPoint;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningRequest;
import com.mapxus.map.mapxusmap.api.services.model.planning.RoutePlanningResult;
import com.mapxus.map.mapxusmap.api.services.model.planning.RouteResponseDto;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.mapxus.map.mapxusmap.overlay.WalkRouteOverlay;
import com.mapxus.mapxusmapandroiddemo.R;

/**
 * Use MapxusMap Search Services to request directions
 */
public class RoutePlanningActivity extends AppCompatActivity implements RoutePlanning.RoutePlanningResultListener {

    private static final String TAG = "RoutePlanningActivity";

    private MapView mapView;
    private MapxusMap mMapxusMap;
    private MapboxMap mapboxMap;
    private MapViewProvider mapViewProvider;
    private RoutePlanning routePlanning;

    private RoutePlanningPoint origin = null;
    private RoutePlanningPoint destination = null;

    private Button planningBtn;

    private TextView pointStartTv;

    private TextView pointEndTv;


    private MapxusMarker startMarker;
    private MapxusMarker endMarker;

    private ProgressDialog dialog;

    private String vehicle = "foot";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searchservices_route_planning);

        planningBtn = findViewById(R.id.btn_planning);
        pointStartTv = findViewById(R.id.point_start);
        pointEndTv = findViewById(R.id.point_end);

        // Setup the MapView
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapViewProvider = new MapboxMapViewProvider(this, mapView);
        mapView.getMapAsync(mapboxMap -> RoutePlanningActivity.this.mapboxMap = mapboxMap);

        mapViewProvider.getMapxusMapAsync(mapxusMap -> {
            RoutePlanningActivity.this.mMapxusMap = mapxusMap;
            mMapxusMap.getMapxusUiSettings().setSelectorPosition(SelectorPosition.BOTTOM_RIGHT);
            routePlanning = RoutePlanning.newInstance();
            routePlanning.setRoutePlanningListener(RoutePlanningActivity.this);
            pointStartTv.setOnClickListener(pointClickListener);
            pointEndTv.setOnClickListener(pointClickListener);
            planningBtn.setOnClickListener(planningBtnClickListener);
        });

    }

    private void getRoute(RoutePlanningPoint origin, RoutePlanningPoint destination) {
        RoutePlanningRequest request = new RoutePlanningRequest(origin, destination);
        request.setVehicle(vehicle);
        routePlanning.route(request);
    }

    private void drawRoute(RouteResponseDto route) {
        WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(this, mapboxMap, mMapxusMap, route, origin, destination);
        walkRouteOverlay.addToMap();
    }


    //radio 选择事件
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.foot:
                if (checked)
                    vehicle = "foot";
                break;
            case R.id.wheelchair:
                if (checked)
                    vehicle = "wheelchair";
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the directions API request
        if (routePlanning != null) {
            routePlanning.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onGetRoutePlanningResult(RoutePlanningResult routePlanningResult) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (routePlanningResult.status != 0) {
            Toast.makeText(this, routePlanningResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (routePlanningResult.getRouteResponseDto() == null) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }
        RouteResponseDto routeResponseDto = routePlanningResult.getRouteResponseDto();
        drawRoute(routeResponseDto);
        mMapxusMap.switchFloor(origin.getFloor());
    }


    private View.OnClickListener pointClickListener = v -> {
        switch (v.getId()) {
            case R.id.point_start: {
                pointStartClick();
                break;
            }

            case R.id.point_end: {
                pointEndClick();
                break;
            }
        }

    };

    private View.OnClickListener planningBtnClickListener = v -> {
        if (origin == null || destination == null) {
            return;
        }
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("wait");
        dialog.setMessage("get route");
        dialog.show();
        removeAllSelectedMapClickListener();
        getRoute(origin, destination);
    };


    private void pointStartClick() {

        pointStartTv.setText("please click map");
        removeAllSelectedMapClickListener();

        mMapxusMap.addOnMapClickListener(pointStartMapClickListener);
    }

    private void pointEndClick() {
        pointEndTv.setText("please click map");
        removeAllSelectedMapClickListener();
        mMapxusMap.addOnMapClickListener(pointEndMapClickListener);
    }

    private void removeAllSelectedMapClickListener() {
        mMapxusMap.removeOnMapClickListener(pointEndMapClickListener);
        mMapxusMap.removeOnMapClickListener(pointStartMapClickListener);
    }


    private MapxusMap.OnMapClickListener pointStartMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
            origin = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());
            pointStartTv.setText(String.format("%s,%s,%s", latLng.getLatitude(), latLng.getLongitude(), floor));
            if (startMarker != null) {
                mMapxusMap.removeMarker(startMarker);
                startMarker = null;
            }
            MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
            mapxusMarkerOptions.setPosition(latLng);
            mapxusMarkerOptions.setBuildingId(buildingId);
            mapxusMarkerOptions.setFloor(floor);
            startMarker = mMapxusMap.addMarker(mapxusMarkerOptions);
        }
    };

    private MapxusMap.OnMapClickListener pointEndMapClickListener = new MapxusMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng, String floor, String buildingId, String floorId) {
            destination = new RoutePlanningPoint(buildingId, floor, latLng.getLongitude(), latLng.getLatitude());
            pointEndTv.setText(String.format("%s,%s,%s", latLng.getLatitude(), latLng.getLongitude(), floor));

            if (endMarker != null) {
                mMapxusMap.removeMarker(endMarker);
                endMarker = null;
            }
            MapxusMarkerOptions mapxusMarkerOptions = new MapxusMarkerOptions();
            mapxusMarkerOptions.setPosition(latLng);
            mapxusMarkerOptions.setBuildingId(buildingId);
            mapxusMarkerOptions.setFloor(floor);
            endMarker = mMapxusMap.addMarker(mapxusMarkerOptions);
        }
    };

}