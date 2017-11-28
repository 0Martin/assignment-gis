package com.example.martin.mapbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;

public class MapController {

    private MapView mMapView;
    private Marker mLocationMarker;
    private MarkerOptions mLocationMarkerOptions;
    private final Icon mUserPositionIcon;
    private final Icon[] mMarkerIcons;

    public MapController( Context context, MapView mapView){
        mMapView = mapView;

        mUserPositionIcon = createIcon(context, R.drawable.mapbox_mylocation_icon_default);
        Icon mBlueMarker = createIcon(context, R.drawable.blue_markerh);
        Icon mGreenMarker = createIcon(context, R.drawable.green_markerh);
        Icon mPurpleMarker = createIcon(context, R.drawable.purple_markerh);
        Icon mYellowMarker = createIcon(context, R.drawable.yellow_markerh);
        mMarkerIcons = new Icon[] {mBlueMarker, mGreenMarker, mPurpleMarker, mYellowMarker};
    }

    private Icon createIcon(Context context, int i){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), i);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    public void setMapPosition(final double latitude, final double longtitude,
                                final double zoom){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longtitude))
                        .zoom(zoom)
                        .build();
                mapboxMap.setCameraPosition(cameraPosition);
            }
        });
    }

    public void setLocationMarker(final Location location){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                if(mLocationMarker == null) {
                    mLocationMarkerOptions = new MarkerOptions()
                            .position(new LatLng(location.getX(), location.getY()))
                            .title(location.getName())
                            .snippet(location.getName())
                            .setIcon(mUserPositionIcon);
                    mLocationMarker = mapboxMap.addMarker(mLocationMarkerOptions);
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getX(), location.getY()), 16));
                }
                else {
                    mLocationMarker.setPosition(new LatLng(location.getX(), location.getY()));
                }
            }
        });
    }

    public void moveCamera(final double x, final double y) {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(x, y), 16));
            }
        });
    }

    public void addPointMarkers(final ArrayList<Location> locations){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                for(int i = 0; i < locations.size(); i++){
                    Location loc = locations.get(i);
                    mapboxMap.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getY(), loc.getX()))
                            .title(loc.getName())
                            .snippet(loc.getAmenity())
                            .setIcon(mMarkerIcons[loc.getIndex()]));
                }
            }
        });
    }

    public void addLineMarker(ArrayList<Way> ways){
        for(Way way: ways){
            ArrayList<Point> pointsArray = way.getPoints();
            LatLng[] points = new LatLng[pointsArray.size()];
            for (int i = 0; i < pointsArray.size(); i++) {
                points[i] = new LatLng(
                        pointsArray.get(i).getY(),
                        pointsArray.get(i).getX());
            }
            final LatLng[] pointsFinal = points;

            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mapboxMap.addPolyline(new PolylineOptions()
                            .add(pointsFinal)
                            .color(Color.argb(255, 0, 0, 255))
                            .width(5));
                }
            });
        }
    }

    public void deleteMarkers(){
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapboxMap.clear();
                mLocationMarker = mapboxMap.addMarker(mLocationMarkerOptions);
            }
        });
    }
}
