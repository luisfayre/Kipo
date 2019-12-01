package com.example.kipo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OptionsFragment extends Fragment {
    ListView listOptions;
    String[] comida ={"Opcion 1","Opcion 2","Opcion 3","Opcion 4",
            "Opcion 5","Opcion 6","Opcion 7",
            "Opcion 8","Opcion 9",
            "Opcion 10","Opcion 11","Opcion 12"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View theInflatedView = inflater.inflate(R.layout.fragment_options,container,false);
        listOptions = theInflatedView.findViewById(R.id.listOptions);
        listOptions.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,comida));
        return theInflatedView;
    }
}
