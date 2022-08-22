package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livevideostreaming.MainScreens.MainScreen;
import com.example.livevideostreaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PaymentOverdue extends AppCompatActivity implements PaymentResultListener {

    String intUserID,intEmail,intContact,intFirstName,intLastName;
    TextView toolBarSignIn;
    RadioButton btnBasic, btnStandard, btnPremium;
    String planName, planCost, planFormat;
    FirebaseFirestore firebaseFirestore;
    Date today,newValidDate;
    String TAG = "Payment error occurred";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_overdue);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        intUserID = intent.getStringExtra("Uid");
        intEmail = intent.getStringExtra("emailLogin");
        intContact = intent.getStringExtra("contactNoLogin");
        intFirstName = intent.getStringExtra("firstNameLogin");
        intLastName = intent.getStringExtra("lastNameLogin");

        toolBarSignIn = findViewById(R.id.toolbarSignIn);
        btnBasic = findViewById(R.id.btnBasicOverdue);
        btnStandard = findViewById(R.id.btnStandardOverdue);
        btnPremium = findViewById(R.id.btnPremiumOverdue);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        calendar.add(Calendar.MONTH,1);
        newValidDate = calendar.getTime();

        // By default basic
        btnBasic.setChecked(true);
        planName = "Basic";
        planCost = "349";
        planFormat = "₹ 349/month";

        btnBasic.setOnCheckedChangeListener(new radioCheck());
        btnStandard.setOnCheckedChangeListener(new radioCheck());
        btnPremium.setOnCheckedChangeListener(new radioCheck());

        toolBarSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentOverdue.this, Login.class);
                startActivity(intent);
            }
        });


    }


    public void btnOverDue(View view) {
        progressDialog = new ProgressDialog(PaymentOverdue.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        startMembership();

    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(PaymentOverdue.this,"Payment successful",Toast.LENGTH_LONG).show();

                // firestore Database
                DocumentReference docRef = firebaseFirestore.collection("Users").document(intUserID);
                HashMap<String,Object> user = new HashMap();
                user.put("Email",intEmail);
                user.put("First_Name",intFirstName);
                user.put("Last_Name",intLastName);
                user.put("Contact_No",intContact);
                user.put("Plan_Cost",planCost);
                user.put("Plan_Name",planName);
                user.put("Valid_Date",newValidDate);

                docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d("success","Uploaded");
                        Intent intent = new Intent(PaymentOverdue.this, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();

                        if(e instanceof FirebaseNetworkException)
                        {
                            Toast.makeText(PaymentOverdue.this,"No Internet connection",Toast.LENGTH_LONG).show();
                        }

                        Log.d("Exc",e.toString());
                    }
                });



    }

    @Override
    public void onPaymentError(int i, String s) {

        Toast.makeText(PaymentOverdue.this,"Payment Failed",Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    public void startMembership() {


        String name = intFirstName+intLastName;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_ZvIgk4loVJBueA");
        Activity activity = this;

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",name);
            jsonObject.put("description","App payment");
            jsonObject.put("Currency","INR");
            String payment = planCost;
            Double total = Double.parseDouble(payment);
            total = total * 100;
            jsonObject.put("amount",total);
            jsonObject.put("prefill.email",intEmail);
            jsonObject.put("prefill.contact",intContact);

            checkout.open(activity,jsonObject);
        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }

    }


    private class radioCheck implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                if(buttonView.getId() == R.id.btnBasicOverdue)
                {
                    planName = "Basic";
                    planCost = "349";
                    planFormat = "₹ 349/month";
                    btnStandard.setChecked(false);
                    btnPremium.setChecked(false);
                } else
                if(buttonView.getId() == R.id.btnStandardOverdue)
                {
                    planName = "Standard";
                    planCost = "649";
                    planFormat = "₹ 649/month";
                    btnBasic.setChecked(false);
                    btnPremium.setChecked(false);

                } else
                if(buttonView.getId() == R.id.btnPremiumOverdue)
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