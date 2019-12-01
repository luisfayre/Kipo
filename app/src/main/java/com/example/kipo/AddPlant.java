package com.example.kipo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class AddPlant extends AppCompatActivity implements View.OnClickListener {
    Button btnBack;
    Button btnAdd;
    Button b1,b2,b3,b4,b5,b6;
    EditText txtName;
    EditText txtDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_add_plant);
        txtName = findViewById(R.id.edTxtPlanName);
        txtDesc = findViewById(R.id.edTxtDesc);
        asignarBotones();
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int random = r.nextInt((2 - 0) + 1) + 0;
                int imgChooseRandom;
                switch (random){
                    case 0:
                        imgChooseRandom = R.drawable.plantaa;
                        break;
                    case 1:
                        imgChooseRandom = R.drawable.plantaaa;
                        break;
                    case 2:
                        imgChooseRandom = R.drawable.cactus;
                        break;
                    default:
                        imgChooseRandom = R.drawable.plant;
                        break;
                }
                Intent intent = getIntent();
                intent.putExtra("IMG",imgChooseRandom);
                intent.putExtra("IMGD",R.drawable.planthappy);
                intent.putExtra("NAME",txtName.getText().toString());
                intent.putExtra("DESC",txtDesc.getText().toString());
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Button selected = (Button) view;
        //selected.setEnabled(true);
        //selected.getBackground().getState();
    }
    public void asignarBotones(){
        b1 = findViewById(R.id.button9);
        b2 = findViewById(R.id.button10);
        b3 = findViewById(R.id.button11);
        b4 = findViewById(R.id.button12);
        b5 = findViewById(R.id.button13);
        b6 = findViewById(R.id.button14);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
    }

}
