package com.example.kipo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    //Botones y textos
    EditText userName;
    EditText userPass;
    EditText userPassConfirm;
    EditText userEmail;
    Button btnSingUp;

    //Datos a enviar
    String userDB;
    String passDB;
    String emailDB;

    //Firebase referencia a la instancia principal de la base de datos
    DatabaseReference rootRef;
    DatabaseReference userRef;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_register);

        //Vincular cosas
        vicular();

        //Inicilizar la instancia de firebase y la persistencia
        inicializarFirebase();

        //Firebase authentication si hay un usuario en linea se manda a la pantalla principal
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    goMainScreen();
                }
            }
        };

        //Iniciar el registro de usuario
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registarUsuarioEmail();
            }
        });

    }

    public void inicializarFirebase() {
        //Firebase hacer persistentes los datos y funcionalidad offline
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Firebase referencia a la instancia principal de la base de datos
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    public void vicular() {
        userName = findViewById(R.id.edTxtUserNameR);
        userPass = findViewById(R.id.edTxtUserPassR);
        userPassConfirm = findViewById(R.id.edTxtUserPassConfirmR);
        userEmail = findViewById(R.id.edTxtUserEmailR);
        btnSingUp = findViewById(R.id.btnSingUpR);
    }

    //Verifica si existen usuarios con un especifico username
    public void verificarUsuariosActivos(){

        //Obtener los valores del password y el user de la base de datos
        userRef = rootRef.child("users").child(userDB).child("password");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //Metodo asyncronico
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String passT = dataSnapshot.getValue(String.class);
                //Hacer comprobacion
                if(passT == null){
                    crearUsuario();
                }else{
                    Toast.makeText(Register.this,"User already exists",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void crearUsuario(){
        //Hacer el registro en la base de datos
        rootRef.child("users").child(userDB).child("email").setValue(emailDB);
        rootRef.child("users").child(userDB).child("password").setValue(passDB);
        Toast.makeText(Register.this,"User registered successfully",Toast.LENGTH_SHORT).show();
    }

    public void goMainScreen(){
        Intent intent = new Intent(Register.this, PrincipalMenu.class);
        startActivity(intent);
        finish();
    }

    private void registrarUsuario(){
        String user = userName.getText().toString();
        String pass = userPass.getText().toString();
        String passCon = userPassConfirm.getText().toString();
        String email = userEmail.getText().toString();

        //Verificar que ningun espacio este vacio
        if(user.equals("")||pass.equals("")||passCon.equals("")||email.equals("")){
            Toast.makeText(Register.this,"Missing data",Toast.LENGTH_SHORT).show();
        }else{
            //Verificar que las contraseñas coincidan
            if(pass.equals(passCon)){
                //Actualiza variables globales ya verificadas
                userDB = user;
                passDB = pass;
                emailDB = email;
                //Verifica que no existe el usuario
                verificarUsuariosActivos();

            }else{
                Toast.makeText(Register.this,"Password do not match",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void registarUsuarioEmail(){
        String user = userName.getText().toString().trim();
        String pass = userPass.getText().toString().trim();
        String passCon = userPassConfirm.getText().toString().trim();
        String email = userEmail.getText().toString().trim();

        btnSingUp.setEnabled(false);
        //Verificar que ningun espacio este vacio
        if(user.equals("")||pass.equals("")||passCon.equals("")||email.equals("")){
            Toast.makeText(Register.this,"Missing data",Toast.LENGTH_SHORT).show();
        }else{
            //Verificar que las contraseñas coincidan
            if(pass.equals(passCon)){
                //Actualiza variables globales ya verificadas
                userDB = user;
                passDB = pass;
                emailDB = email;
                //Crear al usaurio
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //REGISTRAR USUSARIO
                            saveUserInformation();
                            Toast.makeText(Register.this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Register.this, "No se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                            btnSingUp.setEnabled(true);
                        }
                    }
                });

            }else{
                Toast.makeText(Register.this,"Password do not match",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Hacer el registro en la base de datos
    private void saveUserInformation(){
        //Usuario actualmente activo
        FirebaseUser user = firebaseAuth.getCurrentUser();
        rootRef.child("users").child(user.getUid()).child("email").setValue(emailDB);
        rootRef.child("users").child(user.getUid()).child("password").setValue(passDB);
        rootRef.child("users").child(user.getUid()).child("username").setValue(userDB);
        Toast.makeText(Register.this,"User registered successfully",Toast.LENGTH_SHORT).show();
    }
}
