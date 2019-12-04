package com.example.kipo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kipo.plants.plantDataItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    //Interface
    Button btnLogOut;
    View theInflatedView;
    TextView txtUsername, txtUseremail;

    //Firebase authentication
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser user;

    //Firebase database
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.fragment_profile, container, false);

        btnLogOut = theInflatedView.findViewById(R.id.btnLogOut);
        txtUsername = theInflatedView.findViewById(R.id.txtUsername);
        txtUseremail = theInflatedView.findViewById(R.id.txtUseremail);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        myRef.keepSynced(true);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cerrar la sesion actual del ususario
                FirebaseAuth.getInstance().signOut();
                //Mandar a la pantalla de logIn
                goLoginScreen();
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userData value = dataSnapshot.getValue(userData.class);
                txtUsername.setText("Username: " + value.getUsername());
                txtUseremail.setText("Email: " + value.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return theInflatedView;
    }

    private void goLoginScreen() {
        //Se obtiene la actividad en la que se encuentra en fragmente actual y se destruye
        getActivity().finish();

        //Se manda a la pantalla de login
        Intent intent = new Intent(theInflatedView.getContext(), MainActivity.class);
        startActivity(intent);

    }
}
