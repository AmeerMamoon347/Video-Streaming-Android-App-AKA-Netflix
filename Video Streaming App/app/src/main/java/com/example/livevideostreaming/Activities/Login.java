package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class Login extends AppCompatActivity {

     EditText email,password;
     TextView forget,signUp;
     MotionButton btnSignIn;
     ProgressBar progressBar;
     FirebaseAuth firebaseAuth;
     FirebaseFirestore firebaseFirestore;
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    String userID,firstName,lastName,contact;
    Date today,validDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();

        email = findViewById(R.id.enter_email);
        password = findViewById(R.id.enter_password);
        btnSignIn = findViewById(R.id.btn_Login);
        forget = findViewById(R.id.forget);
        progressBar = findViewById(R.id.progressBarSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().length() == 0 || password.getText().toString().length() == 0)
                {
                    Snackbar.make(v,"Please fill all the Fields",Snackbar.LENGTH_LONG).show();
                } else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            if(email.getText().toString().matches(emailRegex) && password.length() >=6)
                            {
                                signIn();
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            } else
                            {
                                progressBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                if(!email.getText().toString().matches(emailRegex))
                                {
                                    Toast.makeText(getApplicationContext(),"Enter valid email",Toast.LENGTH_LONG).show();
                                } else if(password.getText().toString().length() < 6)
                                {
                                    Toast.makeText(getApplicationContext(),"Wrong password",Toast.LENGTH_LONG).show();
                                }
                            }

                        }
                    };

                    new Handler().postDelayed(runnable,1000);
                }


            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(email.getText().toString().matches(emailRegex))
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
                    alertDialog.setMessage("Press YES or NO");
                    alertDialog.setTitle("Send password reset link to email");

                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(Login.this,"Password reset link has been sent to the email",Toast.LENGTH_LONG)
                                            .show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if(e instanceof FirebaseNetworkException)
                                    {
                                        Toast.makeText(Login.this,"No Internet connection",Toast.LENGTH_LONG).show();
                                    }
                                    if(e instanceof FirebaseAuthInvalidUserException)
                                    {
                                        Toast.makeText(Login.this,"No user exist with the given Email",Toast.LENGTH_LONG)
                                          .show();
                                    }
                                }
                            });
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                   alertDialog.create().show();

                } else
                    if(email.getText().toString().length() == 0)
                    {
                        Toast.makeText(Login.this,"Please enter email to reset the password",Toast.LENGTH_LONG)
                                .show();
                    } else
                {
                    Toast.makeText(Login.this,"Enter valid email",Toast.LENGTH_LONG)
                            .show();
                }


            }
        });




    }

    public  void signIn()
    {
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                       if(task.isSuccessful())
                       {
                           userID = firebaseAuth.getCurrentUser().getUid();
                           DocumentReference dbRef = firebaseFirestore.collection("Users").document(userID);
                           dbRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {

                                   firstName = documentSnapshot.getString("First_Name");
                                   lastName = documentSnapshot.getString("Last_Name");
                                   contact = documentSnapshot.getString("Contact_No");

                                   validDate = documentSnapshot.getDate("Valid_Date");
                                   progressBar.setVisibility(View.GONE);
                                   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                 if(validDate.compareTo(today) >=0)
                                 {
                                     Intent intent = new Intent(Login.this, MainScreen.class);
                                     intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                     startActivity(intent);
                                     Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                     finish();
                                 } else
                                 {
                                     Toast.makeText(getApplicationContext(),"Plan Expired",Toast.LENGTH_LONG).show();
                                     Intent intent = new Intent(Login.this,PaymentOverdue.class);
                                     intent.putExtra("Uid",userID);
                                     intent.putExtra("emailLogin",email.getText().toString());
                                     intent.putExtra("firstNameLogin",firstName);
                                     intent.putExtra("lastNameLogin",lastName);
                                     intent.putExtra("contactNoLogin",contact);

                                     startActivity(intent);
                                 }
                               }
                           }).addOnFailureListener(new OnFailureListener() {

                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   progressBar.setVisibility(View.GONE);
                                   getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                 Log.d("exc",e.toString());
                                 if(e instanceof FirebaseNetworkException)
                                 {
                                     Toast.makeText(Login.this,"No Internet connection",Toast.LENGTH_LONG).show();
                                 }
                               }
                           });




                       } else
                       if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                       {
                           Toast.makeText(getApplicationContext(),"Wrong email or password",Toast.LENGTH_LONG);
                       } else
                       if(task.getException() instanceof FirebaseNetworkException)
                       {
                           Toast.makeText(Login.this,"No Internet connection",Toast.LENGTH_LONG).show();
                       } else
                       {
                           Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG);
                       }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d("exc",e.toString());

                if(e instanceof FirebaseNetworkException)
                {
                    Toast.makeText(Login.this,"No Internet connection",Toast.LENGTH_LONG).show();
                } else
                if(e instanceof FirebaseAuthInvalidCredentialsException)
                {
                    Toast.makeText(Login.this,"Incorrect password",Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(Login.this,"No account with the given email or password is wrong" , Toast.LENGTH_SHORT).show();
                    Log.d("exc",e.toString());
                }


            }
        });


    }




    public void btnStepOne(View view) {

        Intent intent = new Intent(Login.this,StepOne.class);
        startActivity(intent);
    }
}