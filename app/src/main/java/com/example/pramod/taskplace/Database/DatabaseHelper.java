package com.example.pramod.taskplace.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.example.pramod.taskplace.TaskDetails;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

/**
 * Created by ashish on 1/6/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    static String db_name="TaskPlace.db";
    static String t_name="PlaceDatabase";
    static String PLACE="place";
    static String LAT="latitude";
    static String LONG="longitude";
    static String TASK_TITLE="task_title";
    static String TASK_DESC="task_desc";
    static String DATE_="taskdate";
    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+t_name+"(sr_no INTEGER primary key,task_id varchar(30),"+PLACE+" varchar(50),"+TASK_TITLE+" varchar(20),"+TASK_DESC+" varchar(60),"+DATE_+" varchar(20),"+LAT+" varchar(20),"+LONG+" varchar(20));");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+db_name);
        onCreate(db);
    }
    public void removeAlldata(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(t_name,null,null);
    }
    public boolean isDataExist(){
        Cursor cursor=this.getReadableDatabase().rawQuery("select * from PlaceDatabase",null);
        if(cursor.getCount()==0){
            return false;
        }
        else {
            return true;
        }
    }
    public void insertData(TaskDetails details,String task_id){
        SQLiteDatabase sql=this.getWritableDatabase();
        //adding data to offline db
        ContentValues values = new ContentValues();
        values.put("sr_no",details.getTaskid());
        values.put("task_id", task_id);
        values.put("place", details.getPlace());
        values.put("task_title", details.getContent());
        values.put("task_desc", details.getTaskDesc());
        values.put("taskdate", details.getTaskdate());
        values.put("latitude", details.getLat());
        values.put("longitude", details.getLng());
        // Inserting Row
        sql.insert("PlaceDatabase", null, values);
        sql.close(); //closing sql connection
        this.close(); // Closing database connection
    }
    public void deteleData(String ids){
        SQLiteDatabase sql=this.getWritableDatabase();
        sql.delete("PlaceDatabase","task_id=?",new String[]{ids});

    }
    public ArrayList<TaskDetails> fetchData(){
        Log.i("INSIDE fetchData()","fetching data from offline database");
        ArrayList<TaskDetails> details=new ArrayList<>();
        Cursor cursor=this.getReadableDatabase().rawQuery("select * from PlaceDatabase",null);
        while(cursor.moveToNext()){
            TaskDetails d=new TaskDetails();
            d.setTaskid(cursor.getString(1));
            d.setPlace(cursor.getString(2));
            d.setLat(cursor.getString(6));
            d.setLng(cursor.getString(7));
            d.setContent(cursor.getString(3));
            d.setTaskDesc(cursor.getString(4));
            d.setTaskdate(cursor.getString(5));
            details.add(d);
        }
        return details;
    }
}
