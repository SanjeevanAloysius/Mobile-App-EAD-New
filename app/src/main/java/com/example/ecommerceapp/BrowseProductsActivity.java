//package com.example.ecommerceapp;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BrowseProductsActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private ProductAdapter productAdapter;
//    private List<Product> productList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_browse_products);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        productList = new ArrayList<>();
//
//        // Initialize the product adapter with just the product list
//        productAdapter = new ProductAdapter(productList);
//
//        // Set the layout manager for the RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(productAdapter);
//
//        // Load products (you can replace this with actual data loading logic)
//        loadProducts();
//    }
//
//    private void loadProducts() {
//        // Example data - replace with your data source
//        productList.add(new Product(1, "Product 1", "Description of Product 1", 29.99, 10, "Category 1", "user@example.com", null));
//        productList.add(new Product(2, "Product 2", "Description of Product 2", 49.99, 5, "Category 2", "user@example.com", null));
//        productList.add(new Product(3, "Product 3", "Description of Product 3", 19.99, 15, "Category 3", "user@example.com", null));
//
//        // Notify the adapter about data changes
//        productAdapter.notifyDataSetChanged();
//    }
//}
