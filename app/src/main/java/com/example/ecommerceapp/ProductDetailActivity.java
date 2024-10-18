package com.example.ecommerceapp;



import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView productName;
    private ImageView productImage;
    private TextView productPrice;
    private TextView totalPriceText;
    private TextView quantityText;
    private Button addToCartButton, plusButton, minusButton;

    private double productPriceValue;  // Store product price as a double for calculations
    private int quantity = 1;  // Default quantity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get views
        productName = findViewById(R.id.productNameDetail);
        productPrice = findViewById(R.id.productPriceDetail);
        totalPriceText = findViewById(R.id.totalPriceText);
        quantityText = findViewById(R.id.quantityText);
        addToCartButton = findViewById(R.id.addToCartButton);
        plusButton = findViewById(R.id.plusButton);
        minusButton = findViewById(R.id.minusButton);
        productImage = findViewById(R.id.productImageDetail);

        // Retrieve data passed from ProductAdapter
        Intent intent = getIntent();
        String productId = intent.getStringExtra("PRODUCT_ID");
        String imageBase64 = intent.getStringExtra("PRODUCT_IMAGE");
        String productNameStr = intent.getStringExtra("PRODUCT_NAME");
        String productPriceStr = intent.getStringExtra("PRODUCT_PRICE").replace("Rs. ", "");  // Remove Rs. for parsing

        // Set product name and price
        productName.setText(productNameStr);
        productPrice.setText("Rs. " + productPriceStr);

        // Convert the price to a double for calculations
        productPriceValue = Double.parseDouble(productPriceStr);

        // Set initial total price
        updateTotalPrice();

        // Decode and set product image from Base64 string
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            Bitmap productBitmap = convertStringToBitmap(imageBase64);  // Convert Base64 string to Bitmap
            if (productBitmap != null) {
                productImage.setImageBitmap(productBitmap);  // Set the bitmap to ImageView
            }
        }

        // Handle quantity changes with buttons
        plusButton.setOnClickListener(v -> {
            quantity++;
            updateQuantityAndPrice();
        });

        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantityAndPrice();
            }
        });

        // Handle "Add to Cart" button click
        addToCartButton.setOnClickListener(v -> {
            addToCart(productId, quantity);
        });
    }

    // Method to update the quantity and total price
    private void updateQuantityAndPrice() {
        quantityText.setText(String.valueOf(quantity));
        updateTotalPrice();
    }

    // Method to update total price based on quantity
    private void updateTotalPrice() {
        double totalPrice = productPriceValue * quantity;
        totalPriceText.setText("Total Price: Rs. " + String.format("%.2f", totalPrice));
    }

    // Method to add product to cart and call the API using Volley
    private void addToCart(String productId, int quantity) {
        // Get userEmail from SharedPreferences or Intent
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userEmail = prefs.getString("userEmail", null);  // Replace with actual key if needed
        String token = prefs.getString("token", "");
        if (userEmail == null) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare the data to send in the request
        JSONObject cartData = new JSONObject();
        try {
            cartData.put("productId", productId);  // Add product ID
            cartData.put("quantity", quantity);    // Add quantity

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("cartData" , cartData.toString());

        // API endpoint URL
        String url = "http://10.0.2.2:5296/api/Cart/add/" + userEmail;

// Create a new JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                cartData,
                response -> {
                    // Handle success response
                    Toast.makeText(ProductDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Handle error response
                    Toast.makeText(ProductDetailActivity.this, "Error adding product to cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json"); // Set Content-Type header
                headers.put("Authorization", "Bearer " + token); // Set your token here
                return headers;
            }
        };

// Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    // Method to convert Base64 string to Bitmap
    private Bitmap convertStringToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;  // Return null if there's an error in decoding
        }
    }
}
