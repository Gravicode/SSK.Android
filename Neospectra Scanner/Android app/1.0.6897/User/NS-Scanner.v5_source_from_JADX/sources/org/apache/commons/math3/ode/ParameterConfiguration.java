package org.apache.commons.math3.ode;

import java.io.Serializable;

class ParameterConfiguration implements Serializable {
    private static final long serialVersionUID = 2247518849090889379L;

    /* renamed from: hP */
    private double f647hP;
    private String parameterName;

    ParameterConfiguration(String parameterName2, double hP) {
        this.parameterName = parameterName2;
        this.f647hP = hP;
    }

    public String getParameterName() {
        return this.parameterName;
    }

    public double getHP() {
        return this.f647hP;
    }

    public void setHP(double hParam) {
        this.f647hP = hParam;
    }
}
