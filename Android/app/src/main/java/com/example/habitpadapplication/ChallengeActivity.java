package com.example.habitpadapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.habitpadapplication.Adapters.ChallengeViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ChallengeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Challenges");
        setContentView(R.layout.activity_challenge);

        userID = getIntent().getExtras().getString("intentUserID");

        tabLayout = findViewById(R.id.challenge_tab_layout);
        viewPager = findViewById(R.id.challenge_view_pager);

        tabLayout.setupWithViewPager(viewPager);

        ChallengeViewPagerAdapter challengeViewPagerAdapter = new ChallengeViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        challengeViewPagerAdapter.addFragment(new ChallengeFragment(), "Challenge");
//        challengeViewPagerAdapter.addFragment(new LeaderboardFragment(), "Leaderboard");
        viewPager.setAdapter(challengeViewPagerAdapter);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        Intent intent = new Intent(ChallengeActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}