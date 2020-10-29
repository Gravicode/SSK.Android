package com.si_ware.neospectra.dbtable;

public class Constants {

    //db name

    public static final String DB_NAME="NEO_DB";

    public static final int  DB_VERSION = 1 ;

    public static final String TABLE_NAME="NEO_TB";

    public static final String C_ID = "ID";
    public static final String C_BRAY = "BRAY";
    public static final String C_CA = "CA";
    public static final String C_CLAY = "CLAY";
    public static final String C_CN = "CN";
    public static final String C_ADDED_TIME_STAMP = "ADDED_TIME_STAMP";


    //create table query

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_BRAY + " TEXT,"
            + C_CA + " TEXT,"
            + C_CLAY + " TEXT,"
            + C_CN + " TEXT,"
            + C_ADDED_TIME_STAMP+ " TEXT"

            + ")";
}
