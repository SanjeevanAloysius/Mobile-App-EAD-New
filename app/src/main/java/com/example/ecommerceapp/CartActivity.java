package com.example.ecommerceapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private LinearLayout cartItemsContainer;
    private String userEmail;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemsContainer = findViewById(R.id.cartItemsContainer);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail", null);
        authToken = sharedPreferences.getString("token", null);

        if (userEmail != null) {
            fetchCartItems(userEmail);
        } else {
            Toast.makeText(this, "User email is not set.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCartItems(String email) {
        String url = "http://10.0.2.2:5296/api/Cart/view/" + email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleCartResponse,
                error -> Toast.makeText(CartActivity.this, "Error fetching cart items: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void handleCartResponse(JSONObject response) {
        try {
            JSONArray cartItems = response.getJSONArray("items");
            Log.d("cartItems", cartItems.toString());

            for (int i = 0; i < cartItems.length(); i++) {
                JSONObject item = cartItems.getJSONObject(i);
                addCartItemView(item);
            }

        } catch (JSONException e) {
            Log.e("CartActivity", "JSON Parsing error: " + e.getMessage());
            Toast.makeText(this, "Error parsing cart items.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCartItemView(JSONObject item) throws JSONException {
        View itemView = LayoutInflater.from(this).inflate(R.layout.cart_item, cartItemsContainer, false);

        ImageView productImage = itemView.findViewById(R.id.cartProductImage);
        TextView productNameText = itemView.findViewById(R.id.productNameText);
        TextView productPriceText = itemView.findViewById(R.id.productPriceText);
        TextView productQuantityText = itemView.findViewById(R.id.productQuantityText);

        String productName = item.getString("productName");
        String productPrice = item.getString("price");
        int quantity = item.getInt("quantity");
        String productImageBase64 = item.getString("productPicture");

        productNameText.setText("Product: " + productName);
        productPriceText.setText("Price: " + productPrice);
        productQuantityText.setText("Quantity: " + quantity);

        if (productImageBase64.contains("base64,")) {
            productImageBase64 = productImageBase64.split("base64,")[1];
        }

        byte[] decodedString = Base64.decode(productImageBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null) {
            productImage.setImageBitmap(decodedByte);
        }

        cartItemsContainer.addView(itemView);
    }
}