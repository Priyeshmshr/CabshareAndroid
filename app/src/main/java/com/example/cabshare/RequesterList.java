package com.example.cabshare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Priyesh Mishra on 01-01-2016.
 */
public class RequesterList extends Activity {

    String uid[], slat[], slon[], dlat[], dlon[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        int i = 0;
        JSONArray list = GcmBroadcastReceiver.jarray;
        while (i < list.length()) {
            try {
                JSONObject obj = list.getJSONObject(i);
                uid[i] = obj.getString("user_id");
                slat[i] = obj.getString("startLat");
                slon[i] = obj.getString("startLon");
                dlat[i] = obj.getString("destLat");
                dlon[i] = obj.getString("destLon");
                i++;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


}
