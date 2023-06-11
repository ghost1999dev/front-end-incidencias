package com.android.proyectoincidencias;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncidenceRegister extends AppCompatActivity {

    public static String url="http://192.168.76.44/api-incidencias/api-listar-incidencias.php";
    List<Incidence>incidencesList;
    RecyclerView recyclerView;
    AdaptadorIncidence adaptadorIncidence;

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidence_register);
        recyclerView=findViewById(R.id.recycler);
        search= findViewById(R.id.buscador);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        incidencesList= new ArrayList<>();

        cargarDatos();

        FloatingActionButton fab = findViewById(R.id.btnAddIncidence);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IncidenceRegister.this,InsertActivityIncidence.class);
                startActivity(i);
            }
        });


        //EVENTO DEL INPUT BUSCAR

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

    }

    private void cargarDatos() {
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    if (status.equals("success")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object=jsonArray.getJSONObject(i);
                           incidencesList.add(new Incidence (
                                   object.getString("id"),
                                   object.getString("descripcion"),
                                   object.getString("imagen"),

                                   object.getString("fecha"),
                                   object.getString("estado"),
                                   object.getString("nombre"),
                                   object.getString("token")


                           ));


                        }

                         adaptadorIncidence = new AdaptadorIncidence(IncidenceRegister.this,incidencesList);
                        recyclerView.setAdapter(adaptadorIncidence);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IncidenceRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void filter(String text){
        ArrayList<Incidence>filterList=new ArrayList<>();
        for (Incidence incidence: incidencesList){
            if(incidence.getDescripcion().toLowerCase().contains(text.toLowerCase())){
                filterList.add(incidence);
            }
        }
        adaptadorIncidence.filtrar(filterList);

    }
}