package com.balittanah.gravicode.pkdss;

import org.junit.Test;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        //assertEquals(4, 2 + 2);

        String filename = "src/main/assets/config.ini";
        Ini ini = null;
        try {
            ini = new Ini(new File(filename));
            java.util.prefs.Preferences prefs = new IniPreferences(ini);
            //System.out.println("grumpy/homePage: " + prefs.node("grumpy").get("homePage", null));

            String DataRekomendasi = ini.get("Config","DataRekomendasi");
            try {
                double ureaConst = Double.parseDouble(ini.get("Config","Urea"));
                double sp36Const = Double.parseDouble(ini.get("Config","SP36"));
                double kclConst = Double.parseDouble(ini.get("Config","KCL"));
                FertilizerCalculator calc = new FertilizerCalculator(DataRekomendasi);
                String TxtUrea = String.valueOf(calc.GetFertilizerDoze(10, "Padi", "Urea")*ureaConst);
                String TxtSP36 = String.valueOf(calc.GetFertilizerDoze(10, "Padi", "SP36")*sp36Const);
                String TxtKCL = String.valueOf(calc.GetFertilizerDoze(10, "Padi", "KCL")*kclConst);
                System.out.println(String.format("Rekomendasi KCL : %1$s, SP36 : %2$s, Urea : %3$s", TxtKCL, TxtSP36, TxtUrea));

                FertilizerInfo x = calc.GetNPKDoze(10, 10, "Padi");

                System.out.println(String.format("Rekomendasi NPK 15:15:15 = %1$s",x.getNPK()));
                System.out.println(String.format("UREA 15:15:15 = %1$s",x.getUrea()));

            }catch (RuntimeException ex){
                System.out.println(ex);
            }
            /*
            String WorkingDirectory = ini.get("Config","WorkingDirectory");
            String ModelScript = ini.get("Config","ModelScript");
            String SensorData = ini.get("Config","SensorData");
            String AnacondaFolder = ini.get("Config","AnacondaFolder");
            Resources.PathToData = ini.get("Config","PathToData");
            double ureaConst = Double.parseDouble(ini.get("Config","Urea"));
            double sp36Const = Double.parseDouble(ini.get("Config","SP36"));
            double kclConst = Double.parseDouble(ini.get("Config","KCL"));

            ModelRunner ml = new ModelRunner(WorkingDirectory, ModelScript, SensorData, AnacondaFolder);

            InferenceResult hasil = ml.InferenceModel(false, true);
            if (hasil.getIsSucceed())
            {
                try
                {

                    System.out.println("start recommendation process");
                    FertilizerCalculator calc = new FertilizerCalculator(DataRekomendasi);
                    String TxtUrea = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getCN(), "Padi", "Urea")*ureaConst);
                    String TxtSP36 = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getHCl25P2O5(), "Padi", "SP36")*sp36Const);
                    String TxtKCL = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getHCl25K2O(), "Padi", "KCL")*kclConst);
                    System.out.println(String.format("Rekomendasi KCL : %1$s, SP36 : %2$s, Urea : %3$s", TxtKCL, TxtSP36, TxtUrea));

                    FertilizerInfo x = calc.GetNPKDoze(hasil.getModel().getHCl25P2O5(), hasil.getModel().getHCl25K2O(), "Padi");

                    System.out.println(String.format("Rekomendasi NPK 15:15:15 = %1$s",x.getNPK()));
                    System.out.println(String.format("UREA 15:15:15 = %1$s",x.getUrea()));

                }
                catch (RuntimeException ex)
                {
                    System.out.println(ex);
                }
            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void print(PrintStream out) {
        out.println("Hello, World!");
    }
}