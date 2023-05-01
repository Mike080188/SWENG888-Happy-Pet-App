package com.happypet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happypet.R;
import com.happypet.fragments.CartFragment;
import com.happypet.model.PetStore;
import com.happypet.model.Product;
import com.happypet.model.ProductsAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    private final String TAG = "PRODUCTS ACTIVITY";
    private TextView mTextViewPetStoreName;
    private RecyclerView mRecyclerView;
    private FloatingActionButton cartButton;
    private DatabaseReference firebaseDatabase;
    private ProductsAdapter mProductsAdapter;
    private List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mTextViewPetStoreName = findViewById(R.id.text_view_pet_store);
        PetStore selectedPetStore = (PetStore)getIntent().getSerializableExtra("petStore");
        mTextViewPetStoreName.setText("Pet Store: "+selectedPetStore.getName());

        mRecyclerView = findViewById(R.id.product_recycler_view);
        cartButton = findViewById(R.id.go_to_cart_button);
        cartButton.show();


        /** Implement the Call to FirebaseProductDAO  - retrieves proudcuts from database and filters
         * by pet store*/

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("products");
        List<Product> petStoreProductList = new ArrayList<>();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    product.setKey(dataSnapshot.getKey());
                    productList.add(product);
                    if(product.getStoreName().equals(selectedPetStore.getName())){
                        petStoreProductList.add(product);
                    }
                    Log.d(TAG, "Product storeName same as selectedPetStore name? "
                            + (product.getStoreName().equals(selectedPetStore.getName())));
                    i++;
                }
                /**Set product adapter with only products from the selected pet store*/
                mProductsAdapter = new ProductsAdapter(petStoreProductList);
                mRecyclerView.setAdapter(mProductsAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductListActivity", "Error retrieving products from database", error.toException());
            }
        });
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this,MainActivity.class);
                intent.putExtra("cartFragmentFlag", true);
                startActivity(intent);

            }
        });
    }
}