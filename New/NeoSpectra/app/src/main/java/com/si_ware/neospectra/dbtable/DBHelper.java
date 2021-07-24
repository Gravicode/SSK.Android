package com.si_ware.neospectra.dbtable;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    public SharedPreferences sharedPreferences;

    public DBHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, (Constants.DB_VERSION));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table on db
        db.execSQL(Constants.CREATE_TABLE);
        db.execSQL(RawData.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //upgrade database
        //drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RawData.TABLE_NAME);
        //create table again
        onCreate(db);

    }

    public long insertRecord(String bray, String ca, String clay, String cn, String hclk2o, String hclp2o5, String jumlah, String k, String kb, String kjeldahl, String ktk, String mg, String morgan, String na, String olsen, String phh2o, String phkcl, String retensip, String sand, String silt, String wbc, String addedTime) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //insert data
        values.put(Constants.C_BRAY, bray);
        values.put(Constants.C_CA, ca);
        values.put(Constants.C_CLAY, clay);
        values.put(Constants.C_HCL25_K2O, hclk2o);
        values.put(Constants.C_HCL25_P2O5, hclp2o5);
        values.put(Constants.C_CN, cn);
        values.put(Constants.C_JUMLAH, jumlah);
        values.put(Constants.C_K, k);
        values.put(Constants.C_KB_ADJUSTED, kb);
        values.put(Constants.C_KJELDAHL_N, kjeldahl);
        values.put(Constants.C_KTK, ktk);
        values.put(Constants.C_MG, mg);
        values.put(Constants.C_MORGAN_K2O, morgan);
        values.put(Constants.C_NA, na);
        values.put(Constants.C_OLSEN_P2O5, olsen);
        values.put(Constants.C_PH_H2O, phh2o);
        values.put(Constants.C_PH_KCL, phkcl);
        values.put(Constants.C_RETENSIP, retensip);
        values.put(Constants.C_SAND, sand);
        values.put(Constants.C_SILT, silt);
        values.put(Constants.C_WBC, wbc);
        values.put(Constants.C_ADDED_TIME_STAMP, addedTime);
        //insert row
        long id = db.insert(Constants.TABLE_NAME, null, values);

        //close db connection
        db.close();
        // return
        return id;
    }

    //public long insertRawData(long ElementID, double[] Data) {
    public long insertRawData(long ElementID, String strData) {
        /*
        JSONArray strData = new JSONArray();
        for (int i = 0; i < Data.length; i++) {
            try {
                strData.put(Data[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //insert data
        values.put(RawData.C_ELEMENT_ID, ElementID);
        values.put(RawData.C_RAWDATA, strData);
        //insert row
        long id = db.insert(RawData.TABLE_NAME, null, values);
        //close db connection
        db.close();
        // return
        return id;
    }

    //get all data
    public ArrayList<ModelRecord> getAllRecords(String orderby) {
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT "
                + Constants.TABLE_NAME + "." + Constants.C_ID + ", "
                + Constants.TABLE_NAME + "." + Constants.C_BRAY + ", "
                + Constants.TABLE_NAME + "." + Constants.C_CA + ", "
                + Constants.TABLE_NAME + "." + Constants.C_CLAY + ", "
                + Constants.TABLE_NAME + "." + Constants.C_CN + ", "
                + Constants.TABLE_NAME + "." + Constants.C_HCL25_K2O + ", "
                + Constants.TABLE_NAME + "." + Constants.C_HCL25_P2O5 + ", "
                + Constants.TABLE_NAME + "." + Constants.C_JUMLAH + ", "
                + Constants.TABLE_NAME + "." + Constants.C_K + ", "
                + Constants.TABLE_NAME + "." + Constants.C_KB_ADJUSTED + ", "
                + Constants.TABLE_NAME + "." + Constants.C_KJELDAHL_N + ", "
                + Constants.TABLE_NAME + "." + Constants.C_KTK + ", "
                + Constants.TABLE_NAME + "." + Constants.C_MG + ", "
                + Constants.TABLE_NAME + "." + Constants.C_MORGAN_K2O + ", "
                + Constants.TABLE_NAME + "." + Constants.C_NA + ", "
                + Constants.TABLE_NAME + "." + Constants.C_OLSEN_P2O5 + ", "
                + Constants.TABLE_NAME + "." + Constants.C_PH_H2O + ", "
                + Constants.TABLE_NAME + "." + Constants.C_PH_KCL + ", "
                + Constants.TABLE_NAME + "." + Constants.C_RETENSIP + ", "
                + Constants.TABLE_NAME + "." + Constants.C_SAND + ", "
                + Constants.TABLE_NAME + "." + Constants.C_SILT + ", "
                + Constants.TABLE_NAME + "." + Constants.C_WBC + ", "
                + RawData.TABLE_NAME + "." + RawData.C_RAWDATA + ", "
                + Constants.TABLE_NAME + "." + Constants.C_ADDED_TIME_STAMP
                + " FROM " + Constants.TABLE_NAME
                + " LEFT JOIN " + RawData.TABLE_NAME
                + " ON " + Constants.TABLE_NAME + "." + Constants.C_ID + " = " +
                RawData.TABLE_NAME + "." + RawData.C_ELEMENT_ID
                + " ORDER BY " + orderby;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list

        if (cursor.moveToFirst()) {
            do {
                ModelRecord modelRecord = new ModelRecord(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_JUMLAH)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_K)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KB_ADJUSTED)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KJELDAHL_N)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KTK)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MG)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MORGAN_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_OLSEN_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_H2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_KCL)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_RETENSIP)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SAND)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SILT)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_WBC)),
                        "" + cursor.getString(cursor.getColumnIndex(RawData.C_RAWDATA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));
                //add record to list
                recordsList.add(modelRecord);
            } while (cursor.moveToNext());
        }
        //close db connection
        db.close();
        //return to list
        return recordsList;
    }


    //search data
    public ArrayList<ModelRecord> SearchRecords(String query) {
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " LIKE '%" + query + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list

        if (cursor.moveToFirst()) {
            do {
                ModelRecord modelRecord = new ModelRecord(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_JUMLAH)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_K)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KB_ADJUSTED)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KJELDAHL_N)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KTK)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MG)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MORGAN_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_OLSEN_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_H2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_KCL)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_RETENSIP)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SAND)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SILT)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_WBC)),
                        "" + cursor.getString(cursor.getColumnIndex(RawData.C_RAWDATA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));

                //add record to list
                recordsList.add(modelRecord);
            } while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }

    public ArrayList<ModelRecord> SearchRecords(String query, String query2) {
        ArrayList<ModelRecord> recordsList = new ArrayList<>();
        //query to select records
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ADDED_TIME_STAMP + " BETWEEN " + "'" + query + "'" + " AND " + "'" + query2 + "'";
        ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all records and add to list
        if (cursor.moveToFirst()) {
            do {
                ModelRecord modelRecord = new ModelRecord(
                        "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_BRAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CLAY)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_CN)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_HCL25_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_JUMLAH)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_K)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KB_ADJUSTED)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KJELDAHL_N)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_KTK)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MG)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_MORGAN_K2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_NA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_OLSEN_P2O5)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_H2O)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_PH_KCL)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_RETENSIP)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SAND)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_SILT)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_WBC)),
                        "" + cursor.getString(cursor.getColumnIndex(RawData.C_RAWDATA)),
                        "" + cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIME_STAMP)));

                //add record to list
                recordsList.add(modelRecord);
            } while (cursor.moveToNext());
        }

        //close db connection
        db.close();

        //return to list
        return recordsList;

    }


    // delete data using id
    public void deleteData(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.C_ID + " = ?", new String[]{id});
        db.delete(RawData.TABLE_NAME, RawData.C_ELEMENT_ID + " = ?", new String[]{id});
        db.close();
    }

    //delete all data from table
    public void deleteAllData() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.TABLE_NAME);
        db.execSQL("DELETE FROM " + RawData.TABLE_NAME);
/*        db.equals("DELETE FROM " + Constants.TABLE_NAME);
        db.equals("DELETE FROM " + RawData.TABLE_NAME);*/
        db.close();
    }

    //get number of records
    public int getRecordsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        return count;
    }
}
