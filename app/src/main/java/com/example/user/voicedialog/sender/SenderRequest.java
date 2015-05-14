package com.example.user.voicedialog.sender;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
        mQueque.add(getUpdateOnDateRequest);
    }


    }
