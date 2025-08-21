package com.example.dropshippingcourier.util;


import com.example.dropshippingcourier.Coordinates;
import com.example.dropshippingcourier.Customer;
import com.example.dropshippingcourier.Parcel;

import com.example.dropshippingcourier.Product;
import com.example.dropshippingcourier.ShippingAddress;
import com.example.dropshippingcourier.ShippingStatus;
import com.example.dropshippingcourier.StatusHistory;
import com.example.dropshippingcourier.Store;


import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class JsonParserHelper {

    public static List<Parcel> parseParcelResponse(String jsonResponse) {
        List<Parcel> parcels = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);

            if (root.has("data")) {
                JSONArray dataArray = root.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject orderObj = dataArray.getJSONObject(i);
                    Parcel parcel = parseParcel(orderObj);
                    if (parcel != null) {
                        parcels.add(parcel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parcels;
    }

    private static Parcel parseParcel(JSONObject orderObj) {
        try {
            String orderId = orderObj.optString("order_id", "");
            String orderNumber = orderObj.optString("order_number", "");
            String trackingNumber = orderObj.optString("tracking_number", "");
            String totalAmount = orderObj.optString("total_amount", "0.00");
            String orderDate = orderObj.optString("order_date", "");

            // Parse customer
            Customer customer = parseCustomer(orderObj.optJSONObject("customer"));

            // Parse shipping address
            ShippingAddress shippingAddress = parseShippingAddress(orderObj.optJSONObject("shipping_address"));

            // Parse store
            Store store = parseStore(orderObj.optJSONObject("store"));

            // Parse products
            List<Product> products = parseProducts(orderObj.optJSONArray("products"));

            // Parse shipping statuses (remove duplicates)
            List<ShippingStatus> shippingStatuses = parseShippingStatuses(orderObj.optJSONArray("shipping_statuses"));

            // Parse status history (remove duplicates)
            List<StatusHistory> statusHistory = parseStatusHistory(orderObj.optJSONArray("status_history"));

            return new Parcel(orderId, orderNumber, trackingNumber, totalAmount, orderDate,
                    customer, shippingAddress, store, products, shippingStatuses, statusHistory);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Customer parseCustomer(JSONObject customerObj) {
        if (customerObj == null) return null;

        return new Customer(
                customerObj.optString("user_id", ""),
                customerObj.optString("first_name", ""),
                customerObj.optString("last_name", ""),
                customerObj.optString("email", ""),
                customerObj.optString("phone", ""),
                customerObj.optString("avatar_url", "")
        );
    }

    private static ShippingAddress parseShippingAddress(JSONObject addressObj) {
        if (addressObj == null) return null;

        return new ShippingAddress(
                addressObj.optString("address_line", ""),
                addressObj.optString("region", ""),
                addressObj.optString("city", ""),
                addressObj.optString("barangay", ""),
                addressObj.optString("postal_code", "")
        );
    }

    private static Store parseStore(JSONObject storeObj) {
        if (storeObj == null) return null;

        return new Store(
                storeObj.optString("store_id", ""),
                storeObj.optString("store_name", ""),
                storeObj.optString("store_logo_url", ""),
                storeObj.optString("store_contact", "")
        );
    }

    private static List<Product> parseProducts(JSONArray productsArray) {
        List<Product> products = new ArrayList<>();
        if (productsArray == null) return products;

        for (int i = 0; i < productsArray.length(); i++) {
            try {
                JSONObject productObj = productsArray.getJSONObject(i);
                Product product = new Product(
                        productObj.optInt("product_id", 0),
                        productObj.optString("name", ""),
                        productObj.optString("sku", ""),
                        productObj.optInt("quantity", 0),
                        productObj.optString("price", "0.00"),
                        productObj.optString("primary_image", "")
                );
                products.add(product);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    private static List<ShippingStatus> parseShippingStatuses(JSONArray statusesArray) {
        List<ShippingStatus> statuses = new ArrayList<>();
        Set<String> uniqueStatuses = new HashSet<>(); // To remove duplicates

        if (statusesArray == null) return statuses;

        for (int i = 0; i < statusesArray.length(); i++) {
            try {
                JSONObject statusObj = statusesArray.getJSONObject(i);

                // Create unique key to identify duplicates
                String uniqueKey = statusObj.optString("status_id", "") + "_" +
                        statusObj.optString("update_time", "") + "_" +
                        statusObj.optString("remarks", "");

                if (!uniqueStatuses.contains(uniqueKey)) {
                    uniqueStatuses.add(uniqueKey);

                    Coordinates coordinates = null;
                    if (statusObj.has("coordinates")) {
                        JSONObject coordObj = statusObj.getJSONObject("coordinates");
                        coordinates = new Coordinates(
                                coordObj.optString("latitude", ""),
                                coordObj.optString("longitude", "")
                        );
                    }

                    ShippingStatus status = new ShippingStatus(
                            statusObj.optString("status_id", ""),
                            statusObj.optString("remarks", ""),
                            statusObj.optString("location", ""),
                            coordinates,
                            statusObj.optString("update_time", "")
                    );
                    statuses.add(status);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statuses;
    }

    private static List<StatusHistory> parseStatusHistory(JSONArray historyArray) {
        List<StatusHistory> history = new ArrayList<>();
        Set<String> uniqueHistory = new HashSet<>(); // To remove duplicates

        if (historyArray == null) return history;

        for (int i = 0; i < historyArray.length(); i++) {
            try {
                JSONObject historyObj = historyArray.getJSONObject(i);

                // Create unique key to identify duplicates
                String uniqueKey = historyObj.optString("status", "") + "_" +
                        historyObj.optString("update_time", "");

                if (!uniqueHistory.contains(uniqueKey)) {
                    uniqueHistory.add(uniqueKey);

                    StatusHistory statusHistory = new StatusHistory(
                            historyObj.optString("status", ""),
                            historyObj.optString("update_time", "")
                    );
                    history.add(statusHistory);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return history;
    }
}