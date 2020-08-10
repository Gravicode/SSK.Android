package org.apache.commons.math3.geometry.spherical.twod;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.geometry.Point;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

public class S2Point implements Point<Sphere2D> {
    public static final S2Point MINUS_I;
    public static final S2Point MINUS_J;
    public static final S2Point MINUS_K;
    public static final S2Point NaN;
    public static final S2Point PLUS_I;
    public static final S2Point PLUS_J;
    public static final S2Point PLUS_K;
    private static final long serialVersionUID = 20131218;
    private final double phi;
    private final double theta;
    private final Vector3D vector;

    static {
        S2Point s2Point = new S2Point(0.0d, 1.5707963267948966d, Vector3D.PLUS_I);
        PLUS_I = s2Point;
        S2Point s2Point2 = new S2Point(1.5707963267948966d, 1.5707963267948966d, Vector3D.PLUS_J);
        PLUS_J = s2Point2;
        S2Point s2Point3 = new S2Point(0.0d, 0.0d, Vector3D.PLUS_K);
        PLUS_K = s2Point3;
        S2Point s2Point4 = new S2Point(3.141592653589793d, 1.5707963267948966d, Vector3D.MINUS_I);
        MINUS_I = s2Point4;
        S2Point s2Point5 = new S2Point(4.71238898038469d, 1.5707963267948966d, Vector3D.MINUS_J);
        MINUS_J = s2Point5;
        S2Point s2Point6 = new S2Point(0.0d, 3.141592653589793d, Vector3D.MINUS_K);
        MINUS_K = s2Point6;
        S2Point s2Point7 = new S2Point(Double.NaN, Double.NaN, Vector3D.NaN);
        NaN = s2Point7;
    }

    public S2Point(double theta2, double phi2) throws OutOfRangeException {
        this(theta2, phi2, vector(theta2, phi2));
    }

    public S2Point(Vector3D vector2) throws MathArithmeticException {
        this(FastMath.atan2(vector2.getY(), vector2.getX()), Vector3D.angle(Vector3D.PLUS_K, vector2), vector2.normalize());
    }

    private S2Point(double theta2, double phi2, Vector3D vector2) {
        this.theta = theta2;
        this.phi = phi2;
        this.vector = vector2;
    }

    private static Vector3D vector(double theta2, double phi2) throws OutOfRangeException {
        if (phi2 < 0.0d || phi2 > 3.141592653589793d) {
            throw new OutOfRangeException(Double.valueOf(phi2), Integer.valueOf(0), Double.valueOf(3.141592653589793d));
        }
        double cosTheta = FastMath.cos(theta2);
        double sinTheta = FastMath.sin(theta2);
        double cosPhi = FastMath.cos(phi2);
        double sinPhi = FastMath.sin(phi2);
        Vector3D vector3D = new Vector3D(cosTheta * sinPhi, sinTheta * sinPhi, cosPhi);
        return vector3D;
    }

    public double getTheta() {
        return this.theta;
    }

    public double getPhi() {
        return this.phi;
    }

    public Vector3D getVector() {
        return this.vector;
    }

    public Space getSpace() {
        return Sphere2D.getInstance();
    }

    public boolean isNaN() {
        return Double.isNaN(this.theta) || Double.isNaN(this.phi);
    }

    public S2Point negate() {
        S2Point s2Point = new S2Point(-this.theta, 3.141592653589793d - this.phi, this.vector.negate());
        return s2Point;
    }

    public double distance(Point<Sphere2D> point) {
        return distance(this, (S2Point) point);
    }

    public static double distance(S2Point p1, S2Point p2) {
        return Vector3D.angle(p1.vector, p2.vector);
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof S2Point)) {
            return false;
        }
        S2Point rhs = (S2Point) other;
        if (rhs.isNaN()) {
            return isNaN();
        }
        if (!(this.theta == rhs.theta && this.phi == rhs.phi)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (isNaN()) {
            return 542;
        }
        return ((MathUtils.hash(this.theta) * 37) + MathUtils.hash(this.phi)) * ShapeTypes.FLOW_CHART_INPUT_OUTPUT;
    }
}
