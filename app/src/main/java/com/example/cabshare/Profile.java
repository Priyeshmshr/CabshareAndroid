package com.example.cabshare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
/**
* Created by Priyesh Mishra.
*/
public class Profile extends Activity implements OnClickListener {

    ImageView ivProfilePic;
    Button bSendRequest;
    TextView tvName;
    GoogleMap fMap;
    int index;
    JSONObject Jobject;
    SharedPreferences sp;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        ivProfilePic = (ImageView) findViewById(R.id.ivPicture);
        bSendRequest = (Button) findViewById(R.id.bSendRequest);
        tvName = (TextView) findViewById(R.id.tvName);
        fMap = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.fMap)).getMap();
        bSendRequest.setOnClickListener(this);
        Bundle extra = getIntent().getExtras();
        index = extra.getInt("position");

        sp = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
        String value = sp.getString("User_Array", null);
        JSONArray json;
        try {
            json = new JSONArray(value);
            if (value != null) {
                Jobject = json.getJSONObject(index);
                tvName.setText(Jobject.getString("user_id"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bSendRequest:
                //Code for Sending request.
                Bundle data = new Bundle();
                try {
                    data.putString("ToRegId", Jobject.getString("regid"));
                    JSONObject MyInfo = new JSONObject();
                    MyInfo.put("user_id", sp.getString(Properties.PROPERTY_USER_ID, "NotFound"));
                    MyInfo.put("startLat", String.valueOf(Properties.sCoord.latitude));
                    MyInfo.put("startLon", String.valueOf(Properties.sCoord.longitude));
                    MyInfo.put("destLat", String.valueOf(Properties.dCoord.latitude));
                    MyInfo.put("destLon", String.valueOf(Properties.dCoord.longitude));
                    data.putString("my_action", "sendRequest");
                    data.putString("UserInfo", MyInfo.toString());
                    sendRequest(data);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Sent!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void sendRequest(final Bundle data) {
        new AsyncTask<Void, Integer, String>() {

            @Override
            protected String doInBackground(Void... params) {
                // TODO Auto-generated method stub
                ToGCM gcm = new ToGCM(getApplicationContext());
                String res = gcm.send(data);
                return res;
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                if (result.equals("Sent")) {
                    Toast.makeText(getApplicationContext(), "Sent to gcm server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Sent to gcm server!!", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(null, null, null);
    }
}
