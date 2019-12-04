package com.example.kipo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kipo.plants.plantDataItem;
import com.example.kipo.plants.showPlantData;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GardenFragment2 extends Fragment implements View.OnClickListener {
    private int CODE = 1000;
    Button addPlant;
    PlantCard[] plants = new PlantCard[1];
    ListView listPlants;
    ImageView imgU;
    ImageView imgD;
    TextView txtU;
    PlantCardAdapter plantCardAdapter;
    View theInflatedView;

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<plantDataItem, showPlantData.ShowDataViewHolder> mFirebaseAdapter;
    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    public GardenFragment2() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.fragment_show_data, container, false);

        imgU = theInflatedView.findViewById(R.id.imgU);
        imgD = theInflatedView.findViewById(R.id.imgD);
        txtU = theInflatedView.findViewById(R.id.txtU);
        addPlant = theInflatedView.findViewById(R.id.btnAddPlant);
        addPlant.setOnClickListener(this);

        // listPlants = theInflatedView.findViewById(R.id.listPlants);
        recyclerView = theInflatedView.findViewById(R.id.mostrar_datos_ly);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("plants").child(user.getUid());

        myRef.keepSynced(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(theInflatedView.getContext()));
        Toast.makeText(getContext(), "Cargando tus plantas...", Toast.LENGTH_SHORT).show();


        /*plantCardAdapter = new PlantCardAdapter(theInflatedView.getContext(),R.layout.layout_plant_card, plants);
        if(plants[0] != null){
            imgGone();
            listPlants.setAdapter(plantCardAdapter);
            return theInflatedView;
        }else {
            return theInflatedView;
        }*/
        return theInflatedView;
    }


    public void imgGone() {
        imgU.setVisibility(View.GONE);
        imgD.setVisibility(View.GONE);
        txtU.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intenAdd = new Intent(getActivity(), AddPlant.class);
        startActivityForResult(intenAdd, CODE);
    }


    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<plantDataItem, showPlantData.ShowDataViewHolder>
                (plantDataItem.class, R.layout.layout_plant_card, showPlantData.ShowDataViewHolder.class, myRef) {


            public void populateViewHolder(final showPlantData.ShowDataViewHolder viewHolder, plantDataItem model, final int position) {
                //viewHolder.Image_URL(model.getImage_URL());
                //viewHolder.Image_Title(model.getImage_Title());

                viewHolder.plantName(model.getName());
                viewHolder.plantDesc(model.getDescription());
                viewHolder.plantImag(model.getImage());
                ;
                int selecteItems = position;
                if (mFirebaseAdapter.getItem(position) != null) {
                    //Toast.makeText(theInflatedView.getContext(), "Existe", Toast.LENGTH_SHORT).show();
                    imgGone();
                } else {
                    //  Toast.makeText(theInflatedView.getContext(), "No xiste", Toast.LENGTH_SHORT).show();

                }

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        Toast.makeText(theInflatedView.getContext(), "Pos:" + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(theInflatedView.getContext(), InfoPlant.class);

                        int selectedItems = position;
                        String fRef = String.valueOf(mFirebaseAdapter.getRef(selectedItems).getKey());
                        intent.putExtra("PLANT_REF", fRef);
                        startActivity(intent);

                        Toast.makeText(theInflatedView.getContext(), ""+ mFirebaseAdapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                        /*
                        AlertDialog.Builder builder = new AlertDialog.Builder(theInflatedView.getContext());
                        builder.setMessage("Deseas eliminar este elemento?").setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int selectedItems = position;
                                        mFirebaseAdapter.getRef(selectedItems).removeValue();
                                        mFirebaseAdapter.notifyItemRemoved(selectedItems);
                                        recyclerView.invalidate();
                                        onStart();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.setTitle("Confimar");
                        dialog.show();*/
                    }
                });


            }
        };

        mLayoutManager = new LinearLayoutManager(theInflatedView.getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);


    }
    /*
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


    }*/
/*
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
    }*/
}

