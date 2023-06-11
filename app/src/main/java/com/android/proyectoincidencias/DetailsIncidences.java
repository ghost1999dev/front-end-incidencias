package com.android.proyectoincidencias;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailsIncidences extends AppCompatActivity {
    TextView text1,text2,text3,text4;
    ImageView img;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_incidences);
        text1=findViewById(R.id.DescripcionIncidencia);
        text2=findViewById(R.id.Incidenciafecha);
        text3=findViewById(R.id.Incidenciaestado);
        text4=findViewById(R.id.IncidenciaNombre);
        img=findViewById(R.id.IncidenciaImage);
        Intent intent = getIntent();
        position= intent.getExtras().getInt("position");

        text1.setText("DESCRIPCION " + AdaptadorIncidence.arraylistIncidence.get(position).getDescripcion());
        text2.setText("FECHA " + AdaptadorIncidence.arraylistIncidence.get(position).getFecha());
        text3.setText("ESTADO " + AdaptadorIncidence.arraylistIncidence.get(position).getEstado());
        text4.setText("USUARIO " + AdaptadorIncidence.arraylistIncidence.get(position).getNombre());
        Log.d("DetailsIncidences", "Descripciom" + AdaptadorIncidence.arraylistIncidence.get(position).getImagen());


        Glide.with(this)
                .load(AdaptadorIncidence.arraylistIncidence.get(position).getImagen())
                .into(img);
    }
}