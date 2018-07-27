package com.mapxus.mapxusmapandroiddemo.examples.searchservices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapxus.mapxusmapandroiddemo.R;
import com.mapxus.mapxusmapandroiddemo.model.overlay.MyPoiOverlay;
import com.mapxus.services.PoiSearch;
import com.mapxus.services.model.InBuildingSearchOption;
import com.mapxus.services.model.poi.PoiDetailResult;
import com.mapxus.services.model.poi.PoiResult;

/**
 * Use MapxusMap Search Services to request directions
 */
public class SearchPoiInBuildingActivity extends AppCompatActivity implements OnMapReadyCallback, PoiSearch.PoiSearchResultListener {

    private static final String TAG = "SearchPoiInBuildingActivity";

    private MapView mapView;
    private MapboxMap mapboxMap;

    private String keyWord = "";
    private EditText mSearchText;
    private EditText mBuildingText;

    private int currentPage;
    private PoiSearch poiSearch;
    private MyPoiOverlay poiOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchservices_search_poi_in_building);

        // Setup the MapView
        mapView =  findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        TextView searchButton = (TextView) findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_search:
                        doSearchQuery();
                        break;

                    default:
                        break;
                }
            }
        });

        mSearchText = (EditText) findViewById(R.id.input_edittext);
        mBuildingText = (EditText) findViewById(R.id.building_edittext);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setPoiSearchResultListener(this);

    }

    @Override
    public void onMapReady(MapboxMap beeMap) {
        mapboxMap = beeMap;
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
        if (poiSearch != null) {
            poiSearch.destroy();
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * 开始进行poi搜索
     */
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        keyWord = mSearchText.getText().toString().trim();

        String buildingId = mBuildingText.getText().toString().trim();

        InBuildingSearchOption inBuildingSearchOption = new InBuildingSearchOption();
        inBuildingSearchOption.buildingId(buildingId);
        inBuildingSearchOption.keyword(keyWord);

        poiSearch.searchInBuilding(inBuildingSearchOption);
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.status != 0) {
            Toast.makeText(this, poiResult.error.toString(), Toast.LENGTH_LONG).show();
            return;
        }
        if (poiResult.getAllPoi() == null || poiResult.getAllPoi().isEmpty()) {
            Toast.makeText(this, getString(R.string.no_result), Toast.LENGTH_LONG).show();
            return;
        }

        mapboxMap.clear();
        poiOverlay = new MyPoiOverlay(mapboxMap, poiResult.getAllPoi());
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }


}


