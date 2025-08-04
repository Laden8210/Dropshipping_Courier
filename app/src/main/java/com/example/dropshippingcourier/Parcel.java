package com.example.dropshippingcourier;

import java.util.List;

public class Parcel {
    private String trackingNumber;
    private String status;
    private String scanTime;
    private String storeName;
    private String storeAddress;
    private String customerName;
    private String customerAddress;
    private String estimatedDelivery;
    private int itemCount;
    private String weight;
    private List<ScanEvent> scanEvents;

    public Parcel(String trackingNumber, String status, String scanTime,
                  String storeName, String storeAddress, String customerName,
                  String customerAddress, String estimatedDelivery,
                  int itemCount, String weight, List<ScanEvent> scanEvents) {
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.scanTime = scanTime;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.estimatedDelivery = estimatedDelivery;
        this.itemCount = itemCount;
        this.weight = weight;
        this.scanEvents = scanEvents;
    }

    // Getters
    public String getTrackingNumber() { return trackingNumber; }
    public String getStatus() { return status; }
    public String getScanTime() { return scanTime; }
    public String getStoreName() { return storeName; }
    public String getStoreAddress() { return storeAddress; }
    public String getCustomerName() { return customerName; }
    public String getCustomerAddress() { return customerAddress; }
    public String getEstimatedDelivery() { return estimatedDelivery; }
    public int getItemCount() { return itemCount; }
    public String getWeight() { return weight; }
    public List<ScanEvent> getScanEvents() { return scanEvents; }
    public void setStatus(String status) { this.status = status; }
}