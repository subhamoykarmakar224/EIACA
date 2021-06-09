package com.example.collectdata.services.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.collectdata.Constants;

import java.io.UnsupportedEncodingException;

public class SendDataToServer {
    private String TAG = "SendDataToServer::ollo";
    private Context context;
    private String strPacket;

    public SendDataToServer(Context context, String strPacket) {
        this.context = context;
        this.strPacket = strPacket;
    }

    public void sendEmotionCode() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.URI_API_PRODUCER;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response is : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Response is : ERROR : " + error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return strPacket == null ? null : strPacket.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    Log.i(TAG, "Error :: " + e);
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(stringRequest);
    }

    public void getMyFriendsStatus() {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = Constants.URI_API_CONSUMER;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response is : " + response);
                // TODO : Notification
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Response is : ERROR : " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
}
