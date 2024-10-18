package com.example.ecommerceapp;

public class Product {
    private String id; // New id field
    private String name;
    private String price;
    private String imageBase64;

    // Constructor including the id field
    public Product(String id, String name, String price, String imageBase64) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageBase64 = imageBase64; // Initialize base64 image string
    }

    // Getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageBase64() {
        return imageBase64;
    }
}

