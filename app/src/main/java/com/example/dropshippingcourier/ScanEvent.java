package com.example.dropshippingcourier;

public class ScanEvent {
    private String time;
    private String location;
    private String description;

    public ScanEvent(String time, String location, String description) {
        this.time = time;
        this.location = location;
        this.description = description;
    }

    // Getters
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
}