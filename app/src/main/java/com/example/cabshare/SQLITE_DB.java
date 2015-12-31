package com.example.cabshare;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLITE_DB {

    public static final String KEY_ID = "_id";
    public static final String KEY_userID = "user_id";
    public static final String KEY_Name = "name";
    public static final String KEY_StartLat = "sLat";
    public static final String KEY_DestLat = "dLat";
    public static final String KEY_StartLon = "sLon";
    public static final String KEY_DestLon = "dLon";
    public static final String KEY_REGISTRATION = "RegId";

    public static final String Database_Name = "USER_LIST";
    public static final String Table_Name = "users";
    public static final int Database_version = 1;
    private final Context ourContext;
    private sqlHelper dbHelper;
    private SQLiteDatabase ourDatabase;

    public SQLITE_DB(Context c) {
        ourContext = c;
    }

    public SQLITE_DB open() {
        dbHelper = new sqlHelper(ourContext);
        ourDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertList(JSONArray jarray) {
        int temp = 0;
        while (temp < jarray.length()) {
            JSONObject json = new JSONObject();
            try {
                json = jarray.getJSONObject(temp++);
                ContentValues cv = new ContentValues();
                cv.put(KEY_userID, json.getString("user_id"));
                cv.put(KEY_StartLat, json.getString("startLat"));
                cv.put(KEY_StartLon, json.getString("startLon"));
                cv.put(KEY_DestLat, json.getString("destLat"));
                cv.put(KEY_DestLon, json.getString("destLon"));
                ourDatabase.insert(Table_Name, null, cv);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d("TAG", e.getMessage().toString());
            }
        }
    }

    public void readList() {
        String[] columns = new String[]{KEY_userID, KEY_StartLat, KEY_StartLon, KEY_DestLat, KEY_DestLon};
        Cursor c = ourDatabase.query(Table_Name, columns, null, null, null, null, null);
        String result = "";
        int iUser = c.getColumnIndex(KEY_userID);
        int iSLat = c.getColumnIndex(KEY_StartLat);
        int iSLon = c.getColumnIndex(KEY_StartLon);
        int iDLat = c.getColumnIndex(KEY_DestLat);
        int iDLon = c.getColumnIndex(KEY_DestLon);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

        }
    }

    public static class sqlHelper extends SQLiteOpenHelper {

        public sqlHelper(Context context) {
            super(context, Database_Name, null, Database_version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("Create table" + Table_Name + "(" +
                            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_userID + " TEXT NOT NULL, " +
                            KEY_Name + "TEXT, " +
                            KEY_StartLat + "TEXT NOT NULL, " +
                            KEY_StartLon + "TEXT NOT NULL, " +
                            KEY_DestLat + "TEXT NOT NULL, " +
                            KEY_DestLon + "TEXT NOT NULL, " +
                            KEY_REGISTRATION + "TEXT"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS" + Table_Name);
            onCreate(db);
        }
    }
}
