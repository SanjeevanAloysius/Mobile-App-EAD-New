package com.example.ecommerceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        TextView productNameTextView = findViewById(R.id.productName);
        TextView productPriceTextView = findViewById(R.id.productPrice);
        ImageView productImageView = findViewById(R.id.productImage);

        // Retrieve data from the intent
        String productName = getIntent().getStringExtra("PRODUCT_NAME");
        String productPrice = getIntent().getStringExtra("PRODUCT_PRICE");
        String productImageBase64 = getIntent().getStringExtra("PRODUCT_IMAGE");

        // Set data to views
        productNameTextView.setText(productName);
        productPriceTextView.setText("Rs. " + productPrice);

        // Decode and set the product image
        if (productImageBase64 != null) {
            byte[] decodedString = Base64.decode(productImageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            productImageView.setImageBitmap(decodedByte);
        }
    }
}
