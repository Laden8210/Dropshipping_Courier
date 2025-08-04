package com.example.dropshippingcourier.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import com.example.dropshippingcourier.util.SessionManager;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class PostTask extends AsyncTask<JSONObject, String, String> {

    private PostCallback callback;
    private String errorMessage = "";
    private String apiRequest;
    private Context context;


    public PostTask(Context context, PostCallback callback, String errorMessage, String apiRequest) {
        this.context = context;
        this.callback = callback;
        this.apiRequest = apiRequest;
        this.errorMessage = errorMessage;
    }

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject postData = params[0];

        Log.d("post", postData.toString());

        String response = "";

        try {
            URL url = new URL(ApiAddress.url + apiRequest);
            Log.d("PostTask", "URL: " + url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (SessionManager.getInstance(context).getToken() != null){
                Log.d("Token", "Token: " + SessionManager.getInstance(context).getToken());
                urlConnection.setRequestProperty("Authorization", "Bearer " + SessionManager.getInstance(context).getToken());
            }else if(postData.has("token")){
                urlConnection.setRequestProperty("Authorization", "Bearer " + postData.getString("token"));
            }

            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(postData.toString());
            outputStream.flush();
            outputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response += line;
            }
            urlConnection.disconnect();
        } catch (Exception e) {
            Log.e("PostTask", "Error in doInBackground: " + e.getMessage());
            response = "error";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("PostTask", "Response: " + result);

        if (result.equals("server_error")) {
            new AlertDialog.Builder(context).setTitle("Server Error")
                    .setMessage("An error occurred during request!")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((Activity) context).finishAffinity();
                            System.exit(0);
                        }
                    }).show();
        }

        if (result != null && !result.equals("[]")) {
            Log.d("Result", result);

            if (result.contains("success")) {
                try {
                    JSONObject jsonError = new JSONObject(result);
                    if (jsonError.has("success")) {
                        String successValue = jsonError.getString("success");
                        callback.onPostSuccess(successValue);
                    } else if (jsonError.has("error")) {
                        String errorValue = jsonError.getString("error");
                        callback.onPostError(errorValue);
                    } else {

                        callback.onPostSuccess(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            else if (result.contains("error")) {
                try {
                    JSONObject jsonError = new JSONObject(result);
                    String errorValue = jsonError.getString("message");
                    callback.onPostError(errorValue);
                } catch (JSONException e) {
                    Log.d("PostTask", "Error: " + result);
                    e.printStackTrace();
                }
                return;
            }
            callback.onPostSuccess(result);
        } else {
            callback.onPostError(errorMessage);
            return;
        }
        callback.onPostSuccess(result);
    }
}
