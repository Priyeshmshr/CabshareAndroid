package com.example.cabshare;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ToGCM {

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

    public ToGCM(Context ctx) {
        gcm = GoogleCloudMessaging.getInstance(ctx);
    }

    public String send(final Bundle data) {
        String msg;
        try {
            gcm.send(Properties.SENDER_ID + "@gcm.googleapis.com", Integer.toString(msgId.incrementAndGet()), data);
            Log.d("ToGcm", "message sent");
            msg = "Sent";
        } catch (IOException ex) {
            Log.e("ToGcm", "could Not send");
            Log.e("ToGcm", ex.getMessage().toString());
            msg = "false";
        }
        return msg;
    }
}
