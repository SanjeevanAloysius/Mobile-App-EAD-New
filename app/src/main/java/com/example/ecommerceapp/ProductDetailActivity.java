package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        addToCartButton = findViewById(R.id.addToCartButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("PRODUCT_NAME");
        String price = intent.getStringExtra("PRODUCT_PRICE");
        int imageResId = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.ic_product_placeholder);

        productName.setText(name);
        productPrice.setText(price);
        productImage.setImageResource(imageResId);
    }
}
