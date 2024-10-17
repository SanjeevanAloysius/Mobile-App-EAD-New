package com.example.ecommerceapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        // Get the intent data
        String name = getIntent().getStringExtra("PRODUCT_NAME");
        String price = getIntent().getStringExtra("PRODUCT_PRICE");
        String imageBase64 = getIntent().getStringExtra("PRODUCT_IMAGE");

        // Set the data to views
        productName.setText(name);
        productPrice.setText(price);

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            // If the Base64 string starts with "data:image/jpeg", remove it
            if (imageBase64.startsWith("data:image")) {
                imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
            }

            // Decode Base64 string to byte array
            byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            productImage.setImageBitmap(decodedByte);
        }
        }
    }

