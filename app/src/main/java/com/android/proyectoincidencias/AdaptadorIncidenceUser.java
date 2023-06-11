package com.android.proyectoincidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorIncidenceUser extends RecyclerView.Adapter<AdaptadorIncidence.PlayerViewnHolder>{
    Context context;
    public static List<UserIncidence> arraylistIncidenceUser;

    public AdaptadorIncidenceUser(Context context,List<UserIncidence> arraylistIncidenceUser) {
        this.context = context;
        this.arraylistIncidenceUser = arraylistIncidenceUser;
    }

    @NonNull
    @Override
    public AdaptadorIncidence.PlayerViewnHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.my_list_item_incidence_user,null);
        return new AdaptadorIncidence.PlayerViewnHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorIncidence.PlayerViewnHolder holder, int position) {
        UserIncidence incidence = arraylistIncidenceUser.get(position);
        try {
            Glide.with(context)
                    .load(incidence.getImagen())
                    .into(holder.img);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
        holder.text1.setText(incidence.getId());
        holder.text2.setText(incidence.getDescripcion());

    }

    @Override
    public int getItemCount() {
        return arraylistIncidenceUser.size();
    }

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

    public void filtrar(ArrayList<UserIncidence> filtroIncidence){
        this.arraylistIncidenceUser=filtroIncidence;
        notifyDataSetChanged();
    }
}
