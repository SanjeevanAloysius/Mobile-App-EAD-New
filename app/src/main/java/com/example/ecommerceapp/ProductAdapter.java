package com.example.ecommerceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());

        String imageBase64 = product.getImageBase64();

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            try {
                // If the Base64 string starts with "data:image", remove it
                if (imageBase64.startsWith("data:image")) {
                    imageBase64 = imageBase64.substring(imageBase64.indexOf(",") + 1);
                }

                // Decode Base64 string to byte array
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);

                // Convert byte array to Bitmap
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                // Set the Bitmap to ImageView if it's not null
                if (decodedByte != null) {
                    holder.productImage.setImageBitmap(decodedByte);
                } else {
                    Log.e("ProductAdapter", "Decoded image is null");
                }
            } catch (IllegalArgumentException e) {
                Log.e("ProductAdapter", "Base64 decoding error: " + e.getMessage());
            }
        } else {
            Log.e("ProductAdapter", "Image Base64 string is empty or null");
        }

        // Declare a final variable for the final imageBase64 value
        final String finalImageBase64 = imageBase64;

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_NAME", product.getName());
            intent.putExtra("PRODUCT_PRICE", product.getPrice());
            intent.putExtra("PRODUCT_IMAGE", finalImageBase64);  // Use the final variable here
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productPrice;
        public ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
