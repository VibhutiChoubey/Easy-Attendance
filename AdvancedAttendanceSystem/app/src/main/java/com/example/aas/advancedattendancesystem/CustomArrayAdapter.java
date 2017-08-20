package com.example.aas.advancedattendancesystem;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import model_class.Attendance_database;
import model_class.Attendance_database_handler;
import model_class.Constants;



public class CustomArrayAdapter extends BaseAdapter {

    private ArrayList<Attendance_database> alist = new ArrayList<>();
    Activity activity;
    int layoutResource;
    Attendance_database_handler adh=new Attendance_database_handler(activity);


    public CustomArrayAdapter(Activity act, int resource, ArrayList<Attendance_database> data) {
        activity = act;
        layoutResource = resource;
        alist = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
      return alist.size();
    }
    @Override
    public Attendance_database getItem(int position) {
        return  alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        final ViewHolder holder;

        if (view == null || (view.getTag())==null) {
            LayoutInflater inflater;
            inflater = LayoutInflater.from(activity);
            view = inflater.inflate(layoutResource, null);
            holder=new ViewHolder();
            holder.txvroll= (TextView)view.findViewById(R.id.tvroll);
            holder.name= (TextView) view.findViewById(R.id.tvname);
            view.setTag(holder);
        }else{
            holder=(ViewHolder) view.getTag();
        }

        holder.mylist=getItem(position);
        holder.txvroll.setText(String.valueOf(holder.mylist.getRoll()));
        holder.name.setText(holder.mylist.getStudent_name());

        return view;
    }


    class ViewHolder{
        Attendance_database mylist;
        TextView txvroll;
        TextView name;
    }
}