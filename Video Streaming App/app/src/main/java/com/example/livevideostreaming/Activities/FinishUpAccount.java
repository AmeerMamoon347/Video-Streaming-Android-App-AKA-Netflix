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

public class FinishUpAccount extends AppCompatActivity {

    String planName,planCost,planFormat;
    TextView toolBarSignIn, step1of3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_up_account);

        getSupportActionBar().hide();

        toolBarSignIn = findViewById(R.id.toolbarSignIn);
        step1of3 = findViewById(R.id.step1of3_Finish);

        planName = getIntent().getStringExtra("planName");
        planCost = getIntent().getStringExtra("planCost");
        planFormat = getIntent().getStringExtra("planFormat");

        toolBarSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishUpAccount.this, Login.class);
                startActivity(intent);
            }
        });


        SpannableString st = new SpannableString("Step 1 of 3");
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);

        st.setSpan(styleSpan1,5,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(styleSpan2,10,11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        step1of3.setText(st);

    }

    public void finishContinue(View view) {

        Intent intent = new Intent(FinishUpAccount.this, StepTwo.class);
        intent.putExtra("planName",planName);
        intent.putExtra("planCost",planCost);
        intent.putExtra("planFormat",planFormat);

        startActivity(intent);
    }
}