package com.android.proyectoincidencias;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideType;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorIncidence extends RecyclerView.Adapter<AdaptadorIncidence.PlayerViewnHolder> {
    Context context;
    public static List<Incidence> arraylistIncidence;
    private static final String PREFS_TOKEN="TokenNotificacion";
    //EditText userName;





    public AdaptadorIncidence(Context context, List<Incidence> arraylistIncidence) {
        this.context = context;

        this.arraylistIncidence = arraylistIncidence;
    }

    @NonNull
    @Override
    public PlayerViewnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_list_item_incidence,null);
        return new PlayerViewnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewnHolder holder, @SuppressLint("RecyclerView") int position) {
        Incidence incidence = arraylistIncidence.get(position);


        Glide.with(context)
                .load(incidence.getImagen())
                .into(holder.img);
        holder.text1.setText(incidence.getId());
        holder.text2.setText(incidence.getDescripcion());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                CharSequence[] dialogItem={"Ver datos","Reportar incidencia","Eliminar datos"};
                builder.setTitle("Opciones")
                        .setItems(dialogItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //final int itemPosition=position;
                                switch (which){
                                    case 0:
                                        Intent verDatosIntent = new Intent(context,DetailsIncidences.class);
                                        verDatosIntent.putExtra("position",position);
                                        context.startActivity(verDatosIntent);
                                        break;

                                    case 1:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        LayoutInflater inflater = LayoutInflater.from(context);
                                        View dialogView = inflater.inflate(R.layout.activity_editar_incidence, null);

                                        // Obtén una referencia a los elementos de la vista del diálogo
                                        // TextView textView = dialogView.findViewById(R.id.textView);
                                        // ...

                                        EditText username=dialogView.findViewById(R.id.username);
                                        EditText estado= dialogView.findViewById(R.id.status);
                                        estado.setText(incidence.getEstado());
                                        username.setText(incidence.getNombre());


                                        builder.setTitle("Editar datos")
                                                .setView(dialogView)
                                                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Obtén los datos del cuadro de diálogo
                                                        // String newData = textView.getText().toString();
                                                        // ...
                                                        String usernameData=username.getText().toString();
                                                        String estadoData=estado.getText().toString();
                                                        String id = incidence.getId();
                                                        String userToken=incidence.getToken();
                                                        String api="http://192.168.76.44/api-incidencias/api-editar-incidencias.php";
                                                        String title="Reporte de incidencia";
                                                        String message="Se te ha sido reportado la incidencia";
                                                        System.out.println("TOKEN...."+userToken);
                                                        //SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_TOKEN,Context.MODE_PRIVATE);
                                                        //String userToken = sharedPreferences.getString("token",null);
                                                        //String id =sharedPreferences.getString("id", null);
                                                        //String token="dAEbiBerSoioNsMZTwjclJ:APA91bHXpfBCi20TUFgYRMI4bQLNhENnU7yr7-luGFmrzn93Aj62hnkX124UVByBX7_xuRK1gkiTk69PBt4H47-wo8igf4dYl3Vg8YszAuOfsVq8xI3eOmYih915GNogcU4r5zLUmBJz";
                                                       // System.out.println(userToken);
                                                        EditarActivityIncidence.EditarIncidencias(context,estadoData,usernameData,id,api,userToken,message,title);

                                                        //editarActivityIncidence.ImprimirDatos(usernameData,estadoData);

                                                        // Crea un nuevo Intent para enviar los datos a la actividad
                                                        //Intent editarDatosIntent = new Intent(context, EditarActivityIncidence.class);
                                                        //editarDatosIntent.putExtra("position", position);
                                                        // editarDatosIntent.putExtra("newData", newData);
                                                        // ...

                                                        //context.startActivity(editarDatosIntent);
                                                    }
                                                })
                                                .setNegativeButton("Cancelar", null);

                                        AlertDialog dialog1 = builder.create();
                                        dialog1.show();
                                        break;

                                    case 2:
                                        System.out.println("Hola mundo 3");
                                        break;
                                }
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }



    @Override
    public int getItemCount() {
        return arraylistIncidence.size();
    }

    /*public interface ItemClickListener{
        void onItemClick(Incidence incidence);
    }*/

    static class PlayerViewnHolder extends RecyclerView.ViewHolder {
        TextView text1,text2;
        ImageView img;
        public PlayerViewnHolder(@NonNull View itemView) {
            super(itemView);
            text1=itemView.findViewById(R.id.idIncidencia);
            text2=itemView.findViewById(R.id.descripcion);
            img=itemView.findViewById(R.id.imagen);
        }
    }



    public void filtrar(ArrayList<Incidence> filtroIncidence){
        this.arraylistIncidence=filtroIncidence;
        notifyDataSetChanged();
    }

}
