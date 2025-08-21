package com.example.dropshippingcourier;

public class ShippingAddress {
    private String addressLine;
    private String region;
    private String city;
    private String barangay;
    private String postalCode;

    public ShippingAddress(String addressLine, String region, String city, String barangay, String postalCode) {
        this.addressLine = addressLine;
        this.region = region;
        this.city = city;
        this.barangay = barangay;
        this.postalCode = postalCode;
    }

    // Getters
    public String getAddressLine() { return addressLine; }
    public String getRegion() { return region; }
    public String getCity() { return city; }
    public String getBarangay() { return barangay; }
    public String getPostalCode() { return postalCode; }
}