package com.example.vahitdurmus.fusedlocationapiproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * Created by vahit.durmus on 27.07.2018.
 */

public class LocationFactory {


    private Context context;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    int numberOfSatellites=0;
    int numberOfConnectedSatellites=0;

    public LocationFactory(Context pContext) {
        this.context = pContext;
    }
    public void startLocationTrack() {
        buildGoogleApiClient();
        createLocationRequest();
        connectGoogleAPI();
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.addGpsStatusListener((GpsStatus.Listener) context);

    }
    public  void  stopLocationTrack() throws Exception{
        stopLocationRequest();
        disConnectGoogleAPI();
        locationManager.removeGpsStatusListener((GpsStatus.Listener)context);
    }
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder((Activity)context)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks)context)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener)context)
                .addApi(LocationServices.API)
                .build();
    }
    private void createLocationRequest() {
        locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    public void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest,(LocationListener)context);
    }
    public void stopLocationRequest() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,(LocationListener)context);
    }
    public void findNumberOfSatellites() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int satellites = 0;
        int satellitesInFix = 0;
        int timetofix = locationManager.getGpsStatus(null).getTimeToFirstFix();

        for (GpsSatellite sat : locationManager.getGpsStatus(null).getSatellites()) {
            if (sat.usedInFix()) {
                satellitesInFix++;
            }
            satellites++;
        }
        numberOfConnectedSatellites = satellitesInFix;
        numberOfSatellites = satellites;
    }
    public  void disConnectGoogleAPI(){

        if(mGoogleApiClient.isConnected()==true)
            mGoogleApiClient.disconnect();
    }
    public void connectGoogleAPI(){
        if(mGoogleApiClient.isConnected()==false || mGoogleApiClient==null)
            mGoogleApiClient.connect();

    }
    public int getNumberOfSatellites(){
        return numberOfSatellites;
    }
    public  int getNumberOfConnectedSatellites(){
        return  numberOfConnectedSatellites;
    }
}
