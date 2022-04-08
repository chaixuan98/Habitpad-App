package com.example.habitpadapplication.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.habitpadapplication.R;

public class WorkoutReminderActivity extends PreferenceActivity {
    static Toolbar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
        bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.workout_reminder_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.workout_reminder_fragment, root, false);
        root.addView(frameLayout,1);
        getFragmentManager().beginTransaction().replace(R.id.workout_content_frame, new WorkoutFragmentPrefs()).commit();
    }
}