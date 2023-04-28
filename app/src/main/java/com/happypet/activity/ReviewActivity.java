package com.happypet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.happypet.R;
import com.happypet.model.PetStore;
import com.happypet.model.Product;
import com.happypet.model.Review;

import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {
    private TextView mProductNameTV;
    private TextView mProductDescriptionTV;
    private TextView mProductSellerTV;
    private TextView mProductPriceTV;
    private RatingBar mRatingBar;
    private EditText mReviewCommentsET;
    private Button mLeaveReviewButton;
    private Button mShareOnSocialButton;
    private DatabaseReference reviewDBReference;
    private Product reviewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mProductNameTV = findViewById(R.id.review_name_tv);
        mProductDescriptionTV = findViewById(R.id.review_description_tv);
        mProductSellerTV = findViewById(R.id.review_seller_tv);
        mProductPriceTV = findViewById(R.id.review_price_tv);
        mRatingBar = findViewById(R.id.review_rating_bar);
        mReviewCommentsET = findViewById(R.id.review_comments_et);
        mLeaveReviewButton = findViewById(R.id.review_button);
        mShareOnSocialButton = findViewById(R.id.share_on_twitter_button);

        //Set all fields for the selected product
        reviewProduct = (Product) getIntent().getSerializableExtra("productToReview");
        mProductPriceTV.setText(reviewProduct.getName());
        mProductDescriptionTV.setText(reviewProduct.getDescription());
        mProductSellerTV.setText(reviewProduct.getStoreName());
        mProductPriceTV.setText(reviewProduct.getPrice().toString());

        reviewDBReference = FirebaseDatabase.getInstance().getReference("reviews");

        mLeaveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertReview(makeReview());
            }
        });
        mShareOnSocialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Review sharedReview = makeReview();
                insertReview(sharedReview);
                shareReview(sharedReview);

            }
        });


    }

    //handles insertion of new store information to Realtime DB
    private void insertReview(Review newReview){
        reviewDBReference.push().setValue(newReview);
        Toast.makeText(ReviewActivity.this, "Review has been added",Toast.LENGTH_SHORT).show();
    }
    private Review makeReview(){
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String productKey = reviewProduct.getKey();
        float rating = mRatingBar.getRating();
        String comments = mReviewCommentsET.getText().toString();
        Review newReview = new Review(userID,productKey,rating,comments);
        return newReview;
    }
    private void shareReview(Review sharedReview){
        String message = "I used the Happy Pet App to order this product! Here is my review: \n"+"Product: "+reviewProduct.getName()+"\nSeller: "+reviewProduct.getStoreName()+
               "\nPrice: "+String.valueOf(reviewProduct.getPrice()) +"\nRating: " + String.valueOf(sharedReview.getRating()) +"\nReview: "+
                sharedReview.getComments();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share to: "));
    }
}