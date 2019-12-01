package com.example.kipo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GardenFragment extends Fragment implements  View.OnClickListener{
    private int CODE = 1000;
    Button addPlant;
    PlantCard[] plants = new PlantCard[1];
    ListView listPlants;
    ImageView imgU;
    ImageView imgD;
    TextView txtU;
    PlantCardAdapter plantCardAdapter;
    View theInflatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.fragment_garden, container, false);
        imgU = theInflatedView.findViewById(R.id.imgU);
        imgD = theInflatedView.findViewById(R.id.imgD);
        txtU = theInflatedView.findViewById(R.id.txtU);
        addPlant = theInflatedView.findViewById(R.id.btnAddPlant);
        addPlant.setOnClickListener(this);
        listPlants = theInflatedView.findViewById(R.id.listPlants);
        plantCardAdapter = new PlantCardAdapter(theInflatedView.getContext(),R.layout.layout_plant_card, plants);
        if(plants[0] != null){
            imgGone();
            listPlants.setAdapter(plantCardAdapter);
            return theInflatedView;
        }else {
            return theInflatedView;
        }
    }
    public void imgGone(){
        imgU.setVisibility(View.GONE);
        imgD.setVisibility(View.GONE);
        txtU.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View view) {
        Intent intenAdd = new Intent(getActivity(), AddPlant.class);
        startActivityForResult(intenAdd,CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == Activity.RESULT_OK){ //resultCode es la seleccion que da el usuario
                int photo = data.getIntExtra("IMG",R.drawable.cactus);
                int phStat = data.getIntExtra("IMGD",R.drawable.planthappy);
                String name = data.getStringExtra("NAME");
                String desc = data.getStringExtra("DESC");
                updateArray(photo,phStat,name,desc);
                plantCardAdapter = new PlantCardAdapter(theInflatedView.getContext(),R.layout.layout_plant_card, plants);
                listPlants.setAdapter(plantCardAdapter);
                imgGone();

            }else {

            }


    }

    public void updateArray(int p, int pS, String n, String d){
        if(plants[0] != null){
            PlantCard[] newPC = new PlantCard[plants.length+1];
            for (int i =0; i<newPC.length-1; i++){
                newPC[i] = new PlantCard(
                        plants[i].getImagePlant(),
                        plants[i].getImagePlantState(),
                        plants[i].getPlantName(),
                        plants[i].getPlantDescription()
                        );
            }
            newPC[newPC.length-1] = new PlantCard(p,pS,n,d);

            plants = new PlantCard[newPC.length];
            for (int i=0; i<newPC.length;i++){
                plants[i] = new PlantCard(
                        newPC[i].getImagePlant(),
                        newPC[i].getImagePlantState(),
                        newPC[i].getPlantName(),
                        newPC[i].getPlantDescription()
                );
            }
        }else{

            plants[0] = new PlantCard(p,pS,n,d);

        }
    }
}

