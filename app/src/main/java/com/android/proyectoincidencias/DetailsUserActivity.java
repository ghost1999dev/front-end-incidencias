package com.android.proyectoincidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class DetailsUserActivity extends AppCompatActivity {

    TextView text1,text2,text3,text4;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        text1=findViewById(R.id.id);
        text2=findViewById(R.id.nombre);
        text3=findViewById(R.id.usuario);
        text4=findViewById(R.id.rol);

        Intent intent = getIntent();
        position= intent.getExtras().getInt("position");

        Log.d("DetailsUserActivity", "Position" + position);

        text1.setText("ID " +UserRegister.users.get(position).getId());
        text2.setText("Nombre " +UserRegister.users.get(position).getNombre());
        text3.setText("Usuario " +UserRegister.users.get(position).getUser());
        text4.setText("Rol " +UserRegister.users.get(position).getRol());
    }
}