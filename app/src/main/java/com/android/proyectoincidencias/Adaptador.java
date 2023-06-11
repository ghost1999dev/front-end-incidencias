package com.android.proyectoincidencias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends ArrayAdapter<User> {
    Context context;
    List<User> arraylistUsers;
    public Adaptador(@NonNull Context context, List<User>arraylistUsers) {
        super(context, R.layout.my_list_item);
        this.context=context;
        this.arraylistUsers=arraylistUsers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_list_item, null,true);
        TextView textView=view.findViewById(R.id.tvid);
        TextView textView1=view.findViewById(R.id.tvnombre);
        textView.setText(arraylistUsers.get(position).getId());
        textView1.setText(arraylistUsers.get(position).getNombre());
        return view;
    }

    @Override
    public int getCount() {
        return arraylistUsers.size();
    }

    public void filtrar(ArrayList<User> filtroUsers){
        this.arraylistUsers=filtroUsers;
        notifyDataSetChanged();
    }
}
