package com.happypet.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.happypet.service.MapsActivity;
import com.happypet.R;
import com.happypet.activity.ProductsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetStoreAdapter extends RecyclerView.Adapter<PetStoreAdapter.PetStoreViewHolder> {

    private List<PetStore> mPetStores;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference storesDbRef;

    private Context mContext;
    public PetStoreAdapter(List<PetStore> PetStores) {
        mPetStores = PetStores;
    }

    @NonNull
    @Override
    public PetStoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.pet_store_item, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.pet_store_card_view);

        // Get user's uid and the DB reference for their cart
        String uid = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        storesDbRef = FirebaseDatabase.getInstance().getReference().child("Stores");

        cardView.setUseCompatPadding(true); // Optional: adds padding for pre-lollipop devices
        return new PetStoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetStoreViewHolder holder, int position) {
        PetStore petStore = mPetStores.get(position);
        holder.mViewProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passPetStoreToProductsActivity(petStore);
            }
        });
        holder.mShowOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passPetStoreToMapsActivity(petStore);
            }
        });
        holder.bind(petStore);
    }

    @Override
    public int getItemCount() {
        return mPetStores.size();
    }

    public void setPetStores(List<PetStore> petStores) {
        mPetStores = petStores;
        notifyDataSetChanged();
    }

    static class PetStoreViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;

        private Button mViewProductsButton;
        private Button mShowOnMapButton;

        public PetStoreViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.pet_store_name_text_view);
            mViewProductsButton = itemView.findViewById(R.id.pet_store_view_products_button);
            mShowOnMapButton= itemView.findViewById(R.id.pet_store_order_show_map);
        }

        public void bind(PetStore petStore) {
            mNameTextView.setText(petStore.getName());
            mViewProductsButton.setText("View Products");
            mShowOnMapButton.setText("Show On Map");
        }
    }

    public void passPetStoreToProductsActivity(PetStore petStore) {
        Intent intent = new Intent(mContext,ProductsActivity.class);
        intent.putExtra("petStore", petStore);
        mContext.startActivity(intent);

    }
    public void passPetStoreToMapsActivity(PetStore petStore){
        Intent intent = new Intent(mContext, MapsActivity.class);
        intent.putExtra("petStore", petStore);
        mContext.startActivity(intent);
    }

}
