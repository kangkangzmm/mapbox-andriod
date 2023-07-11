package com.yinglan.scrolllayout.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MapBoxActivity extends Fragment implements
        OnMapReadyCallback, PermissionsListener,
        MapboxMap.OnMapClickListener, MapView.OnDidFinishLoadingStyleListener {

    private FragmentActivity context;
    private MapView mapView;
    private MapboxMap mapboxMap;
    private boolean streetsStyleWaterShouldEqualToolbarColor = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Mapbox.getInstance(context, getString(R.string.mapbox_access_token));
//        mapView =findViewById(R.id.main_mapView);
//        mapView.onCreate(savedInstanceState);
//        mapView.getMapAsync((OnMapReadyCallback) this.mapView);
        return inflater.inflate(R.layout.vehicle_map_fragment_layout, container, false);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {

    }

    @Override
    public void onDidFinishLoadingStyle() {

    }

    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        return false;
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS,
                new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        mapView.addOnDidFinishLoadingStyleListener(MapBoxActivity.this);
                        MapBoxActivity.this.mapboxMap = mapboxMap;

//                        if (streetsStyleWaterShouldEqualToolbarColor) {
//                            changeWaterColorToDifferentColor(style);
//                        }

//                        scooterClustersVisible = false;
//                        scooterClustersLayersAlreadyAdded = false;
//
//                        initBuildingPlugin(style);
//
//                        setUpMapData(style);

//                        view.findViewById(R.id.start_turn_by_turn_navigate_to_bike_button).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (View.VISIBLE == view.findViewById(R.id.single_vehicle_distance_and_time_info_cardview).getVisibility()) {
//                                    view.findViewById(R.id.main_mapView).setVisibility(View.INVISIBLE);
//
//                                    startNavigation(useNavigationLauncher, currentSelectedVehicleLatLng);
//                                }
//                            }
//                        });

                        MapBoxActivity.this.mapboxMap.addOnMapClickListener(MapBoxActivity.this);

//                        view.findViewById(R.id.device_location_fab).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                        locationComponent != null && locationComponent.getLastKnownLocation() != null ?
//                                                new LatLng(locationComponent.getLastKnownLocation()) : MIDDLE_OF_SF_COORDINATES, 15), 15);
//                            }
//                        });

//                        enableLocationComponent(style);
//
//                        enableLocalizationPlugin(style);
                    }
                });
    }
}
