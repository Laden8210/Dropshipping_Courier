package com.example.dropshippingcourier;

public class ShippingStatus {
    private String statusId;
    private String remarks;
    private String location;
    private Coordinates coordinates;
    private String updateTime;

    public ShippingStatus(String statusId, String remarks, String location, Coordinates coordinates, String updateTime) {
        this.statusId = statusId;
        this.remarks = remarks;
        this.location = location;
        this.coordinates = coordinates;
        this.updateTime = updateTime;
    }

    // Getters
    public String getStatusId() { return statusId; }
    public String getRemarks() { return remarks; }
    public String getLocation() { return location; }
    public Coordinates getCoordinates() { return coordinates; }
    public String getUpdateTime() { return updateTime; }
}
