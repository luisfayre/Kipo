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

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    Button btnLogOut;

    View theInflatedView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        theInflatedView = inflater.inflate(R.layout.fragment_profile,container,false);

        btnLogOut = theInflatedView.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cerrar la sesion actual del ususario
                FirebaseAuth.getInstance().signOut();
                //Mandar a la pantalla de logIn
                goLoginScreen();
            }
        });

        return theInflatedView;
    }

    private void goLoginScreen(){
        //Se obtiene la actividad en la que se encuentra en fragmente actual y se destruye
        getActivity().finish();

        //Se manda a la pantalla de login
        Intent intent = new Intent(theInflatedView.getContext(), MainActivity.class);
        startActivity(intent);

    }
}
