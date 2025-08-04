package com.example.dropshippingcourier.api;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class GetTask extends AsyncTask<String, Void, String> {
    private GetCallback callback;
    private String errorMessage = "";
    private String apiRequest;

    public GetTask(GetCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        errorMessage = params[0];
        apiRequest = params[1];
        String response = "";
        try {
            URL url = new URL(ApiAddress.url+apiRequest);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response += line;
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e("GetTask", "Error in doInBackground: " + e.getMessage());
            response = "Error: " + e.getMessage();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("GetTask", "Response: " + result);

        if (result != null && !result.equals("[]")) {
            Log.d("Result", result);

            if (result.contains("success")) {
                try {
                    callback.onGetSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (result.contains("error")) {
                try {
                    callback.onGetError(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            callback.onGetSuccess(result);
        } else {
            callback.onGetError(errorMessage);
        }
    }
}
