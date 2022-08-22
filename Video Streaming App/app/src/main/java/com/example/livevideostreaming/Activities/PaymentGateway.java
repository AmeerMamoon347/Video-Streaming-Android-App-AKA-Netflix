package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livevideostreaming.MainScreens.MainScreen;
import com.example.livevideostreaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class PaymentGateway extends AppCompatActivity implements PaymentResultListener {

    String planName,planCost,planFormat,email,password,firstName,lastName,contactNo;
    TextView tv_PlanFormat,tv_PlanName,change,termsCondition,tvAgree,step3of3_payment;
    EditText tv_firstName,tv_LastName,tv_ContactNo;
    CheckBox checkBox;
    boolean isCheck = false;
    String TAG = "Payment error occurred";
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date today,validDate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);

        getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();
        calendar.add(Calendar.MONTH,1);
        validDate = calendar.getTime();

        planName = getIntent().getStringExtra("planName");
        planCost = getIntent().getStringExtra("planCost");
        planFormat = getIntent().getStringExtra("planFormat");
        email = getIntent().getStringExtra("emailStepTwo");
        password = getIntent().getStringExtra("passwordStepTwo");
        Log.d("Data",planName+" "+planCost+" "+planFormat+" "+email+" "+password);

        tv_PlanFormat = findViewById(R.id.planFormat);
        tv_PlanName = findViewById(R.id.planName);
        change = findViewById(R.id.change);
        termsCondition = findViewById(R.id.termsCondition);
        tvAgree = findViewById(R.id.tv_Agree);
        tv_firstName = findViewById(R.id.firstName);
        tv_LastName = findViewById(R.id.lastName);
        tv_ContactNo = findViewById(R.id.contactNo);
        step3of3_payment = findViewById(R.id.step3of3_payment);
        checkBox = findViewById(R.id.checkbox);

        tv_PlanFormat.setText(planFormat);
        tv_PlanName.setText(planName);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentGateway.this,ChooseYourPlan.class);
                startActivity(intent);
            }
        });

       // Step 3 of 3 -> Highlight
        SpannableString st1 = new SpannableString("Step 3 of 3");
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);
        st1.setSpan(styleSpan1,5,6 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st1.setSpan(styleSpan2,10,11 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        step3of3_payment.setText(st1);

       // Terms of use and privacy
        SpannableString st = new SpannableString("By checking the checkbox below, you agree to our Terms of Use, Privacy Statement, and that you are over 18. Netflix will automatically continue your membership and charge the monthly membership fee to your payment method until you cancel. You may cancel at any time to avoid future charges.");
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://help.netflix.com/legal/termsofuse"));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.Blue));
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("https://help.netflix.com/legal/privacy"));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.Blue));
            }
        };

        st.setSpan(clickableSpan1,49,61, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(clickableSpan2,63,80, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsCondition.setText(st);
        termsCondition.setMovementMethod(LinkMovementMethod.getInstance());

        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isCheck)
                {
                    checkBox.setChecked(true);
                    isCheck = true;
                } else
                {
                    checkBox.setChecked(false);
                    isCheck = false;
                }

            }
        });



    }

    public void btnStartMembership(View view) {
        firstName = tv_firstName.getText().toString();
        lastName = tv_LastName.getText().toString();
        contactNo = tv_ContactNo.getText().toString();

        progressDialog = new ProgressDialog(PaymentGateway.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        if(firstName.length() >=3 && lastName.length()>=3 && contactNo.length() == 10 && checkBox.isChecked())
        {
            startMembership();
        } else
        {
            progressDialog.dismiss();

            if(firstName.length() == 0 || lastName.length() == 0 || contactNo.length() == 0)
            {
                Snackbar.make(view,"Please fill all the Fields",Snackbar.LENGTH_LONG).show();
            }
            else if(firstName.length() < 3 || lastName.length() < 3)
            {
                Toast.makeText(getApplicationContext(),"First and Last name should be of length 3 at least",Toast.LENGTH_LONG).show();
            } else if(contactNo.length() != 10)
            {
                Toast.makeText(getApplicationContext(),"Enter valid contact number",Toast.LENGTH_LONG).show();
            } else if(!checkBox.isChecked())
            {
                Toast.makeText(getApplicationContext()," Please agree Terms of use and Policy",Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(getApplicationContext()," Error",Toast.LENGTH_LONG).show();
            }

        }


    }

    public void startMembership() {


        String name = firstName+lastName;

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
            jsonObject.put("prefill.email",email);
            jsonObject.put("prefill.contact",contactNo);

            checkout.open(activity,jsonObject);
        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        progressDialog.show();
        Toast.makeText(PaymentGateway.this,"Payment successful",Toast.LENGTH_LONG).show();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // firestore Database
                String userID = firebaseAuth.getUid();
                DocumentReference docRef = firebaseFirestore.collection("Users").document(userID);
                HashMap<String,Object> user = new HashMap();
                user.put("Email",email);
                user.put("First_Name",firstName);
                user.put("Last_Name",lastName);
                user.put("Contact_No",contactNo);
                user.put("Plan_Cost",planCost);
                user.put("Plan_Name",planName);
                user.put("Valid_Date",validDate);

                docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Log.d("Success","Uploaded");
                        Intent intent = new Intent(PaymentGateway.this, MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                     Log.d("Exc",e.toString());
                        if(e instanceof FirebaseNetworkException)
                        {
                            Toast.makeText(PaymentGateway.this,"No Internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("exc",e.toString());
                if(e instanceof FirebaseNetworkException)
                {
                    Toast.makeText(PaymentGateway.this,"No Internet connection",Toast.LENGTH_LONG).show();
                }
            }
        });




    }



    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(PaymentGateway.this,"Payment Failed",Toast.LENGTH_LONG).show();
        progressDialog.dismiss();
    }



}