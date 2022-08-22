package com.example.livevideostreaming.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.livevideostreaming.R;

public class ChooseYourPlan extends AppCompatActivity {

    TextView toolBarSignIn;
    RadioButton btnBasic, btnStandard, btnPremium;
    String planName, planCost, planFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_your_plan);

        getSupportActionBar().hide();

        toolBarSignIn = findViewById(R.id.toolbarSignIn);
        btnBasic = findViewById(R.id.btnBasic);
        btnStandard = findViewById(R.id.btnStandard);
        btnPremium = findViewById(R.id.btnPremium);

        btnBasic.setOnCheckedChangeListener(new radioCheck());
        // By default basic
        btnBasic.setChecked(true);
        planName = "Basic";
        planCost = "349";
        planFormat = "₹ 349/month";

        btnStandard.setOnCheckedChangeListener(new radioCheck());
        btnPremium.setOnCheckedChangeListener(new radioCheck());

        toolBarSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseYourPlan.this, Login.class);
                startActivity(intent);
            }
        });
    }

    public void btnCont(View view) {

        Intent intent = new Intent(ChooseYourPlan.this,FinishUpAccount.class);
        intent.putExtra("planName",planName);
        intent.putExtra("planCost",planCost);
        intent.putExtra("planFormat",planFormat);

        startActivity(intent);

    }

    public void btnOverDue(View view) {
    }


    private class radioCheck implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                if(buttonView.getId() == R.id.btnBasic)
                {
                   planName = "Basic";
                   planCost = "349";
                   planFormat = "₹ 349/month";
                   btnStandard.setChecked(false);
                   btnPremium.setChecked(false);
                } else
                if(buttonView.getId() == R.id.btnStandard)
                {
                    planName = "Standard";
                    planCost = "649";
                    planFormat = "₹ 649/month";
                    btnBasic.setChecked(false);
                    btnPremium.setChecked(false);

                } else
                if(buttonView.getId() == R.id.btnPremium)
                {
                    planName = "Premium";
                    planCost = "799";
                    planFormat = "₹ 799/month";
                    btnBasic.setChecked(false);
                    btnStandard.setChecked(false);
                }
            }
        }
    }
}