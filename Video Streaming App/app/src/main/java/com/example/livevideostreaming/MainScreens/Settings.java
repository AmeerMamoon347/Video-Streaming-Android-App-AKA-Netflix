package com.example.livevideostreaming.MainScreens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.MotionButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livevideostreaming.Activities.Login;
import com.example.livevideostreaming.Activities.OnBoarding;
import com.example.livevideostreaming.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class Settings extends AppCompatActivity {

    String month[] = {"","January","February","March","April","May","June","July","Aug","Sep","Oct","Nov","Dec"};

    BottomNavigationView bottomNavigationView;
    private TextView email,plan,validDate;
    private EditText password;
    Button btnReseetPW,btnSignout;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    String userPW,userEmail;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        btnReseetPW = findViewById(R.id.btnResetPW);
        btnSignout = findViewById(R.id.btnSignout);
        email = findViewById(R.id.emailSetting);
        plan = findViewById(R.id.planSetting);
        validDate = findViewById(R.id.dateSetting);
        password = findViewById(R.id.et_pwReset);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(Settings.this);


        getUserDataFromDB();

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.search_item: Intent intent = new Intent(Settings.this,Search.class);
                        startActivity(intent);
                        break;
                    case R.id.setting_item:
                        if(item.getItemId() != R.id.setting_item)
                        {
                            Intent intent1 = new Intent(Settings.this,Settings.class);
                            startActivity(intent1);
                            finish();
                        }
                        break;

                    case R.id.home_item: Intent intent2 = new Intent(Settings.this,MainScreen.class);
                                         intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                         intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         startActivity(intent2);
                                         finish();
                                         break;


                }

                return true;
            }
        });

        // To reset the password
        btnReseetPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPW = password.getText().toString();
                if(userPW.length() >=6)
                {
                    progressDialog.show();

                    firebaseAuth.signInWithEmailAndPassword(userEmail,userPW).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            // Dialogue
                            progressDialog.dismiss();
                            EditText editText = new EditText(Settings.this);
                            AlertDialog.Builder dialogue = new AlertDialog.Builder(Settings.this);
                            dialogue.setTitle("Update Password?");
                            dialogue.setCancelable(false);
                            editText.setSingleLine();
                            editText.setHint("New Password");
                            dialogue.setView(editText);

                            dialogue.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String newPassword = editText.getText().toString();

                                    password.setText("");
                                    if(newPassword.length() >= 6)
                                    {
                                        progressDialog.show();
                                        firebaseUser.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Settings.this,"Password updated",Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                if ( e instanceof  FirebaseNetworkException)
                                                {
                                                    Toast.makeText(Settings.this,"No Internet connection",Toast.LENGTH_LONG).show();
                                                } else
                                                {
                                                    Toast.makeText(Settings.this,"Error",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }) ;
                                    } else
                                        if(password.length() == 0)
                                        {
                                            Toast.makeText(Settings.this,"Enter password again to change",Toast.LENGTH_LONG).show();
                                        } else
                                    {
                                        Toast.makeText(Settings.this,"Password is too short",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            dialogue.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                 password.setText("");
                                }
                            });

                            dialogue.create().show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            if ( e instanceof  FirebaseNetworkException)
                            {
                                Toast.makeText(Settings.this,"No Internet connection",Toast.LENGTH_LONG).show();
                            } else
                            if(e instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(Settings.this,"Invalid password",Toast.LENGTH_LONG).show();
                            } else
                            {
                                Toast.makeText(Settings.this,"Error",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else
                if(userPW.length() == 0)
                {
                    Toast.makeText(Settings.this,"Enter your current password",Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(Settings.this,"Password too short",Toast.LENGTH_LONG).show();
                }

            }
        });


        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Settings.this);
                alertDialog.setTitle("Do you want to really Sign out ?");
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(Settings.this, OnBoarding.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.create().show();




            }
        });


    }

    private void getUserDataFromDB()
    {
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        firebaseFirestore.collection("Users").document(firebaseAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                       Date timeStamp = documentSnapshot.getDate("Valid_Date");
                       Calendar calendar = Calendar.getInstance();
                       calendar.setTime(timeStamp);

                       int monthNumber = calendar.get(Calendar.MONTH);
                       String resMonth = month[monthNumber];
                       String date = String.valueOf(calendar.get(Calendar.DATE));
                       String year = String.valueOf(calendar.get(Calendar.YEAR));

                      email.setText(documentSnapshot.getString("Email"));
                      plan.setText("₹"+documentSnapshot.getString("Plan_Cost")+" /month"+"("+documentSnapshot.getString("Plan_Name")+")");
                      validDate.setText(date+" "+resMonth+" "+year+"(Validity)");

                      progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Log.d("internet","connetion");

                if(e instanceof FirebaseNetworkException)
                {
                    Toast.makeText(Settings.this,"No Internet connection",Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(Settings.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}