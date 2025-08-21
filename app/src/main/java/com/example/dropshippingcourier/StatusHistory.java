package com.example.dropshippingcourier;

public class StatusHistory {
    private String status;
    private String updateTime;

    public StatusHistory(String status, String updateTime) {
        this.status = status;
        this.updateTime = updateTime;
    }

    // Getters
    public String getStatus() { return status; }
    public String getUpdateTime() { return updateTime; }
}
