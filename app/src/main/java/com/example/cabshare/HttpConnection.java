package com.example.cabshare;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Priyesh Mishra on 01-01-2016.
 */
public class HttpConnection {


    public String loginRequest(String data){
        String reqUrl = "http://192.168.1.5:8080/servlets/LoginServlet";
        String response = request(reqUrl,data);
        return response;
    }
    public String registrationRequest(String data){
        String reqUrl = "http://192.168.1.5:8080/servlets/RegistrationServlet";
        String response = request(reqUrl,data);
        return response;
    }
    private String request(String reqUrl,String data){
        String response = "";
        URL url = null;
        HttpURLConnection conn=null;
        try {
            url = new URL(reqUrl);
            if (url != null)
                conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();

            int responseCode = conn.getResponseCode();
            String line =null;
            if(responseCode == HttpURLConnection.HTTP_OK ) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = in.readLine()) != null) {
                    response += line;
                }
                in.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        finally{
            conn.disconnect();
        }
        return response.toString();
    }
}
