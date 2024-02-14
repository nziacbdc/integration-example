package com.movmintdigitalfiat.icepic.utils;

import static com.movmintdigitalfiat.icepic.utils.Constants.baseUrl;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movmintdigitalfiat.icepic.MainActivity;
import com.movmintdigitalfiat.icepic.SecondActivity;
import com.movmintdigitalfiat.icepic.ThirdActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebAppInterface {
    Context mContext;
    WebView icePicWebView;
    String icePicToken;

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();

    public WebAppInterface(Context c, WebView webView, String icePicToken) {
        mContext = c;
        icePicWebView = webView;
        this.icePicToken = icePicToken;
    }

    // This method signature and the annotation is required
    // Method name have to be the one provided by MovMint dev team `postMessage()`
    // This method invoked by the MovMint Web app with values for message argument
    // The parameter message is a JSON encoded String which send you the necessary information from WebView to your app
    @RequiresApi(api = Build.VERSION_CODES.O)
    @JavascriptInterface
    public void postMessage(String message) {

        // You can use the any preferred method to decode this message to a JSON object or any type
        // To demonstrate we are using Gson library to decode the message parameter to a Java Map
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> parsedMessageData = gson.fromJson(message, type);
        System.out.println("parsedMessageData " + parsedMessageData);

        // Refer the provided IcePic integration documentation to see what Key-value pairs are included in the JSON body
        // First one is `loaded` to verify if the web page is ready and expecting event from your mobile to the WebView
        if (Boolean.TRUE.equals(parsedMessageData.get("loaded"))) {

            // Invoke the Android WebView post() method by the WebView object/instance we created and pass to this custom interface class
            // This post() method expects a Runnable with the actions you want to perform within it.
            // We just have one action which is to send your icepicToken provided by MovMint dev team to Web app using window.postMessage({})
            icePicWebView.post(() -> {
                // Use the Android WebView available method `evaluateJavascript()` to run the JS code snippet
                // Send the `icepicToken` using the Web API window.postMessage()
                // Expected argument should be in this format: {"icepicToken": "<your token>", "apikeyid": "<your api key id>"}
                String jsonString = String.format("{\"icepicToken\": \"%s\", \"apikeyid\":\"%s\"}", icePicToken, Constants.icePicApiKey);
                String workerJs = String.format("window.postMessage(%s);", jsonString);
                icePicWebView.evaluateJavascript(workerJs, null);
            });
        } else if (Boolean.TRUE.equals(parsedMessageData.get("isBacked"))) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
            Toast.makeText(mContext, "[IcePicMsg] " + message, Toast.LENGTH_SHORT).show();
        } else if (parsedMessageData.get("icepicToken") != null) {
            // The next JSON property to check from the parsed/deserialized message is `data`
            // Once `data` property becomes available with data, can initiate the pair-cardless API call with available data
            pairCardLess(parsedMessageData);
        }
    }

    // From the passing `data` argument few properties are required to do the API calls. They are,
    // "data", "userId", and "icepicToken"
    // Follow the integration documentation and do the API call with required info as you wish
    // Following is a sample API request using OkHttPClient
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void pairCardLess(Map<String, Object> parsedMessageData) {
        new Thread(() -> {
            try {

                // Endpoint
                String url = baseUrl + "/devices/pair-cardless";

                // Get the device name
                String deviceName = Build.MODEL;

                // Prepare the Request body with received deserialized message "data"
                Map<String, String> keyValueMap = new HashMap<>();
                // Activation code is included in the deserialized/passed message in the format of "nzia:activate/<activation code>"
                // Replace the "nzia:activate/" from the data property / key and include in request body "activationCode" property
                keyValueMap.put("activationCode", parsedMessageData.get("activationCode").toString().replaceAll("nzia:activate/", ""));
                // Use your mobile app generated public key
                keyValueMap.put("publicKey", Constants.publicKey);
                keyValueMap.put("model", deviceName);

                RequestBody requestBody = RequestBody.Companion.create(gson.toJson(keyValueMap), Constants.JSON);

                // Generate a deviceId
                String generatedUuid = UUID.randomUUID().toString();

                // Include API request required headers
                // Include generated JWT from Sand dollar onboarding Auth Secret as the "Authorization"
                // Include Sand dollar onboarding API key as the "apikeyid"
                Request request = new Request.Builder()
                        .addHeader("Content-Type", String.valueOf(Constants.JSON))
                        .addHeader("Authorization", "Bearer " + KeyPairGeneratorUtils.createJwt(Constants.sandDollarSecret))
                        .addHeader("deviceId", generatedUuid)
                        .addHeader("apikeyid", Constants.sandDollarApiKey)
                        .url(url)
                        .post(requestBody)
                        .build();

                // Send the API request
                Response response = client.newCall(request).execute();

                // Check for the response status
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseBody = response.body().string();

                    Log.d("Response", responseBody);

                    // As the last step have to call the user/registerDevice API request
                    // This API request can perform here or any action your mobile app wanted
                    // As example navigating to a another screen and perform the user/registerDevice API request

                    Intent intent = new Intent(mContext, SecondActivity.class);
                    intent.putExtra("pairCardLessResponse", responseBody);
                    intent.putExtra("parsedMsgUserId", parsedMessageData.get("userId").toString());
                    intent.putExtra("parsedMsgIcePicToken", parsedMessageData.get("icepicToken").toString());

                    mContext.startActivity(intent);
                } else {
                    Log.e("Error", "Request failed with code: " + response.code());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
