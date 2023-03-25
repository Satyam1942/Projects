package com.example.majorproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class readingRoom extends AppCompatActivity {
WebView readingView;
String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_room);
        url =  getIntent().getStringExtra("url");
        readingView = findViewById(R.id.readingView);
        readingView.setWebViewClient(new Browser());
        readingView.getSettings().setJavaScriptEnabled(true);
        readingView.getSettings().setLoadsImagesAutomatically(true);
        readingView.getSettings().setLoadWithOverviewMode(true);
        readingView.getSettings().setUseWideViewPort(true);
        readingView.loadUrl(url);

    }
    private class Browser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}