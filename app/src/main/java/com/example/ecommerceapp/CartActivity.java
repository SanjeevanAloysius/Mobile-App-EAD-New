package com.example.ecommerceapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    private ImageView cartProductImage;
    private TextView cartTextView;
    private String userEmail; // Store the user's email
    private String authToken; // Store the authorization token

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Get the ImageView and TextView to display cart items
        cartProductImage = findViewById(R.id.cartProductImage);
        cartTextView = findViewById(R.id.cartTextView);

        // Retrieve user email and token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", null); // Adjust key accordingly
        authToken = sharedPreferences.getString("token", null); // Adjust key for token

        if (userEmail != null) {
            fetchCartItems(userEmail); // Call method to fetch cart items
        } else {
            cartTextView.setText("User email is not set.");
        }
    }

    private void fetchCartItems(String email) {
        String url = "http://10.0.2.2:5296/api/Cart/view/" + email;

        // Create a JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> handleCartResponse(response),
                error -> Toast.makeText(CartActivity.this, "Error fetching cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken); // Set the token
                headers.put("Content-Type", "application/json"); // Set content type
                return headers;
            }
        };

        // Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void handleCartResponse(JSONObject response) {
        try {
            // Assuming the response is an array of cart items
            JSONArray cartItems = response.getJSONArray("items");
            Log.d("cartItems", cartItems.toString());
            StringBuilder cartMessage = new StringBuilder();

            for (int i = 0; i < cartItems.length(); i++) {
                JSONObject item = cartItems.getJSONObject(i);
                String productName = item.getString("productName");
                String productPrice = item.getString("price");
                int quantity = item.getInt("quantity");
                String productImageBase64 = item.getString("productPicture"); // Adjust field name

                cartMessage.append("Product: ").append(productName)
                        .append("\nPrice: ").append(productPrice)
                        .append("\nQuantity: ").append(quantity).append("\n\n");

                // Log the Base64 image string to check if it is valid
                Log.d("Base64Image", productImageBase64);

                // Decode Base64 image and handle any "data:image/jpeg;base64," prefix
                if (productImageBase64.contains("base64,")) {
                    productImageBase64 = productImageBase64.split("base64,")[1];
                }

                // Decode and set product image
                byte[] decodedString = Base64.decode(productImageBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte != null) {
                    // Downscale the image to fit the ImageView properly (optional)
                    int width = cartProductImage.getWidth();
                    int height = (decodedByte.getHeight() * width) / decodedByte.getWidth();
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(decodedByte, width, height, false);
                    cartProductImage.setImageBitmap(scaledBitmap); // Set the image for the product
                }
            }

            cartTextView.setText(cartMessage.toString());

        } catch (JSONException e) {
            Log.e("CartActivity", "JSON Parsing error: " + e.getMessage());
            Toast.makeText(this, "Error parsing cart items.", Toast.LENGTH_SHORT).show();
        }
    }
}
