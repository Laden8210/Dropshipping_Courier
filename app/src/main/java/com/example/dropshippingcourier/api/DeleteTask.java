package com.example.dropshippingcourier.api;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeleteTask extends AsyncTask<String, Void, String> {

    private DeleteCallback callback;
    private String errorMessage = "";
    private String apiRequest;

    public DeleteTask(DeleteCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String postData = params[0];
        errorMessage = params[1];
        apiRequest = params[2];

        String response = "";

        try {
            URL url = new URL(ApiAddress.url + apiRequest);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(postData);
            outputStream.flush();
            outputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response += line;
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e("DeleteTask", "Error in doInBackground: " + e.getMessage());
            response = "Error: " + e.getMessage();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("DeleteTask", "Response: " + result);

        if (result != null && !result.equals("[]")) {
            Log.d("Result", result);

            if (result.contains("success")) {
                try {
                    JSONObject jsonError = new JSONObject(result);
                    String errorValue = jsonError.getString("success");
                    callback.onDeleteSuccess(errorValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            if (result.contains("error")) {
                try {
                    JSONObject jsonError = new JSONObject(result);
                    String errorValue = jsonError.getString("error");
                    callback.onDeleteFail(errorValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            callback.onDeleteSuccess(result);
        } else {
            callback.onDeleteFail(errorMessage);
        }
    }
}

