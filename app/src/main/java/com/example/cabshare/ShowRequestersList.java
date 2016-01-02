package com.example.cabshare;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Priyesh Mishra on 01-01-2016.
 */
public class ShowRequestersList extends Activity implements OnItemClickListener {

    ArrayList<String> userID;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        lv = (ListView) findViewById(R.id.lvUserList);
        SharedPreferences sp = getSharedPreferences(
                Properties.CABSHARE_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
        String s = sp.getString("User_Array", null);
        //Log.d("ShowUsers", s);
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        userID = new ArrayList<String>();
        userID.add("USERS:");
        try {
            int temp = 0;
            if (s != null) {
                JSONArray json = new JSONArray(s);
                while (temp < json.length()) {
                    JSONObject js = json.getJSONObject(temp++);
                    userID.add(js.getString("user_id"));
                }
                userID.add("Priyesh");
            } else {
                userID.add("NO user");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, userID);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent i = new Intent("com.example.cabshare.PROFILE");
        i.putExtra("position", position);
        startActivity(i);
    }

}
