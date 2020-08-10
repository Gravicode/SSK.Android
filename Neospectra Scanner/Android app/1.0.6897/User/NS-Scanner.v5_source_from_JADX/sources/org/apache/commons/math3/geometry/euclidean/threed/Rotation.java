package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

public class Rotation implements Serializable {
    public static final Rotation IDENTITY;
    private static final long serialVersionUID = -2153622329907944313L;

    /* renamed from: q0 */
    private final double f586q0;

    /* renamed from: q1 */
    private final double f587q1;

    /* renamed from: q2 */
    private final double f588q2;

    /* renamed from: q3 */
    private final double f589q3;

    static {
        Rotation rotation = new Rotation(1.0d, 0.0d, 0.0d, 0.0d, false);
        IDENTITY = rotation;
    }

    public Rotation(double q0, double q1, double q2, double q3, boolean needsNormalization) {
        if (needsNormalization) {
            double inv = 1.0d / FastMath.sqrt((((q0 * q0) + (q1 * q1)) + (q2 * q2)) + (q3 * q3));
            q0 *= inv;
            q1 *= inv;
            q2 *= inv;
            q3 *= inv;
        }
        this.f586q0 = q0;
        this.f587q1 = q1;
        this.f588q2 = q2;
        this.f589q3 = q3;
    }

    @Deprecated
    public Rotation(Vector3D axis, double angle) throws MathIllegalArgumentException {
        this(axis, angle, RotationConvention.VECTOR_OPERATOR);
    }

    public Rotation(Vector3D axis, double angle, RotationConvention convention) throws MathIllegalArgumentException {
        double norm = axis.getNorm();
        if (norm == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
        }
        double halfAngle = (convention == RotationConvention.VECTOR_OPERATOR ? -0.5d : 0.5d) * angle;
        double coeff = FastMath.sin(halfAngle) / norm;
        this.f586q0 = FastMath.cos(halfAngle);
        this.f587q1 = axis.getX() * coeff;
        this.f588q2 = axis.getY() * coeff;
        this.f589q3 = axis.getZ() * coeff;
    }

    public Rotation(double[][] m, double threshold) throws NotARotationMatrixException {
        double[][] dArr = m;
        if (dArr.length == 3 && dArr[0].length == 3 && dArr[1].length == 3 && dArr[2].length == 3) {
            double[][] ort = orthogonalizeMatrix(m, threshold);
            double det = ((ort[0][0] * ((ort[1][1] * ort[2][2]) - (ort[2][1] * ort[1][2]))) - (ort[1][0] * ((ort[0][1] * ort[2][2]) - (ort[2][1] * ort[0][2])))) + (ort[2][0] * ((ort[0][1] * ort[1][2]) - (ort[1][1] * ort[0][2])));
            if (det < 0.0d) {
                throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, Double.valueOf(det));
            }
            double[] quat = mat2quat(ort);
            this.f586q0 = quat[0];
            this.f587q1 = quat[1];
            this.f588q2 = quat[2];
            this.f589q3 = quat[3];
            return;
        }
        throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, Integer.valueOf(dArr.length), Integer.valueOf(dArr[0].length));
    }

    public Rotation(Vector3D u1, Vector3D u2, Vector3D v1, Vector3D v2) throws MathArithmeticException {
        Vector3D u3 = u1.crossProduct(u2).normalize();
        Vector3D u22 = u3.crossProduct(u1).normalize();
        Vector3D u12 = u1.normalize();
        Vector3D v3 = v1.crossProduct(v2).normalize();
        Vector3D v22 = v3.crossProduct(v1).normalize();
        Vector3D v12 = v1.normalize();
        double[] quat = mat2quat(new double[][]{new double[]{MathArrays.linearCombination(u12.getX(), v12.getX(), u22.getX(), v22.getX(), u3.getX(), v3.getX()), MathArrays.linearCombination(u12.getY(), v12.getX(), u22.getY(), v22.getX(), u3.getY(), v3.getX()), MathArrays.linearCombination(u12.getZ(), v12.getX(), u22.getZ(), v22.getX(), u3.getZ(), v3.getX())}, new double[]{MathArrays.linearCombination(u12.getX(), v12.getY(), u22.getX(), v22.getY(), u3.getX(), v3.getY()), MathArrays.linearCombination(u12.getY(), v12.getY(), u22.getY(), v22.getY(), u3.getY(), v3.getY()), MathArrays.linearCombination(u12.getZ(), v12.getY(), u22.getZ(), v22.getY(), u3.getZ(), v3.getY())}, new double[]{MathArrays.linearCombination(u12.getX(), v12.getZ(), u22.getX(), v22.getZ(), u3.getX(), v3.getZ()), MathArrays.linearCombination(u12.getY(), v12.getZ(), u22.getY(), v22.getZ(), u3.getY(), v3.getZ()), MathArrays.linearCombination(u12.getZ(), v12.getZ(), u22.getZ(), v22.getZ(), u3.getZ(), v3.getZ())}});
        this.f586q0 = quat[0];
        this.f587q1 = quat[1];
        this.f588q2 = quat[2];
        this.f589q3 = quat[3];
    }

    public Rotation(Vector3D u, Vector3D v) throws MathArithmeticException {
        double normProduct = u.getNorm() * v.getNorm();
        if (normProduct == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
        }
        double dot = u.dotProduct(v);
        if (dot < -0.999999999999998d * normProduct) {
            Vector3D w = u.orthogonal();
            this.f586q0 = 0.0d;
            this.f587q1 = -w.getX();
            this.f588q2 = -w.getY();
            this.f589q3 = -w.getZ();
            return;
        }
        this.f586q0 = FastMath.sqrt(((dot / normProduct) + 1.0d) * 0.5d);
        double coeff = 1.0d / ((this.f586q0 * 2.0d) * normProduct);
        Vector3D q = v.crossProduct(u);
        this.f587q1 = q.getX() * coeff;
        this.f588q2 = q.getY() * coeff;
        this.f589q3 = q.getZ() * coeff;
    }

    @Deprecated
    public Rotation(RotationOrder order, double alpha1, double alpha2, double alpha3) {
        this(order, RotationConvention.VECTOR_OPERATOR, alpha1, alpha2, alpha3);
    }

    public Rotation(RotationOrder order, RotationConvention convention, double alpha1, double alpha2, double alpha3) {
        Rotation composed = new Rotation(order.getA1(), alpha1, convention).compose(new Rotation(order.getA2(), alpha2, convention).compose(new Rotation(order.getA3(), alpha3, convention), convention), convention);
        this.f586q0 = composed.f586q0;
        this.f587q1 = composed.f587q1;
        this.f588q2 = composed.f588q2;
        this.f589q3 = composed.f589q3;
    }

    private static double[] mat2quat(double[][] ort) {
        double[] quat = new double[4];
        double s = ort[0][0] + ort[1][1] + ort[2][2];
        if (s > -0.19d) {
            quat[0] = FastMath.sqrt(1.0d + s) * 0.5d;
            double inv = 0.25d / quat[0];
            quat[1] = (ort[1][2] - ort[2][1]) * inv;
            quat[2] = (ort[2][0] - ort[0][2]) * inv;
            quat[3] = (ort[0][1] - ort[1][0]) * inv;
        } else {
            double s2 = (ort[0][0] - ort[1][1]) - ort[2][2];
            if (s2 > -0.19d) {
                quat[1] = FastMath.sqrt(1.0d + s2) * 0.5d;
                double inv2 = 0.25d / quat[1];
                quat[0] = (ort[1][2] - ort[2][1]) * inv2;
                quat[2] = (ort[0][1] + ort[1][0]) * inv2;
                quat[3] = (ort[0][2] + ort[2][0]) * inv2;
            } else {
                double s3 = (ort[1][1] - ort[0][0]) - ort[2][2];
                if (s3 > -0.19d) {
                    quat[2] = FastMath.sqrt(1.0d + s3) * 0.5d;
                    double inv3 = 0.25d / quat[2];
                    quat[0] = (ort[2][0] - ort[0][2]) * inv3;
                    quat[1] = (ort[0][1] + ort[1][0]) * inv3;
                    quat[3] = (ort[2][1] + ort[1][2]) * inv3;
                } else {
                    quat[3] = FastMath.sqrt(1.0d + ((ort[2][2] - ort[0][0]) - ort[1][1])) * 0.5d;
                    double inv4 = 0.25d / quat[3];
                    quat[0] = (ort[0][1] - ort[1][0]) * inv4;
                    quat[1] = (ort[0][2] + ort[2][0]) * inv4;
                    quat[2] = (ort[2][1] + ort[1][2]) * inv4;
                }
            }
        }
        return quat;
    }

    public Rotation revert() {
        Rotation rotation = new Rotation(-this.f586q0, this.f587q1, this.f588q2, this.f589q3, false);
        return rotation;
    }

    public double getQ0() {
        return this.f586q0;
    }

    public double getQ1() {
        return this.f587q1;
    }

    public double getQ2() {
        return this.f588q2;
    }

    public double getQ3() {
        return this.f589q3;
    }

    @Deprecated
    public Vector3D getAxis() {
        return getAxis(RotationConvention.VECTOR_OPERATOR);
    }

    public Vector3D getAxis(RotationConvention convention) {
        double squaredSine = (this.f587q1 * this.f587q1) + (this.f588q2 * this.f588q2) + (this.f589q3 * this.f589q3);
        if (squaredSine == 0.0d) {
            return convention == RotationConvention.VECTOR_OPERATOR ? Vector3D.PLUS_I : Vector3D.MINUS_I;
        }
        double sgn = convention == RotationConvention.VECTOR_OPERATOR ? 1.0d : -1.0d;
        if (this.f586q0 < 0.0d) {
            double inverse = sgn / FastMath.sqrt(squaredSine);
            Vector3D vector3D = new Vector3D(this.f587q1 * inverse, this.f588q2 * inverse, this.f589q3 * inverse);
            return vector3D;
        }
        double inverse2 = (-sgn) / FastMath.sqrt(squaredSine);
        Vector3D vector3D2 = new Vector3D(this.f587q1 * inverse2, this.f588q2 * inverse2, this.f589q3 * inverse2);
        return vector3D2;
    }

    public double getAngle() {
        if (this.f586q0 < -0.1d || this.f586q0 > 0.1d) {
            return FastMath.asin(FastMath.sqrt((this.f587q1 * this.f587q1) + (this.f588q2 * this.f588q2) + (this.f589q3 * this.f589q3))) * 2.0d;
        }
        if (this.f586q0 < 0.0d) {
            return FastMath.acos(-this.f586q0) * 2.0d;
        }
        return FastMath.acos(this.f586q0) * 2.0d;
    }

    @Deprecated
    public double[] getAngles(RotationOrder order) throws CardanEulerSingularityException {
        return getAngles(order, RotationConvention.VECTOR_OPERATOR);
    }

    public double[] getAngles(RotationOrder order, RotationConvention convention) throws CardanEulerSingularityException {
        if (convention == RotationConvention.VECTOR_OPERATOR) {
            if (order == RotationOrder.XYZ) {
                Vector3D v1 = applyTo(Vector3D.PLUS_K);
                Vector3D v2 = applyInverseTo(Vector3D.PLUS_I);
                if (v2.getZ() < -0.9999999999d || v2.getZ() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(-v1.getY(), v1.getZ()), FastMath.asin(v2.getZ()), FastMath.atan2(-v2.getY(), v2.getX())};
            } else if (order == RotationOrder.XZY) {
                Vector3D v12 = applyTo(Vector3D.PLUS_J);
                Vector3D v22 = applyInverseTo(Vector3D.PLUS_I);
                if (v22.getY() < -0.9999999999d || v22.getY() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(v12.getZ(), v12.getY()), -FastMath.asin(v22.getY()), FastMath.atan2(v22.getZ(), v22.getX())};
            } else if (order == RotationOrder.YXZ) {
                Vector3D v13 = applyTo(Vector3D.PLUS_K);
                Vector3D v23 = applyInverseTo(Vector3D.PLUS_J);
                if (v23.getZ() < -0.9999999999d || v23.getZ() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(v13.getX(), v13.getZ()), -FastMath.asin(v23.getZ()), FastMath.atan2(v23.getX(), v23.getY())};
            } else if (order == RotationOrder.YZX) {
                Vector3D v14 = applyTo(Vector3D.PLUS_I);
                Vector3D v24 = applyInverseTo(Vector3D.PLUS_J);
                if (v24.getX() < -0.9999999999d || v24.getX() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(-v14.getZ(), v14.getX()), FastMath.asin(v24.getX()), FastMath.atan2(-v24.getZ(), v24.getY())};
            } else if (order == RotationOrder.ZXY) {
                Vector3D v15 = applyTo(Vector3D.PLUS_J);
                Vector3D v25 = applyInverseTo(Vector3D.PLUS_K);
                if (v25.getY() < -0.9999999999d || v25.getY() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(-v15.getX(), v15.getY()), FastMath.asin(v25.getY()), FastMath.atan2(-v25.getX(), v25.getZ())};
            } else if (order == RotationOrder.ZYX) {
                Vector3D v16 = applyTo(Vector3D.PLUS_I);
                Vector3D v26 = applyInverseTo(Vector3D.PLUS_K);
                if (v26.getX() < -0.9999999999d || v26.getX() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(true);
                }
                return new double[]{FastMath.atan2(v16.getY(), v16.getX()), -FastMath.asin(v26.getX()), FastMath.atan2(v26.getY(), v26.getZ())};
            } else if (order == RotationOrder.XYX) {
                Vector3D v17 = applyTo(Vector3D.PLUS_I);
                Vector3D v27 = applyInverseTo(Vector3D.PLUS_I);
                if (v27.getX() < -0.9999999999d || v27.getX() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v17.getY(), -v17.getZ()), FastMath.acos(v27.getX()), FastMath.atan2(v27.getY(), v27.getZ())};
            } else if (order == RotationOrder.XZX) {
                Vector3D v18 = applyTo(Vector3D.PLUS_I);
                Vector3D v28 = applyInverseTo(Vector3D.PLUS_I);
                if (v28.getX() < -0.9999999999d || v28.getX() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v18.getZ(), v18.getY()), FastMath.acos(v28.getX()), FastMath.atan2(v28.getZ(), -v28.getY())};
            } else if (order == RotationOrder.YXY) {
                Vector3D v19 = applyTo(Vector3D.PLUS_J);
                Vector3D v29 = applyInverseTo(Vector3D.PLUS_J);
                if (v29.getY() < -0.9999999999d || v29.getY() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v19.getX(), v19.getZ()), FastMath.acos(v29.getY()), FastMath.atan2(v29.getX(), -v29.getZ())};
            } else if (order == RotationOrder.YZY) {
                Vector3D v110 = applyTo(Vector3D.PLUS_J);
                Vector3D v210 = applyInverseTo(Vector3D.PLUS_J);
                if (v210.getY() < -0.9999999999d || v210.getY() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v110.getZ(), -v110.getX()), FastMath.acos(v210.getY()), FastMath.atan2(v210.getZ(), v210.getX())};
            } else if (order == RotationOrder.ZXZ) {
                Vector3D v111 = applyTo(Vector3D.PLUS_K);
                Vector3D v211 = applyInverseTo(Vector3D.PLUS_K);
                if (v211.getZ() < -0.9999999999d || v211.getZ() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v111.getX(), -v111.getY()), FastMath.acos(v211.getZ()), FastMath.atan2(v211.getX(), v211.getY())};
            } else {
                Vector3D v112 = applyTo(Vector3D.PLUS_K);
                Vector3D v212 = applyInverseTo(Vector3D.PLUS_K);
                if (v212.getZ() < -0.9999999999d || v212.getZ() > 0.9999999999d) {
                    throw new CardanEulerSingularityException(false);
                }
                return new double[]{FastMath.atan2(v112.getY(), v112.getX()), FastMath.acos(v212.getZ()), FastMath.atan2(v212.getY(), -v212.getX())};
            }
        } else if (order == RotationOrder.XYZ) {
            Vector3D v113 = applyTo(Vector3D.PLUS_I);
            Vector3D v213 = applyInverseTo(Vector3D.PLUS_K);
            if (v213.getX() < -0.9999999999d || v213.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v213.getY(), v213.getZ()), FastMath.asin(v213.getX()), FastMath.atan2(-v113.getY(), v113.getX())};
        } else if (order == RotationOrder.XZY) {
            Vector3D v114 = applyTo(Vector3D.PLUS_I);
            Vector3D v214 = applyInverseTo(Vector3D.PLUS_J);
            if (v214.getX() < -0.9999999999d || v214.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v214.getZ(), v214.getY()), -FastMath.asin(v214.getX()), FastMath.atan2(v114.getZ(), v114.getX())};
        } else if (order == RotationOrder.YXZ) {
            Vector3D v115 = applyTo(Vector3D.PLUS_J);
            Vector3D v215 = applyInverseTo(Vector3D.PLUS_K);
            if (v215.getY() < -0.9999999999d || v215.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v215.getX(), v215.getZ()), -FastMath.asin(v215.getY()), FastMath.atan2(v115.getX(), v115.getY())};
        } else if (order == RotationOrder.YZX) {
            Vector3D v116 = applyTo(Vector3D.PLUS_J);
            Vector3D v216 = applyInverseTo(Vector3D.PLUS_I);
            if (v216.getY() < -0.9999999999d || v216.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v216.getZ(), v216.getX()), FastMath.asin(v216.getY()), FastMath.atan2(-v116.getZ(), v116.getY())};
        } else if (order == RotationOrder.ZXY) {
            Vector3D v117 = applyTo(Vector3D.PLUS_K);
            Vector3D v217 = applyInverseTo(Vector3D.PLUS_J);
            if (v217.getZ() < -0.9999999999d || v217.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(-v217.getX(), v217.getY()), FastMath.asin(v217.getZ()), FastMath.atan2(-v117.getX(), v117.getZ())};
        } else if (order == RotationOrder.ZYX) {
            Vector3D v118 = applyTo(Vector3D.PLUS_K);
            Vector3D v218 = applyInverseTo(Vector3D.PLUS_I);
            if (v218.getZ() < -0.9999999999d || v218.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(true);
            }
            return new double[]{FastMath.atan2(v218.getY(), v218.getX()), -FastMath.asin(v218.getZ()), FastMath.atan2(v118.getY(), v118.getZ())};
        } else if (order == RotationOrder.XYX) {
            Vector3D v119 = applyTo(Vector3D.PLUS_I);
            Vector3D v219 = applyInverseTo(Vector3D.PLUS_I);
            if (v219.getX() < -0.9999999999d || v219.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v219.getY(), -v219.getZ()), FastMath.acos(v219.getX()), FastMath.atan2(v119.getY(), v119.getZ())};
        } else if (order == RotationOrder.XZX) {
            Vector3D v120 = applyTo(Vector3D.PLUS_I);
            Vector3D v220 = applyInverseTo(Vector3D.PLUS_I);
            if (v220.getX() < -0.9999999999d || v220.getX() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v220.getZ(), v220.getY()), FastMath.acos(v220.getX()), FastMath.atan2(v120.getZ(), -v120.getY())};
        } else if (order == RotationOrder.YXY) {
            Vector3D v121 = applyTo(Vector3D.PLUS_J);
            Vector3D v221 = applyInverseTo(Vector3D.PLUS_J);
            if (v221.getY() < -0.9999999999d || v221.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v221.getX(), v221.getZ()), FastMath.acos(v221.getY()), FastMath.atan2(v121.getX(), -v121.getZ())};
        } else if (order == RotationOrder.YZY) {
            Vector3D v122 = applyTo(Vector3D.PLUS_J);
            Vector3D v222 = applyInverseTo(Vector3D.PLUS_J);
            if (v222.getY() < -0.9999999999d || v222.getY() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v222.getZ(), -v222.getX()), FastMath.acos(v222.getY()), FastMath.atan2(v122.getZ(), v122.getX())};
        } else if (order == RotationOrder.ZXZ) {
            Vector3D v123 = applyTo(Vector3D.PLUS_K);
            Vector3D v223 = applyInverseTo(Vector3D.PLUS_K);
            if (v223.getZ() < -0.9999999999d || v223.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v223.getX(), -v223.getY()), FastMath.acos(v223.getZ()), FastMath.atan2(v123.getX(), v123.getY())};
        } else {
            Vector3D v124 = applyTo(Vector3D.PLUS_K);
            Vector3D v224 = applyInverseTo(Vector3D.PLUS_K);
            if (v224.getZ() < -0.9999999999d || v224.getZ() > 0.9999999999d) {
                throw new CardanEulerSingularityException(false);
            }
            return new double[]{FastMath.atan2(v224.getY(), v224.getX()), FastMath.acos(v224.getZ()), FastMath.atan2(v124.getY(), -v124.getX())};
        }
    }

    public double[][] getMatrix() {
        double q0q0 = this.f586q0 * this.f586q0;
        double q0q3 = this.f586q0 * this.f589q3;
        double q1q2 = this.f587q1 * this.f588q2;
        double q0q1 = this.f586q0 * this.f587q1;
        double q1q3 = this.f587q1 * this.f589q3;
        double q0q2 = this.f586q0 * this.f588q2;
        double q2q2 = this.f588q2 * this.f588q2;
        double q2q3 = this.f588q2 * this.f589q3;
        double q3q3 = this.f589q3 * this.f589q3;
        double[][] m = {new double[3], new double[3], new double[3]};
        m[0][0] = ((q0q0 + (this.f587q1 * this.f587q1)) * 2.0d) - 1.0d;
        m[1][0] = (q1q2 - q0q3) * 2.0d;
        m[2][0] = (q1q3 + q0q2) * 2.0d;
        m[0][1] = (q1q2 + q0q3) * 2.0d;
        m[1][1] = ((q0q0 + q2q2) * 2.0d) - 1.0d;
        m[2][1] = (q2q3 - q0q1) * 2.0d;
        m[0][2] = (q1q3 - q0q2) * 2.0d;
        m[1][2] = (q2q3 + q0q1) * 2.0d;
        m[2][2] = ((q0q0 + q3q3) * 2.0d) - 1.0d;
        return m;
    }

    public Vector3D applyTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = (this.f587q1 * x) + (this.f588q2 * y) + (this.f589q3 * z);
        double z2 = z;
        double d = y;
        Vector3D vector3D = new Vector3D((((this.f586q0 * ((this.f586q0 * x) - ((this.f588q2 * z) - (this.f589q3 * y)))) + (this.f587q1 * s)) * 2.0d) - x, (((this.f586q0 * ((this.f586q0 * y) - ((this.f589q3 * x) - (this.f587q1 * z2)))) + (this.f588q2 * s)) * 2.0d) - y, (((this.f586q0 * ((this.f586q0 * z2) - ((this.f587q1 * y) - (this.f588q2 * x)))) + (this.f589q3 * s)) * 2.0d) - z2);
        return vector3D;
    }

    public void applyTo(double[] in, double[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        double s = (this.f587q1 * x) + (this.f588q2 * y) + (this.f589q3 * z);
        double y2 = y;
        double x2 = x;
        double z2 = z;
        out[0] = (((this.f586q0 * ((this.f586q0 * x) - ((this.f588q2 * z) - (this.f589q3 * y2)))) + (this.f587q1 * s)) * 2.0d) - x2;
        out[1] = (((this.f586q0 * ((this.f586q0 * y2) - ((this.f589q3 * x2) - (this.f587q1 * z2)))) + (this.f588q2 * s)) * 2.0d) - y2;
        out[2] = (((this.f586q0 * ((z2 * this.f586q0) - ((this.f587q1 * y2) - (this.f588q2 * x2)))) + (this.f589q3 * s)) * 2.0d) - z2;
    }

    public Vector3D applyInverseTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double s = (this.f587q1 * x) + (this.f588q2 * y) + (this.f589q3 * z);
        double m0 = -this.f586q0;
        double z2 = z;
        double d = y;
        Vector3D vector3D = new Vector3D((((((x * m0) - ((this.f588q2 * z) - (this.f589q3 * y))) * m0) + (this.f587q1 * s)) * 2.0d) - x, (((((y * m0) - ((this.f589q3 * x) - (this.f587q1 * z2))) * m0) + (this.f588q2 * s)) * 2.0d) - y, (((((z2 * m0) - ((this.f587q1 * y) - (this.f588q2 * x))) * m0) + (this.f589q3 * s)) * 2.0d) - z2);
        return vector3D;
    }

    public void applyInverseTo(double[] in, double[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        double s = (this.f587q1 * x) + (this.f588q2 * y) + (this.f589q3 * z);
        double m0 = -this.f586q0;
        double x2 = x;
        double z2 = z;
        out[0] = (((((x * m0) - ((this.f588q2 * z) - (this.f589q3 * y))) * m0) + (this.f587q1 * s)) * 2.0d) - x2;
        double y2 = y;
        out[1] = (((((y * m0) - ((this.f589q3 * x2) - (this.f587q1 * z2))) * m0) + (this.f588q2 * s)) * 2.0d) - y2;
        out[2] = (((((z2 * m0) - ((this.f587q1 * y2) - (this.f588q2 * x2))) * m0) + (this.f589q3 * s)) * 2.0d) - z2;
    }

    public Rotation applyTo(Rotation r) {
        return compose(r, RotationConvention.VECTOR_OPERATOR);
    }

    public Rotation compose(Rotation r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInternal(r) : r.composeInternal(this);
    }

    private Rotation composeInternal(Rotation r) {
        Rotation rotation = r;
        Rotation rotation2 = new Rotation((rotation.f586q0 * this.f586q0) - (((rotation.f587q1 * this.f587q1) + (rotation.f588q2 * this.f588q2)) + (rotation.f589q3 * this.f589q3)), (rotation.f587q1 * this.f586q0) + (rotation.f586q0 * this.f587q1) + ((rotation.f588q2 * this.f589q3) - (rotation.f589q3 * this.f588q2)), (rotation.f588q2 * this.f586q0) + (rotation.f586q0 * this.f588q2) + ((rotation.f589q3 * this.f587q1) - (rotation.f587q1 * this.f589q3)), ((rotation.f587q1 * this.f588q2) - (rotation.f588q2 * this.f587q1)) + (rotation.f589q3 * this.f586q0) + (rotation.f586q0 * this.f589q3), false);
        return rotation2;
    }

    public Rotation applyInverseTo(Rotation r) {
        return composeInverse(r, RotationConvention.VECTOR_OPERATOR);
    }

    public Rotation composeInverse(Rotation r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInverseInternal(r) : r.composeInternal(revert());
    }

    private Rotation composeInverseInternal(Rotation r) {
        Rotation rotation = r;
        Rotation rotation2 = new Rotation(((-rotation.f586q0) * this.f586q0) - (((rotation.f587q1 * this.f587q1) + (rotation.f588q2 * this.f588q2)) + (rotation.f589q3 * this.f589q3)), ((-rotation.f587q1) * this.f586q0) + (rotation.f586q0 * this.f587q1) + ((rotation.f588q2 * this.f589q3) - (rotation.f589q3 * this.f588q2)), ((-rotation.f588q2) * this.f586q0) + (rotation.f586q0 * this.f588q2) + ((rotation.f589q3 * this.f587q1) - (rotation.f587q1 * this.f589q3)), ((rotation.f587q1 * this.f588q2) - (rotation.f588q2 * this.f587q1)) + ((-rotation.f589q3) * this.f586q0) + (rotation.f586q0 * this.f589q3), false);
        return rotation2;
    }

    /* JADX WARNING: type inference failed for: r73v1 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private double[][] orthogonalizeMatrix(double[][] r76, double r77) throws org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException {
        /*
            r75 = this;
            r1 = 0
            r2 = r76[r1]
            r3 = 1
            r4 = r76[r3]
            r5 = 2
            r6 = r76[r5]
            r7 = r2[r1]
            r9 = r2[r3]
            r11 = r2[r5]
            r13 = r4[r1]
            r15 = r4[r3]
            r17 = r4[r5]
            r19 = r6[r1]
            r21 = r6[r3]
            r23 = r6[r5]
            r25 = 0
            r5 = 3
            int[] r5 = new int[]{r5, r5}
            java.lang.Class<double> r3 = double.class
            java.lang.Object r3 = java.lang.reflect.Array.newInstance(r3, r5)
            double[][] r3 = (double[][]) r3
            r5 = r3[r1]
            r28 = 1
            r29 = r3[r28]
            r27 = 2
            r30 = r3[r27]
            r31 = r25
            r25 = r23
            r23 = r17
            r17 = r11
            r10 = r9
            r8 = r7
            r7 = 0
        L_0x003f:
            int r7 = r7 + 1
            r12 = 11
            if (r7 >= r12) goto L_0x021d
            r33 = r2[r1]
            double r33 = r33 * r8
            r35 = r4[r1]
            double r35 = r35 * r13
            double r33 = r33 + r35
            r35 = r6[r1]
            double r35 = r35 * r19
            double r33 = r33 + r35
            r12 = 1
            r35 = r2[r12]
            double r35 = r35 * r8
            r37 = r4[r12]
            double r37 = r37 * r13
            double r35 = r35 + r37
            r37 = r6[r12]
            double r37 = r37 * r19
            double r35 = r35 + r37
            r12 = 2
            r37 = r2[r12]
            double r37 = r37 * r8
            r39 = r4[r12]
            double r39 = r39 * r13
            double r37 = r37 + r39
            r39 = r6[r12]
            double r39 = r39 * r19
            double r37 = r37 + r39
            r39 = r2[r1]
            double r39 = r39 * r10
            r41 = r4[r1]
            double r41 = r41 * r15
            double r39 = r39 + r41
            r41 = r6[r1]
            double r41 = r41 * r21
            double r39 = r39 + r41
            r12 = 1
            r41 = r2[r12]
            double r41 = r41 * r10
            r43 = r4[r12]
            double r43 = r43 * r15
            double r41 = r41 + r43
            r43 = r6[r12]
            double r43 = r43 * r21
            double r41 = r41 + r43
            r12 = 2
            r43 = r2[r12]
            double r43 = r43 * r10
            r45 = r4[r12]
            double r45 = r45 * r15
            double r43 = r43 + r45
            r45 = r6[r12]
            double r45 = r45 * r21
            double r43 = r43 + r45
            r45 = r2[r1]
            double r45 = r45 * r17
            r47 = r4[r1]
            double r47 = r47 * r23
            double r45 = r45 + r47
            r47 = r6[r1]
            double r47 = r47 * r25
            double r45 = r45 + r47
            r12 = 1
            r47 = r2[r12]
            double r47 = r47 * r17
            r49 = r4[r12]
            double r49 = r49 * r23
            double r47 = r47 + r49
            r49 = r6[r12]
            double r49 = r49 * r25
            double r47 = r47 + r49
            r12 = 2
            r49 = r2[r12]
            double r49 = r49 * r17
            r51 = r4[r12]
            double r51 = r51 * r23
            double r49 = r49 + r51
            r51 = r6[r12]
            double r51 = r51 * r25
            double r49 = r49 + r51
            double r51 = r8 * r33
            double r53 = r10 * r35
            double r51 = r51 + r53
            double r53 = r17 * r37
            double r51 = r51 + r53
            r53 = r2[r1]
            double r51 = r51 - r53
            r53 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            double r51 = r51 * r53
            double r51 = r8 - r51
            r5[r1] = r51
            double r51 = r8 * r39
            double r55 = r10 * r41
            double r51 = r51 + r55
            double r55 = r17 * r43
            double r51 = r51 + r55
            r12 = 1
            r55 = r2[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r10 - r51
            r5[r12] = r51
            double r51 = r8 * r45
            double r55 = r10 * r47
            double r51 = r51 + r55
            double r55 = r17 * r49
            double r51 = r51 + r55
            r12 = 2
            r55 = r2[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r17 - r51
            r5[r12] = r51
            double r51 = r13 * r33
            double r55 = r15 * r35
            double r51 = r51 + r55
            double r55 = r23 * r37
            double r51 = r51 + r55
            r55 = r4[r1]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r13 - r51
            r29[r1] = r51
            double r51 = r13 * r39
            double r55 = r15 * r41
            double r51 = r51 + r55
            double r55 = r23 * r43
            double r51 = r51 + r55
            r12 = 1
            r55 = r4[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r15 - r51
            r29[r12] = r51
            double r51 = r13 * r45
            double r55 = r15 * r47
            double r51 = r51 + r55
            double r55 = r23 * r49
            double r51 = r51 + r55
            r12 = 2
            r55 = r4[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r23 - r51
            r29[r12] = r51
            double r51 = r19 * r33
            double r55 = r21 * r35
            double r51 = r51 + r55
            double r55 = r25 * r37
            double r51 = r51 + r55
            r55 = r6[r1]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r19 - r51
            r30[r1] = r51
            double r51 = r19 * r39
            double r55 = r21 * r41
            double r51 = r51 + r55
            double r55 = r25 * r43
            double r51 = r51 + r55
            r12 = 1
            r55 = r6[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r21 - r51
            r30[r12] = r51
            double r51 = r19 * r45
            double r55 = r21 * r47
            double r51 = r51 + r55
            double r55 = r25 * r49
            double r51 = r51 + r55
            r12 = 2
            r55 = r6[r12]
            double r51 = r51 - r55
            double r51 = r51 * r53
            double r51 = r25 - r51
            r30[r12] = r51
            r51 = r5[r1]
            r53 = r2[r1]
            double r51 = r51 - r53
            r27 = 1
            r53 = r5[r27]
            r55 = r2[r27]
            double r53 = r53 - r55
            r55 = r5[r12]
            r57 = r2[r12]
            double r55 = r55 - r57
            r57 = r29[r1]
            r59 = r4[r1]
            double r57 = r57 - r59
            r59 = r29[r27]
            r61 = r4[r27]
            double r59 = r59 - r61
            r61 = r29[r12]
            r63 = r4[r12]
            double r61 = r61 - r63
            r63 = r30[r1]
            r65 = r6[r1]
            double r63 = r63 - r65
            r65 = r30[r27]
            r67 = r6[r27]
            double r65 = r65 - r67
            r67 = r30[r12]
            r69 = r6[r12]
            double r67 = r67 - r69
            double r69 = r51 * r51
            double r71 = r53 * r53
            double r69 = r69 + r71
            double r71 = r55 * r55
            double r69 = r69 + r71
            double r71 = r57 * r57
            double r69 = r69 + r71
            double r71 = r59 * r59
            double r69 = r69 + r71
            double r71 = r61 * r61
            double r69 = r69 + r71
            double r71 = r63 * r63
            double r69 = r69 + r71
            double r71 = r65 * r65
            double r69 = r69 + r71
            double r71 = r67 * r67
            double r69 = r69 + r71
            r73 = r2
            double r1 = r69 - r31
            double r1 = org.apache.commons.math3.util.FastMath.abs(r1)
            int r1 = (r1 > r77 ? 1 : (r1 == r77 ? 0 : -1))
            if (r1 > 0) goto L_0x01fe
            return r3
        L_0x01fe:
            r1 = 0
            r8 = r5[r1]
            r2 = 1
            r10 = r5[r2]
            r12 = 2
            r17 = r5[r12]
            r13 = r29[r1]
            r15 = r29[r2]
            r23 = r29[r12]
            r19 = r30[r1]
            r21 = r30[r2]
            r25 = r30[r12]
            r31 = r69
            r2 = r73
            r1 = 0
            r28 = 1
            goto L_0x003f
        L_0x021d:
            r73 = r2
            r2 = 1
            org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException r1 = new org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException
            org.apache.commons.math3.exception.util.LocalizedFormats r12 = org.apache.commons.math3.exception.util.LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX
            java.lang.Object[] r2 = new java.lang.Object[r2]
            int r0 = r7 + -1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r27 = 0
            r2[r27] = r0
            r1.<init>(r12, r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.geometry.euclidean.threed.Rotation.orthogonalizeMatrix(double[][], double):double[][]");
    }

    public static double distance(Rotation r1, Rotation r2) {
        return r1.composeInverseInternal(r2).getAngle();
    }
}
