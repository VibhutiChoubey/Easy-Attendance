package com.example.aas.advancedattendancesystem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import util.progressGenerator;

public class forgot_password extends AppCompatActivity implements progressGenerator.OnCompleteListener{

    private EditText resetemail;
    private ActionProcessButton btnreset;
    private TextView back;
    private progressGenerator ProgressGenerator;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetemail=(EditText) findViewById(R.id.resetpasswordemail);
        btnreset=(ActionProcessButton) findViewById(R.id.btnResetPassword);
        back=(TextView) findViewById(R.id.back);
        ProgressGenerator=new progressGenerator(this);

        mAuth=FirebaseAuth.getInstance();

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(resetemail.getText().toString());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void resetPassword(final String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ProgressGenerator.start(btnreset);
                    btnreset.setEnabled(false);
                    resetemail.setEnabled(false);
                }else{
                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onComplete() {

    }
}
