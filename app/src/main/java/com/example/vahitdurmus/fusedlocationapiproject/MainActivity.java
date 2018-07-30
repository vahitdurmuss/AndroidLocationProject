package com.example.vahitdurmus.fusedlocationapiproject;


import android.location.GpsStatus;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;


public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GpsStatus.Listener  {

    TextView latitudeView;
    TextView longitudeView;
    TextView connectedSatellitesView;
    TextView satellitesView;

    LocationFactory locationFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeView=(TextView)findViewById(R.id.tVlatitude);
        longitudeView=(TextView)findViewById(R.id.tVlongitude);
        connectedSatellitesView=(TextView)findViewById(R.id.tVnumberOfConnectedSatellites);
        satellitesView=(TextView)findViewById(R.id.tVnumberOfSatellites);

        latitudeView.setText("0");
        longitudeView.setText("0");
        connectedSatellitesView.setText("0");
        satellitesView.setText("0");

        locationFactory=new LocationFactory(this);
        locationFactory.startLocationTrack();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();
        locationFactory.stopLocationRequest();
    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        locationFactory.startLocationUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationFactory.startLocationTrack();
    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:

                break;
            case GpsStatus.GPS_EVENT_STOPPED:

                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                locationFactory.findNumberOfSatellites();
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                locationFactory.findNumberOfSatellites();
                break;
            default:

                break;
        }

    }
    @Override
    public void onConnected(Bundle bundle) {
       locationFactory.startLocationUpdates();
    }
    @Override
    public void onConnectionSuspended(int i) {
        locationFactory.connectGoogleAPI();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    @Override
    public void onLocationChanged(Location location) {
        try{

            latitudeView.setText(String.valueOf(location.getLatitude()));
            longitudeView.setText(String.valueOf(location.getLongitude()));
            satellitesView.setText(String.valueOf(locationFactory.getNumberOfSatellites()));
            connectedSatellitesView.setText(String.valueOf(locationFactory.getNumberOfConnectedSatellites()));
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
