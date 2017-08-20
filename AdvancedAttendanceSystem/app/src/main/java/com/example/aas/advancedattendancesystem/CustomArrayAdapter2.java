package com.example.aas.advancedattendancesystem;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model_class.Attendance_database;


public class CustomArrayAdapter2 extends BaseAdapter {

    Activity activity;
    int layoutResource;
    private ArrayList<Attendance_database> alist = new ArrayList<>();

    public CustomArrayAdapter2(Activity act, int resource, ArrayList<Attendance_database> data) {
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
    public Object getItem(int position) {
        return  alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        ViewHolder holder=null;
        if(row==null || row.getTag()==null){
            LayoutInflater inflater=LayoutInflater.from(activity);
            row= inflater.inflate(layoutResource,null);
            holder=new ViewHolder();
            holder.roll=(TextView) row.findViewById(R.id.msgroll);
            holder.time=(TextView) row.findViewById(R.id.time);
            holder.name=(TextView) row.findViewById(R.id.msgname);
            row.setTag(holder);
        }else {
            holder=(ViewHolder) row.getTag();
        }

        holder.mylist= (Attendance_database) getItem(position);
        holder.time.setText(DateFormat.getDateTimeInstance().format(new Date()));
        holder.roll.setText(String.valueOf(holder.mylist.getRoll()));
        holder.name.setText(holder.mylist.getStudent_name());
        return row;
    }

    class ViewHolder{
        Attendance_database mylist;
        TextView roll;
        TextView name;
        TextView time;
    }
}
