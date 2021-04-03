package com.balittanah.gravicode.pkdss;
import com.balittanah.gravicode.pkdss.sgfilter.*;
import java.util.ArrayList;
import java.util.List;

public class Preprocess {
    public static double calculateMean(Double[] m) {
        double sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }

    public static double calculateSD(Double[] numArray)
    {
        double sum = 0.0, standardDeviation = 0.0;
        int length = numArray.length;
        for(double num : numArray) {
            sum += num;
        }
        double mean = sum/length;
        for(double num: numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

    public static double[] TreatSpec(double DataReflectance[]){
        //1. check length (spectral trimming)
        if (DataReflectance == null || DataReflectance.length != 154) return null;
        try {

            //2. convert to absorbance
            for (int col = 0; col < DataReflectance.length; col++)
            {
                DataReflectance[col] = (float)Math.log(1 / DataReflectance[col]);
            }

            //3.savitzky golay treatment
            double[] coef = SGFilter.computeSGCoefficients(5,5,2);
            SGFilter filter = new SGFilter(5,5);
            DataReflectance = filter.smooth(DataReflectance,coef);

            //4.snv treatment
            List<Double> rowDatas = new ArrayList<Double>();

            for (int col = 0; col < DataReflectance.length; col++)
            {
                rowDatas.add(DataReflectance[col]);
            }
            Double[] xx = rowDatas.toArray(new Double[rowDatas.size()]);
            double mean = calculateMean(xx);
            double stdDev = calculateSD(xx);
            for (int col = 0; col < DataReflectance.length; col++)
            {
                DataReflectance[col] = ((DataReflectance[col] - mean) / stdDev);
            }

        }catch (Exception exception){
            System.out.println("preprocessing error:"+exception.getMessage());
        }

        return DataReflectance;
    }
}


