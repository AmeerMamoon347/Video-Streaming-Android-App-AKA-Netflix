package com.example.livevideostreaming.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.livevideostreaming.R;

import org.w3c.dom.TypeInfo;

public class StepOne extends AppCompatActivity {

    TextView toolBarSignIn;
    TextView stepOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);

        getSupportActionBar().hide();
        toolBarSignIn = findViewById(R.id.toolbarSignIn);
        stepOne = findViewById(R.id.Step1of3);

        toolBarSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepOne.this, Login.class);
                startActivity(intent);
            }
        });

        // To highlight some part of text
        SpannableString st = new SpannableString("Step 1 of 3");
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);

        st.setSpan(styleSpan1,5,6,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(styleSpan2,10,11,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        stepOne.setText(st);
    }

    public void btnStepOne(View view) {

        Intent intent = new Intent(StepOne.this,ChooseYourPlan.class);
        startActivity(intent);

    }
}