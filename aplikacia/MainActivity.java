package com.example.martin.mapbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_CODE = 105;
    private Mapbox mMapBox;
    private MapView mMapView;
    private MapController mMapController;
    private Location mUserLocation;
    private Server mServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapBox = Mapbox.getInstance(this, "pk.eyJ1IjoiYmFsYXoiLCJhIjoiY2o3enpmaGUyMXZ3cTJxczY4M2ExNzczbiJ9.QedsdpK3xd6ptLx-W5sxZw");
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapController = new MapController(getApplicationContext(), mMapView);
        mMapController.setMapPosition(48.1529, 17.0720, 10);
        mUserLocation = new Location();
        mUserLocation.setX(48.1529);
        mUserLocation.setY(17.0720);
        findLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        mServer = new Server();
        if (id == R.id.showActualPostion)
            showActualLocation();
        else if (id == R.id.findFirstHelp)
            findLocations(mServer, "medical");
        else if (id == R.id.findBicycleFacilities) {
            findLocations(mServer, "bicycle");
        }
        else if (id == R.id.findFood) {
            findLocations(mServer, "food");
        }
        else if (id == R.id.findWay) {
            Intent intent = new Intent(this, LengthActivity.class);
            startActivityForResult(intent, 1);
        }
        else if (id == R.id.findCyrcle) {
            findCycleWay(mServer);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showActualLocation() {
        if (mUserLocation == null)
            Toast.makeText(getApplicationContext(), "Searching for location",
                    Toast.LENGTH_SHORT).show();
        else
            mMapController.moveCamera(mUserLocation.getX(), mUserLocation.getY());
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void findLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new CustomLocationListener(this);

        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if (!(PermissionHandler.checkPermissions(getApplicationContext(), permissions))) {
            PermissionHandler.requestAplicationPermissions(this, permissions,
                    PERMISSIONS_CODE);
        } else {
            Toast.makeText(getApplicationContext(), "Searching for location",
                    Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        }
    }

    public void setLocation(double latitude, double longtitude) {
        Location location = new Location();
        location.setX(latitude);
        location.setY(longtitude);
        location.setName("actual position");
        mMapController.setLocationMarker(location);

        mUserLocation = location;
    }

    private boolean findLocations(Server server, String amenity) {
        if(mUserLocation != null) {
            final ArrayList<Location> locations = server.getLocations(amenity, mUserLocation);
            mMapController.deleteMarkers();
            mMapController.addPointMarkers(locations);
        }
        return true;
    }

    private boolean findCycleWay(Server server) {
        if(mUserLocation != null) {
            ArrayList<Way> ways = server.getCycrcle(mUserLocation);
            mMapController.deleteMarkers();
            mMapController.addLineMarker(ways);
        }
        return true;
    }

    private boolean findWay(Server server, int length) {
        if(mUserLocation != null) {
            ArrayList<Way> ways = server.getWay(mUserLocation, length);
            mMapController.deleteMarkers();
            mMapController.addLineMarker(ways);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_CODE)
            findLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            if(mUserLocation != null)
                findWay(mServer,data.getIntExtra("result", 1000));
        }

    }
}
