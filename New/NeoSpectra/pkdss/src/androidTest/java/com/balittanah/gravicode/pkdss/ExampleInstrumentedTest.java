package com.balittanah.gravicode.pkdss;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.balittanah.gravicode.pkdss.helpers.FileHelpers;

import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.balittanah.gravicode.pkdss.test", appContext.getPackageName());

            Ini ini = null;
            try {
                InputStream inputStream = appContext.getAssets().open("config.ini");
                ini = new Ini(inputStream);
                java.util.prefs.Preferences prefs = new IniPreferences(ini);
                //System.out.println("grumpy/homePage: " + prefs.node("grumpy").get("homePage", null));

                String DataRekomendasi = ini.get("Config","DataRekomendasi");
                try {
                    double ureaConst = Double.parseDouble(ini.get("Config","Urea"));
                    double sp36Const = Double.parseDouble(ini.get("Config","SP36"));
                    double kclConst = Double.parseDouble(ini.get("Config","KCL"));
                    FertilizerCalculator calc = new FertilizerCalculator(appContext);
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

        public void print(PrintStream out) {
            out.println("Hello, World!");
        }

    }