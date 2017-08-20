package com.example.aas.advancedattendancesystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import model_class.Attendance_database;
import model_class.Attendance_database_handler;

public class Admin_Profile extends AppCompatActivity {
    Button add_record;
    EditText etdbname,etroll,etname;
    Button arrow;
    Attendance_database_handler adh;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#003'>Add Record </font>"));
        setContentView(R.layout.activity_admin__profile);

        add_record=(Button) findViewById(R.id.button_add_record);
        etdbname=(EditText) findViewById(R.id.dbname);
        etroll=(EditText) findViewById(R.id.roll);
        etname=(EditText) findViewById(R.id.name);
        arrow=(Button) findViewById(R.id.arrow_forward);
        adh=new Attendance_database_handler(Admin_Profile.this);

        mAuth=FirebaseAuth.getInstance();



        add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveToDb();
                Toast.makeText(getApplicationContext(),"Record added!",Toast.LENGTH_SHORT).show();
            }
        });


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),Show_databases.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    public void saveToDb(){
       try{ Attendance_database adb=new Attendance_database();

        adb.setClass_name(etdbname.getText().toString().trim());
        adb.setRoll(Integer.parseInt(etroll.getText().toString().trim()));
        adb.setStudent_name(etname.getText().toString().trim());

        adh.addRecord(adb);
        adh.close();

        etdbname.setText("");
        etroll.setText("");
        etname.setText("");}
       catch (Exception e){
           Toast.makeText(getApplicationContext(), "ERROR "+e.toString(), Toast.LENGTH_LONG).show();
       }

    }

}
