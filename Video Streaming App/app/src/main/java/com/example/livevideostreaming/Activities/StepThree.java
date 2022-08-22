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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.livevideostreaming.R;

public class StepThree extends AppCompatActivity {

    String planName,planCost,planFormat,email,password;
    TextView toolbarSignOut,step3of3;
    LinearLayout paymentLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_three);

        getSupportActionBar().hide();

        planName = getIntent().getStringExtra("planName");
        planCost = getIntent().getStringExtra("planCost");
        planFormat = getIntent().getStringExtra("planFormat");
        email = getIntent().getStringExtra("emailStepTwo");
        password = getIntent().getStringExtra("passwordStepTwo");

        toolbarSignOut = findViewById(R.id.toolbarSignOut);
        step3of3 = findViewById(R.id.step3of3);
        paymentLinearLayout = findViewById(R.id.paymentLinearLayout);

        toolbarSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThree.this, Login.class);
                startActivity(intent);
            }
        });

        paymentLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepThree.this, PaymentGateway.class);
                intent.putExtra("planName",planName);
                intent.putExtra("planCost",planCost);
                intent.putExtra("planFormat",planFormat);
                intent.putExtra("emailStepTwo",email);
                intent.putExtra("passwordStepTwo",password);

                startActivity(intent);
            }
        });

        SpannableString st = new SpannableString("Step 3 of 3");
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(styleSpan1,5,6 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(styleSpan2,10,11 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        step3of3.setText(st);

    }
}