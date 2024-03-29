package org.apache.commons.math3.optimization.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;

@Deprecated
public class LinearObjectiveFunction implements Serializable {
    private static final long serialVersionUID = -4531815507568396090L;
    private final transient RealVector coefficients;
    private final double constantTerm;

    public LinearObjectiveFunction(double[] coefficients2, double constantTerm2) {
        this((RealVector) new ArrayRealVector(coefficients2), constantTerm2);
    }

    public LinearObjectiveFunction(RealVector coefficients2, double constantTerm2) {
        this.coefficients = coefficients2;
        this.constantTerm = constantTerm2;
    }

    public RealVector getCoefficients() {
        return this.coefficients;
    }

    public double getConstantTerm() {
        return this.constantTerm;
    }

    public double getValue(double[] point) {
        return this.coefficients.dotProduct(new ArrayRealVector(point, false)) + this.constantTerm;
    }

    public double getValue(RealVector point) {
        return this.coefficients.dotProduct(point) + this.constantTerm;
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof LinearObjectiveFunction)) {
            return false;
        }
        LinearObjectiveFunction rhs = (LinearObjectiveFunction) other;
        if (this.constantTerm != rhs.constantTerm || !this.coefficients.equals(rhs.coefficients)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Double.valueOf(this.constantTerm).hashCode() ^ this.coefficients.hashCode();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        MatrixUtils.serializeRealVector(this.coefficients, oos);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        MatrixUtils.deserializeRealVector(this, "coefficients", ois);
    }
}
