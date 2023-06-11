package com.android.proyectoincidencias;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    EditText txtuser,txtpassword;
    Button iniciarSesion;
    String API="http://192.168.76.44/api-incidencias/api-login.php";
    private static final String PREFS_NAME="MyPrefs";
    private static final String PREFS_TOKEN="TokenNotificacion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtuser=findViewById(R.id.user);
        txtpassword=findViewById(R.id.contrasenia);
        iniciarSesion=findViewById(R.id.iniciarSesion);

        iniciarSesion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = txtuser.getText().toString().trim();
                String password=txtpassword.getText().toString().trim();
               // Log.d("MainActivity","Usuario"+user);
                //Log.d("MainActivity","Password"+password);
                insertar(user,password,API);
            }
        });


    }
    private void insertar(String usuario,String password,String API){

            StringRequest request= new StringRequest(Request.Method.POST, API, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   try {
                       JSONObject jsonObject = new JSONObject(response);
                       String status= jsonObject.getString("status");

                       if(status.equals("success")) {
                            String userType = jsonObject.getString("userType");
                            String id=jsonObject.getString("id");
                            String token=jsonObject.getString("token");
                            SharedPreferences sharedPreferencesToken=getSharedPreferences(PREFS_TOKEN,Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorToken=sharedPreferencesToken.edit();
                            editorToken.putString("token",token);
                            editorToken.apply();
                           SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                           SharedPreferences.Editor editor=sharedPreferences.edit();
                           editor.putString("id",id);
                           editor.apply();


                           if(userType.equals("admin")){
                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                                startActivity(intent);
                            }
                       }else {
                           Toast.makeText(getApplicationContext(),
                                   jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                       }

                   }catch (JSONException e){
                       e.printStackTrace();
                       Toast.makeText(getApplicationContext(), "JSON ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Volley error:"+error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String ,String>params=new HashMap<String,String>();
                    params.put("usuario",usuario);
                    params.put("contrasenia",password);
                    return params;
                }
            };
            Volley.newRequestQueue(this).add(request);


    }



}