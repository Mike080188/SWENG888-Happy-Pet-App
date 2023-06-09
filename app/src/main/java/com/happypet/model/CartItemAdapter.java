package com.happypet.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartItem> mCartItems;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference cartDbRef;
    public CartItemAdapter(List<CartItem> cartItems) {
        mCartItems = cartItems;
    }

    private Context mContext;

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.cart_card_view);

        // Get DB ref for the user's cart
        String uid = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        cartDbRef = FirebaseDatabase.getInstance().getReference().child("users").
                child(uid).child("cart");

        cardView.setUseCompatPadding(true); // Optional: adds padding for pre-lollipop devices
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = mCartItems.get(position);
        holder.mRemoveFromCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveItemFromCart(item);
            }
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mCartItems.size();
    }

    public void setCartItems(List<CartItem> cartItems) {
        mCartItems = cartItems;
        notifyDataSetChanged();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mStoreTextView;
        private TextView mDescriptionDateTextView;
        private TextView mPriceTextView;
        private TextView mQuantityTextView;

        private ImageView mItemImageView;
        private Button mRemoveFromCartButton;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.cart_name_text_view);
            mStoreTextView = itemView.findViewById(R.id.cart_store_name_text_view);
            mDescriptionDateTextView = itemView.findViewById(R.id.cart_description_text_view);
            mPriceTextView = itemView.findViewById(R.id.cart_price_text_view);
            mQuantityTextView = itemView.findViewById(R.id.cart_quantity_text_view);
            mItemImageView = itemView.findViewById(R.id.cart_item_image);
            mRemoveFromCartButton = itemView.findViewById(R.id.remove_from_cart_button);
        }

        public void bind(CartItem item) {
            mNameTextView.setText(item.getName());
            mStoreTextView.setText("Store: "+item.getStoreName());
            mDescriptionDateTextView.setText("Description: "+item.getDescription());
            mPriceTextView.setText("Price: "+item.getPrice().toString());
            setImageBitmap(item.getImageUri(), mItemImageView);
            mQuantityTextView.setText("Quantity: "+item.getQuantity());
            mRemoveFromCartButton.setText("Remove Item");
        }
    }

    public void RemoveItemFromCart(CartItem cartItem) {
        // Remove item from user's cart. If there is more than 1 of this item, then just decrement quantity.

        String productKey = cartItem.getKey();
        Log.d("firebase", "prod key: " + productKey);
        DatabaseReference prodCartDbRef = cartDbRef.child(productKey);

        prodCartDbRef.child("quantity").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    return;
                }

                int quantity = task.getResult().getValue(Integer.class);

                //Remove item completely if only 1 in cart
                if (quantity == 1) {
                    prodCartDbRef.removeValue();
                }
                else {
                    // Decrement quantity
                    CartItem item = new CartItem(quantity - 1, cartItem.getName(),
                            cartItem.getDescription(), cartItem.getPrice(), cartItem.getStoreName(),
                            cartItem.getStoreAddress(), cartItem.getImageUri());
                    prodCartDbRef.setValue(item);
                }
                Toast.makeText(mContext, "Item Removed from Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void setImageBitmap(String imageURL, ImageView imageView){
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new android.os.Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream inputStream = new URL(imageURL).openStream();
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(image);
                        }
                    });
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }
}

