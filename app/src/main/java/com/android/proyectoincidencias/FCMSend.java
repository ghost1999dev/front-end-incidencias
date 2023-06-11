package com.android.proyectoincidencias;

import android.app.DownloadManager;
import android.content.Context;
import android.os.StrictMode;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FCMSend {

    private static  String BASE_URL ="https://fcm.googleapis.com/fcm/send";
    private static  String SERVER_KEY="key=AAAAqw3l6NQ:APA91bG8rN0DduHf_XRv80_ol0qs-W7CwzDCXCFo88a8xUrw1DAVMigLEvlmSeJN-_jwS0apa5PTWHqNzMu4nkDtBZK7JQW2rsYp1xlaOfxX6MRDfm8IR9U3Om9C67WruFt7INcWXJUc";
   // private static  Context context;
   // private static  String title="Hola";
    //private static  String message="Hola mundoi";
    //private static  String token="f5E4xc8tTk-wPP6ViwaBE-:APA91bE9ekYI1fXMczwgR0cUdwGIKTzxjjDA2WOxYeGlbl3lEAoMyWDamplFzIf2cVmpxIlAzjsNgmlup4weywrSK_RAmoHHvoa8osla058sWsk-PUUtThSce4gXaE8YPKEdrThHbYr8";


    public static void pushNotificacion(Context context,String title,String message,String token){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        RequestQueue queue= Volley.newRequestQueue(context);


        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("to",token);
            JSONObject notification=new JSONObject();
            notification.put("title",title);
            notification.put("body",message);
            jsonObject.put("notification",notification);

            JsonObjectRequest jsonObje = new JsonObjectRequest(Request.Method.POST, BASE_URL, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM "+ response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //System.out.println(error);

                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params=new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization",SERVER_KEY);
                    return params;

                }
            };
            queue.add(jsonObje);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
