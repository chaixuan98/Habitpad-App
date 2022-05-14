package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.habitpadapplication.Dialogs.TermConditionDialog;

public class TermConditionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);

        termDialog();

    }

        private void termDialog(){
        TermConditionDialog termConditionDialog = new TermConditionDialog(this);
        termConditionDialog.show();
    }
}