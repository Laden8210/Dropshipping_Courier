package com.example.dropshippingcourier;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dropshippingcourier.CaptureActivityPortrait;
import com.example.dropshippingcourier.api.PostCallback;
import com.example.dropshippingcourier.api.PostTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;





public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ParcelAdapter adapter;
    private List<Parcel> parcelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentStatus = "Pending";
    private String socNumber = "";
    private String address = "";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Log.d("bar code", result.getContents());
                    showStatusSelectionDialog(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        setupChipGroup();
//        setupRecyclerView();
//        setupSwipeRefresh();
            setupScanButton();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void setupChipGroup() {
        ChipGroup chipGroup = findViewById(R.id.chip_group);
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                Chip chip = group.findViewById(checkedIds.get(0));
                currentStatus = chip.getText().toString();
                filterParcelsByStatus(currentStatus);
            }
        });
    }

    private void filterParcelsByStatus(String status) {
        List<Parcel> filteredList = new ArrayList<>();
        for (Parcel parcel : parcelList) {
            if (parcel.getStatus().equalsIgnoreCase(status)) {
                filteredList.add(parcel);
            }
        }
        adapter.updateList(filteredList);
    }

    private void setupRecyclerView() {
//        recyclerView = findViewById(R.id.rv_scanned_items);
//
//        List<ScanEvent> scanEvents = new ArrayList<>();
//        scanEvents.add(new ScanEvent("2023-10-01 12:00", "Store A", "Your parcel is ready for pickup"));
//        scanEvents.add(new ScanEvent("2023-10-02 14:00", "Warehouse B", "Parcel is in transit"));
//
//        parcelList.add(new Parcel("123456789", "Pending", "2023-10-01 12:00",
//                "Store A", "123 Store St", "John Doe", "456 Customer Rd",
//                "2023-10-05", 2, "1.5kg", scanEvents));
//
//        parcelList.add(new Parcel("987654321", "In Transit", "2023-10-02 14:00",
//                "Store B", "789 Store Ave", "Jane Smith", "321 Customer Ln",
//                "2023-10-06", 1, "2.0kg", scanEvents));
//
//        parcelList.add(new Parcel("456789123", "Out for Delivery", "2023-10-03 16:00",
//                "Store C", "456 Store Blvd", "Alice Johnson", "654 Customer St",
//                "2023-10-07", 3, "0.5kg", scanEvents));
//
//        adapter = new ParcelAdapter(parcelList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void refreshData() {
        // In a real app, this would fetch new data from server
        Toast.makeText(this, "Data refreshed", Toast.LENGTH_SHORT).show();
    }

    private void setupScanButton() {
        FloatingActionButton fabScan = findViewById(R.id.fab_scan);
        fabScan.setOnClickListener(view -> launchScanner());
    }

    private void launchScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan parcel barcode");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureActivityPortrait.class);
        barcodeLauncher.launch(options);
    }

    private void showStatusSelectionDialog(String barcode) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_status, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupStatus);
        Button btnConfirm = view.findViewById(R.id.btnConfirmStatus);

        btnConfirm.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton radioButton = view.findViewById(selectedId);
                String status = radioButton.getText().toString();
                handleScannedBarcode(barcode, status);
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Please select a status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleScannedBarcode(String barcode, String status) {
        if (checkLocationPermission()) {
            getCurrentLocation(barcode, status);
        } else {
            requestLocationPermission();
        }
    }


    private void getCurrentLocation(String barcode, String status) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Log.d("LOCATION", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                        createScanJson(barcode, status, location);
                    } else {
                        Log.d("LOCATION", "No last known location found, requesting new location");
                        requestNewLocation(barcode, status);
                    }
                });
    }


    private void requestNewLocation(String barcode, String status) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
                new com.google.android.gms.location.LocationCallback() {
                    @Override
                    public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                        if (locationResult == null) return;
                        for (Location location : locationResult.getLocations()) {
                            createScanJson(barcode, status, location);
                            fusedLocationClient.removeLocationUpdates(this);
                            break;
                        }
                    }
                }, null);
    }

    private void createScanJson(String barcode, String status, Location location) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tracking_number", barcode);
            jsonObject.put("status", status);
            jsonObject.put("scan_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));


            jsonObject.put("latitude", location.getLatitude());
            jsonObject.put("longitude", location.getLongitude());

            Log.d("SCAN_DATA", jsonObject.toString());

            new PostTask(this, new PostCallback() {
                @Override
                public void onPostSuccess(String responseData) {
                    Log.d("MainActivity", "Post success: " + responseData);

                    try {
                        JSONObject responseJson = new JSONObject(responseData);
                        String message = responseJson.optString("message", "Scan updated successfully");

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Success")
                                .setMessage(message)
                                .setPositiveButton("OK", null)
                                .show();

                    } catch (JSONException e) {
                        Log.e("MainActivity", "Invalid JSON response", e);
                        Toast.makeText(MainActivity.this, "Scan updated successfully", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onPostError(String errorMessage) {
                    Log.e("POST ERROR", "Post error: " + errorMessage);
                    Toast.makeText(MainActivity.this, "Failed to update scan", Toast.LENGTH_SHORT).show();
                }
            }, "error message", "courier/save_shippping_status.php").execute(jsonObject);

        } catch (Exception e) {
            Log.e("JSON ERROR", "Error creating JSON", e);
        }

//        adapter.notifyDataSetChanged();
        //filterParcelsByStatus(currentStatus);
    }
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }






    private void showSocAddressDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_soc_address, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        EditText etSocNumber = view.findViewById(R.id.etSocNumber);
        EditText etAddress = view.findViewById(R.id.etAddress);
        Button btnSave = view.findViewById(R.id.btnSave);

        etSocNumber.setText(socNumber);
        etAddress.setText(address);

        btnSave.setOnClickListener(v -> {
            socNumber = etSocNumber.getText().toString().trim();
            address = etAddress.getText().toString().trim();

            if (!socNumber.isEmpty() && !address.isEmpty()) {
                Toast.makeText(MainActivity.this, "Details saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        // In a real app, this would clear session and go to login
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            showSocAddressDialog();
            return true;
        } else if (id == R.id.action_logout) {
            logout();
            return true;
        } else if (id == R.id.action_clear) {
            parcelList.clear();
            adapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}