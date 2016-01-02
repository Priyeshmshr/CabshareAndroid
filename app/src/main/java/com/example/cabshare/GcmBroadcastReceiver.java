package com.example.cabshare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Priyesh Mishra.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    public static JSONArray jarray;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        // Start the service, keeping the device awake while it is launching.
        Bundle extra = intent.getExtras();
        JSONObject json = null;
        if (extra.containsKey("Response_type")) {
            String ext = (String) extra.get("Response_type");
            Log.d("GCMBroadcastReceiver", "Response_type exist!");
            try {
                if (ext.equals("userList")) {
                    String list = (String) extra.get("data");
                    json = new JSONObject(list);
                    // if(json.get("type").equals("userList")){
                    jarray = json.getJSONArray("list");
                    @SuppressWarnings("static-access")
                    SharedPreferences shared = context.getSharedPreferences(
                            Properties.CABSHARE_SHARED_PREFERENCES,
                            context.MODE_MULTI_PROCESS);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("User_Array", jarray.toString());
                    editor.commit();
                    // SQLITE_DB store = new SQLITE_DB(context);
                    // store.open();
                    // store.insertList(jarray);
                    // store.close();
                } else if (ext.equals("request")) {
                    String info = (String) extra.get("req");
                    JSONObject request = new JSONObject(info);
                    ComponentName comp = new ComponentName(context.getPackageName(),
                            GcmIntentService.class.getName());
                    startWakefulService(context, (intent.setComponent(comp)));
                    Log.d("GCMBroadcast", info.toString());
                    Toast.makeText(context, info.toString(), Toast.LENGTH_SHORT).show();
                }
                //Code implementation for different response type here.

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("TAG", e.getMessage().toString());
            }
        } else
            Log.d("GCMBroadcastReceiver", "Response_type Doesn't exist!");
        Log.d("GCMBroadcastReceiver", extra.toString());
        Toast.makeText(context, extra.toString(), Toast.LENGTH_SHORT).show();
        setResultCode(Activity.RESULT_OK);
    }
}