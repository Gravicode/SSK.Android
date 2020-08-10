package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.apache.commons.math3.util.FastMath;

public class SphericalCoordinates implements Serializable {
    private static final long serialVersionUID = 20130206;
    private double[][] jacobian;
    private final double phi;
    private double[][] phiHessian;

    /* renamed from: r */
    private final double f593r;
    private double[][] rHessian;
    private final double theta;
    private double[][] thetaHessian;

    /* renamed from: v */
    private final Vector3D f594v;

    private static class DataTransferObject implements Serializable {
        private static final long serialVersionUID = 20130206;

        /* renamed from: x */
        private final double f595x;

        /* renamed from: y */
        private final double f596y;

        /* renamed from: z */
        private final double f597z;

        DataTransferObject(double x, double y, double z) {
            this.f595x = x;
            this.f596y = y;
            this.f597z = z;
        }

        private Object readResolve() {
            Vector3D vector3D = new Vector3D(this.f595x, this.f596y, this.f597z);
            return new SphericalCoordinates(vector3D);
        }
    }

    public SphericalCoordinates(Vector3D v) {
        this.f594v = v;
        this.f593r = v.getNorm();
        this.theta = v.getAlpha();
        this.phi = FastMath.acos(v.getZ() / this.f593r);
    }

    public SphericalCoordinates(double r, double theta2, double phi2) {
        double d = r;
        double cosTheta = FastMath.cos(theta2);
        double sinTheta = FastMath.sin(theta2);
        double cosPhi = FastMath.cos(phi2);
        double sinPhi = FastMath.sin(phi2);
        this.f593r = d;
        this.theta = theta2;
        this.phi = phi2;
        Vector3D vector3D = r15;
        Vector3D vector3D2 = new Vector3D(d * cosTheta * sinPhi, d * sinTheta * sinPhi, d * cosPhi);
        this.f594v = vector3D2;
    }

    public Vector3D getCartesian() {
        return this.f594v;
    }

    public double getR() {
        return this.f593r;
    }

    public double getTheta() {
        return this.theta;
    }

    public double getPhi() {
        return this.phi;
    }

    public double[] toCartesianGradient(double[] sGradient) {
        computeJacobian();
        return new double[]{(sGradient[0] * this.jacobian[0][0]) + (sGradient[1] * this.jacobian[1][0]) + (sGradient[2] * this.jacobian[2][0]), (sGradient[0] * this.jacobian[0][1]) + (sGradient[1] * this.jacobian[1][1]) + (sGradient[2] * this.jacobian[2][1]), (sGradient[0] * this.jacobian[0][2]) + (sGradient[2] * this.jacobian[2][2])};
    }

    public double[][] toCartesianHessian(double[][] sHessian, double[] sGradient) {
        computeJacobian();
        computeHessians();
        double[][] hj = (double[][]) Array.newInstance(double.class, new int[]{3, 3});
        double[][] cHessian = (double[][]) Array.newInstance(double.class, new int[]{3, 3});
        hj[0][0] = (sHessian[0][0] * this.jacobian[0][0]) + (sHessian[1][0] * this.jacobian[1][0]) + (sHessian[2][0] * this.jacobian[2][0]);
        hj[0][1] = (sHessian[0][0] * this.jacobian[0][1]) + (sHessian[1][0] * this.jacobian[1][1]) + (sHessian[2][0] * this.jacobian[2][1]);
        hj[0][2] = (sHessian[0][0] * this.jacobian[0][2]) + (sHessian[2][0] * this.jacobian[2][2]);
        hj[1][0] = (sHessian[1][0] * this.jacobian[0][0]) + (sHessian[1][1] * this.jacobian[1][0]) + (sHessian[2][1] * this.jacobian[2][0]);
        hj[1][1] = (sHessian[1][0] * this.jacobian[0][1]) + (sHessian[1][1] * this.jacobian[1][1]) + (sHessian[2][1] * this.jacobian[2][1]);
        hj[2][0] = (sHessian[2][0] * this.jacobian[0][0]) + (sHessian[2][1] * this.jacobian[1][0]) + (sHessian[2][2] * this.jacobian[2][0]);
        hj[2][1] = (sHessian[2][0] * this.jacobian[0][1]) + (sHessian[2][1] * this.jacobian[1][1]) + (sHessian[2][2] * this.jacobian[2][1]);
        hj[2][2] = (sHessian[2][0] * this.jacobian[0][2]) + (sHessian[2][2] * this.jacobian[2][2]);
        cHessian[0][0] = (this.jacobian[0][0] * hj[0][0]) + (this.jacobian[1][0] * hj[1][0]) + (this.jacobian[2][0] * hj[2][0]);
        cHessian[1][0] = (this.jacobian[0][1] * hj[0][0]) + (this.jacobian[1][1] * hj[1][0]) + (this.jacobian[2][1] * hj[2][0]);
        cHessian[2][0] = (this.jacobian[0][2] * hj[0][0]) + (this.jacobian[2][2] * hj[2][0]);
        cHessian[1][1] = (this.jacobian[0][1] * hj[0][1]) + (this.jacobian[1][1] * hj[1][1]) + (this.jacobian[2][1] * hj[2][1]);
        cHessian[2][1] = (this.jacobian[0][2] * hj[0][1]) + (this.jacobian[2][2] * hj[2][1]);
        cHessian[2][2] = (this.jacobian[0][2] * hj[0][2]) + (this.jacobian[2][2] * hj[2][2]);
        double[] dArr = cHessian[0];
        dArr[0] = dArr[0] + (sGradient[0] * this.rHessian[0][0]) + (sGradient[1] * this.thetaHessian[0][0]) + (sGradient[2] * this.phiHessian[0][0]);
        double[] dArr2 = cHessian[1];
        dArr2[0] = dArr2[0] + (sGradient[0] * this.rHessian[1][0]) + (sGradient[1] * this.thetaHessian[1][0]) + (sGradient[2] * this.phiHessian[1][0]);
        double[] dArr3 = cHessian[2];
        dArr3[0] = dArr3[0] + (sGradient[0] * this.rHessian[2][0]) + (sGradient[2] * this.phiHessian[2][0]);
        double[] dArr4 = cHessian[1];
        dArr4[1] = dArr4[1] + (sGradient[0] * this.rHessian[1][1]) + (sGradient[1] * this.thetaHessian[1][1]) + (sGradient[2] * this.phiHessian[1][1]);
        double[] dArr5 = cHessian[2];
        dArr5[1] = dArr5[1] + (sGradient[0] * this.rHessian[2][1]) + (sGradient[2] * this.phiHessian[2][1]);
        double[] dArr6 = cHessian[2];
        dArr6[2] = dArr6[2] + (sGradient[0] * this.rHessian[2][2]) + (sGradient[2] * this.phiHessian[2][2]);
        cHessian[0][1] = cHessian[1][0];
        cHessian[0][2] = cHessian[2][0];
        cHessian[1][2] = cHessian[2][1];
        return cHessian;
    }

    private void computeJacobian() {
        if (this.jacobian == null) {
            double x = this.f594v.getX();
            double y = this.f594v.getY();
            double z = this.f594v.getZ();
            double rho2 = (x * x) + (y * y);
            double rho = FastMath.sqrt(rho2);
            double r2 = (z * z) + rho2;
            this.jacobian = (double[][]) Array.newInstance(double.class, new int[]{3, 3});
            double rho3 = rho;
            this.jacobian[0][0] = x / this.f593r;
            double rho4 = rho3;
            this.jacobian[0][1] = y / this.f593r;
            this.jacobian[0][2] = z / this.f593r;
            this.jacobian[1][0] = (-y) / rho2;
            this.jacobian[1][1] = x / rho2;
            this.jacobian[2][0] = (x * z) / (rho4 * r2);
            this.jacobian[2][1] = (y * z) / (rho4 * r2);
            double d = x;
            this.jacobian[2][2] = (-rho4) / r2;
        }
    }

    private void computeHessians() {
        if (this.rHessian == null) {
            double x = this.f594v.getX();
            double y = this.f594v.getY();
            double z = this.f594v.getZ();
            double x2 = x * x;
            double y2 = y * y;
            double z2 = z * z;
            double rho2 = x2 + y2;
            double rho = FastMath.sqrt(rho2);
            double r2 = rho2 + z2;
            double y22 = y2;
            double xOr = x / this.f593r;
            double x22 = x2;
            double yOr = y / this.f593r;
            double z22 = z2;
            double xOrho2 = x / rho2;
            double yOrho2 = y / rho2;
            double xOr3 = xOr / r2;
            double yOr3 = yOr / r2;
            double zOr3 = (z / this.f593r) / r2;
            double d = yOr;
            this.rHessian = (double[][]) Array.newInstance(double.class, new int[]{3, 3});
            this.rHessian[0][0] = (y * yOr3) + (z * zOr3);
            double d2 = xOr;
            this.rHessian[1][0] = (-x) * yOr3;
            this.rHessian[2][0] = (-z) * xOr3;
            this.rHessian[1][1] = (x * xOr3) + (z * zOr3);
            this.rHessian[2][1] = (-y) * zOr3;
            this.rHessian[2][2] = (x * xOr3) + (y * yOr3);
            this.rHessian[0][1] = this.rHessian[1][0];
            this.rHessian[0][2] = this.rHessian[2][0];
            this.rHessian[1][2] = this.rHessian[2][1];
            this.thetaHessian = (double[][]) Array.newInstance(double.class, new int[]{2, 2});
            this.thetaHessian[0][0] = xOrho2 * 2.0d * yOrho2;
            this.thetaHessian[1][0] = (yOrho2 * yOrho2) - (xOrho2 * xOrho2);
            this.thetaHessian[1][1] = -2.0d * xOrho2 * yOrho2;
            this.thetaHessian[0][1] = this.thetaHessian[1][0];
            double rhor2 = rho * r2;
            double rho2r2 = rho * rhor2;
            double rhor4 = rhor2 * r2;
            double rho3r4 = rhor4 * rho2;
            double r2P2rho2 = (3.0d * rho2) + z22;
            double d3 = rhor2;
            this.phiHessian = (double[][]) Array.newInstance(double.class, new int[]{3, 3});
            this.phiHessian[0][0] = ((rho2r2 - (x22 * r2P2rho2)) * z) / rho3r4;
            double rho2r22 = rho2r2;
            this.phiHessian[1][0] = ((((-x) * y) * z) * r2P2rho2) / rho3r4;
            this.phiHessian[2][0] = ((rho2 - z22) * x) / rhor4;
            this.phiHessian[1][1] = ((rho2r22 - (y22 * r2P2rho2)) * z) / rho3r4;
            this.phiHessian[2][1] = ((rho2 - z22) * y) / rhor4;
            double d4 = x;
            this.phiHessian[2][2] = ((2.0d * rho) * zOr3) / this.f593r;
            this.phiHessian[0][1] = this.phiHessian[1][0];
            this.phiHessian[0][2] = this.phiHessian[2][0];
            this.phiHessian[1][2] = this.phiHessian[2][1];
        }
    }

    private Object writeReplace() {
        DataTransferObject dataTransferObject = new DataTransferObject(this.f594v.getX(), this.f594v.getY(), this.f594v.getZ());
        return dataTransferObject;
    }
}
