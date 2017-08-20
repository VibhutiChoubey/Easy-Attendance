package com.example.aas.advancedattendancesystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import util.progressGenerator;

public class CreateAccount extends AppCompatActivity implements progressGenerator.OnCompleteListener{

    private EditText newEmail,newPassword;
    private TextView signin;
    private ActionProcessButton bregister;
    private progressGenerator ProgressGenerator;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        newEmail=(EditText) findViewById(R.id.editText5);
        newPassword=(EditText) findViewById(R.id.editText6);
        signin=(TextView) findViewById(R.id.SignIn);
        bregister=(ActionProcessButton) findViewById(R.id.btnSignUp);
        ProgressGenerator=new progressGenerator(this);

        mAuth=FirebaseAuth.getInstance();

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signupuser(newEmail.getText().toString(),newPassword.getText().toString());
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void signupuser(String email,String pwd) {
        mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Error:"+task.getException(),Toast.LENGTH_LONG).show();
                }else{
                    ProgressGenerator.start(bregister);
                    bregister.setEnabled(false);
                    newEmail.setEnabled(false);
                    newPassword.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onComplete() {
        ProgressGenerator.start(bregister);
        bregister.setEnabled(false);
        newEmail.setEnabled(false);
        newPassword.setEnabled(false);
        startActivity(new Intent(getApplicationContext(),Admin_Profile.class));
    }
}
