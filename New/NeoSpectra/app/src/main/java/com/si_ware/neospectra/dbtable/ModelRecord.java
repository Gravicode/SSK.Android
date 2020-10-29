package com.si_ware.neospectra.dbtable;

public class ModelRecord {




    //Variables
    String id, bray, ca, clay, cn, addedTime;
    boolean isSelect;


//constructor


    public ModelRecord(String id, String bray, String ca, String clay, String cn, String addedTime) {
        this.id = id;
        this.bray = bray;
        this.ca = ca;
        this.clay = clay;
        this.cn = cn;
        this.addedTime = addedTime;

    }

    //getter setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBray() {
        return bray;
    }

    public void setBray(String bray) {
        this.bray = bray;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getClay() {
        return clay;
    }

    public void setClay(String clay) {
        this.clay = clay;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }



    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
