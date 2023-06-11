package com.android.proyectoincidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class EditarActivityUser extends AppCompatActivity {

    EditText textIdEditable,textNombreEditable,textUsuarioEditable,textPasswordEditable,textRolEditable;

    Button btnEditar;
    int position;
    String API="http://192.168.76.44/api-incidencias/api-editar.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_user);
        textNombreEditable=findViewById(R.id.nombre);
        textIdEditable=findViewById(R.id.id);
        textRolEditable=findViewById(R.id.rol);
        textUsuarioEditable=findViewById(R.id.usuario);
        btnEditar=findViewById(R.id.editar);
        Intent intent=getIntent();
        position=intent.getExtras().getInt("position");

        textNombreEditable.setText(UserRegister.users.get(position).getNombre());
        textIdEditable.setText(UserRegister.users.get(position).getId());
        textRolEditable.setText(UserRegister.users.get(position).getRol());
        textUsuarioEditable.setText(UserRegister.users.get(position).getUser());
        Log.d("EditarActivityUser",UserRegister.users.get(position).getUser());

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editar(API);
            }
        });

    }

    public void Editar(String API){
        final String id =textIdEditable.getText().toString();
        final String nombre=textNombreEditable.getText().toString();
        final String usuario=textUsuarioEditable.getText().toString();
        final String rol =textRolEditable.getText().toString();
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
                        Toast.makeText(EditarActivityUser.this, message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),UserRegister.class));
                        finish();

                    }else{
                        Toast.makeText(EditarActivityUser.this, message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditarActivityUser.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("id",id);
                params.put("usuario",usuario);
                params.put("nombre",nombre);
                params.put("rol",rol);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditarActivityUser.this);
        requestQueue.add(request);

    }
}