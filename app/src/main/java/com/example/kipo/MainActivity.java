package com.example.kipo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Password para el mecanismo de recuperacion de contraseña
    String sRetrivePass;

    //Info del usuario
    String passDB = "";
    String userInput;
    String passInput;

    //Botones y textos
    Button btnLog;
    TextView createUsser;
    TextView forgotPass;
    EditText editTextUser;
    EditText editTextPass;

    //Firebase referencias a la base de datos
    DatabaseReference rootRef;
    DatabaseReference userRef;

    //Firebase auth
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        quitarBarraSuperior();
        setContentView(R.layout.activity_main);

        //Inicializar las variables a utilizar de Firebase
        inicializarFirebase();

        //Verificar si existe una sesion activa y de ser asi mandarlo a la pantalla principal
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    goMainScreen();
                }
            }
        };

        //Boton que manda al registro de usuario
        createUsser = findViewById(R.id.txtCreateUser);
        createUsser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Register.class);
                startActivity(intent);
            }
        });

        //Al olvidar la contraseña
        forgotPass = findViewById(R.id.txtForgotPass);
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Inicializar dialogo
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_recover_password);


                //Botones y texto del dialogo
                final EditText edtxtUsername = dialog.findViewById(R.id.edDialogUser);
                Button btnDialog = dialog.findViewById(R.id.btnDialog);
                final TextView texto = dialog.findViewById(R.id.txtDialogResult);
                texto.setVisibility(View.INVISIBLE);

                //Al precionar el boton de busqueda del dialogo
                btnDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String user = edtxtUsername.getText().toString();

                        //Obtener los valores del password y el user de la base de datos
                        userRef = rootRef.child("users").child(user).child("password");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String resultado = dataSnapshot.getValue(String.class);
                                resultado = resultado==null ? "":resultado;
                                if(resultado.equals("")){
                                    texto.setText("Username do not match");
                                    texto.setVisibility(View.VISIBLE);
                                }else{
                                    texto.setText("Your password is: " + resultado);
                                    texto.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });

        //Proceso de log in
        editTextUser = findViewById(R.id.edTxtUserName);
        editTextPass = findViewById(R.id.edTxtUserPassR);
        btnLog = findViewById(R.id.btnLogin);
        btnLog.setOnClickListener(this);

    }
    public void inicializarFirebase(){
        if(rootRef == null) {
            //Firebase hacer persistentes los datos y funcionalidad offline
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            //Firebase referencia a la instancia principal de la base de datos
            rootRef = FirebaseDatabase.getInstance().getReference();
        }
    }

    public void quitarBarraSuperior(){
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
    }

    //Metodo sin al autentificacion
    public void logIn(){
        //Desabilitar boton hasta que termine de buscar en la base de datos
        btnLog.setEnabled(false);

        //Obtener usuario y password que el usuario facilito
        userInput = editTextUser.getText().toString();//email
        passInput = editTextPass.getText().toString();//password

        //Obtener los valores del password y el user de la base de datos
        userRef = rootRef.child("users").child(userInput).child("password");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            //Metodo asyncronico
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                passDB = dataSnapshot.getValue(String.class);
                //Hacer comprobacion
                passDB = passDB == null ? "":passDB;
                if(passDB.equals(passInput) && !passDB.equals("")) {
                    goMainScreen();
                }else{
                    Toast.makeText(MainActivity.this,  "Password or user invalid", Toast.LENGTH_SHORT).show();
                }
                btnLog.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void logInEmail(){
        //Desabilitar boton hasta que termine de buscar en la base de datos
        btnLog.setEnabled(false);

        //Obtener usuario y password que el usuario facilito
        userInput = editTextUser.getText().toString();//email
        passInput = editTextPass.getText().toString();//password

        if(userInput.equals("")||passInput.equals("")){
            Toast.makeText(MainActivity.this,  "Password or user missing", Toast.LENGTH_SHORT).show();
        }else{
            firebaseAuth.signInWithEmailAndPassword(userInput,passInput).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        //Exito en la operacion
                    }else{
                        Toast.makeText(MainActivity.this,  "Invalid User", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        btnLog.setEnabled(true);
    }

    public void goMainScreen(){
        Intent intent = new Intent(MainActivity.this, PrincipalMenu.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        logInEmail();
    }

    //Gracias a estos ultimos dos override es posible manejar las sesiones
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuthListener != null){
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }

}
