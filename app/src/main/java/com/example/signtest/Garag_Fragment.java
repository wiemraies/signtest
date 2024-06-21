package com.example.signtest;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Garag_Fragment extends Fragment {

    private FirebaseFirestore firestore;
    private VehiculeInfoAdapter adapter;


    private ArrayList<VehiculeModel> vehiculeInfoList;

    private RecyclerView vehiculeRecyclerView;
    private MaterialCardView addButton;
    private MaterialCardView searchView;

    public Garag_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_garag_, container, false);

            addButton = view.findViewById(R.id.addButton);
            searchView = view.findViewById(R.id.searchView);

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Masquer le bouton et la barre de recherche
                    addButton.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);

                    // Remplacer ce fragment par le fragment de destination
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new AddVehiculeInfoFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        vehiculeRecyclerView = view.findViewById(R.id.CarRecyclerView);
        // Initialiser Firestore et la liste de véhicules
        firestore = FirebaseFirestore.getInstance();
        vehiculeInfoList = new ArrayList<>();

        // Initialiser l'adaptateur ici avec une liste vide
        adapter = new VehiculeInfoAdapter(vehiculeInfoList, getContext());
        vehiculeRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        vehiculeRecyclerView.setLayoutManager(layoutManager);

        // Récupérer les informations des véhicules depuis Firestore
        getVehiculeInfo();

        // Définir un message de chargement ou une vue vide si nécessaire
        // en attendant que les données soient récupérées et affichées

        return view;
    }


    private void getVehiculeInfo() {
        if (!isAdded()) {
            return; // Fragment not attached to activity, return early
        }

        // Accessing context safely
        firestore.collection("VehiculeInfo").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        VehiculeModel model = document.toObject(VehiculeModel.class);
                        if (model != null) {
                            // Handle your data processing here
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                e.printStackTrace();
            }
        });
    }


    private String getAddressFromLatLong(Context context, double latitude, double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
