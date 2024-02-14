package com.movmintdigitalfiat.icepic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.Nullable;

import com.movmintdigitalfiat.icepic.utils.Constants;
import com.movmintdigitalfiat.icepic.utils.WebAppInterface;

@SuppressLint("SetJavaScriptEnabled")
public class IcePicWebViewActivity extends Activity {
    WebView icePicWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icepic_webview);

        Intent intent = getIntent();
        String icePicToken = intent.getStringExtra("icePicToken");

        // If you have the view as a xml layout file link the view to WebView object
        icePicWebView = findViewById(R.id.icePicWebView);

        // To prevent previous data not to be cache
        icePicWebView.clearCache(true);
        icePicWebView.clearFormData();
        icePicWebView.clearHistory();

        // We need to setup web view with JavaScript, for that retrieve web view setting
        WebSettings webSettings = icePicWebView.getSettings();

        // Enable JS in Android WebView
        webSettings.setJavaScriptEnabled(true);

        // Load the provided gen-otp link with the agentId to the WebView
        icePicWebView.loadUrl(Constants.webViewUrl + "/#/3rd-party/self-onboarding");

        // Now there is some Javascript to execute within the initialized WebView
        // To do that we can create a custom interface class to keep the code clean and initialize custom class object to addJavascriptInterface()
        // addJavascriptInterface() expects two arguments, 1st is the custom interface class created and the interface name.
        // This interface name have to be "flutter_inappwebview" which is provided by MovMint dev team
        icePicWebView.addJavascriptInterface(new WebAppInterface(this, icePicWebView, icePicToken), "inappwebview");

    }
}
