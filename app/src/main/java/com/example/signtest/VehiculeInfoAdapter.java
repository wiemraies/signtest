package com.example.signtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class  VehiculeInfoAdapter extends RecyclerView.Adapter<VehiculeInfoAdapter.ViewHolder> {
    private ArrayList <VehiculeModel> vehiculeList;
    private Context context;
    //make constructer


    public VehiculeInfoAdapter(ArrayList<VehiculeModel> vehiculeList,Context context) {
        this.vehiculeList = vehiculeList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout of vehicule item
        View view= LayoutInflater.from(context).inflate(R.layout.vehiculelayouts,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VehiculeModel model = vehiculeList.get(position);

        // Afficher le nom du véhicule
        holder.carName.setText(model.getVehiculeName());

        // Afficher l'adresse réelle du véhicule
        holder.carLocation.setText(model.getCarRealAddress());
        holder.carLocation.setSelected(true); // Permettre le défilement du texte si nécessaire

        // Charger l'image du véhicule en utilisant Picasso
        Picasso.get().load(model.getVehiculeImage()).into(holder.carImageView);

        // Gestion des événements pour démarrer et ouvrir la voiture peut être ajoutée ici
    }


    @Override
    public int getItemCount() {

        return vehiculeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //here we can initialize
        private ImageView carImageView;
        private TextView carName, carLocation, carOpen, carStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            carName=itemView.findViewById(R.id.txtcarName);
            carLocation=itemView.findViewById(R.id.txtcarLocation);
            carOpen=itemView.findViewById(R.id.txtcaropen);
            carStart=itemView.findViewById(R.id.txtcarStart);

        }
    }
}
