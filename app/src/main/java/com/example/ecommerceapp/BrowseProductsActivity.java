package com.example.ecommerceapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class BrowseProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_products);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        prepareProductData();

        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void prepareProductData() {
        productList.add(new Product("Product 1", "Rs 1250.00", R.drawable.ic_product_placeholder));
        productList.add(new Product("Product 2", "Rs 2260.00", R.drawable.ic_product_placeholder));
        productList.add(new Product("Product 3", "Rs 5250.00", R.drawable.ic_product_placeholder));
    }
}
