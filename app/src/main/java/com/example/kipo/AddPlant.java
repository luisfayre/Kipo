package com.example.kipo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kipo.plants.plantDataItem;
import com.example.kipo.plants.showPlantData;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddPlant extends AppCompatActivity{

    //Interface
    private Button btnBack, btnAdd;
    private ImageView imgPlant;
    private Button b1,b2,b3,b4,b5,b6;
    private EditText txtName, txtDesc;

    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRoofRef;
    private Uri mImageUri = null;
    private DatabaseReference mdatabaseRef;
    private StorageReference mStorage;

    //Firebase referencia a la instancia principal de la base de datos
    DatabaseReference rootRef;
    DatabaseReference userRef;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_add_plant);

        Firebase.setAndroidContext(this);

        txtName = findViewById(R.id.edTxtPlanName);
        txtDesc = findViewById(R.id.edTxtDesc);
        imgPlant = findViewById(R.id.imageView6);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callgalary();
            }
        });
        btnAdd = findViewById(R.id.btnAdd);

        //Select Image From External Storage..
        imgPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Check for Runtime Permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    //Toast.makeText(getApplicationContext(), "Call for Permission", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else
                {
                    callgalary();
                }
            }
        });

        //Firebase user
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //Initialize Firebase Database paths for database and Storage

        mdatabaseRef = FirebaseDatabase.getInstance().getReference();
        // mRoofRef = new Firebase("https://fir-tutorial-5800f.firebaseio.com/").child("User_Details").push();  // Push will create new child every time we upload data
        mRoofRef = new Firebase("https://kipo-5c607.firebaseio.com/").child("plants").child(user.getUid()).push();  // Push will create new child every time we upload data
        // mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-tutorial-5800f.appspot.com/");
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://kipo-5c607.appspot.com/");

        //Click on Upload Button Title will upload to Database
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sName = txtName.getText().toString().trim();
                String sDesc = txtDesc.getText().toString().trim();
                String sType = txtName.getText().toString().trim();
                String sImg = txtDesc.getText().toString().trim();

                plantDataItem plantDataItem = new plantDataItem(sName, sDesc, sType, sImg);

                if(!sName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Sin titulo", Toast.LENGTH_SHORT).show();
                    Firebase childRef_name = mRoofRef.child("name");
                    Firebase childRef_desc = mRoofRef.child("description");
                    Firebase childRef_type = mRoofRef.child("type");
                    childRef_name.setValue(sName);
                    childRef_desc.setValue(sDesc);
                    childRef_type.setValue("tipo");
                    finish();
                   // rootRef.child("plants").child(user.getUid()).push().setValue(plantDataItem);
                }else{
                    Toast.makeText(getApplicationContext(), "Sin titulo, por favor inserte uno", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    //Check for Runtime Permissions for Storage Access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callgalary();
                return;
        }
        Toast.makeText(getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }
    
    //If Access Granted gallery Will open
    private void callgalary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            mImageUri = data.getData();
            imgPlant.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("User_Images").child(mImageUri.getLastPathSegment());

            //mProgressDialog.setMessage("Subiendo Imagen…");
            //mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadUri = taskSnapshot.getDownloadUrl();  //Ignore This error


                    assert downloadUri != null;
                    mRoofRef.child("image").setValue(downloadUri.toString());

                    Glide.with(getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(imgPlant);
                    // Toast.makeText(getApplicationContext(), "Updated.", Toast.LENGTH_SHORT).show();
                    //mProgressDialog.dismiss();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    //displaying percentage in progress dialog
                   // mProgressDialog.setMessage("Subiendo " + ((int) progress) + "%...");
                }
            });
        }
    }

    public void tipo(View view) {
    }
/*
    public void ver(View view) {
        Intent intent = new Intent(this, showPlantData.class);
        startActivity(intent);
    }*/
}
