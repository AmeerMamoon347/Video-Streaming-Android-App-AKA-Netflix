package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livevideostreaming.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class StepTwo extends AppCompatActivity {

    String planName,planCost,planFormat,email,password;
    TextView toolBarSignIn,step2of3;
    ProgressBar progressBar;
    EditText etEmail,etPassword;
    FirebaseAuth firebaseAuth;
//    String emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    boolean isExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_two);

        getSupportActionBar().hide();

        planName = getIntent().getStringExtra("planName");
        planCost = getIntent().getStringExtra("planCost");
        planFormat = getIntent().getStringExtra("planFormat");

        etEmail = findViewById(R.id.emailStepTwo);
        etPassword = findViewById(R.id.passwordStepTwo);
        progressBar = findViewById(R.id.progressBarStepTwo);
        toolBarSignIn = findViewById(R.id.toolbarSignIn);
        step2of3 = findViewById(R.id.step2of3);
        firebaseAuth = FirebaseAuth.getInstance();

        toolBarSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StepTwo.this, Login.class);
                startActivity(intent);
            }
        });

        progressBar.setVisibility(View.GONE);

        SpannableString st = new SpannableString("Step 2 of 3");
        StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
        StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);
        st.setSpan(styleSpan1,5,6 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        st.setSpan(styleSpan2,10,11 , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         step2of3.setText(st);

    }

    public void continueStepTwo(View view) {

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if(email.length() == 0 || password.length() == 0)
        {
            Snackbar.make(view,"Please fill all the Fields",Snackbar.LENGTH_LONG).show();
        } else
        if(!email.matches(emailRegex))
        {
            Snackbar.make(view,"Enter valid Email",Snackbar.LENGTH_LONG).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    checkSignIn(email,password);
                }
            };

            new Handler().postDelayed(runnable,2000);
        }


    }

    public void checkSignIn(String email,String password)
    {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful())
                {
                    isExist = true;
                    Toast.makeText(StepTwo.this, "Email is already registered please login via here", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StepTwo.this,Login.class);
                    startActivity(intent);

                } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                {
                    Toast.makeText(StepTwo.this, "Email id already exist ", Toast.LENGTH_SHORT).show();
                } else

                if(!email.matches(emailRegex))
                {
                    Toast.makeText(StepTwo.this,"Enter valid email",Toast.LENGTH_LONG).show();
                } else
                if(password.length() < 6)
                {
                    Toast.makeText(StepTwo.this, "Password too short it should be at least of length 6", Toast.LENGTH_SHORT).
                            show();
                } else if(email.matches(emailRegex))
                {
                    Intent intent = new Intent(StepTwo.this, StepThree.class);
                    intent.putExtra("planName",planName);
                    intent.putExtra("planCost",planCost);
                    intent.putExtra("planFormat",planFormat);
                    intent.putExtra("emailStepTwo",email);
                    intent.putExtra("passwordStepTwo",password);

                    startActivity(intent);
                }
            }
        });


    }




}