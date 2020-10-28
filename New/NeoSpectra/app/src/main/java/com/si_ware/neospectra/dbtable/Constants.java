package com.si_ware.neospectra.dbtable;

public class Constants {
    public static final String ROW_ID="id";
    static final String BRAY1="bray";
    static final String CA="ca";
    static final String CLAY="clay";
    static final String CN="cn";
    static final String HCL="hcl";

    /*
    DB PROPERTIES
     */
    static final String DB_NAME="tv_DB";
    static final String TB_NAME="tv_TB";
    static final int DB_VERSION=1;

    /*
    TABLE CREATION STATEMENT
     */
    static final String CREATE_TB="CREATE TABLE tv_TB(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "bray TEXT NOT NULL,ca TEXT NOT NULL,clay TEXT NOT NULL,cn TEXT NOT NULL,hcl TEXT NOT NULL);";

    /*
    TABLE DELETION STMT
     */
    static final String DROP_TB="DROP TABLE IF EXISTS "+TB_NAME;

}
