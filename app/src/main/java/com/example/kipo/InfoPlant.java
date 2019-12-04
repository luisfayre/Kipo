package com.example.kipo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kipo.plants.plantDataItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class InfoPlant extends AppCompatActivity {

    //Interface
    ImageView imgPlant;
    TextView txtType;
    Button btnAcept;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    //Firebase database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef,myRef2;

   // private String mPostKey;
   // public static final String EXTRA_POST_KEY = "post_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_info_plant);

        imgPlant = findViewById(R.id.imagPlant);
        txtType = findViewById(R.id.txtType);
        btnAcept = findViewById(R.id.btnAcept);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        String plant_ref = intent.getStringExtra("PLANT_REF");

        txtType.setText(plant_ref);

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("plants").child(user.getUid()).child(plant_ref);

        //StorageReference riversRef = storageReference.child("images/").child("perfil/").child(user.getUid());

        //String as = String.valueOf(myRef);


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                plantDataItem value = dataSnapshot.getValue(plantDataItem.class);

                Glide.with(getApplicationContext()).load(value.getImage()).into(imgPlant);
                //txtType.setText(value.getType());

                /** Obtenemos el tipo de la ref*/
                Toast.makeText(InfoPlant.this, value.getType(), Toast.LENGTH_SHORT).show();

                myRef2 = FirebaseDatabase.getInstance().getReference("info").child(value.getType());

                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        infoPlantType infoPlantType = dataSnapshot.getValue(infoPlantType.class);
                        txtType.setText(infoPlantType.getDescription());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                /*
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String value = dataSnapshot.getValue(String.class);
                    Toast.makeText(InfoPlant.this, value, Toast.LENGTH_SHORT).show();

                    if(user !=null){
                        plantDataItem plantDataItem = new plantDataItem();
                        plantDataItem.setName(ds.getValue(plantDataItem.class).getName());
                        txtType.setText(plantDataItem.getName());
                    }

                }*/

                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
