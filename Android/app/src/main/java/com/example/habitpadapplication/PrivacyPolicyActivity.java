package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webView;
    public String fileName = "privacy_policy.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Privacy Policy");
        setContentView(R.layout.activity_privacy_policy);

        webView = (WebView) findViewById(R.id.privacy_policy);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/"+ fileName);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(PrivacyPolicyActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}