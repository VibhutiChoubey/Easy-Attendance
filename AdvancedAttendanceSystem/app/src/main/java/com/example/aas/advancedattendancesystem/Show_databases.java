package com.example.aas.advancedattendancesystem;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model_class.Attendance_database;
import model_class.Attendance_database_handler;

import static com.example.aas.advancedattendancesystem.R.id.dropdown;
import static com.example.aas.advancedattendancesystem.R.id.listshow;

public class Show_databases extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Attendance_database_handler adh;
    private ArrayList<Attendance_database> dblist = new ArrayList<>();
    private CustomArrayAdapter caa;
    private ListView lvshow;
    private Spinner dropdown;
    private Button arrow_back,arrow_fore;
    String[] items = new String[]{"CSE7A","CSE7B"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#003'>Class Database </font>"));
        setContentView(R.layout.activity_show_databases);

        lvshow=(ListView) findViewById(R.id.listshow);
        dropdown=(Spinner) findViewById(R.id.dropdown);
        arrow_back=(Button) findViewById(R.id.arrow_backword1);
        arrow_fore=(Button) findViewById(R.id.arrow_forward1);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),Admin_Profile.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        arrow_fore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j=new Intent(getApplicationContext(),Attendance_Sheet.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selected = parent.getItemAtPosition(position).toString();
                refreshData(selected);
                lvshow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adh.deleteRecord(id,selected);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



        private void refreshData(String selected) {
           try {
               dblist.clear();
               adh = new Attendance_database_handler(getApplicationContext());
               ArrayList<Attendance_database> listFromDB = adh.getAttendance_list(selected);

               for (int i = 0; i < listFromDB.size(); i++) {

                   Integer roll = listFromDB.get(i).getRoll();
                   String name = listFromDB.get(i).getStudent_name();

                   Attendance_database mylist = new Attendance_database();
                   mylist.setClass_name(selected);
                   mylist.setRoll(roll);
                   mylist.setStudent_name(name);

                   dblist.add(mylist);
               }
               adh.close();

               caa = new CustomArrayAdapter(Show_databases.this, R.layout.custom_listview, dblist);
               lvshow.setAdapter(caa);
               caa.notifyDataSetChanged();
           }catch (Exception e){
               Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
           }
        }
}


