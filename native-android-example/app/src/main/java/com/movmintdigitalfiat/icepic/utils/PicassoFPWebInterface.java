package com.movmintdigitalfiat.icepic.utils;

import android.webkit.JavascriptInterface;

public class PicassoFPWebInterface {
    String message;

    @JavascriptInterface
    public void postMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}