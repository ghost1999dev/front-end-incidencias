package com.android.proyectoincidencias;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertActivityIncidence extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btnBuscar, btnSubir;
    Spinner spinnerUsuarios;
    ImageView iv;
    EditText text,textFecha,textStatus;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    String UPLOAD_URL = "http://192.168.76.44/api-incidencias/api-incidencia-registrar.php";
    String API_USUARIOS="http://192.168.76.44/api-incidencias/api-listar-spinner.php";
    String KEY_IMAGE = "imagen";
    String KEY_NOMBRE = "descripcion";
    String KEY_FECHA="fecha";
    String KEY_STATUS="status";
   ArrayList<String>tipoIncidence=new ArrayList<>();
   ArrayAdapter<String>tipoIncidenceAdapter;
    private JSONArray jsonArray;
    private int idSeleccionado;
    private static final String PREFS_NAME="MyPrefs";

    String status="ACTIVO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_incidence);

        btnBuscar = findViewById(R.id.buscar);
        btnSubir = findViewById(R.id.registrar);
        textFecha= findViewById(R.id.fecha);
        textStatus=findViewById(R.id.status);
        long ahora = System.currentTimeMillis();
        Date fecha = new Date(ahora);
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        String date=df.format(fecha);
        textFecha.setText(date);
        //Log.d("InsertActivityIncidence","Fecha:"+date);
        textStatus.setText(status);
        text = findViewById(R.id.usuario);
        iv = findViewById(R.id.image);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        obtenerDatosSpinner(API_USUARIOS);


    }
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(){
       final ProgressDialog loading= new ProgressDialog(this);
       loading.setTitle("Subiendo");
       loading.setMessage("Espere por favor");
       loading.show();
       StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {


               try {
                   JSONObject jsonObject = new JSONObject(response);
                   String status = jsonObject.getString("status");
                   String message = jsonObject.getString("message");

                   if (status.equals("success")) {
                       Toast.makeText(InsertActivityIncidence.this, message, Toast.LENGTH_SHORT).show();
                       loading.dismiss();

                       startActivity(new Intent(getApplicationContext(),IncidenceRegister.class));
                       finish();
                   } else {
                       loading.dismiss();
                       Toast.makeText(InsertActivityIncidence.this, message, Toast.LENGTH_SHORT).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
           }
       }){

           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               String imagen=getStringImagen(bitmap);
               String nombre = text.getText().toString().trim();
               String fecha = textFecha.getText().toString().trim();
               String status= textStatus.getText().toString().trim();
               SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
               String id =sharedPreferences.getString("id", null);

               Map<String,String> params= new HashMap<>();
               params.put(KEY_IMAGE,imagen);
               params.put(KEY_NOMBRE,nombre);
               params.put("id",String.valueOf(idSeleccionado));
               params.put("idUsuario",id);
               params.put(KEY_FECHA,fecha);
               params.put(KEY_STATUS,status);
               return params;
           }
       };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                iv.setImageBitmap(bitmap);
            }  catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void obtenerDatosSpinner(String API){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        spinnerUsuarios=findViewById(R.id.spinner);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, API, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                     jsonArray = response.getJSONArray("datos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String nombre = jsonObject.optString("nombre");
                        //Log.d("InsertActivityIncidence",nombre);
                        tipoIncidence.add(nombre);
                        tipoIncidenceAdapter=new ArrayAdapter<>(InsertActivityIncidence.this,
                                android.R.layout.simple_spinner_item,tipoIncidence);
                        tipoIncidenceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUsuarios.setAdapter(tipoIncidenceAdapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        requestQueue.add(jsonObjectRequest);
        spinnerUsuarios.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            idSeleccionado=jsonObject.optInt("id");
            Log.d("InsertActivityIncidence","ID SELECCIONADO" + idSeleccionado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("InsertActivityIncidence","Nombre seleccionado "+nombreSeleccionado);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }





}