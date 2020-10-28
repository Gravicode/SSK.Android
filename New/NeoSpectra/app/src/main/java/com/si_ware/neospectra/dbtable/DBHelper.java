package com.si_ware.neospectra.dbtable;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }
    /*
    CREATE TABLE
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try
        {
            db.execSQL(Constants.CREATE_TB);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    /*
    UPGRADE TABLE
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            db.execSQL(Constants.DROP_TB);
            db.execSQL(Constants.CREATE_TB);

        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public  void deleteData(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TB_NAME, Constants.ROW_ID + " = ?", new String[]{id});
        db.close();

    }

    //get all data
    public ArrayList<Spacecraft> getAllRecords(String orderby){

        ArrayList<Spacecraft> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + Constants.TB_NAME + " ORDER BY " + orderby;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all records and add to list

        if (cursor.moveToFirst()){
            do{
                Spacecraft spacecraft = new Spacecraft(
                        ""+cursor.getString(cursor.getColumnIndex(Constants.ROW_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.BRAY1)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.CA)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.CLAY)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.CN)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.HCL)));


                //add record to list
                recordsList.add(spacecraft);
            }while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }

}