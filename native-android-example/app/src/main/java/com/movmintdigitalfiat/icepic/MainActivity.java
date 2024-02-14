package com.movmintdigitalfiat.icepic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movmintdigitalfiat.icepic.utils.Constants;
import com.movmintdigitalfiat.icepic.utils.KeyPairGeneratorUtils;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button icePicWebViewBt;
    ProgressBar mkLoader;

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icePicWebViewBt = findViewById(R.id.icePicWebViewBt);
        textView = findViewById(R.id.textView);
        mkLoader = findViewById(R.id.mkLoader);
        mkLoader.setVisibility(View.GONE);

        icePicWebViewBt.setOnClickListener(view -> {
            mkLoader.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            generateAuthenticationToken();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void generateAuthenticationToken() {
        new Thread(() -> {
            try {

                // Endpoint
                String url = Constants.baseUrlIce + "/authenticate/token";

                // Include API request required headers
                // Include generated JWT from Sand dollar onboarding Auth Secret as the "Authorization"
                // Include Sand dollar onboarding API key as the "apikeyid"
                Request request = new Request.Builder()
                        .addHeader("Content-Type", String.valueOf(Constants.JSON))
                        .addHeader("Authorization", "Bearer " + KeyPairGeneratorUtils.createJwt(Constants.icePicSecret))
                        .addHeader("apikeyid", Constants.icePicApiKey)
                        .url(url)
                        .get()
                        .build();

                // Send the API request
                Response response = client.newCall(request).execute();

                // Check for the response status
                if (response.isSuccessful()) {
                    this.runOnUiThread(() -> {
                        mkLoader.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    });
                    assert response.body() != null;
                    String responseBody = response.body().string();

                    Type type = new TypeToken<Map<String, Object>>(){}.getType();
                    Map<String, Object> tokenData = gson.fromJson(responseBody, type);

                    Log.d("Response", responseBody);

                    // As the last step have to call the user/registerDevice API request
                    // This API request can perform here or any action your mobile app wanted
                    // As example navigating to a another screen and perform the user/registerDevice API request

                    if (tokenData != null && tokenData.get("token") != null) {
                        this.runOnUiThread(() -> textView.setText(tokenData.get("token").toString()));
                        Intent intent = new Intent(MainActivity.this, IcePicWebViewActivity.class);
                        intent.putExtra("icePicToken", tokenData.get("token").toString());
                        startActivity(intent);
                    } else {
                        Log.e("Error", "icePic token is null");
                    }
                } else {
                    Log.e("Error", "Request failed with code: " + response.code());
                }
            } catch (Exception e) {
                this.runOnUiThread(() -> {
                    mkLoader.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                });
                throw new RuntimeException(e);
            }
        }).start();
    }
}