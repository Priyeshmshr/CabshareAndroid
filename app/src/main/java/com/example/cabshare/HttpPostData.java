package com.example.cabshare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Deprecated.
 */
public class HttpPostData extends Activity {

    String url = "http://cs-creativeinstinct.rhcloud.com/app.handle.php";
    String urls = "http://192.168.1.6:8080/servlets/LoginServlet";
    String google = "http://www.google.com";
    String data = null;
    HttpClient client;
    HttpPost post;
    Context ctx;

    public HttpPostData(Context context) {
        client = new DefaultHttpClient();
        post = new HttpPost(urls);
        ctx = context;
    }

    public String sendRegistrationData(String devId, String uid, String pwd, String sex, String full) {
        try {
            List<NameValuePair> registrationDetails = new ArrayList<NameValuePair>();
            registrationDetails.add(new BasicNameValuePair("type", "reg"));
            registrationDetails.add(new BasicNameValuePair("devid", devId));
            registrationDetails.add(new BasicNameValuePair("origin", "app"));
            registrationDetails.add(new BasicNameValuePair("uid", uid));
            registrationDetails.add(new BasicNameValuePair("key", pwd));
            registrationDetails.add(new BasicNameValuePair("rkey", pwd));
            registrationDetails.add(new BasicNameValuePair("gndr", sex));
            registrationDetails.add(new BasicNameValuePair("fn", full));
            Log.d("TAG", "Registration variables set");
            post.setEntity(new UrlEncodedFormEntity(registrationDetails));
            Log.d("TAG", "Registration variables about to send");
            HttpResponse response = client.execute(post);
            Log.d("TAG", "Registration variables posted");
            data = EntityUtils.toString(response.getEntity());
            Log.d("TAG", "BASE64");
            Log.d("TAG", String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == 200) {
                String session = null;
                try {
                    JSONObject jsnObj = new JSONObject(data);
                    String type = jsnObj.getString("type");
                    if (type.equalsIgnoreCase("message")) {
                        session = jsnObj.getString("content");
                        SharedPreferences responseData = ctx.getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = responseData.edit();
                        editor.putString(Properties.PROPERTY_SESSION_ID, session);
                        editor.putString(Properties.PROPERTY_USER_ID, uid);
                        editor.commit();
                        return "ok";
                    } else if (type.equals("error")) {
                        return jsnObj.getString("message");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            Log.d("TAG", "client protocol exception");
            return "client protocol exception";
        } catch (IOException e) {
            Log.d("TAG", "io exception");
            return "io exception";
        }
        return "unknown error";
    }

    public String sendLoginDetails(String devId, String uid, String pwd) {
        try {
            List<NameValuePair> registrationDetails = new ArrayList<NameValuePair>();
            registrationDetails.add(new BasicNameValuePair("type", "lgin"));
            registrationDetails.add(new BasicNameValuePair("devid", devId));
            registrationDetails.add(new BasicNameValuePair("origin", "app"));
            registrationDetails.add(new BasicNameValuePair("username", uid));
            registrationDetails.add(new BasicNameValuePair("password", pwd));
            Log.d("TAG", "Registration variables set");
            post.setEntity(new UrlEncodedFormEntity(registrationDetails));
            Log.d("TAG", "Registration variables about to send");
            HttpResponse response = client.execute(post);
            Log.d("TAG", "Registration variables posted");
            data = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200) {    //Change this line

                return data;
             /*String session =null;
            try {
			     JSONObject jsnObj = new JSONObject(data);
     			 String type = jsnObj.getString("type");
	     		 if(type.equalsIgnoreCase("message"))
	    		 {
		     	        session = jsnObj.getString("content");
			        	SharedPreferences responseData = ctx.getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES,MODE_MULTI_PROCESS);
     					SharedPreferences.Editor editor = responseData.edit();
	    				editor.putString(Properties.PROPERTY_SESSION_ID, session);
	    				editor.putString(Properties.PROPERTY_USER_ID, uid);
		     			editor.commit();
		     			return "ok";
	    		 }
		    	 else if(type.equals("error"))
		    	 {
		    		 Log.d("TAG","Internal server Error is here");

	     			 return jsnObj.getString("message") ;
		    	 }
    		} catch (JSONException e) {
			// TODO Auto-generated catch block
	     		e.printStackTrace();
	    	}*/
            }

            Log.d("TAG", String.valueOf(response.getStatusLine().getStatusCode()));
        } catch (ClientProtocolException e) {
            Log.d("TAG", "client protocol exception");
            return "client protocol exception";
        } catch (IOException e) {
            Log.d("TAG", "io exception");
            return "io exception hehehe";
        }
        return "null";
    }

    public String LogOut(String sessid) {
        List<NameValuePair> logout = new ArrayList<NameValuePair>();
        logout.add(new BasicNameValuePair("sid", sessid));
        logout.add(new BasicNameValuePair("type", "lgout"));
        try {
            post.setEntity(new UrlEncodedFormEntity(logout));
            HttpResponse response = client.execute(post);
            data = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() == 200) {    //Change this line
                try {
                    JSONObject jsnObj = new JSONObject(data);
                    String type = jsnObj.getString("type");
                    if (type.equalsIgnoreCase("message")) {
                        SharedPreferences responseData = ctx.getSharedPreferences(Properties.CABSHARE_SHARED_PREFERENCES, MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = responseData.edit();
                        editor.remove(Properties.PROPERTY_SESSION_ID);
                        editor.remove(Properties.PROPERTY_USER_ID);
                        editor.commit();
                        return jsnObj.getString("content");
                    } else if (type.equals("error")) {
                        return jsnObj.getString("message");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "JSONExcepion";
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "null";
    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                "input=" + input + "&location=" + Properties.location.getLatitude() + "," + Properties.location.getLongitude() +
                "&radius=5000&key=AIzaSyAIzCx9e2tCVjg3GcojwYC1pKUYNtYtxyw");
        try {
            HttpGet get = new HttpGet(sb.toString());
            HttpResponse response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                data = EntityUtils.toString(response.getEntity());
                JSONObject json = new JSONObject(data);
                JSONArray predictions = json.getJSONArray("predictions");
                resultList = new ArrayList<String>(predictions.length());
                for (int i = 0; i < predictions.length(); i++) {
                    resultList.add(predictions.getJSONObject(i).getString("description"));
                }
            }
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Log.d("PLACES", e.getMessage());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.d("PLACES", e.getMessage());
        }
        return resultList;
    }
}
