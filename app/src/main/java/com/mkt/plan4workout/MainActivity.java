package com.mkt.plan4workout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mkt.plan4workout.View.MyViewPager;
import com.mkt.plan4workout.View.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    MyViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    Button btnExercises;
    Button btnPlans;
    Button btnCalendar;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewPager = (MyViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(viewPagerAdapter);
        setViewPager(1);


        btnExercises = findViewById(R.id.btn_exercises);
        btnPlans = findViewById(R.id.btn_plans);
        btnCalendar = findViewById(R.id.btn_calendar);

        btnPlans.setOnClickListener(v -> {
            setViewPager(2);
        });
        btnExercises.setOnClickListener(v -> {
            setViewPager(0);
        });
        btnCalendar.setOnClickListener(v -> {
            setViewPager(1);
        });

    }

    public void setViewPager(int fragmentNumber){
        viewPager.setCurrentItem(fragmentNumber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
