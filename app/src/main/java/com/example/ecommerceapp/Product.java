package com.example.ecommerceapp;

public class Product {
    private String name;
    private String price;
    private String imageBase64; // Change from int to String for base64

    public Product(String name, String price, String imageBase64) {
        this.name = name;
        this.price = price;
        this.imageBase64 = imageBase64; // Initialize base64 image string
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageBase64() {
        return imageBase64; // Getter for base64 image
    }
}
