package com.android.proyectoincidencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditarActivityIncidence extends AppCompatDialogFragment {



    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }



    public static void EditarIncidencias(Context context,String estado,String usuario,String id,
                                         String API,String token,String notificacion,String title){

        System.out.println(notificacion);
        System.out.println(token);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Cargando......");
        progressDialog.show();
        StringRequest request= new StringRequest(Request.Method.POST,
                API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("success")) {
                        FCMSend.pushNotificacion(context,title,notificacion,token);
                        Toast.makeText(context, "Se reporto satisfactoriamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context,IncidenceRegister.class);
                        context.startActivity(intent);

                        ((Activity)context).finish();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR EN LA SOLICITUD"+error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params=new HashMap<>();
                params.put("id",id);
                params.put("status",estado);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        requestQueue.add(request);


    }
}