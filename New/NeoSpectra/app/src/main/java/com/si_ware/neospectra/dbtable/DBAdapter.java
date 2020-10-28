package com.si_ware.neospectra.dbtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;


    /*
    1. INITIALIZE DB HELPER AND PASS IT A CONTEXT

     */
    public DBAdapter(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    /*
    SAVE DATA TO DB
     */
    public boolean saveSpacecraft(Spacecraft spacecraft) {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();
            cv.put(Constants.BRAY1, spacecraft.getBray());
            cv.put(Constants.CA, spacecraft.getCa());
            cv.put(Constants.CLAY, spacecraft.getClay());
            cv.put(Constants.CN, spacecraft.getCn());
            cv.put(Constants.HCL, spacecraft.getHcl());

            long result = db.insert(Constants.TB_NAME, Constants.ROW_ID, cv);
            if (result > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

        return false;
    }

    /*
     1. RETRIEVE SPACECRAFTS FROM DB AND POPULATE ARRAYLIST
     2. RETURN THE LIST
     */

    public ArrayList<Spacecraft> retrieveSpacecrafts()
    {
        ArrayList<Spacecraft> spacecrafts=new ArrayList<>();

        String[] columns={Constants.ROW_ID,Constants.BRAY1,Constants.CA,Constants.CLAY,Constants.CN, Constants.HCL};

        try
        {
            db = helper.getWritableDatabase();
            Cursor c=db.query(Constants.TB_NAME,columns,null,null,null,null,null);

            Spacecraft s;

            if(c != null)
            {
                while (c.moveToNext())
                {
//                    String s_id=c.getString(1);
                    String s_bray=c.getString(1);
                    String s_ca=c.getString(2);
                    String s_clay=c.getString(3);
                    String s_cn=c.getString(4);
                    String s_hcl=c.getString(5);

                    s=new Spacecraft();
//                    s.setId(s_id);
                    s.setBray(s_bray);
                    s.setCa(s_ca);
                    s.setClay(s_clay);
                    s.setCn(s_cn);
                    s.setHcl(s_hcl);

                    spacecrafts.add(s);
                }
            }

        }catch (SQLException e)
        {
            e.printStackTrace();
        }

        return spacecrafts;
    }

}