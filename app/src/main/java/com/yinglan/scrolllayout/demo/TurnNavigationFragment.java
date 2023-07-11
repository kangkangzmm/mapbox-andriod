package com.yinglan.scrolllayout.demo;




import static com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_WALKING;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.navigator.VoiceInstruction;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.yinglan.scrolllayout.demo.NavMapFragment;
import com.yinglan.scrolllayout.demo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TurnNavigationFragment extends android.app.Fragment
        implements OnNavigationReadyCallback, NavigationListener {

    public Double selectedDestinationLat;
    public Double selectedDestinationLong;
    public Double selectedOriginLat;
    public Double selectedOriginLong;

    private NavigationView navigationView;
    private Context context;

    public TurnNavigationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.context = activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            selectedDestinationLat = getArguments().getDouble("NAVIGATION_DESTINATION_LAT");
            selectedDestinationLong = getArguments().getDouble("NAVIGATION_DESTINATION_LONG");
            selectedOriginLat = getArguments().getDouble("NAVIGATION_ORIGIN_LAT");
            selectedOriginLong = getArguments().getDouble("NAVIGATION_ORIGIN_LONG");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nav_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigationView = view.findViewById(R.id.turn_by_turn_navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        NavigationRoute.builder(context)
                .accessToken("sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGZ4ajQ4cDcwNXF4M29wODVzMHF4cmoxIn0.CJEnEqnxAL3P1_snTPVfzQ")
//                .baseUrl("http://kangkangtk.gnway.cc/")
                .origin(Point.fromLngLat(selectedOriginLong, selectedOriginLat))
                .destination(Point.fromLngLat(selectedDestinationLong, selectedDestinationLat))
                .profile(PROFILE_WALKING)
//                .profile(PROFILE_DRIVING)
                .build()
//                .accessToken("sk.eyJ1IjoiemhhbmdtbSIsImEiOiJjbGFrbXM3NHYwNnM3M3BrMHVwaWR3NnFwIn0.ddu0apyK2st4E44oSwBLqw")
//                .alternatives(false)
//                .annotations()
//                .baseUrl("http://kangkangtk.gnway.cc/")
//                .profile(PROFILE_DRIVING)
//                .origin(Point.fromLngLat(selectedOriginLong, selectedOriginLat))
//                .destination(Point.fromLngLat(selectedDestinationLong, selectedDestinationLat))
//                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        NavigationViewOptions options = NavigationViewOptions.builder()
                                .navigationListener(TurnNavigationFragment.this)
                                .directionsRoute(response.body().routes().get(0))
                                .shouldSimulateRoute(false)
                                .build();
                        navigationView.startNavigation(options);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Toast.makeText(context, R.string.failure_to_retrieve, Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            navigationView.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        navigationView.onDestroy();
        getActivity().findViewById(R.id.scroll_down_layout).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.tabs_rg).setVisibility(View.VISIBLE);
    }

    @Override
    public void onCancelNavigation() {
        goBackToVehicleMapFragment();
    }

    @Override
    public void onNavigationFinished() {
        goBackToVehicleMapFragment();
    }

    @Override
    public void onNavigationRunning() {
        navigationView.retrieveNavigationMapboxMap().retrieveMap().setStyle(new Style.Builder().fromUri("asset://routetyle.json"));
    }

    private void goBackToVehicleMapFragment() {
        NavMapFragment newFragment = new NavMapFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.map_fragment_container, newFragment);
        transaction.replace(R.id.map_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
