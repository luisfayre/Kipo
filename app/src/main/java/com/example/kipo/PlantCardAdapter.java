package com.example.kipo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlantCardAdapter extends ArrayAdapter<PlantCard> {
    Context contexto;
    int recurso;
    PlantCard[] plants;

    //contexto de la app, resource es el layout, el objeto plantCard
    public PlantCardAdapter(Context context, int resource, PlantCard[] objects) {
        super(context, resource, objects);
        contexto = context;
        recurso = resource;
        plants = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgClima;
        TextView txtCiudad, txtTemperatura, txtClima;
        ImageView imgPlanta;
        ImageView imgPlantaState;
        TextView plantName;
        TextView plantDescription;

        //CONVERTVIEW ES UNA FILA DE LA LISTA
        if(convertView == null){
            //CREAR NUESTRO LAYOUT
            //INFLATER
            LayoutInflater lInflater = ((Activity)contexto).getLayoutInflater();
            convertView = lInflater.inflate(recurso,parent,false);
        }
        //SACAMOS DE LA CONVERVIEW LOS DATOS QUE NECESITAMOS
        imgPlanta = convertView.findViewById(R.id.imgPlantCard);
        imgPlantaState = convertView.findViewById(R.id.imgPlantState);
        plantName = convertView.findViewById(R.id.txtPlantName);
        plantDescription = convertView.findViewById(R.id.txtPlantDesc);

        //LO LLENAMOS
        imgPlanta.setImageResource(plants[position].getImagePlant());
        imgPlantaState.setImageResource(plants[position].getImagePlantState());
        plantName.setText(plants[position].getPlantName());
        plantDescription.setText(plants[position].getPlantDescription());


        return convertView;
    }
}
