package com.android.proyectoincidencias;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserRegister extends AppCompatActivity {

    private ListView list;
    private EditText search;
    Adaptador adapter;
    public static ArrayList<User>users=new ArrayList<>();
    String url="http://192.168.76.44/api-incidencias/api-listar.php";
    String urlEliminar="http://192.168.76.44/api-incidencias/api-eliminar.php";

    User usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        list=findViewById(R.id.listView);
        adapter=new Adaptador(this,users);
        list.setAdapter(adapter);

        search=findViewById(R.id.buscador);

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

        FloatingActionButton fab = findViewById(R.id.btnAgregar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserRegister.this,InsertActivityUser.class);
                startActivity(i);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                CharSequence[] dialogItem={"Ver datos","Editar datos","Eliminar datos"};

                builder.setTitle(users.get(position).getNombre());
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                startActivity(new Intent(getApplicationContext(),DetailsUserActivity.class)
                                        .putExtra("position",position));
                                break;

                            case 1:
                                startActivity(new Intent(getApplicationContext(),EditarActivityUser.class)
                                        .putExtra("position",position));

                            case 2:
                                EliminarDatos(users.get(position).getId(),urlEliminar);
                        }
                    }
                });
                builder.create().show();
            }
        });

        listData();
    }


    public void listData(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                users.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("datos");
                    if (status.equals("success")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object=jsonArray.getJSONObject(i);
                            String idJson=object.getString("id");
                            String nombreJson=object.getString("nombre");
                            String userJson=object.getString("usuario");
                            String rolJson = object.getString("rol");

                            usuarios= new User(idJson,nombreJson,userJson,rolJson);
                            users.add(usuarios);
                            adapter.notifyDataSetChanged();


                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public void filter(String text){
        ArrayList<User>filterList=new ArrayList<>();
        for (User user:users){
            if(user.getNombre().toLowerCase().contains(text.toLowerCase())){
                filterList.add(user);
            }
        }
        adapter.filtrar(filterList);
    }

    public void EliminarDatos(String id,String API){
        StringRequest request = new StringRequest(Request.Method.POST, API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message= jsonResponse.getString("message");

                            if(status.equals("success")){
                                Toast.makeText(UserRegister.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UserRegister.class));
                                finish();

                            }else{
                                Toast.makeText(UserRegister.this, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserRegister.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();
                params.put("id", id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}