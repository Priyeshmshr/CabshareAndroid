package com.example.cabshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Requester_Sharer extends Activity implements OnClickListener, LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static String type = null;
    Button requester, sharer;
    GoogleCloudMessaging gcm;
    String SENDER_ID = "1054444576671";
    Intent in;
    LocationManager lm;
    Location loc;
    boolean isGPSEnabled;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requester_sharer);
        InitializeRS();
        //MyGCM gcm = new MyGCM(getApplicationContext());  //Temporary line...
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Coordinates();
    }

    public void Coordinates() {
        if (servicesConnected()) {
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Criteria c = new Criteria();
            isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (isGPSEnabled) {
                provider = lm.getBestProvider(c, false);
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (lm != null) {
                    loc = lm.getLastKnownLocation(provider);
                    if (loc != null) {
                        Properties.location = loc;
                        Toast.makeText(getApplicationContext(), String.valueOf(Properties.location.getLatitude()), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                showSettingsAlert();
            }
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void InitializeRS() {
        // TODO Auto-generated method stub
        requester = (Button) findViewById(R.id.bRequester);
        sharer = (Button) findViewById(R.id.bSharer);
        requester.setOnClickListener(this);
        sharer.setOnClickListener(this);
        in = new Intent("com.example.cabshare.START_DEST");
    }


    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bRequester:
                type = "requester";
                in.putExtra("type", type);
                startActivity(in);
                type = null;
                break;
            case R.id.bSharer:
                type = "sharer";
                in.putExtra("type", type);
                startActivity(in);
                type = null;
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        Properties.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            return false;
        }
    }
}
