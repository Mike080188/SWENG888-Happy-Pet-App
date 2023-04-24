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

import com.happypet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.happypet.activity.ReviewActivity;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private List<Product> mProducts;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference cartDbRef;

    private Context mContext;
    public ProductsAdapter(List<Product> products) {
        mProducts = products;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(mContext).inflate(R.layout.product_item, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.product_card_view);

        // Get user's uid and the DB reference for their cart
        String uid = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        cartDbRef = FirebaseDatabase.getInstance().getReference().child("users").
                child(uid).child("cart");

        cardView.setUseCompatPadding(true); // Optional: adds padding for pre-lollipop devices
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mProducts.get(position);
        holder.mAddToCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProductToCart(product);
            }
        });
        holder.mLeaveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {goToLeaveReviewActivity(product);}
        });
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public void setProducts(List<Product> products) {
        mProducts = products;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mStoreTextView;
        private TextView mDescriptionDateTextView;
        private TextView mPriceTextView;
        private Button mAddToCartTextView;
        private Button mLeaveReviewButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mStoreTextView = itemView.findViewById(R.id.store_name_text_view);
            mDescriptionDateTextView = itemView.findViewById(R.id.description_text_view);
            mPriceTextView = itemView.findViewById(R.id.price_text_view);
            mAddToCartTextView = itemView.findViewById(R.id.add_to_cart_text_view);
            mLeaveReviewButton = itemView.findViewById(R.id.leave_a_review_text_view);
        }

        public void bind(Product product) {
            mNameTextView.setText(product.getName());
            mStoreTextView.setText("Store: " + product.getStoreName());
            mDescriptionDateTextView.setText("Description: " + product.getDescription());
            mPriceTextView.setText("Price: "+ product.getPrice().toString());
            mAddToCartTextView.setText("Add to Cart");
            mLeaveReviewButton.setText("Leave A Review");
        }
    }

    public void AddProductToCart(Product product) {
//    Add A product to the user's cart. If this product is already in the cart, increment the quantity
        String prodKey = product.getKey();
        Log.d("firebase", "product key: " + prodKey);
        DatabaseReference prodCartDbRef = cartDbRef.child(prodKey);

        prodCartDbRef.child("quantity").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    if(task.getResult().exists()) {
                        // Item is in cart, increment quantity.
                        int quantity = task.getResult().getValue(Integer.class);
                        CartItem item = new CartItem(quantity + 1, product.getName(),
                                product.getDescription(), product.getPrice(), product.getStoreName(),
                                product.getStoreAddress());
                        prodCartDbRef.setValue(item);
                    }
                    else {
                        // Add new item to cart with quantity = 1
                        int quantity = 1;
                        CartItem item = new CartItem(quantity, product.getName(),
                                product.getDescription(), product.getPrice(), product.getStoreName(),
                                product.getStoreAddress());
                        prodCartDbRef.setValue(item);
                    }
                    Toast.makeText(mContext, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void goToLeaveReviewActivity(Product product){
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra("productToReview",product);
        mContext.startActivity(intent);
    }

}

