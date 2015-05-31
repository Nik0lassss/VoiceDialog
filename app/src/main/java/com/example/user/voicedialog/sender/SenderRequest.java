package com.example.user.voicedialog.sender;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

/**
 * Created by user on 05.05.2015.
 */

public class SenderRequest {


    private RequestQueue mQueque;
    private Response.Listener<String> response;
    private Response.ErrorListener errorListener;
    public SenderRequest(Context context,Response.Listener<String> response, Response.ErrorListener errorListener) {
        this.mQueque = Volley.newRequestQueue(context);
        this.response=response;
        this.errorListener=errorListener;
    }
    public SenderRequest(Context context) {
        this.mQueque = Volley.newRequestQueue(context);
    }
    public void getPicture(String URL,Response.Listener<Bitmap> responseListener,Response.ErrorListener errorListener)
    {
        ImageRequest ir = new ImageRequest(URL, responseListener, 0, 0, null, errorListener);
        mQueque.add(ir);
    }
    public void sendRequest(final Map<String, String> mp, String URL) {

        StringRequest getUpdateOnDateRequest = new StringRequest(
                Request.Method.POST, URL, response
        , errorListener) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = mp;
                return params;
            }
        };

        getUpdateOnDateRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueque.add(getUpdateOnDateRequest);
    }


    }
