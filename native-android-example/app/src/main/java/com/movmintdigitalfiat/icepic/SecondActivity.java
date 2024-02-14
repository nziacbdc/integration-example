package com.movmintdigitalfiat.icepic;

import static com.movmintdigitalfiat.icepic.utils.Constants.baseUrlIce;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movmintdigitalfiat.icepic.utils.Constants;
import com.movmintdigitalfiat.icepic.utils.KeyPairGeneratorUtils;
import com.movmintdigitalfiat.icepic.utils.PicassoFPWebInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondActivity extends AppCompatActivity {

    TextView text;
    Button continueBt;

    WebView picassoFpWebView;

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String pairCardLessResponse = intent.getStringExtra("pairCardLessResponse");
        // From the passing `data` argument get "userId"
        String parsedMsgUserId = intent.getStringExtra("parsedMsgUserId");
        // From the passing `data` argument get "icePicToken"
        String parsedMsgIcePicToken = intent.getStringExtra("parsedMsgIcePicToken");

        // Parsed the pair-cardless API response to your own variable / model / or any
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> parsedMessageData = gson.fromJson(pairCardLessResponse, type);

        String decryptedDeviceId = KeyPairGeneratorUtils.decrypt(parsedMessageData.get("deviceId").toString());
        String authSecret = KeyPairGeneratorUtils.decrypt(parsedMessageData.get("authSecret").toString());
        String deviceNo = KeyPairGeneratorUtils.decrypt(parsedMessageData.get("deviceNo").toString());

        parsedMessageData.put("deviceId", decryptedDeviceId);
        parsedMessageData.put("deviceNo", deviceNo);
        parsedMessageData.put("authSecret", authSecret);

        JSONObject jsonObject = new JSONObject(parsedMessageData);

        text = findViewById(R.id.responseText);
        try {
            text.setText(jsonObject.toString(4));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        continueBt = findViewById(R.id.continueBt);
        picassoFpWebView = findViewById(R.id.picassoFpWebView);
        picassoFpWebView.loadUrl("file:///android_asset/picasso_fp.html");
        PicassoFPWebInterface picassoFPWebInterface = new PicassoFPWebInterface();
        picassoFpWebView.addJavascriptInterface(picassoFPWebInterface, "picassoFPWebView");
        picassoFpWebView.setVisibility(View.INVISIBLE);

        continueBt.setOnClickListener(v -> {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            String deviceDetails = String.valueOf(metrics.xdpi) + metrics.ydpi;
            String fingerprint = KeyPairGeneratorUtils.generateFingerPrint(picassoFPWebInterface.getMessage(), deviceDetails);
            // Call the registerDevice API request with the above prepared data
            registerDevice(decryptedDeviceId, parsedMsgUserId, parsedMsgIcePicToken, fingerprint);
        });
    }

    public void registerDevice(String decryptedDeviceId, String userId, String icePicToken, String fingerprint) {
        new Thread(() -> {
            try {

                // Use the integration document given API endpoint
                String url =  baseUrlIce + "/user/registerDevice";

                Map<String, String> keyValueMap = new HashMap<>();
                // Decrypt  from your private key and add the deviceId received from pair-cardless response
                keyValueMap.put("deviceId", decryptedDeviceId);
                // Use the your app's user fingerprint
                keyValueMap.put("fingerPrint", fingerprint);
                // Use the your app's user passcode
                keyValueMap.put("passcode", KeyPairGeneratorUtils.generatePasscode("123456"));
                // Use the integration document contained status
                keyValueMap.put("status", "ACTIVE");
                // Use the integration document contained type
                keyValueMap.put("type", "WHITE_LABEL");
                // Use the parsedMessage contained userId
                keyValueMap.put("userId", userId);

                RequestBody body = RequestBody.Companion.create(gson.toJson(keyValueMap), Constants.JSON);

                // Prepare the API request with headers
                // Use the parsedMessage contained icePicToken
                // Decrypt  from your private key and add the deviceId received from pair-cardless response
                Request request = new Request.Builder()
                        .addHeader("Content-Type", String.valueOf(Constants.JSON))
                        .addHeader("Authorization", "Bearer " + icePicToken)
                        .addHeader("deviceId", decryptedDeviceId)
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                Log.d("Response", response.message());

                if (response.isSuccessful()) {

                    // From here onward your app activity / functionality can performed with the received data
                    // As example: start an activity
                    Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);

                    Map<String, String> map = new HashMap<>();
                    map.put("message", response.message());
                    map.put("code", String.valueOf(response.code()));

                    intent.putExtra("data", gson.toJson(map));
                    startActivity(intent);

                } else {
                    Log.e("Error", "Request failed with code: " + response.code());
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}