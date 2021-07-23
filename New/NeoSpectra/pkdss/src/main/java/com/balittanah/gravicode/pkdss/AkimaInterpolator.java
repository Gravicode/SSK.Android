package com.balittanah.gravicode.pkdss;

import org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.util.Map;

public class AkimaInterpolator {
    private AkimaSplineInterpolator asi;
    private PolynomialSplineFunction psf;
    public AkimaInterpolator(double[] xDouble, double [] yDouble ){

        asi = new AkimaSplineInterpolator();
        psf = asi.interpolate(xDouble, yDouble);

        /*
        for (PolynomialFunction pf : psf.getPolynomials()) {
            System.out.println(pf.polynomialDerivative());
        }*/
    }

    public double getY(double X){
        return psf.value(X);
    }

}
