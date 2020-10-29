package com.si_ware.neospectra.dbtable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;


public class DbHelper extends SQLiteOpenHelper {
    public SharedPreferences sharedPreferences;

    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME , null, (Constants.DB_VERSION));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create teble on db
        db.execSQL(Constants.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database

        //drop table if exists
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);
        //create table again
        onCreate(db);

    }

    public long insertRecord(String bray, String ca, String clay, String cn, String addedTime){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //insert data
        values.put(Constants.C_BRAY,bray);
        values.put(Constants.C_CA,ca);
        values.put(Constants.C_CLAY,clay);
        values.put(Constants.C_CN,cn);
        values.put(Constants.C_ADDED_TIME_STAMP,addedTime);


        //insert row

        long id = db.insert(Constants.TABLE_NAME,null, values);

        //close db connection
        db.close();
        // return
        return id;



    }

    //get all data
    public ArrayList<ModelRecord> getAllRecords(String orderby){
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " ORDER BY " + orderby;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list

        if (cursor.moveToFirst()){
            do{
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));


                //add record to list
                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }



    //search data
    public ArrayList<ModelRecord> SearchRecords(String query){
//
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
//        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " BETWEEN " + " LIKE '%" + query + "'" + " AND " + " LIKE '%" + query2 + "'";
//        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " BETWEEN " + query + " AND " + query2 ;
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP +  " LIKE '%" + query + "'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list

        if (cursor.moveToFirst()){
            do{
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));

                //add record to list
                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }

    public ArrayList<ModelRecord> SearchRecords(String query, String query2)  {
//
        ArrayList<ModelRecord> recordsList = new ArrayList<>();

//        Date minDate =  new SimpleDateFormat("d/M/yyyy").parse(query);
//        Date maxDate =  new SimpleDateFormat("d/M/yyyy").parse(query2);
        //query to select records
//        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " BETWEEN " + " LIKE '%" + query + "'" + " AND " + " LIKE '%" + query2 + "'";
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " BETWEEN " + "'" + query + "'" + " AND " + "'"+ query2 + "'"; ;
//        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP +  " LIKE '%" + query + "'" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list

        if (cursor.moveToFirst()){
            do{
                ModelRecord modelRecord = new ModelRecord(
                        ""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));

                //add record to list
                recordsList.add(modelRecord);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }


    // delete data using id
    public  void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ?", new String[]{id});
        db.close();

    }

    //delete all data from table
    public void deleteAllData(){
        SQLiteDatabase db = getWritableDatabase();
        db.equals("DELETE FROM " + Constants.TABLE_NAME);
        db.close();
    }

    //get number of records
    public  int getRecordsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }
}
