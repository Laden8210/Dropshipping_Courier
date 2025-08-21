// Updated Parcel.java
package com.example.dropshippingcourier;

import java.util.List;

public class Parcel {
    private String orderId;
    private String orderNumber;
    private String trackingNumber;
    private String totalAmount;
    private String orderDate;
    private Customer customer;
    private ShippingAddress shippingAddress;
    private Store store;
    private List<Product> products;
    private List<ShippingStatus> shippingStatuses;
    private List<StatusHistory> statusHistory;

    // Current status derived from latest status history
    private String currentStatus;
    private String estimatedDelivery;

    public Parcel(String orderId, String orderNumber, String trackingNumber,
                  String totalAmount, String orderDate, Customer customer,
                  ShippingAddress shippingAddress, Store store, List<Product> products,
                  List<ShippingStatus> shippingStatuses, List<StatusHistory> statusHistory) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.trackingNumber = trackingNumber;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.customer = customer;
        this.shippingAddress = shippingAddress;
        this.store = store;
        this.products = products;
        this.shippingStatuses = shippingStatuses;
        this.statusHistory = statusHistory;

        // Derive current status from latest status history
        if (statusHistory != null && !statusHistory.isEmpty()) {
            this.currentStatus = statusHistory.get(statusHistory.size() - 1).getStatus();
        }
    }

    // Getters for existing interface compatibility
    public String getTrackingNumber() { return trackingNumber; }
    public String getStatus() { return currentStatus != null ? currentStatus : "unknown"; }
    public String getScanTime() {
        return shippingStatuses != null && !shippingStatuses.isEmpty()
                ? shippingStatuses.get(0).getUpdateTime() : orderDate;
    }
    public String getStoreName() { return store != null ? store.getStoreName() : ""; }
    public String getStoreAddress() {
        return store != null ? store.getStoreName() + " - " + store.getStoreContact() : "";
    }
    public String getCustomerName() {
        return customer != null ? customer.getFirstName() + " " + customer.getLastName() : "";
    }
    public String getCustomerAddress() {
        if (shippingAddress != null) {
            return shippingAddress.getAddressLine() + ", " +
                    shippingAddress.getBarangay() + ", " +
                    shippingAddress.getCity() + ", " +
                    shippingAddress.getRegion() + " " +
                    shippingAddress.getPostalCode();
        }
        return "";
    }
    public String getEstimatedDelivery() {
        return estimatedDelivery != null ? estimatedDelivery : "TBD";
    }
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }
    public String getWeight() { return "N/A"; } // Weight not provided in API
    public List<ScanEvent> getScanEvents() {
        // Convert ShippingStatus to ScanEvent for compatibility
        List<ScanEvent> events = new java.util.ArrayList<>();
        if (shippingStatuses != null) {
            for (ShippingStatus status : shippingStatuses) {
                events.add(new ScanEvent(
                        status.getUpdateTime(),
                        status.getLocation(),
                        status.getRemarks()
                ));
            }
        }
        return events;
    }

    public void setStatus(String status) { this.currentStatus = status; }
    public void setEstimatedDelivery(String estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    // New getters for complete data access
    public String getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public String getTotalAmount() { return totalAmount; }
    public String getOrderDate() { return orderDate; }
    public Customer getCustomer() { return customer; }
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public Store getStore() { return store; }
    public List<Product> getProducts() { return products; }
    public List<ShippingStatus> getShippingStatuses() { return shippingStatuses; }
    public List<StatusHistory> getStatusHistory() { return statusHistory; }
}






