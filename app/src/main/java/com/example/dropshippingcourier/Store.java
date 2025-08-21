package com.example.dropshippingcourier;

public class Store {
    private String storeId;
    private String storeName;
    private String storeLogoUrl;
    private String storeContact;

    public Store(String storeId, String storeName, String storeLogoUrl, String storeContact) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
        this.storeContact = storeContact;
    }

    // Getters
    public String getStoreId() { return storeId; }
    public String getStoreName() { return storeName; }
    public String getStoreLogoUrl() { return storeLogoUrl; }
    public String getStoreContact() { return storeContact; }
}

