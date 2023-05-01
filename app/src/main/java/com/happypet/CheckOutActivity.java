package com.happypet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.happypet.activity.MainActivity;

import java.text.DecimalFormat;

public class CheckOutActivity extends AppCompatActivity {
    private TextView mTotalTV;
    private Button mCheckoutButton;

    /** CheckOutActivity gets total passed as intent extra from CartFragment and displays it to user*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        mTotalTV = findViewById(R.id.total_tv);
        mCheckoutButton = findViewById(R.id.checkout_button);
        DecimalFormat df = new DecimalFormat("0.00");

        mTotalTV.setText(String.valueOf(df.format(getIntent().getDoubleExtra("total",0.0))));
        mCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CheckOutActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}