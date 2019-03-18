package com.example.vahitdurmus.fusedlocationapiproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
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

    private volatile Location currentLocation;
    private volatile Location previousLocation;
    private Context context;
    private LocationRequest locationRequest;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private volatile int numberOfSatellites = 0;
    private volatile int numberOfConnectedSatellites = 0;

    public LocationFactory(Context context) {
        setContext(context);
        setZeroNumberOfSatellites();
    }


    /**
     * is used to set context of activity. it takes context as parameter.
     * @param context
     */
    private void setContext(Context context) {
        this.context = context;
    }

    public void setLocationSettingsAndStart(long interval, long fastestInterval) {

        try {
            buildGoogleApiClient();
            createLocationRequest(interval, fastestInterval);
            connectGoogleAPI();
            locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            addGpsStatusListener();
            addNmeaStatusListener();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopLocationTrack() {

        try{
            stopLocationRequest();
            disConnectGoogleAPI();
            removeGpsStatusListener();
            removeNmeaStatusListener();
        }
        catch (Exception e){

        }
    }


    public void removeGpsStatusListener() {
        try {
            locationManager.removeGpsStatusListener((GpsStatus.Listener) context);
        } catch (Exception e) {

        }
    }

    public void addGpsStatusListener() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addNmeaStatusListener() {
        try{
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
            locationManager.addNmeaListener((GpsStatus.NmeaListener) context);

        }
        catch (Exception e){

        }
    }

    public void removeNmeaStatusListener() {
        try{
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
            locationManager.removeNmeaListener((GpsStatus.NmeaListener) context);

        }
        catch (Exception e){

        }
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder((Activity) context)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) context)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) context)
                .addApi(LocationServices.API)
                .build();
    }
    private void createLocationRequest(long interval,long fastestInterval) {
        locationRequest = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    public void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, (LocationListener) context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stopLocationRequest() {
        try{
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void findNumberOfSatellites() {
        try{
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
        catch (Exception e)
        {

        }


    }
    public void disConnectGoogleAPI() {
        try{
            if (mGoogleApiClient.isConnected() == true)
                mGoogleApiClient.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void connectGoogleAPI() {
        try {
            if (mGoogleApiClient.isConnected() == false || mGoogleApiClient == null)
                mGoogleApiClient.connect();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public int getNumberOfSatellites() {
        return numberOfSatellites;
    }
    public  void setZeroNumberOfSatellites(){
        this.numberOfSatellites=0;
        this.numberOfConnectedSatellites=0;
    }
    public int getNumberOfConnectedSatellites() {
        return numberOfConnectedSatellites;
    }
    public  void  setCurrentLocation(Location currentLocation)
    {
        this.currentLocation=currentLocation;
    }
    public  Location getCurrentLocation(){
        return  this.currentLocation;
    }
    public  void   setPreviousLocation(Location previousLocation)
    {
        this.previousLocation=previousLocation;
    }
    public  Location getPreviousLocation(){
        return  this.previousLocation;
    }


}
