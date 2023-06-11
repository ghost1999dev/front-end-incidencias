package com.android.proyectoincidencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertActivityUser extends AppCompatActivity {

    EditText textNombre,textUsuario,textRol,textPassword,tokenTxt;
    Button btnInsertar;
    String API="http://192.168.76.44/api-incidencias/api-insertar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);
        textNombre = findViewById(R.id.nombre);
        textUsuario=findViewById(R.id.usuario);
        textRol= findViewById(R.id.rol);
        textPassword= findViewById(R.id.contrasenia);
        btnInsertar=findViewById(R.id.registrar);
        tokenTxt=findViewById(R.id.token);


        btnInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertar(API);
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()){
                            return;
                        }

                        String token = task.getResult();
                        System.out.println("TOKEN:....." + token);
                        tokenTxt.setText(token);
                    }
                });


    }


    private void insertar(String API){
        String nombre = textNombre.getText().toString().trim();
        String usuario = textUsuario.getText().toString().trim();
        String rol = textRol.getText().toString().trim();
        String password =textPassword.getText().toString().trim();
        String token = tokenTxt.getText().toString().trim();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando....");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message=jsonObject.getString("message");

                    if(status.equals("success")){
                        Toast.makeText(InsertActivityUser.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),UserRegister.class));
                        finish();

                    }else{
                        Toast.makeText(InsertActivityUser.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InsertActivityUser.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("usuario",usuario);
                params.put("contrasenia",password);
                params.put("nombre",nombre);
                params.put("rol",rol);
                params.put("token",token);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(InsertActivityUser.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}