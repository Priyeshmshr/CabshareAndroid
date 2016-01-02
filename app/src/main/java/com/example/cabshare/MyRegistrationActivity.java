package com.example.cabshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Priyesh Mishra.
 */
public class MyRegistrationActivity extends Activity implements OnClickListener, TextWatcher {

    private Button signUp;
    private Spinner spinner;
    private ProgressDialog progressDialog = null;
    private EditText username, password, fullName, gender,etContact;
    private final String fnKey = "fullname";
    private final String gndrKey = "gender";
    private final String contact = "contact_no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        /*SharedPreferences sessionID = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_PRIVATE);
        String sessId = sessionID.getString(Properties.PROPERTY_SESSION_ID, "");
        if (!sessId.isEmpty()) {
            Intent i = new Intent("com.example.cabshare.REQUESTER_SHARER");
            startActivity(i);
            finish();
        }*/
        setContentView(R.layout.registration_activity);
        Initialize();
    }

    private void Initialize() {
        // TODO Auto-generated method stub

        signUp = (Button) findViewById(R.id.bRegister);
        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        fullName = (EditText) findViewById(R.id.etFullName);
        gender = (EditText) findViewById(R.id.etGender);
        etContact = (EditText) findViewById(R.id.etContact);
        signUp.setOnClickListener(this);
        username.addTextChangedListener(this);
        password.addTextChangedListener(this);
        fullName.addTextChangedListener(this);
        gender.addTextChangedListener(this);
        etContact.addTextChangedListener(this);
    }

    @Override
    public void onClick(View item) {
        // TODO Auto-generated method stub
        switch (item.getId()) {
            case R.id.bRegister:
                //final HttpPostData SendViaPost = new HttpPostData(getApplicationContext());
                //final String ImeiNo = getRegistrationId();
                final String uid = username.getText().toString();
                final String pwd = password.getText().toString();
                final String gndr = gender.getText().toString();
                final String fn = fullName.getText().toString();
                final String cntct = etContact.getText().toString();
                Toast.makeText(this, uid + "\n " + pwd + "\n" + gndr + "\n" + fn + "\n", Toast.LENGTH_SHORT).show();
                try {
                    new AsyncTask<Void, Integer, String>() {
                        protected void onPreExecute() {
                            progressDialog = ProgressDialog.show(MyRegistrationActivity.this, "Registration", "Signing You Up...");
                        }

                        @Override
                        protected String doInBackground(Void... arg0) {
                            // TODO Auto-generated method stub
                            /*res = SendViaPost.sendRegistrationData(ImeiNo, uid, pwd, sex, full);
                            if (res.equals("ok"))
                                return "TRUE";
                            else
                                return "FALSE";*/
                            HttpConnection conn = new HttpConnection();
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter(Properties.Username, uid)
                                    .appendQueryParameter(Properties.Password, pwd)
                                    .appendQueryParameter(fnKey,fn)
                                    .appendQueryParameter(gndrKey,gndr)
                                    .appendQueryParameter(contact,cntct);
                            String data = builder.build().getEncodedQuery();
                            String res = null;
                            Log.d("Registration details:", uid + " " + pwd+" "+gndr+" "+fn);
                            res = conn.registrationRequest(data);
                            return res;
                        }

                        @Override
                        protected void onPostExecute(String msg) {
                            progressDialog.dismiss();
                            if (msg.equalsIgnoreCase("Registration successful")) {
                                /*SharedPreferences responseData = getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
                                String response = responseData.getString(Properties.PROPERTY_SESSION_ID, "Not Present");
                                MyGCM mgcm = new MyGCM(getApplicationContext());*/
                                SharedPreferences responseData = getApplicationContext().getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_PRIVATE);
                                SharedPreferences.Editor editor = responseData.edit();
                                editor.putBoolean(Properties.IS_LOGGED_IN, true);
                                editor.putString(Properties.PROPERTY_USER_ID, uid);
                                editor.commit();
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                Intent in = new Intent("com.example.cabshare.REQUESTER_SHARER");
                                startActivity(in);
                                finish();
                            } else {
                                Toast.makeText(MyRegistrationActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }.execute(null, null, null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.d("TAG", "I dont know what is the problem!! :("+ e.getMessage());
                    Toast.makeText(this, "exception", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private boolean validate() {
        boolean valid = true;
        if(username.getText().toString().isEmpty()) {
            //username.setError("This field is required!");
            valid = false;
        }
        if( password.getText().toString().isEmpty()) {
            //password.setError("This field is required!");
            valid = false;
        }
        if(gender.getText().toString().isEmpty()){
            //gender.setError("This field is required!");
            valid = false;
        }
        if(fullName.getText().toString().isEmpty()){
            //fullName.setError("This field is required!");
            valid = false;
        }
        if(etContact.getText().toString().isEmpty()){
            //etContact.setError("This field is required!");
            valid = false;
        }
        return valid;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void afterTextChanged(Editable s) {
        signUp.setEnabled(validate());
    }
    /*private String getRegistrationId() {
        // TODO Auto-generated method stub
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }*/
}
