package com.si_ware.neospectra;

public class DataElements {
    private static float Bray1_P2O5 = 0.00F;
    public static final float getBray1P2O5()
    {
        if (Bray1_P2O5 > 0)
            return Bray1_P2O5;
        else
            return 0;
    }
    public static final void setBray1P2O5(float value)
    {
        Bray1_P2O5 = value;
    }
    private static float Ca = 0.00F;
    public static final float getCa()
    {
        if (Ca > 0)
            return Ca;
        else
            return 0;
    }
    public static final void setCa(float value)
    {
        Ca = value;
    }
    private static float CLAY = 0.00F;
    public static final float getCLAY()
    {
        if (CLAY > 0)
            return CLAY;
        else
            return 0;
    }
    public static final void setCLAY(float value)
    {
        CLAY = value;
    }
    private static float C_N = 0.00F;
    public static final float getCN()
    {
        if (C_N > 0)
            return C_N;
        else
            return 0;
    }
    public static final void setCN(float value)
    {
        C_N = value;
    }
    private static float HCl25_K2O = 0.00F;
    public static final float getHCl25K2O()
    {
        if (HCl25_K2O > 0)
            return HCl25_K2O;
        else
            return 0;
    }
    public static final void setHCl25K2O(float value)
    {
        HCl25_K2O = value;
    }
    private static float HCl25_P2O5 = 0.00F;
    public static final float getHCl25P2O5()
    {
        if (HCl25_P2O5 > 0)
            return HCl25_P2O5;
        else
            return 0;
    }
    public static final void setHCl25P2O5(float value)
    {
        HCl25_P2O5 = value;
    }
    private static float Jumlah = 0.00F;
    public static final float getJumlah()
    {
        if (Jumlah > 0)
            return Jumlah;
        else
            return 0;
    }
    public static final void setJumlah(float value)
    {
        Jumlah = value;
    }
    private static float K = 0.00F;
    public static final float getK()
    {
        if (K > 0)
            return K;
        else
            return 0;
    }
    public static final void setK(float value)
    {
        K = value;
    }
    private static float KB_adjusted = 0.00F;
    public static final float getKBAdjusted()
    {
        if (KB_adjusted > 0)
            return KB_adjusted;
        else
            return 0;
    }
    public static final void setKBAdjusted(float value)
    {
        KB_adjusted = value;
    }
    private static float KJELDAHL_N = 0.00F;
    public static final float getKjeldahlN()
    {
        if (KJELDAHL_N > 0)
            return KJELDAHL_N;
        else
            return 0;
    }
    public static final void setKjeldahlN(float value)
    {
        KJELDAHL_N = value;
    }
    private static float KTK = 0.00F;
    public static final float getKTK()
    {
        if (KTK > 0)
            return KTK;
        else
            return 0;
    }
    public static final void setKTK(float value)
    {
        KTK = value;
    }
    private static float Mg = 0.00F;
    public static final float getMg()
    {
        if (Mg > 0)
            return Mg;
        else
            return 0;
    }
    public static final void setMg(float value)
    {
        Mg = value;
    }
    private static float Morgan_K2O = 0.00F;
    public static final float getMorganK2O()
    {
        if (Morgan_K2O > 0)
            return Morgan_K2O;
        else
            return 0;
    }
    public static final void setMorganK2O(float value)
    {
        Morgan_K2O = value;
    }
    private static float Na = 0.00F;
    public static final float getNa()
    {
        if (Na > 0)
            return Na;
        else
            return 0;
    }
    public static final void setNa(float value)
    {
        Na = value;
    }
    private static float Olsen_P2O5 = 0.00F;
    public static final float getOlsenP2O5()
    {
        if(Olsen_P2O5 > 0)
            return Olsen_P2O5;
        else
            return 0;
    }
    public static final void setOlsenP2O5(float value)
    {
        Olsen_P2O5 = value;
    }
    private static float PH_H2O = 0.00F;
    public static final float getPhH2o()
    {
        if (PH_H2O > 0)
            return PH_H2O;
        else
            return 0;
    }
    public static final void setPhH2o(float value)
    {
        PH_H2O = value;
    }
    private static float PH_KCL = 0.00F;
    public static final float getPhKcl()
    {
        if(PH_KCL > 0)
            return PH_KCL;
        else
            return 0;
    }
    public static final void setPhKcl(float value)
    {
        PH_KCL = value;
    }
    private static float RetensiP = 0.00F;
    public static final float getRetensiP()
    {
        if (RetensiP > 0)
            return RetensiP;
        else
            return 0;
    }
    public static final void setRetensiP(float value)
    {
        RetensiP = value;
    }
    private static float SAND = 0.00F;
    public static final float getSAND()
    {
        if (SAND > 0)
            return SAND;
        else
            return 0;
    }
    public static final void setSAND(float value)
    {
        SAND = value;
    }
    private static float SILT = 0.00F;
    public static final float getSILT()
    {
        if (SILT > 0)
            return SILT;
        else
            return 0;
    }
    public static final void setSILT(float value)
    {
        SILT = value;
    }
    private static float WBC = 0.00F;
    public static final float getWBC()
    {
        if (WBC > 0)
            return WBC;
        else
            return 0;
    }
    public static final void setWBC(float value)
    {
        WBC = value;
    }

    // rekomendasi object
    private static String Urea;
    private static String Sp36;
    private static String Kcl;
    private static String Npk;
    private static String Urea15;
    private static String Komoditas="Padi";

    public static String getUrea() {
        return Urea;
    }

    public static void setUrea(String urea) {
        Urea = urea;
    }

    public static String getSp36() {
        return Sp36;
    }

    public static void setSp36(String sp36) {
        Sp36 = sp36;
    }

    public static String getKcl() {
        return Kcl;
    }

    public static void setKcl(String kcl) {
        Kcl = kcl;
    }

    public static String getNpk() {
        return Npk;
    }

    public static void setNpk(String npk) {
        Npk = npk;
    }

    public static String getUrea15() {
        return Urea15;
    }

    public static void setUrea15(String urea15) {
        Urea15 = urea15;
    }

    public static String getKomoditas() {
        return Komoditas;
    }

    public static void setKomoditas(String komoditas) {
        Komoditas = komoditas;
    }
}
