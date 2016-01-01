package com.example.cabshare;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

    Button LogIn;
    TextView SignUp;
    EditText username, password;
    ProgressDialog progressDialog;
    Context context;
    HttpPostData post;
    HttpConnection send;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        SharedPreferences sessionID = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_PRIVATE);
        boolean loggedIn = sessionID.getBoolean(Properties.IS_LOGGED_IN, false);
        if (loggedIn) {
            Intent i = new Intent("com.example.cabshare.REQUESTER_SHARER");
            startActivity(i);
            finish();
        }
        /*Intent in = new Intent("com.example.cabshare.REQUESTER_SHARER");
	    startActivity(in);
	    finish();*/

        setContentView(R.layout.login);
        context = getApplicationContext();
        post = new HttpPostData(context);
        send = new HttpConnection();
        Initialize();
    }

    private void Initialize() {
        // TODO Auto-generated method stub
        LogIn = (Button) findViewById(R.id.bLogIn);
        SignUp = (TextView) findViewById(R.id.tvSignUp);
        username = (EditText) findViewById(R.id.etLoginUsername);
        password = (EditText) findViewById(R.id.etLoginPassword);
        LogIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.bLogIn:
                //final HttpPostData SendViaPost = new HttpPostData(getApplicationContext());

                //final String ImeiNo = getRegistrationId();
                final String uid = username.getText().toString();
                final String pwd = password.getText().toString();
                Toast.makeText(this, "ImeiNo" + "\n " + uid + "\n " + pwd, Toast.LENGTH_SHORT).show();
                try {
                    new AsyncTask<Void, Integer, String>() {
                        protected void onPreExecute() {
                            progressDialog = ProgressDialog.show(LoginActivity.this, "Signing in", "Please Wait...");
                        }

                        @Override
                        protected String doInBackground(Void... arg0) {
                            // TODO Auto-generated method stub
                            //res = SendViaPost.sendLoginDetails(ImeiNo, uid, pwd);
                            /*JSONObject data = new JSONObject();
                            try {
                                data.put(Properties.Username, uid);
                                data.put(Properties.Password, pwd);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            //ToGCM gotoGcm = new ToGCM(context)
                            //res = gotoGcm.send(data);
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter(Properties.Username, uid)
                                    .appendQueryParameter(Properties.Password, pwd);
                            String data = builder.build().getEncodedQuery();
                            Log.d("Login details", uid + " " + pwd);
                            String res = null;
                            //res = post.sendLoginDetails("123", uid, pwd);
                            res = send.loginRequest(data);
                            Log.d("Response", res);
                            return res;
                        }

                        @Override
                        protected void onPostExecute(String msg) {
                            progressDialog.dismiss();
                            if (msg.equalsIgnoreCase("success")) {
                                //SharedPreferences responseData = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES,MODE_MULTI_PROCESS);
                                //String response = responseData.getString(Properties.PROPERTY_SESSION_ID, "Not Present");
                                //MyGCM mgcm = new MyGCM(getApplicationContext());
                                SharedPreferences responseData = context.getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor editor = responseData.edit();
                                editor.putBoolean(Properties.IS_LOGGED_IN, true);
                                editor.putString(Properties.PROPERTY_USER_ID, uid);
                                editor.commit();
                                MyGCM gcm = new MyGCM(context);
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                Log.d("Response",msg);
                                Intent in = new Intent("com.example.cabshare.REQUESTER_SHARER");
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(null, null, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d("TAG", "I dont know what is the problem!! :(");
                    Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tvSignUp:
                Intent in = new Intent("com.example.cabshare.MYREGISTRATIONACTIVITY");
                startActivity(in);
                finish();
                //setContentView(R.layout.registration_activity);
                break;
        }
    }
    /*private String getRegistrationId() {
        // TODO Auto-generated method stub
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }*/
}
