package com.example.livevideostreaming.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.livevideostreaming.MainScreens.MainScreen;
import com.example.livevideostreaming.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;


public class Splash extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Date today,validDate;
    String firstName,lastName,contact,email,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Calendar calendar = Calendar.getInstance();
        today = calendar.getTime();

        if(firebaseAuth.getCurrentUser() != null)
        {
            userID = firebaseAuth.getCurrentUser().getUid();
            DocumentReference dbRef = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
            dbRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    validDate = documentSnapshot.getDate("Valid_Date");
                    email = documentSnapshot.getString("Email");
                    contact = documentSnapshot.getString("Contact_No");
                    firstName = documentSnapshot.getString("First_Name");
                    lastName = documentSnapshot.getString("Last_Name");
                    Log.d("result",validDate+" "+email+" "+contact+" "+firstName+" "+lastName);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseNetworkException)
                    {
                        Toast.makeText(Splash.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(Splash.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                        Log.d("exc",e.toString());
                    }

                }
            });

        }


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                if(firebaseAuth.getCurrentUser() != null)
                {
                    if(validDate.compareTo(today) >= 0)
                    {
                        Intent intent = new Intent(Splash.this, MainScreen.class);
                        startActivity(intent);
                        finish();
                    } else
                    {
                        Intent intent = new Intent(Splash.this,PaymentOverdue.class);
                        intent.putExtra("Uid",userID);
                        intent.putExtra("emailLogin",email);
                        intent.putExtra("firstNameLogin",firstName);
                        intent.putExtra("lastNameLogin",lastName);
                        intent.putExtra("contactNoLogin",contact);

                        startActivity(intent);
                        finish();
                    }

                } else
                {
                   Intent intent = new Intent(Splash.this,OnBoarding.class);
                   startActivity(intent);
                   finish();
                }


            }
        };

        handler.postDelayed(runnable,5000);
    }



    @Override
    protected void onStart() {
        super.onStart();


    }


}