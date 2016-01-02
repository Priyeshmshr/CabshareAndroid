package com.example.cabshare;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

/**
 * Created by Priyesh Mishra on 01-01-2016.
 */
public class Start_Dest extends Activity implements OnClickListener {

    public int start = 1, dest = 2, i = 0;
    // Add a call to location manager in onResume() to get coordinates.
    // Delete the shared preference "User_Array" to get a fresh list.
    Button startPoint, destPoint;
    double latitude = 0.0, longitude = 0.0;
    Intent in;
    String startAddress = null, destAddress = null, type = null;
    ProgressDialog pd;
    SharedPreferences userid;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_dest);
        Initialize();
        Bundle recieved = getIntent().getExtras();
        type = recieved.getString("type", "Error");
        userid = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES,
                MODE_MULTI_PROCESS);
        SharedPreferences.Editor edit = userid.edit();
        edit.remove("User_Array");
        edit.commit();
    }

    private void Initialize() {
        startPoint = (Button) findViewById(R.id.bStartPoint);
        destPoint = (Button) findViewById(R.id.bDestPoint);
        startPoint.setOnClickListener(this);
        destPoint.setOnClickListener(this);
        in = new Intent("com.example.cabshare.AUTOCOMPLETE");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bStartPoint:
                in.putExtra("hint", "Choose Starting Point...");
                startActivityForResult(in, start);
                break;
            case R.id.bDestPoint:
                in.putExtra("hint", "Choose Destination...");
                startActivityForResult(in, dest);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    startAddress = data.getStringExtra("Place");
                    startPoint.setText(startAddress);
                    Toast.makeText(getApplicationContext(), startAddress,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    destAddress = data.getStringExtra("Place");
                    destPoint.setText(destAddress);
                    Toast.makeText(getApplicationContext(), destAddress,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            if (startAddress != null && destAddress != null) {
                SendAddress();
            }
        }
    }

    private void SendAddress() {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                // pd =
                // ProgressDialog.show(getApplicationContext(),"Waiting For Location","Please Wait...");
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                String res;
                String user = userid.getString(Properties.PROPERTY_USER_ID,
                        "NotFound");
                Bundle data = new Bundle();
                LatLng coord = GeocodeAddress(startAddress);
                Properties.sCoord = coord;
                Log.d("Start_dest!!", String.valueOf(coord.latitude));
                data.putString("startLat", String.valueOf(coord.latitude));
                data.putString("startLon", String.valueOf(coord.longitude));
                coord = GeocodeAddress(destAddress);
                Properties.dCoord = coord;
                Log.d("Start_dest", String.valueOf(coord.latitude));
                data.putString("destLat", String.valueOf(coord.latitude));
                data.putString("destLon", String.valueOf(coord.longitude));
                data.putString("requestType", type);
                data.putString("id", user);
                data.putString("my_action", "Update Location");
                ToGCM gcm = new ToGCM(getApplicationContext());
                res = gcm.send(data);
                for (int i = 0; i < 5; ) {
                    if (userid.contains("User_Array")) {
                        break;
                    }
                }
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // if(pd.isShowing())
                // pd.dismiss();
                if (result.equals("Sent")) {
                    /*
					 * Intent intent = new
					 * Intent("com.example.cabshare.MAINACTIVITY");
					 * intent.putExtra("start", startAddress);
					 * intent.putExtra("dest", destAddress);
					 */
                    Intent intent = new Intent(
                            "com.example.cabshare.SHOWREQUESTERSLIST");
                    startAddress = destAddress = null;
                    startPoint.setText("Choose start Point...");
                    destPoint.setText("Choose destination point...");
                    startActivity(intent);
                }
            }
        }.execute(null, null, null);
    }

    private LatLng GeocodeAddress(String address) {
        // TODO Auto-generated method stub
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addr = geocoder.getFromLocationName(address, 1);
            if (addr.size() > 0) {
                GeoPoint geo = new GeoPoint(
                        (int) (addr.get(0).getLatitude() * 1E6), (int) (addr
                        .get(0).getLongitude() * 1E6));
                latitude = geo.getLatitudeE6() / 1E6;
                longitude = geo.getLongitudeE6() / 1E6;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LatLng coord = new LatLng(latitude, longitude);
        return coord;
    }
}
