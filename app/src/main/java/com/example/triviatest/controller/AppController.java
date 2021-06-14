package com.example.triviatest.controller;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {
    public static final String Tag = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private RequestQueue mRequestQueue;

    public static synchronized AppController getInstance() {
//        if (instance == null) {
//            instance = new MySingleton(context);
        return mInstance;
        }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req,String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? Tag : tag);
        getRequestQueue().add(req);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);

    }

    public void cancelPendingRequests(Object tag){
        if (mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }


}
