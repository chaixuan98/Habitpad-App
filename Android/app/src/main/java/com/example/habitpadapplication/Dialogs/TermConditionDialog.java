package com.example.habitpadapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.habitpadapplication.DoctorMainActivity;
import com.example.habitpadapplication.R;
import com.example.habitpadapplication.TermConditionActivity;
import com.google.android.material.button.MaterialButton;

public class TermConditionDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private MaterialButton agreeBtn, declineBtn;
    private WebView webView;
    public String fileName = "term_condition.html";

    public TermConditionDialog(Context context) {
        super(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_term_dialog_layout);

        webView = (WebView) findViewById(R.id.term_condition);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/"+ fileName);


        agreeBtn  = findViewById(R.id.agree_btn);
        declineBtn  = findViewById(R.id.decline_btn);

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        agreeBtn.setOnClickListener(this);
        declineBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.agree_btn:
                Intent intent = new Intent(context, DoctorMainActivity.class);
                context.startActivity(intent);
                break;
            case R.id.decline_btn:
                dismiss();
                break;
        }
    }
}
