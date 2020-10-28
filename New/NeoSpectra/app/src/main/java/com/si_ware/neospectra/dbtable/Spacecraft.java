package com.si_ware.neospectra.dbtable;

public class Spacecraft {

    private String id;
    private String bray;
    private String ca;
    private String clay;
    private String cn;
    private String hcl;
    private String p2o5;


    public Spacecraft(String id, String bray, String ca, String clay, String cn, String hcl) {
        this.id = id;
        this.bray = bray;
        this.ca = ca;
        this.clay = clay;
        this.cn = cn;
        this.hcl = hcl;
//        this.p2o5 = p2o5;
    }

    public Spacecraft() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getP2o5() {
        return p2o5;
    }

    public void setP2o5(String p2o5) {
        this.p2o5 = p2o5;
    }

    public String getHcl() {
        return hcl;
    }

    public void setHcl(String hcl) {
        this.hcl = hcl;
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
}