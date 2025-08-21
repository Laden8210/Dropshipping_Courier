package com.example.dropshippingcourier;

public class Customer {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String avatarUrl;

    public Customer(String userId, String firstName, String lastName, String email, String phone, String avatarUrl) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAvatarUrl() { return avatarUrl; }
}
