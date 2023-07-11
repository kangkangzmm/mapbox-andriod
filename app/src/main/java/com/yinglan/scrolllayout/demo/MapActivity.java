package com.yinglan.scrolllayout.demo;


import android.app.Application;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.vision.VisionManager;
import com.yinglan.scrolllayout.demo.viewpager.VehicleMapFragment;

public class MapActivity extends AppCompatActivity implements
        com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Mapbox.getInstance(getApplicationContext(), "sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg");

        VisionManager.init((Application) getApplicationContext(), "sk.eyJ1Ijoia2FuZ2thbmdrIiwiYSI6ImNsZ2txajAzaTBhemYzcG5vbTB6M2hlMDAifQ.fN0T8NXrBc8VQhfjIy1XGg");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        VehicleMapFragment newFragment = new VehicleMapFragment();
        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.map_fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.navigation_drawer_account) {
            // Handle showing account information
        } else if (id == R.id.navigation_drawer_payment) {
            // Handle showing payment information
        } else if (id == R.id.navigation_drawer_help) {
            // Handle showing help information
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}