package com.example.livevideostreaming.MainScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.livevideostreaming.R;

public class MovieDetails extends AppCompatActivity {

    int id;
    String movieName,imageUrl,fileUrl;
    ImageView movieImage;
    MotionButton playButton;
    TextView tv_movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();

        movieImage = findViewById(R.id.movieImage);
        tv_movieName = findViewById(R.id.movieName);
        playButton = findViewById(R.id.playButton);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        movieName = intent.getStringExtra("movieName");
        imageUrl =  intent.getStringExtra("imageUrl");
        fileUrl = intent.getStringExtra("fileUrl");

        Glide.with(MovieDetails.this).load(imageUrl).into(movieImage);
        tv_movieName.setText(movieName);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetails.this,VideoPlayer.class);
                intent.putExtra("videoFileUrl",fileUrl);
                startActivity(intent);
            }
        });

    }
}