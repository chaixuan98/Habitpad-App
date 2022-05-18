package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.habitpadapplication.Adapters.MissionViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MissionRewardActivity extends AppCompatActivity {

    private String intentUserID;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Missions & Rewards");
        setContentView(R.layout.activity_mission_reward);

        intentUserID = getIntent().getExtras().getString("intentUserID");

        tabLayout = findViewById(R.id.mission_tab_layout);
        viewPager = findViewById(R.id.mission_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        MissionViewPagerAdapter missionViewPagerAdapter = new MissionViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        missionViewPagerAdapter.addFragment(new MissionFragment(), "Missions");
        missionViewPagerAdapter.addFragment(new RedeemRewardFragment(), "Redeem");
        missionViewPagerAdapter.addFragment(new MyRewardFragment(), "My Rewards");
        viewPager.setAdapter(missionViewPagerAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(MissionRewardActivity.this, HomeActivity.class);
        //intent.putExtra("intentUserID", intentUserID);
        startActivity(intent);
        finish();
    }
}