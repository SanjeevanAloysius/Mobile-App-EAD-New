package com.example.ecommerceapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListActivity extends AppCompatActivity {

    private static final String TAG = "ProductListActivity";
    private static final String SHARED_PREF_NAME = "user_prefs";
    private static final String TOKEN_KEY = "token";

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        recyclerView = findViewById(R.id.recyclerView);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList); // Pass the activity context
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);

        fetchProducts();
    }

    private void fetchProducts() {
        String url = "http://10.0.2.2:5296/api/Product/all-products";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = response.getJSONObject(i);
                                String productName = product.getString("name");
                                double price = product.getDouble("price");
                                String imageBase64 = product.getString("productPicture");

                                productList.add(new Product(productName, "Rs. " + price + "0", imageBase64));
                            }
                            productAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductListActivity.this, "Error parsing products", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e(TAG, "Error: HTTP Status Code " + error.networkResponse.statusCode);
                            Log.e(TAG, "Error response data: " + new String(error.networkResponse.data));
                        } else {
                            Log.e(TAG, "Volley error: " + error.getMessage());
                        }

                        if (error instanceof TimeoutError) {
                            Log.e(TAG, "Timeout error: Server took too long to respond");
                            Toast.makeText(ProductListActivity.this, "Request timed out", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Log.e(TAG, "Authentication error");
                            Toast.makeText(ProductListActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "Unexpected error: " + error.getMessage());
                            Toast.makeText(ProductListActivity.this, "An unexpected error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                String token = sharedPreferences.getString(TOKEN_KEY, "");
                Log.d(TAG, "Using token: " + token);
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}
