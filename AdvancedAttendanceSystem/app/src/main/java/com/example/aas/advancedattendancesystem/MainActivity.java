package com.example.aas.advancedattendancesystem;

import android.*;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import model_class.LoginActivityManager;
import pub.devrel.easypermissions.EasyPermissions;
import util.progressGenerator;

public class MainActivity extends AppCompatActivity implements progressGenerator.OnCompleteListener {

    private static final String TAG = "Login Activity";
    private static final int RC_READ_PHONE_STATE = 101;
    private static final int RC_CHANGE_WIFI_STATE = 102;
    private ActionProcessButton blogin;
    private EditText eemail,epwd;
    private TextView forgotpwd,signup;
    private progressGenerator ProgressGenerator;
    private LoginActivityManager manager;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date date = new Date();
        String s=DateFormat.getDateInstance().format(date);
        manager = new LoginActivityManager(this);
        Log.v("DATE","DATE: "+s);
        if (manager.isFirstTime()) {
            getPermissions();
        } else {
            gotoNextActivity();
        }
        setContentView(R.layout.activity_main);

        blogin=(ActionProcessButton) findViewById(R.id.btnSignIn);
        ProgressGenerator=new progressGenerator(this);
        eemail=(EditText)findViewById(R.id.editText5);
        epwd=(EditText)findViewById(R.id.editText6);
        forgotpwd=(TextView) findViewById(R.id.forgotpwd);
        signup=(TextView) findViewById(R.id.SignUp);
        blogin.setMode(ActionProcessButton.Mode.PROGRESS);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() !=null)
            startActivity(new Intent(getApplicationContext(),Admin_Profile.class));

        blogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!validateForm()){
                    Toast.makeText(getApplicationContext(),"Some Field is empty!",Toast.LENGTH_LONG).show();
                }
                else if(epwd.getText().toString().length()<6){
                    Toast.makeText(MainActivity.this,"Password length should be over 6!",Toast.LENGTH_LONG).show();

                }else{
                    loginUser(eemail.getText().toString(),epwd.getText().toString());
                }
            }
        });

        forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),forgot_password.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CreateAccount.class));
            }
        });
    }



    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Account not found! Go for signing up",Toast.LENGTH_LONG).show();
                }else {
                    manager.setPassword(epwd.getText().toString());
                    ProgressGenerator.start(blogin);
                    blogin.setEnabled(false);
                    eemail.setEnabled(false);
                    epwd.setEnabled(false);
                }
            }
        });
    }

    private boolean validateForm() {
        return  !eemail.getText().toString().trim().equals("") &&
                !epwd.getText().toString().trim().equals("");
    }

    private void gotoNextActivity() {
        Intent intent = new Intent(this, Admin_Profile.class);
        startActivity(intent);
        finish();
    }


    private void getPermissions() {
        if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_PHONE_STATE)) {
            EasyPermissions.requestPermissions(this, "This app needs to Read Device ID for classification", RC_READ_PHONE_STATE, android.Manifest.permission.READ_PHONE_STATE);
        }
        if (!EasyPermissions.hasPermissions(this, android.Manifest.permission.CHANGE_WIFI_STATE)) {
            EasyPermissions.requestPermissions(this, "This app needs this permission to function properly", RC_CHANGE_WIFI_STATE, android.Manifest.permission.CHANGE_WIFI_STATE);
        }
    }


    @Override
    public void onComplete() {
        manager.setFirstLaunch(false);
        gotoNextActivity();
    }
}
