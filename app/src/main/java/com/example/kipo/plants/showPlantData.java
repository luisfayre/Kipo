package com.example.kipo.plants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kipo.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by luisf on 01/06/2017.
 */
public class showPlantData extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<plantDataItem, ShowDataViewHolder> mFirebaseAdapter;
    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    public showPlantData() {
    // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_data);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("plants").child(user.getUid());

        recyclerView = findViewById(R.id.mostrar_datos_ly);
        recyclerView.setLayoutManager(new LinearLayoutManager(showPlantData.this));
        Toast.makeText(showPlantData.this, "Cargando tus plantas...", Toast.LENGTH_SHORT).show();



    }


    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter = new FirebaseRecyclerAdapter<plantDataItem, ShowDataViewHolder>
                (plantDataItem.class, R.layout.layout_plant_card, ShowDataViewHolder.class, myRef)

        {

            public void populateViewHolder(final ShowDataViewHolder viewHolder, plantDataItem model, final int position) {
                //viewHolder.Image_URL(model.getImage_URL());
                //viewHolder.Image_Title(model.getImage_Title());

                viewHolder.plantName(model.getName());
                viewHolder.plantDesc(model.getDescription());
                viewHolder.plantImag(model.getImage());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(showPlantData.this);
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
                        dialog.show();
                    }
                });


            }
        };

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mFirebaseAdapter);


    }



    //View Holder For Recycler View
    public static class ShowDataViewHolder extends RecyclerView.ViewHolder {
        //private final TextView image_title;
        //private final ImageView image_url;

        ImageView imgPlantaState;
        TextView plantName;
        TextView plantDescription;
        //TextView plantType;
        ImageView plantImage;

        public ShowDataViewHolder(final View itemView) {
            super(itemView);

           //image_url = (ImageView) itemView.findViewById(R.id.img_meme);
            //image_title = (TextView) itemView.findViewById(R.id.texto_meme);

            plantImage = itemView.findViewById(R.id.imgPlantCard);
            imgPlantaState = itemView.findViewById(R.id.imgPlantState);
            plantName = itemView.findViewById(R.id.txtPlantName);
            plantDescription = itemView.findViewById(R.id.txtPlantDesc);
        }

        private void plantImag(String title) {
            Glide.with(itemView.getContext())
                    .load(title)
                    .crossFade()
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(plantImage);
        }

        public void plantName(String name) {
            plantName.setText(name);

        }

        public void plantDesc(String description) {
            plantDescription.setText(description);
        }

    }


}