package com.si_ware.neospectra;

import com.si_ware.neospectra.ml.WBC;
import com.si_ware.neospectra.ml.SAND;
import com.si_ware.neospectra.ml.SILT;
import com.si_ware.neospectra.ml.RetensiP;
import com.si_ware.neospectra.ml.PhKcl;
import com.si_ware.neospectra.ml.PhH2o;
import com.si_ware.neospectra.ml.OlsenP2o5;
import com.si_ware.neospectra.ml.Na;
import com.si_ware.neospectra.ml.MorganK2o;
import com.si_ware.neospectra.ml.Mg;
import com.si_ware.neospectra.ml.KTK;
import com.si_ware.neospectra.ml.KjeldahlN;
import com.si_ware.neospectra.ml.KbAdjusted;
import com.si_ware.neospectra.ml.K;
import com.si_ware.neospectra.ml.Jumlah;
import com.si_ware.neospectra.ml.Hcl25P2o5;
import com.si_ware.neospectra.ml.Hcl25K2o;
import com.si_ware.neospectra.ml.Ca;
import com.si_ware.neospectra.ml.CN;
import com.si_ware.neospectra.ml.CLAY;
import com.si_ware.neospectra.ml.Bray1P2o5;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import com.balittanah.gravicode.pkdss.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredictionEngine {

    public static ResultPrediction[] DoInference(double data[], android.content.Context context){
        List<ResultPrediction> hasil = new ArrayList<ResultPrediction>();
        UnsurModel result = new UnsurModel();
        //pre-process data
        data = Preprocess.TreatSpec(data);
        //convert to float for inference
        float[] dataF = new float[data.length];
        for (int i = 0 ; i < data.length; i++)
        {
            dataF[i] = (float) data[i];
        }
        //wbc
        try {
            WBC model = WBC.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
            }, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            WBC.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setWBC(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("WBC",result.getWBC()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("wbc:"+result.getWBC());
        } catch (IOException e) {
            System.out.println("inference wbc error:"+e.getMessage());
            // TODO Handle the exception
        }
        //silt
        try {
            SILT model = SILT.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            SILT.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setSILT(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("SILT",result.getSILT()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("silt:"+result.getSILT());
        } catch (IOException e) {
            System.out.println("inference silt error:"+e.getMessage());
            // TODO Handle the exception
        }
        //sand
        try {
            SAND model = SAND.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            SAND.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setSAND(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("SAND",result.getSAND()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("sand:"+result.getSAND());
        } catch (IOException e) {
            System.out.println("inference sand error:"+e.getMessage());
            // TODO Handle the exception
        }
        //retensi p
        try {
            RetensiP model = RetensiP.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            RetensiP.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setRetensiP(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("RetensiP",result.getRetensiP()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("retensip:"+result.getRetensiP());
        } catch (IOException e) {
            System.out.println("inference retensip error:"+e.getMessage());
            // TODO Handle the exception
        }
        //PhKcl
        try {
            PhKcl model = PhKcl.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            PhKcl.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setPhKcl(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("PH_KCL",result.getPhKcl()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("PhKcl:"+result.getPhKcl());
        } catch (IOException e) {
            System.out.println("inference PhKcl error:"+e.getMessage());
            // TODO Handle the exception
        }
        //PhH2o
        try {
            PhH2o model = PhH2o.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            PhH2o.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setPhH2o(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("PH_H2O",result.getPhH2o()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("PhH2o:"+result.getPhH2o());
        } catch (IOException e) {
            System.out.println("inference PhH2o error:"+e.getMessage());
            // TODO Handle the exception
        }
        //OlsenP2o5
        try {
            OlsenP2o5 model = OlsenP2o5.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            OlsenP2o5.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setOlsenP2O5(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Olsen_P2O5",result.getOlsenP2O5()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("OlsenP2o5:"+result.getOlsenP2O5());
        } catch (IOException e) {
            System.out.println("inference OlsenP2o5 error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Na
        try {
            Na model = Na.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Na.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setNa(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Na",result.getNa()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Na:"+result.getNa());
        } catch (IOException e) {
            System.out.println("inference Na error:"+e.getMessage());
            // TODO Handle the exception
        }
        //MorganK2o
        try {
            MorganK2o model = MorganK2o.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            MorganK2o.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setMorganK2O(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Morgan_K2O",result.getMorganK2O()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("MorganK2o:"+result.getMorganK2O());
        } catch (IOException e) {
            System.out.println("inference MorganK2o error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Mg
        try {
            Mg model = Mg.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Mg.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setMg(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Mg",result.getMg()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Mg:"+result.getMg());
        } catch (IOException e) {
            System.out.println("inference Mg error:"+e.getMessage());
            // TODO Handle the exception
        }
        //KTK
        try {
            KTK model = KTK.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            KTK.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setKTK(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("KTK",result.getKTK()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("KTK:"+result.getKTK());
        } catch (IOException e) {
            System.out.println("inference KTK error:"+e.getMessage());
            // TODO Handle the exception
        }
        //KjeldahlN
        try {
            KjeldahlN model = KjeldahlN.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            KjeldahlN.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setKjeldahlN(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("KJELDAHL_N",result.getKjeldahlN()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("KjeldahlN:"+result.getKjeldahlN());
        } catch (IOException e) {
            System.out.println("inference KjeldahlN error:"+e.getMessage());
            // TODO Handle the exception
        }
        //KbAdjusted
        try {
            KbAdjusted model = KbAdjusted.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            KbAdjusted.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setKBAdjusted(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("KB_adjusted",result.getKBAdjusted()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("KbAdjusted:"+result.getKBAdjusted());
        } catch (IOException e) {
            System.out.println("inference KbAdjusted error:"+e.getMessage());
            // TODO Handle the exception
        }
        //K
        try {
            K model = K.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            K.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setK(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("K",result.getK()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("K:"+result.getK());
        } catch (IOException e) {
            System.out.println("inference K error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Jumlah
        try {
            Jumlah model = Jumlah.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Jumlah.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setJumlah(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Jumlah",result.getJumlah()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Jumlah:"+result.getJumlah());
        } catch (IOException e) {
            System.out.println("inference Jumlah error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Hcl25P2o5
        try {
            Hcl25P2o5 model = Hcl25P2o5.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Hcl25P2o5.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setHCl25P2O5(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("HCl25_P2O5",result.getHCl25P2O5()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Hcl25P2o5:"+result.getHCl25P2O5());
        } catch (IOException e) {
            System.out.println("inference Hcl25P2o5 error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Hcl25K2o
        try {
            Hcl25K2o model = Hcl25K2o.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Hcl25K2o.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setHCl25K2O(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("HCl25_K2O",result.getHCl25K2O()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Hcl25K2o:"+result.getHCl25K2O());
        } catch (IOException e) {
            System.out.println("inference Hcl25K2o error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Ca
        try {
            Ca model = Ca.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Ca.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setCa(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Ca",result.getCa()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Ca:"+result.getCa());
        } catch (IOException e) {
            System.out.println("inference Ca error:"+e.getMessage());
            // TODO Handle the exception
        }
        //CN
        try {
            CN model = CN.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            CN.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setCN(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("C_N",result.getCN()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("CN:"+result.getCN());
        } catch (IOException e) {
            System.out.println("inference CN error:"+e.getMessage());
            // TODO Handle the exception
        }
        //CLAY
        try {
            CLAY model = CLAY.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            CLAY.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setCLAY(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("CLAY",result.getCLAY()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("CLAY:"+result.getCLAY());
        } catch (IOException e) {
            System.out.println("inference CLAY error:"+e.getMessage());
            // TODO Handle the exception
        }
        //Bray1P2o5
        try {
            Bray1P2o5 model = Bray1P2o5.newInstance(context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 154
}, DataType.FLOAT32);
            inputFeature0.loadArray(dataF);

            // Runs model inference and gets result.
            Bray1P2o5.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            result.setBray1P2O5(outputFeature0.getFloatValue(0));
            hasil.add(new ResultPrediction("Bray1_P2O5",result.getBray1P2O5()));
            // Releases model resources if no longer used.
            model.close();
            System.out.println("Bray1P2o5:"+result.getBray1P2O5());
        } catch (IOException e) {
            System.out.println("inference Bray1P2o5 error:"+e.getMessage());
            // TODO Handle the exception
        }
        ResultPrediction[] tempResult = hasil.toArray(new ResultPrediction[0]);
        return tempResult;
    }
}
