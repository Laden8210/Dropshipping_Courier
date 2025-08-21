package com.example.dropshippingcourier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dropshippingcourier.api.PostCallback;
import com.example.dropshippingcourier.api.PostTask;
import com.example.dropshippingcourier.util.Messenger;
import com.example.dropshippingcourier.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends AppCompatActivity implements PostCallback {


    private MaterialButton loginButton;

    private TextInputEditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (SessionManager.getInstance(this).getToken() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        this.usernameEditText = findViewById(R.id.usernameEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);


        this.loginButton = findViewById(R.id.loginButton);

        this.loginButton.setOnClickListener(this::loginAction);

    }

    private void loginAction(View view) {


        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();


        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", username);
            jsonObject.put("password", password);

            new PostTask(this, this, "error message", "auth/login-courier.php").execute(jsonObject);

        }catch (Exception e) {
            Messenger.showAlertDialog(this, "Login Error", "Please check your username and password.", "Ok").show();
        }

    }


    @Override
    public void onPostSuccess(String responseData) {
        Gson gson = new Gson();
        Map<String, String> responseMap = gson.fromJson(responseData, Map.class);
        SessionManager.getInstance(this).setToken(responseMap.get("token"));
        SessionManager.getInstance(this).setVerified(false);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPostError(String errorMessage) {
        Messenger.showAlertDialog(this, "Login Error", errorMessage, "Ok").show();
    }
}