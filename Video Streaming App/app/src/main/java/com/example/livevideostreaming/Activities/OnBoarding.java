package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livevideostreaming.Adapters.OnBoardingAdapter;
import com.example.livevideostreaming.R;

public class OnBoarding extends AppCompatActivity {

    RecyclerView recyclerView;
    OnBoardingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // onBoarding circle image by default
        ImageView circle1 = findViewById(R.id.circle_1);
        circle1.setColorFilter(getResources().getColor(R.color.white));

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new OnBoardingAdapter(this);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int position = linearLayoutManager.findFirstVisibleItemPosition();
                ImageView circle1 = findViewById(R.id.circle_1);
                ImageView circle2 = findViewById(R.id.circle_2);
                ImageView circle3 = findViewById(R.id.circle_3);
                ImageView circle4 = findViewById(R.id.circle_4);

                TextView heading = findViewById(R.id.heading);
                TextView desc = findViewById(R.id.desc);

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    // Dragging

                } else
                    // idle state
                    if(newState == RecyclerView.SCROLL_STATE_IDLE)
                    {

                        if(position==0)
                        {
                            circle1.setColorFilter(getResources().getColor(R.color.white));
                            circle2.setColorFilter(getResources().getColor(R.color.black));
                            circle3.setColorFilter(getResources().getColor(R.color.black));
                            circle4.setColorFilter(getResources().getColor(R.color.black));
                            heading.setText("Unlimited Entertainment,\nat low price");
                            desc.setText("All of Netlix starting at just ₹ 149");
                        } else
                        if(position==1)
                        {
                            circle1.setColorFilter(getResources().getColor(R.color.black));
                            circle2.setColorFilter(getResources().getColor(R.color.white));
                            circle3.setColorFilter(getResources().getColor(R.color.black));
                            circle4.setColorFilter(getResources().getColor(R.color.black));
                            heading.setText("Download and \nwatch offline");
                            desc.setText("Always have something to watch offline");
                        } else
                        if(position==2)
                        {
                            circle1.setColorFilter(getResources().getColor(R.color.black));
                            circle2.setColorFilter(getResources().getColor(R.color.black));
                            circle3.setColorFilter(getResources().getColor(R.color.white));
                            circle4.setColorFilter(getResources().getColor(R.color.black));
                            heading.setText("No Fuzzy \ncontracts");
                            desc.setText("Join today, cancel anytime");
                        } else
                        if(position == 3)
                        {
                            circle1.setColorFilter(getResources().getColor(R.color.black));
                            circle2.setColorFilter(getResources().getColor(R.color.black));
                            circle3.setColorFilter(getResources().getColor(R.color.black));
                            circle4.setColorFilter(getResources().getColor(R.color.white));
                            heading.setText("Watch \neverywhere");
                            desc.setText("Stream on your phone, tablet, \nlaptop, TV and more");
                        }
                    }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });



    }

    public void signIn(View view) {

        Intent intent = new Intent(OnBoarding.this, Login.class);
        startActivity(intent);

    }

    public void btnGetStart(View view) {

        Intent intent = new Intent(OnBoarding.this,StepOne.class);
        startActivity(intent);


    }
}