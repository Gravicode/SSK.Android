package com.si_ware.neospectra.dbtable;


import android.content.Context;

import java.util.ArrayList;

public class TableHelper {

    Context c;

    private String[] spaceProbeHeaders={"Id","Bray1_P2O5","Ca","CLAY","CN","HCL"};
    private String[][] spaceProbes;

    public TableHelper(Context c) {
        this.c = c;
    }

    public String[] getSpaceProbeHeaders()
    {
        return spaceProbeHeaders;
    }

    public  String[][] getSpaceProbes()
    {
        ArrayList<Spacecraft> spacecrafts=new DBAdapter(c).retrieveSpacecrafts();
        Spacecraft s;

        spaceProbes= new String[spacecrafts.size()][6];

        for (int i=0;i<spacecrafts.size();i++) {

            s=spacecrafts.get(i);

            spaceProbes[i][0]=s.getId();
            spaceProbes[i][1]=s.getBray();
            spaceProbes[i][2]=s.getCa();
            spaceProbes[i][3]=s.getClay();
            spaceProbes[i][4]=s.getCn();
            spaceProbes[i][5]=s.getHcl();
        }
        return spaceProbes;
    }
}