package com.happypet.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happypet.R;
import com.happypet.activity.MainActivity;
import com.happypet.model.PetStore;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ImageButton mBackButton;

    private PetStore selectedPetStore;

    public void setPetStoreCoordinates(LatLng petStoreCoordinates) {
        this.petStoreCoordinates = petStoreCoordinates;
    }

    public LatLng petStoreCoordinates;


    // [START maps_activity_on_create]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // [END maps_current_place_map_fragment]
        mBackButton = findViewById(R.id.maps_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*Gets selected pet store LatLng from intent extra passed from order activity and
         * puts a marker on the map there, puts camera on that marker*/
        setSelectedPetStore();
        String petStoreAddress = selectedPetStore.getAddress();
        setCoordinates(petStoreAddress);

    }


    private void setSelectedPetStore() {
        /*Gets the intent passed from MainActivity and the selected patent passed in the extra */
        Intent intent = getIntent();
        selectedPetStore = (PetStore) intent.getSerializableExtra("petStore");
        if(selectedPetStore != null){
            Toast.makeText(MapsActivity.this,"Pet Store received by MapsActivity",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MapsActivity.this,"MapsActivity failed to retrieve pet store",Toast.LENGTH_SHORT).show();
        }
    }

    private void setCoordinates(String address){
        Geocoder geocoder = new Geocoder(this);

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new android.os.Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    List<Address> addressList = geocoder.getFromLocationName(address, 1);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(addressList != null){
                                setPetStoreCoordinates(new LatLng(addressList.get(0).getLatitude(),addressList.get(0).getLongitude()));
                                mMap.addMarker(new MarkerOptions().position(petStoreCoordinates).title(selectedPetStore.getName()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(petStoreCoordinates, 10));
                            }else{
                                Toast.makeText(MapsActivity.this,"Failed to retrieve coordinates at that address",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });





    }

}