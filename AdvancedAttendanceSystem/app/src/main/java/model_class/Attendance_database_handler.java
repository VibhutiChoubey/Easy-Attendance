package model_class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.aas.advancedattendancesystem.Show_databases;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static model_class.Constants.DEVICE_ID;
import static model_class.Constants.ROLL;
import static model_class.Constants.STUDENT_NAME;
import static model_class.Constants.TABLE_NAME;


public class Attendance_database_handler extends SQLiteOpenHelper{


    public Attendance_database_handler(Context context) {
        super(context,Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        Log.v("OnUpgrade","Dropping the table");
        onCreate(db);

    }



    public void addRecord(Attendance_database adb){
        SQLiteDatabase db=this.getWritableDatabase();
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+
                adb.getClass_name()+"("+
                ROLL+" INTEGER PRIMARY KEY,"+
                STUDENT_NAME+" TEXT, "+DEVICE_ID+" TEXT);";
        db.execSQL(CREATE_TABLE);
        ContentValues values=new ContentValues();


        values.put(ROLL,adb.getRoll());
        values.put(STUDENT_NAME,adb.getStudent_name());

        db.insert(adb.getClass_name(),null,values);
        db.close();
    }

    public void deleteRecord(long id,String selected) {
        Log.v("msg", id + " " + selected);
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            int numberofRows = db.delete(selected, "id=" + id, null);
            if (numberofRows > 0)
                Log.v("abc", "Delete Successful");
            else
                Log.v("abc", "Delete Unsuccessful");
        } else
            Log.v("abc", "Database is null");

    }

    public ArrayList<Attendance_database> getAttendance_list(String selected) {


        String selectQuery = "SELECT * FROM " + selected;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery,null);
        ArrayList<Attendance_database> attendance_list = new ArrayList<>();

        if (cursor.moveToFirst()) {

            do {
                Attendance_database myList = new Attendance_database();

                myList.setRoll(cursor.getInt(cursor.getColumnIndex(ROLL)));
                myList.setStudent_name(cursor.getString(cursor.getColumnIndex(STUDENT_NAME)));
                myList.setDevice_id("");
                attendance_list.add(myList);

            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return attendance_list;

    }

    public void updateTable(String selected){
        Date date = new Date();
        String s= DateFormat.getDateInstance().format(date);
        String updateQuery="ALTER TABLE "+selected+" ADD "+s+" TEXT";
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery(updateQuery,null);
    }

    public void updateRecordP(String selected,Integer roll){
        Date date = new Date();
        String s = DateFormat.getDateInstance().format(date);
        String updateRQuery="UPDATE "+selected+" SET "+s+" ='P' WHERE " +ROLL+" = "+roll;
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(updateRQuery,null);
    }
}
