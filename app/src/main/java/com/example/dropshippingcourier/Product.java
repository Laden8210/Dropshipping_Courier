package com.example.dropshippingcourier;

public class Product {
    private int productId;
    private String name;
    private String sku;
    private int quantity;
    private String price;
    private String primaryImage;

    public Product(int productId, String name, String sku, int quantity, String price, String primaryImage) {
        this.productId = productId;
        this.name = name;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.primaryImage = primaryImage;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public int getQuantity() { return quantity; }
    public String getPrice() { return price; }
    public String getPrimaryImage() { return primaryImage; }
}