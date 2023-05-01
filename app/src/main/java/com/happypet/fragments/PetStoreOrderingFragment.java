package com.happypet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.happypet.R;
import com.happypet.activity.MainActivity;
import com.happypet.model.PetStore;
import com.happypet.model.PetStoreAdapter;
import com.happypet.model.Product;
import com.happypet.model.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PetStoreOrderingFragment extends Fragment {
    private RecyclerView mRecyclerView;

    private DatabaseReference firebasePetStores;

    private PetStoreAdapter mPetStoreAdapter;
    private Spinner mSpinner;
    private SearchView mSearchView;
    private List<PetStore> petStoreList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("PetStoreOrderingFragment", "onCreate()");
        /** Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_pet_store_ordering, container, false);
        /** Instantiate the RecyclerView */
        mRecyclerView = view.findViewById(R.id.pet_store_order_recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /** Instantiate the Search and Spinner for searching/filtering pet stores by location */
        mSearchView = view.findViewById(R.id.pet_store_order_search);
        mSpinner = view.findViewById(R.id.pet_store_order_spinner);
        /* Set adapter for spinner */
       ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),R.array.cities_array, android.R.layout.simple_spinner_item);
       spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       mSpinner.setAdapter(spinnerAdapter);




        /** Implement the Call to FirebaseProductDAO - retrieves and materializes all pet stores,
         * sets adapter for recycler view to all pet stores */

        firebasePetStores = FirebaseDatabase.getInstance().getReference("stores");

        firebasePetStores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PetStore petStore = dataSnapshot.getValue(PetStore.class);
                    // Set key of product so it can be easily edited/deleted later
                    petStore.setKey(dataSnapshot.getKey());
                    petStoreList.add(petStore);
                }

                mPetStoreAdapter = new PetStoreAdapter(petStoreList);
                mRecyclerView.setAdapter(mPetStoreAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PetStoreOrderingFragment", "Error retrieving products from database", error.toException());
            }
        });
        /** Set OnItemSelectedListener for spinner for user to filter pet stores by city*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String city = mSpinner.getSelectedItem().toString();
                List<PetStore> petStoresInCityList = new ArrayList<>();
                for(PetStore petStore: petStoreList){
                    if(petStore.getAddress().contains(city)){
                        petStoresInCityList.add(petStore);
                    }
                }
                if(petStoresInCityList.size()>0) {
                    mPetStoreAdapter = new PetStoreAdapter(petStoresInCityList);
                    mRecyclerView.setAdapter(mPetStoreAdapter);
                }else{
                    mPetStoreAdapter = new PetStoreAdapter(petStoreList);
                    mRecyclerView.setAdapter(mPetStoreAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mPetStoreAdapter = new PetStoreAdapter(petStoreList);
                mRecyclerView.setAdapter(mPetStoreAdapter);
            }
        });
        /** Sets listener for user submitting query to filter pet stores by name*/
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                List<PetStore> petStoresMatchingQuery = new ArrayList<>();
                for(PetStore petStore: petStoreList){
                    if(petStore.getName().toLowerCase().contains(query.toLowerCase())){
                        petStoresMatchingQuery.add(petStore);
                    } else {
                        // Search query not found in List View
                        Toast.makeText(getContext(), "Not found", Toast.LENGTH_LONG).show();
                    }
                    if(petStoresMatchingQuery.size()>0){
                        mPetStoreAdapter = new PetStoreAdapter(petStoresMatchingQuery);
                        mRecyclerView.setAdapter(mPetStoreAdapter);
                    }else{
                        mPetStoreAdapter = new PetStoreAdapter(petStoreList);
                        mRecyclerView.setAdapter(mPetStoreAdapter);
                    }
                }
                return false;
            }
            /** sets up searchbar to filter pet stores by name with each new character the user
             * types*/
            @Override
            public boolean onQueryTextChange(String newText) {
                List<PetStore> petStoresMatchingQuery = new ArrayList<>();
                for (PetStore petStore : petStoreList) {
                    if (petStore.getName().toLowerCase().contains(newText.toLowerCase())) {
                        petStoresMatchingQuery.add(petStore);
                    }
                }
                if (!newText.isEmpty()) {
                    mPetStoreAdapter = new PetStoreAdapter(petStoresMatchingQuery);
                    mRecyclerView.setAdapter(mPetStoreAdapter);
                }else{
                    mPetStoreAdapter = new PetStoreAdapter(petStoreList);
                    mRecyclerView.setAdapter(mPetStoreAdapter);
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}

