package com.si_ware.neospectra.dbtable;

public class RawData {
    public static final String DB_NAME = "sskdb.db";
    public static final int DB_VERSION = 1 ;
    public static final String TABLE_NAME = "RAWDATA_TB";

    public static final String C_ID = "ID";
    public static final String C_ELEMENT_ID = "ELEMENT_ID";
    public static final String C_RAWDATA = "RAWDATA";
    public static final String C_CREATED_DATE = "CREATED_DATE";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
            + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_ELEMENT_ID +" INTEGER,"
            + C_RAWDATA +" TEXT,"
            + C_CREATED_DATE +" TEXT"
            +")";
}
