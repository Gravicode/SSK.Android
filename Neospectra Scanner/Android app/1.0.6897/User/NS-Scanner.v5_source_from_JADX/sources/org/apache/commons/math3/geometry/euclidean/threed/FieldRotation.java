package org.apache.commons.math3.geometry.euclidean.threed;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

public class FieldRotation<T extends RealFieldElement<T>> implements Serializable {
    private static final long serialVersionUID = 20130224;

    /* renamed from: q0 */
    private final T f573q0;

    /* renamed from: q1 */
    private final T f574q1;

    /* renamed from: q2 */
    private final T f575q2;

    /* renamed from: q3 */
    private final T f576q3;

    public FieldRotation(T q0, T q1, T q2, T q3, boolean needsNormalization) {
        if (needsNormalization) {
            T inv = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) q0.multiply(q0)).add(q1.multiply(q1))).add(q2.multiply(q2))).add(q3.multiply(q3))).sqrt()).reciprocal();
            this.f573q0 = (RealFieldElement) inv.multiply(q0);
            this.f574q1 = (RealFieldElement) inv.multiply(q1);
            this.f575q2 = (RealFieldElement) inv.multiply(q2);
            this.f576q3 = (RealFieldElement) inv.multiply(q3);
            return;
        }
        this.f573q0 = q0;
        this.f574q1 = q1;
        this.f575q2 = q2;
        this.f576q3 = q3;
    }

    @Deprecated
    public FieldRotation(FieldVector3D<T> axis, T angle) throws MathIllegalArgumentException {
        this(axis, angle, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation(FieldVector3D<T> axis, T angle, RotationConvention convention) throws MathIllegalArgumentException {
        T norm = axis.getNorm();
        if (norm.getReal() == 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_AXIS, new Object[0]);
        }
        T halfAngle = (RealFieldElement) angle.multiply(convention == RotationConvention.VECTOR_OPERATOR ? -0.5d : 0.5d);
        T coeff = (RealFieldElement) ((RealFieldElement) halfAngle.sin()).divide(norm);
        this.f573q0 = (RealFieldElement) halfAngle.cos();
        this.f574q1 = (RealFieldElement) coeff.multiply(axis.getX());
        this.f575q2 = (RealFieldElement) coeff.multiply(axis.getY());
        this.f576q3 = (RealFieldElement) coeff.multiply(axis.getZ());
    }

    public FieldRotation(T[][] m, double threshold) throws NotARotationMatrixException {
        T[][] tArr = m;
        if (tArr.length == 3 && tArr[0].length == 3 && tArr[1].length == 3 && tArr[2].length == 3) {
            T[][] ort = orthogonalizeMatrix(m, threshold);
            T det = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ort[0][0].multiply((RealFieldElement) ((RealFieldElement) ort[1][1].multiply(ort[2][2])).subtract(ort[2][1].multiply(ort[1][2])))).subtract(ort[1][0].multiply((RealFieldElement) ((RealFieldElement) ort[0][1].multiply(ort[2][2])).subtract(ort[2][1].multiply(ort[0][2]))))).add(ort[2][0].multiply((RealFieldElement) ((RealFieldElement) ort[0][1].multiply(ort[1][2])).subtract(ort[1][1].multiply(ort[0][2]))));
            if (det.getReal() < 0.0d) {
                throw new NotARotationMatrixException(LocalizedFormats.CLOSEST_ORTHOGONAL_MATRIX_HAS_NEGATIVE_DETERMINANT, det);
            }
            T[] quat = mat2quat(ort);
            this.f573q0 = quat[0];
            this.f574q1 = quat[1];
            this.f575q2 = quat[2];
            this.f576q3 = quat[3];
            return;
        }
        throw new NotARotationMatrixException(LocalizedFormats.ROTATION_MATRIX_DIMENSIONS, Integer.valueOf(tArr.length), Integer.valueOf(tArr[0].length));
    }

    public FieldRotation(FieldVector3D<T> u1, FieldVector3D<T> u2, FieldVector3D<T> v1, FieldVector3D<T> v2) throws MathArithmeticException {
        FieldVector3D<T> u3 = FieldVector3D.crossProduct(u1, u2).normalize();
        FieldVector3D<T> u22 = FieldVector3D.crossProduct(u3, u1).normalize();
        FieldVector3D<T> u12 = u1.normalize();
        FieldVector3D<T> v3 = FieldVector3D.crossProduct(v1, v2).normalize();
        FieldVector3D<T> v22 = FieldVector3D.crossProduct(v3, v1).normalize();
        FieldVector3D<T> v12 = v1.normalize();
        T[][] array = (RealFieldElement[][]) MathArrays.buildArray(u12.getX().getField(), 3, 3);
        array[0][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getX().multiply(v12.getX())).add(u22.getX().multiply(v22.getX()))).add(u3.getX().multiply(v3.getX()));
        array[0][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getY().multiply(v12.getX())).add(u22.getY().multiply(v22.getX()))).add(u3.getY().multiply(v3.getX()));
        array[0][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getZ().multiply(v12.getX())).add(u22.getZ().multiply(v22.getX()))).add(u3.getZ().multiply(v3.getX()));
        array[1][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getX().multiply(v12.getY())).add(u22.getX().multiply(v22.getY()))).add(u3.getX().multiply(v3.getY()));
        array[1][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getY().multiply(v12.getY())).add(u22.getY().multiply(v22.getY()))).add(u3.getY().multiply(v3.getY()));
        array[1][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getZ().multiply(v12.getY())).add(u22.getZ().multiply(v22.getY()))).add(u3.getZ().multiply(v3.getY()));
        array[2][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getX().multiply(v12.getZ())).add(u22.getX().multiply(v22.getZ()))).add(u3.getX().multiply(v3.getZ()));
        array[2][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getY().multiply(v12.getZ())).add(u22.getY().multiply(v22.getZ()))).add(u3.getY().multiply(v3.getZ()));
        array[2][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) u12.getZ().multiply(v12.getZ())).add(u22.getZ().multiply(v22.getZ()))).add(u3.getZ().multiply(v3.getZ()));
        T[] quat = mat2quat(array);
        this.f573q0 = quat[0];
        this.f574q1 = quat[1];
        this.f575q2 = quat[2];
        this.f576q3 = quat[3];
    }

    public FieldRotation(FieldVector3D<T> u, FieldVector3D<T> v) throws MathArithmeticException {
        T normProduct = (RealFieldElement) u.getNorm().multiply(v.getNorm());
        if (normProduct.getReal() == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ZERO_NORM_FOR_ROTATION_DEFINING_VECTOR, new Object[0]);
        }
        T dot = FieldVector3D.dotProduct(u, v);
        if (dot.getReal() < normProduct.getReal() * -0.999999999999998d) {
            FieldVector3D<T> w = u.orthogonal();
            this.f573q0 = (RealFieldElement) normProduct.getField().getZero();
            this.f574q1 = (RealFieldElement) w.getX().negate();
            this.f575q2 = (RealFieldElement) w.getY().negate();
            this.f576q3 = (RealFieldElement) w.getZ().negate();
            return;
        }
        this.f573q0 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) dot.divide(normProduct)).add(1.0d)).multiply(0.5d)).sqrt();
        T coeff = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(normProduct)).multiply(2.0d)).reciprocal();
        FieldVector3D<T> q = FieldVector3D.crossProduct(v, u);
        this.f574q1 = (RealFieldElement) coeff.multiply(q.getX());
        this.f575q2 = (RealFieldElement) coeff.multiply(q.getY());
        this.f576q3 = (RealFieldElement) coeff.multiply(q.getZ());
    }

    @Deprecated
    public FieldRotation(RotationOrder order, T alpha1, T alpha2, T alpha3) {
        this(order, RotationConvention.VECTOR_OPERATOR, alpha1, alpha2, alpha3);
    }

    public FieldRotation(RotationOrder order, RotationConvention convention, T alpha1, T alpha2, T alpha3) {
        T one = (RealFieldElement) alpha1.getField().getOne();
        FieldRotation<T> composed = new FieldRotation<>(new FieldVector3D(one, order.getA1()), alpha1, convention).compose(new FieldRotation<>(new FieldVector3D(one, order.getA2()), alpha2, convention).compose(new FieldRotation<>(new FieldVector3D(one, order.getA3()), alpha3, convention), convention), convention);
        this.f573q0 = composed.f573q0;
        this.f574q1 = composed.f574q1;
        this.f575q2 = composed.f575q2;
        this.f576q3 = composed.f576q3;
    }

    private T[] mat2quat(T[][] ort) {
        T[] quat = (RealFieldElement[]) MathArrays.buildArray(ort[0][0].getField(), 4);
        T s = (RealFieldElement) ((RealFieldElement) ort[0][0].add(ort[1][1])).add(ort[2][2]);
        if (s.getReal() > -0.19d) {
            quat[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) s.add(1.0d)).sqrt()).multiply(0.5d);
            T inv = (RealFieldElement) ((RealFieldElement) quat[0].reciprocal()).multiply(0.25d);
            quat[1] = (RealFieldElement) inv.multiply(ort[1][2].subtract(ort[2][1]));
            quat[2] = (RealFieldElement) inv.multiply(ort[2][0].subtract(ort[0][2]));
            quat[3] = (RealFieldElement) inv.multiply(ort[0][1].subtract(ort[1][0]));
        } else {
            T s2 = (RealFieldElement) ((RealFieldElement) ort[0][0].subtract(ort[1][1])).subtract(ort[2][2]);
            if (s2.getReal() > -0.19d) {
                quat[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) s2.add(1.0d)).sqrt()).multiply(0.5d);
                T inv2 = (RealFieldElement) ((RealFieldElement) quat[1].reciprocal()).multiply(0.25d);
                quat[0] = (RealFieldElement) inv2.multiply(ort[1][2].subtract(ort[2][1]));
                quat[2] = (RealFieldElement) inv2.multiply(ort[0][1].add(ort[1][0]));
                quat[3] = (RealFieldElement) inv2.multiply(ort[0][2].add(ort[2][0]));
            } else {
                T s3 = (RealFieldElement) ((RealFieldElement) ort[1][1].subtract(ort[0][0])).subtract(ort[2][2]);
                if (s3.getReal() > -0.19d) {
                    quat[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) s3.add(1.0d)).sqrt()).multiply(0.5d);
                    T inv3 = (RealFieldElement) ((RealFieldElement) quat[2].reciprocal()).multiply(0.25d);
                    quat[0] = (RealFieldElement) inv3.multiply(ort[2][0].subtract(ort[0][2]));
                    quat[1] = (RealFieldElement) inv3.multiply(ort[0][1].add(ort[1][0]));
                    quat[3] = (RealFieldElement) inv3.multiply(ort[2][1].add(ort[1][2]));
                } else {
                    quat[3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ort[2][2].subtract(ort[0][0])).subtract(ort[1][1])).add(1.0d)).sqrt()).multiply(0.5d);
                    T inv4 = (RealFieldElement) ((RealFieldElement) quat[3].reciprocal()).multiply(0.25d);
                    quat[0] = (RealFieldElement) inv4.multiply(ort[0][1].subtract(ort[1][0]));
                    quat[1] = (RealFieldElement) inv4.multiply(ort[0][2].add(ort[2][0]));
                    quat[2] = (RealFieldElement) inv4.multiply(ort[2][1].add(ort[1][2]));
                }
            }
        }
        return quat;
    }

    public FieldRotation<T> revert() {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) this.f573q0.negate(), this.f574q1, this.f575q2, this.f576q3, false);
        return fieldRotation;
    }

    public T getQ0() {
        return this.f573q0;
    }

    public T getQ1() {
        return this.f574q1;
    }

    public T getQ2() {
        return this.f575q2;
    }

    public T getQ3() {
        return this.f576q3;
    }

    @Deprecated
    public FieldVector3D<T> getAxis() {
        return getAxis(RotationConvention.VECTOR_OPERATOR);
    }

    public FieldVector3D<T> getAxis(RotationConvention convention) {
        T squaredSine = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(this.f574q1)).add(this.f575q2.multiply(this.f575q2))).add(this.f576q3.multiply(this.f576q3));
        if (squaredSine.getReal() == 0.0d) {
            Field<T> field = squaredSine.getField();
            return new FieldVector3D<>((RealFieldElement) (convention == RotationConvention.VECTOR_OPERATOR ? field.getOne() : ((RealFieldElement) field.getOne()).negate()), (RealFieldElement) field.getZero(), (RealFieldElement) field.getZero());
        }
        double sgn = convention == RotationConvention.VECTOR_OPERATOR ? 1.0d : -1.0d;
        if (this.f573q0.getReal() < 0.0d) {
            T inverse = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) squaredSine.sqrt()).reciprocal()).multiply(sgn);
            return new FieldVector3D<>((RealFieldElement) this.f574q1.multiply(inverse), (RealFieldElement) this.f575q2.multiply(inverse), (RealFieldElement) this.f576q3.multiply(inverse));
        }
        T inverse2 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) squaredSine.sqrt()).reciprocal()).negate()).multiply(sgn);
        return new FieldVector3D<>((RealFieldElement) this.f574q1.multiply(inverse2), (RealFieldElement) this.f575q2.multiply(inverse2), (RealFieldElement) this.f576q3.multiply(inverse2));
    }

    public T getAngle() {
        if (this.f573q0.getReal() < -0.1d || this.f573q0.getReal() > 0.1d) {
            return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(this.f574q1)).add(this.f575q2.multiply(this.f575q2))).add(this.f576q3.multiply(this.f576q3))).sqrt()).asin()).multiply(2);
        }
        if (this.f573q0.getReal() < 0.0d) {
            return (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.negate()).acos()).multiply(2);
        }
        return (RealFieldElement) ((RealFieldElement) this.f573q0.acos()).multiply(2);
    }

    @Deprecated
    public T[] getAngles(RotationOrder order) throws CardanEulerSingularityException {
        return getAngles(order, RotationConvention.VECTOR_OPERATOR);
    }

    public T[] getAngles(RotationOrder order, RotationConvention convention) throws CardanEulerSingularityException {
        RotationOrder rotationOrder = order;
        if (convention == RotationConvention.VECTOR_OPERATOR) {
            if (rotationOrder == RotationOrder.XYZ) {
                FieldVector3D<T> v1 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> v2 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (v2.getZ().getReal() >= -0.9999999999d && v2.getZ().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) ((RealFieldElement) v1.getY().negate()).atan2(v1.getZ()), (RealFieldElement) v2.getZ().asin(), (RealFieldElement) ((RealFieldElement) v2.getY().negate()).atan2(v2.getX()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.XZY) {
                FieldVector3D<T> v12 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> v22 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (v22.getY().getReal() >= -0.9999999999d && v22.getY().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v12.getZ().atan2(v12.getY()), (RealFieldElement) ((RealFieldElement) v22.getY().asin()).negate(), (RealFieldElement) v22.getZ().atan2(v22.getX()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.YXZ) {
                FieldVector3D<T> v13 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> v23 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (v23.getZ().getReal() >= -0.9999999999d && v23.getZ().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v13.getX().atan2(v13.getZ()), (RealFieldElement) ((RealFieldElement) v23.getZ().asin()).negate(), (RealFieldElement) v23.getX().atan2(v23.getY()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.YZX) {
                FieldVector3D<T> v14 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> v24 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (v24.getX().getReal() >= -0.9999999999d && v24.getX().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) ((RealFieldElement) v14.getZ().negate()).atan2(v14.getX()), (RealFieldElement) v24.getX().asin(), (RealFieldElement) ((RealFieldElement) v24.getZ().negate()).atan2(v24.getY()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.ZXY) {
                FieldVector3D<T> v15 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> v25 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (v25.getY().getReal() >= -0.9999999999d && v25.getY().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) ((RealFieldElement) v15.getX().negate()).atan2(v15.getY()), (RealFieldElement) v25.getY().asin(), (RealFieldElement) ((RealFieldElement) v25.getX().negate()).atan2(v25.getZ()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.ZYX) {
                FieldVector3D<T> v16 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> v26 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (v26.getX().getReal() >= -0.9999999999d && v26.getX().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v16.getY().atan2(v16.getX()), (RealFieldElement) ((RealFieldElement) v26.getX().asin()).negate(), (RealFieldElement) v26.getY().atan2(v26.getZ()));
                }
                throw new CardanEulerSingularityException(true);
            } else if (rotationOrder == RotationOrder.XYX) {
                FieldVector3D<T> v17 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> v27 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (v27.getX().getReal() >= -0.9999999999d && v27.getX().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v17.getY().atan2(v17.getZ().negate()), (RealFieldElement) v27.getX().acos(), (RealFieldElement) v27.getY().atan2(v27.getZ()));
                }
                throw new CardanEulerSingularityException(false);
            } else if (rotationOrder == RotationOrder.XZX) {
                FieldVector3D<T> v18 = applyTo(vector(1.0d, 0.0d, 0.0d));
                FieldVector3D<T> v28 = applyInverseTo(vector(1.0d, 0.0d, 0.0d));
                if (v28.getX().getReal() >= -0.9999999999d && v28.getX().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v18.getZ().atan2(v18.getY()), (RealFieldElement) v28.getX().acos(), (RealFieldElement) v28.getZ().atan2(v28.getY().negate()));
                }
                throw new CardanEulerSingularityException(false);
            } else if (rotationOrder == RotationOrder.YXY) {
                FieldVector3D<T> v19 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> v29 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (v29.getY().getReal() >= -0.9999999999d && v29.getY().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v19.getX().atan2(v19.getZ()), (RealFieldElement) v29.getY().acos(), (RealFieldElement) v29.getX().atan2(v29.getZ().negate()));
                }
                throw new CardanEulerSingularityException(false);
            } else if (rotationOrder == RotationOrder.YZY) {
                FieldVector3D<T> v110 = applyTo(vector(0.0d, 1.0d, 0.0d));
                FieldVector3D<T> v210 = applyInverseTo(vector(0.0d, 1.0d, 0.0d));
                if (v210.getY().getReal() >= -0.9999999999d && v210.getY().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v110.getZ().atan2(v110.getX().negate()), (RealFieldElement) v210.getY().acos(), (RealFieldElement) v210.getZ().atan2(v210.getX()));
                }
                throw new CardanEulerSingularityException(false);
            } else if (rotationOrder == RotationOrder.ZXZ) {
                FieldVector3D<T> v111 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> v211 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (v211.getZ().getReal() >= -0.9999999999d && v211.getZ().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v111.getX().atan2(v111.getY().negate()), (RealFieldElement) v211.getZ().acos(), (RealFieldElement) v211.getX().atan2(v211.getY()));
                }
                throw new CardanEulerSingularityException(false);
            } else {
                FieldVector3D<T> v112 = applyTo(vector(0.0d, 0.0d, 1.0d));
                FieldVector3D<T> v212 = applyInverseTo(vector(0.0d, 0.0d, 1.0d));
                if (v212.getZ().getReal() >= -0.9999999999d && v212.getZ().getReal() <= 0.9999999999d) {
                    return buildArray((RealFieldElement) v112.getY().atan2(v112.getX()), (RealFieldElement) v212.getZ().acos(), (RealFieldElement) v212.getY().atan2(v212.getX().negate()));
                }
                throw new CardanEulerSingularityException(false);
            }
        } else if (rotationOrder == RotationOrder.XYZ) {
            FieldVector3D<T> v113 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> v213 = applyInverseTo(Vector3D.PLUS_K);
            if (v213.getX().getReal() >= -0.9999999999d && v213.getX().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) ((RealFieldElement) v213.getY().negate()).atan2(v213.getZ()), (RealFieldElement) v213.getX().asin(), (RealFieldElement) ((RealFieldElement) v113.getY().negate()).atan2(v113.getX()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.XZY) {
            FieldVector3D<T> v114 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> v214 = applyInverseTo(Vector3D.PLUS_J);
            if (v214.getX().getReal() >= -0.9999999999d && v214.getX().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v214.getZ().atan2(v214.getY()), (RealFieldElement) ((RealFieldElement) v214.getX().asin()).negate(), (RealFieldElement) v114.getZ().atan2(v114.getX()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.YXZ) {
            FieldVector3D<T> v115 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> v215 = applyInverseTo(Vector3D.PLUS_K);
            if (v215.getY().getReal() >= -0.9999999999d && v215.getY().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v215.getX().atan2(v215.getZ()), (RealFieldElement) ((RealFieldElement) v215.getY().asin()).negate(), (RealFieldElement) v115.getX().atan2(v115.getY()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.YZX) {
            FieldVector3D<T> v116 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> v216 = applyInverseTo(Vector3D.PLUS_I);
            if (v216.getY().getReal() >= -0.9999999999d && v216.getY().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) ((RealFieldElement) v216.getZ().negate()).atan2(v216.getX()), (RealFieldElement) v216.getY().asin(), (RealFieldElement) ((RealFieldElement) v116.getZ().negate()).atan2(v116.getY()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.ZXY) {
            FieldVector3D<T> v117 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> v217 = applyInverseTo(Vector3D.PLUS_J);
            if (v217.getZ().getReal() >= -0.9999999999d && v217.getZ().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) ((RealFieldElement) v217.getX().negate()).atan2(v217.getY()), (RealFieldElement) v217.getZ().asin(), (RealFieldElement) ((RealFieldElement) v117.getX().negate()).atan2(v117.getZ()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.ZYX) {
            FieldVector3D<T> v118 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> v218 = applyInverseTo(Vector3D.PLUS_I);
            if (v218.getZ().getReal() >= -0.9999999999d && v218.getZ().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v218.getY().atan2(v218.getX()), (RealFieldElement) ((RealFieldElement) v218.getZ().asin()).negate(), (RealFieldElement) v118.getY().atan2(v118.getZ()));
            }
            throw new CardanEulerSingularityException(true);
        } else if (rotationOrder == RotationOrder.XYX) {
            FieldVector3D<T> v119 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> v219 = applyInverseTo(Vector3D.PLUS_I);
            if (v219.getX().getReal() >= -0.9999999999d && v219.getX().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v219.getY().atan2(v219.getZ().negate()), (RealFieldElement) v219.getX().acos(), (RealFieldElement) v119.getY().atan2(v119.getZ()));
            }
            throw new CardanEulerSingularityException(false);
        } else if (rotationOrder == RotationOrder.XZX) {
            FieldVector3D<T> v120 = applyTo(Vector3D.PLUS_I);
            FieldVector3D<T> v220 = applyInverseTo(Vector3D.PLUS_I);
            if (v220.getX().getReal() >= -0.9999999999d && v220.getX().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v220.getZ().atan2(v220.getY()), (RealFieldElement) v220.getX().acos(), (RealFieldElement) v120.getZ().atan2(v120.getY().negate()));
            }
            throw new CardanEulerSingularityException(false);
        } else if (rotationOrder == RotationOrder.YXY) {
            FieldVector3D<T> v121 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> v221 = applyInverseTo(Vector3D.PLUS_J);
            if (v221.getY().getReal() >= -0.9999999999d && v221.getY().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v221.getX().atan2(v221.getZ()), (RealFieldElement) v221.getY().acos(), (RealFieldElement) v121.getX().atan2(v121.getZ().negate()));
            }
            throw new CardanEulerSingularityException(false);
        } else if (rotationOrder == RotationOrder.YZY) {
            FieldVector3D<T> v122 = applyTo(Vector3D.PLUS_J);
            FieldVector3D<T> v222 = applyInverseTo(Vector3D.PLUS_J);
            if (v222.getY().getReal() >= -0.9999999999d && v222.getY().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v222.getZ().atan2(v222.getX().negate()), (RealFieldElement) v222.getY().acos(), (RealFieldElement) v122.getZ().atan2(v122.getX()));
            }
            throw new CardanEulerSingularityException(false);
        } else if (rotationOrder == RotationOrder.ZXZ) {
            FieldVector3D<T> v123 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> v223 = applyInverseTo(Vector3D.PLUS_K);
            if (v223.getZ().getReal() >= -0.9999999999d && v223.getZ().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v223.getX().atan2(v223.getY().negate()), (RealFieldElement) v223.getZ().acos(), (RealFieldElement) v123.getX().atan2(v123.getY()));
            }
            throw new CardanEulerSingularityException(false);
        } else {
            FieldVector3D<T> v124 = applyTo(Vector3D.PLUS_K);
            FieldVector3D<T> v224 = applyInverseTo(Vector3D.PLUS_K);
            if (v224.getZ().getReal() >= -0.9999999999d && v224.getZ().getReal() <= 0.9999999999d) {
                return buildArray((RealFieldElement) v224.getY().atan2(v224.getX()), (RealFieldElement) v224.getZ().acos(), (RealFieldElement) v124.getY().atan2(v124.getX().negate()));
            }
            throw new CardanEulerSingularityException(false);
        }
    }

    private T[] buildArray(T a0, T a1, T a2) {
        T[] array = (RealFieldElement[]) MathArrays.buildArray(a0.getField(), 3);
        array[0] = a0;
        array[1] = a1;
        array[2] = a2;
        return array;
    }

    private FieldVector3D<T> vector(double x, double y, double z) {
        T zero = (RealFieldElement) this.f573q0.getField().getZero();
        return new FieldVector3D<>((RealFieldElement) zero.add(x), (RealFieldElement) zero.add(y), (RealFieldElement) zero.add(z));
    }

    public T[][] getMatrix() {
        T q0q0 = (RealFieldElement) this.f573q0.multiply(this.f573q0);
        T q0q1 = (RealFieldElement) this.f573q0.multiply(this.f574q1);
        T q0q2 = (RealFieldElement) this.f573q0.multiply(this.f575q2);
        T q0q3 = (RealFieldElement) this.f573q0.multiply(this.f576q3);
        T q1q1 = (RealFieldElement) this.f574q1.multiply(this.f574q1);
        T q1q2 = (RealFieldElement) this.f574q1.multiply(this.f575q2);
        T q1q3 = (RealFieldElement) this.f574q1.multiply(this.f576q3);
        T q2q2 = (RealFieldElement) this.f575q2.multiply(this.f575q2);
        T q2q3 = (RealFieldElement) this.f575q2.multiply(this.f576q3);
        T q3q3 = (RealFieldElement) this.f576q3.multiply(this.f576q3);
        T[][] m = (RealFieldElement[][]) MathArrays.buildArray(this.f573q0.getField(), 3, 3);
        m[0][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) q0q0.add(q1q1)).multiply(2)).subtract(1.0d);
        m[1][0] = (RealFieldElement) ((RealFieldElement) q1q2.subtract(q0q3)).multiply(2);
        m[2][0] = (RealFieldElement) ((RealFieldElement) q1q3.add(q0q2)).multiply(2);
        m[0][1] = (RealFieldElement) ((RealFieldElement) q1q2.add(q0q3)).multiply(2);
        m[1][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) q0q0.add(q2q2)).multiply(2)).subtract(1.0d);
        m[2][1] = (RealFieldElement) ((RealFieldElement) q2q3.subtract(q0q1)).multiply(2);
        m[0][2] = (RealFieldElement) ((RealFieldElement) q1q3.subtract(q0q2)).multiply(2);
        m[1][2] = (RealFieldElement) ((RealFieldElement) q2q3.add(q0q1)).multiply(2);
        m[2][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) q0q0.add(q3q3)).multiply(2)).subtract(1.0d);
        return m;
    }

    public Rotation toRotation() {
        Rotation rotation = new Rotation(this.f573q0.getReal(), this.f574q1.getReal(), this.f575q2.getReal(), this.f576q3.getReal(), false);
        return rotation;
    }

    public FieldVector3D<T> applyTo(FieldVector3D<T> u) {
        T x = u.getX();
        T y = u.getY();
        T z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) x.multiply(this.f573q0)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) y.multiply(this.f573q0)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) z.multiply(this.f573q0)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z));
    }

    public FieldVector3D<T> applyTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(x)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(y)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(z)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z));
    }

    public void applyTo(T[] in, T[] out) {
        T x = in[0];
        T y = in[1];
        T z = in[2];
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        out[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) x.multiply(this.f573q0)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x);
        out[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) y.multiply(this.f573q0)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y);
        out[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) z.multiply(this.f573q0)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z);
    }

    public void applyTo(double[] in, T[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        out[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(x)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x);
        out[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(y)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y);
        out[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(((RealFieldElement) this.f573q0.multiply(z)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> applyTo(Rotation r, FieldVector3D<T> u) {
        T x = u.getX();
        T y = u.getY();
        T z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) x.multiply(r.getQ1())).add(y.multiply(r.getQ2()))).add(z.multiply(r.getQ3()));
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) x.multiply(r.getQ0())).subtract(((RealFieldElement) z.multiply(r.getQ2())).subtract(y.multiply(r.getQ3())))).multiply(r.getQ0())).add(s.multiply(r.getQ1()))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) y.multiply(r.getQ0())).subtract(((RealFieldElement) x.multiply(r.getQ3())).subtract(z.multiply(r.getQ1())))).multiply(r.getQ0())).add(s.multiply(r.getQ2()))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) z.multiply(r.getQ0())).subtract(((RealFieldElement) y.multiply(r.getQ1())).subtract(x.multiply(r.getQ2())))).multiply(r.getQ0())).add(s.multiply(r.getQ3()))).multiply(2)).subtract(z));
    }

    public FieldVector3D<T> applyInverseTo(FieldVector3D<T> u) {
        T x = u.getX();
        T y = u.getY();
        T z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        T m0 = (RealFieldElement) this.f573q0.negate();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) x.multiply(m0)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) y.multiply(m0)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) z.multiply(m0)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z));
    }

    public FieldVector3D<T> applyInverseTo(Vector3D u) {
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        T m0 = (RealFieldElement) this.f573q0.negate();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(x)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(y)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(z)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z));
    }

    public void applyInverseTo(T[] in, T[] out) {
        T x = in[0];
        T y = in[1];
        T z = in[2];
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        T m0 = (RealFieldElement) this.f573q0.negate();
        out[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) x.multiply(m0)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x);
        out[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) y.multiply(m0)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y);
        out[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) z.multiply(m0)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z);
    }

    public void applyInverseTo(double[] in, T[] out) {
        double x = in[0];
        double y = in[1];
        double z = in[2];
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(x)).add(this.f575q2.multiply(y))).add(this.f576q3.multiply(z));
        T m0 = (RealFieldElement) this.f573q0.negate();
        out[0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(x)).subtract(((RealFieldElement) this.f575q2.multiply(z)).subtract(this.f576q3.multiply(y))))).add(s.multiply(this.f574q1))).multiply(2)).subtract(x);
        out[1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(y)).subtract(((RealFieldElement) this.f576q3.multiply(x)).subtract(this.f574q1.multiply(z))))).add(s.multiply(this.f575q2))).multiply(2)).subtract(y);
        out[2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) m0.multiply(((RealFieldElement) m0.multiply(z)).subtract(((RealFieldElement) this.f574q1.multiply(y)).subtract(this.f575q2.multiply(x))))).add(s.multiply(this.f576q3))).multiply(2)).subtract(z);
    }

    public static <T extends RealFieldElement<T>> FieldVector3D<T> applyInverseTo(Rotation r, FieldVector3D<T> u) {
        T x = u.getX();
        T y = u.getY();
        T z = u.getZ();
        T s = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) x.multiply(r.getQ1())).add(y.multiply(r.getQ2()))).add(z.multiply(r.getQ3()));
        double m0 = -r.getQ0();
        return new FieldVector3D<>((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) x.multiply(m0)).subtract(((RealFieldElement) z.multiply(r.getQ2())).subtract(y.multiply(r.getQ3())))).multiply(m0)).add(s.multiply(r.getQ1()))).multiply(2)).subtract(x), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) y.multiply(m0)).subtract(((RealFieldElement) x.multiply(r.getQ3())).subtract(z.multiply(r.getQ1())))).multiply(m0)).add(s.multiply(r.getQ2()))).multiply(2)).subtract(y), (RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) z.multiply(m0)).subtract(((RealFieldElement) y.multiply(r.getQ1())).subtract(x.multiply(r.getQ2())))).multiply(m0)).add(s.multiply(r.getQ3()))).multiply(2)).subtract(z));
    }

    public FieldRotation<T> applyTo(FieldRotation<T> r) {
        return compose(r, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> compose(FieldRotation<T> r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInternal(r) : r.composeInternal(this);
    }

    private FieldRotation<T> composeInternal(FieldRotation<T> r) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) r.f573q0.multiply(this.f573q0)).subtract(((RealFieldElement) ((RealFieldElement) r.f574q1.multiply(this.f574q1)).add(r.f575q2.multiply(this.f575q2))).add(r.f576q3.multiply(this.f576q3))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f574q1.multiply(this.f573q0)).add(r.f573q0.multiply(this.f574q1))).add(((RealFieldElement) r.f575q2.multiply(this.f576q3)).subtract(r.f576q3.multiply(this.f575q2))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f575q2.multiply(this.f573q0)).add(r.f573q0.multiply(this.f575q2))).add(((RealFieldElement) r.f576q3.multiply(this.f574q1)).subtract(r.f574q1.multiply(this.f576q3))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f576q3.multiply(this.f573q0)).add(r.f573q0.multiply(this.f576q3))).add(((RealFieldElement) r.f574q1.multiply(this.f575q2)).subtract(r.f575q2.multiply(this.f574q1))), false);
        return fieldRotation;
    }

    public FieldRotation<T> applyTo(Rotation r) {
        return compose(r, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> compose(Rotation r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInternal(r) : applyTo(r, this);
    }

    private FieldRotation<T> composeInternal(Rotation r) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) this.f573q0.multiply(r.getQ0())).subtract(((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(r.getQ1())).add(this.f575q2.multiply(r.getQ2()))).add(this.f576q3.multiply(r.getQ3()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(r.getQ1())).add(this.f574q1.multiply(r.getQ0()))).add(((RealFieldElement) this.f576q3.multiply(r.getQ2())).subtract(this.f575q2.multiply(r.getQ3()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(r.getQ2())).add(this.f575q2.multiply(r.getQ0()))).add(((RealFieldElement) this.f574q1.multiply(r.getQ3())).subtract(this.f576q3.multiply(r.getQ1()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(r.getQ3())).add(this.f576q3.multiply(r.getQ0()))).add(((RealFieldElement) this.f575q2.multiply(r.getQ1())).subtract(this.f574q1.multiply(r.getQ2()))), false);
        return fieldRotation;
    }

    public static <T extends RealFieldElement<T>> FieldRotation<T> applyTo(Rotation r1, FieldRotation<T> rInner) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) rInner.f573q0.multiply(r1.getQ0())).subtract(((RealFieldElement) ((RealFieldElement) rInner.f574q1.multiply(r1.getQ1())).add(rInner.f575q2.multiply(r1.getQ2()))).add(rInner.f576q3.multiply(r1.getQ3()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f574q1.multiply(r1.getQ0())).add(rInner.f573q0.multiply(r1.getQ1()))).add(((RealFieldElement) rInner.f575q2.multiply(r1.getQ3())).subtract(rInner.f576q3.multiply(r1.getQ2()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f575q2.multiply(r1.getQ0())).add(rInner.f573q0.multiply(r1.getQ2()))).add(((RealFieldElement) rInner.f576q3.multiply(r1.getQ1())).subtract(rInner.f574q1.multiply(r1.getQ3()))), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f576q3.multiply(r1.getQ0())).add(rInner.f573q0.multiply(r1.getQ3()))).add(((RealFieldElement) rInner.f574q1.multiply(r1.getQ2())).subtract(rInner.f575q2.multiply(r1.getQ1()))), false);
        return fieldRotation;
    }

    public FieldRotation<T> applyInverseTo(FieldRotation<T> r) {
        return composeInverse(r, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> composeInverse(FieldRotation<T> r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInverseInternal(r) : r.composeInternal(revert());
    }

    private FieldRotation<T> composeInverseInternal(FieldRotation<T> r) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f573q0.multiply(this.f573q0)).add(((RealFieldElement) ((RealFieldElement) r.f574q1.multiply(this.f574q1)).add(r.f575q2.multiply(this.f575q2))).add(r.f576q3.multiply(this.f576q3)))).negate(), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f573q0.multiply(this.f574q1)).add(((RealFieldElement) r.f575q2.multiply(this.f576q3)).subtract(r.f576q3.multiply(this.f575q2)))).subtract(r.f574q1.multiply(this.f573q0)), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f573q0.multiply(this.f575q2)).add(((RealFieldElement) r.f576q3.multiply(this.f574q1)).subtract(r.f574q1.multiply(this.f576q3)))).subtract(r.f575q2.multiply(this.f573q0)), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) r.f573q0.multiply(this.f576q3)).add(((RealFieldElement) r.f574q1.multiply(this.f575q2)).subtract(r.f575q2.multiply(this.f574q1)))).subtract(r.f576q3.multiply(this.f573q0)), false);
        return fieldRotation;
    }

    public FieldRotation<T> applyInverseTo(Rotation r) {
        return composeInverse(r, RotationConvention.VECTOR_OPERATOR);
    }

    public FieldRotation<T> composeInverse(Rotation r, RotationConvention convention) {
        return convention == RotationConvention.VECTOR_OPERATOR ? composeInverseInternal(r) : applyTo(r, revert());
    }

    private FieldRotation<T> composeInverseInternal(Rotation r) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f573q0.multiply(r.getQ0())).add(((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(r.getQ1())).add(this.f575q2.multiply(r.getQ2()))).add(this.f576q3.multiply(r.getQ3())))).negate(), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f574q1.multiply(r.getQ0())).add(((RealFieldElement) this.f576q3.multiply(r.getQ2())).subtract(this.f575q2.multiply(r.getQ3())))).subtract(this.f573q0.multiply(r.getQ1())), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f575q2.multiply(r.getQ0())).add(((RealFieldElement) this.f574q1.multiply(r.getQ3())).subtract(this.f576q3.multiply(r.getQ1())))).subtract(this.f573q0.multiply(r.getQ2())), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.f576q3.multiply(r.getQ0())).add(((RealFieldElement) this.f575q2.multiply(r.getQ1())).subtract(this.f574q1.multiply(r.getQ2())))).subtract(this.f573q0.multiply(r.getQ3())), false);
        return fieldRotation;
    }

    public static <T extends RealFieldElement<T>> FieldRotation<T> applyInverseTo(Rotation rOuter, FieldRotation<T> rInner) {
        FieldRotation fieldRotation = new FieldRotation((T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f573q0.multiply(rOuter.getQ0())).add(((RealFieldElement) ((RealFieldElement) rInner.f574q1.multiply(rOuter.getQ1())).add(rInner.f575q2.multiply(rOuter.getQ2()))).add(rInner.f576q3.multiply(rOuter.getQ3())))).negate(), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f573q0.multiply(rOuter.getQ1())).add(((RealFieldElement) rInner.f575q2.multiply(rOuter.getQ3())).subtract(rInner.f576q3.multiply(rOuter.getQ2())))).subtract(rInner.f574q1.multiply(rOuter.getQ0())), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f573q0.multiply(rOuter.getQ2())).add(((RealFieldElement) rInner.f576q3.multiply(rOuter.getQ1())).subtract(rInner.f574q1.multiply(rOuter.getQ3())))).subtract(rInner.f575q2.multiply(rOuter.getQ0())), (T) (RealFieldElement) ((RealFieldElement) ((RealFieldElement) rInner.f573q0.multiply(rOuter.getQ3())).add(((RealFieldElement) rInner.f574q1.multiply(rOuter.getQ2())).subtract(rInner.f575q2.multiply(rOuter.getQ1())))).subtract(rInner.f576q3.multiply(rOuter.getQ0())), false);
        return fieldRotation;
    }

    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private T[][] orthogonalizeMatrix(T[][] r64, double r65) throws org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException {
        /*
            r63 = this;
            r1 = 0
            r2 = r64[r1]
            r2 = r2[r1]
            r3 = r64[r1]
            r4 = 1
            r3 = r3[r4]
            r5 = r64[r1]
            r6 = 2
            r5 = r5[r6]
            r7 = r64[r4]
            r7 = r7[r1]
            r8 = r64[r4]
            r8 = r8[r4]
            r9 = r64[r4]
            r9 = r9[r6]
            r10 = r64[r6]
            r10 = r10[r1]
            r11 = r64[r6]
            r11 = r11[r4]
            r12 = r64[r6]
            r12 = r12[r6]
            r13 = 0
            r15 = r64[r1]
            r15 = r15[r1]
            org.apache.commons.math3.Field r15 = r15.getField()
            r6 = 3
            java.lang.Object[][] r6 = org.apache.commons.math3.util.MathArrays.buildArray(r15, r6, r6)
            org.apache.commons.math3.RealFieldElement[][] r6 = (org.apache.commons.math3.RealFieldElement[][]) r6
            r14 = r13
            r13 = r12
            r12 = r9
            r9 = r5
            r5 = r3
            r3 = r2
            r2 = 0
        L_0x003f:
            int r2 = r2 + r4
            r4 = 11
            if (r2 >= r4) goto L_0x0513
            r4 = r64[r1]
            r4 = r4[r1]
            java.lang.Object r4 = r4.multiply(r3)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r17 = 1
            r18 = r64[r17]
            r19 = r2
            r2 = r18[r1]
            java.lang.Object r2 = r2.multiply(r7)
            java.lang.Object r2 = r4.add(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r4 = 2
            r18 = r64[r4]
            r4 = r18[r1]
            java.lang.Object r4 = r4.multiply(r10)
            java.lang.Object r2 = r2.add(r4)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r4 = r64[r1]
            r17 = 1
            r4 = r4[r17]
            java.lang.Object r4 = r4.multiply(r3)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r18 = r64[r17]
            r1 = r18[r17]
            java.lang.Object r1 = r1.multiply(r7)
            java.lang.Object r1 = r4.add(r1)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r4 = 2
            r16 = r64[r4]
            r4 = r16[r17]
            java.lang.Object r4 = r4.multiply(r10)
            java.lang.Object r1 = r1.add(r4)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r4 = 0
            r16 = r64[r4]
            r22 = r14
            r4 = 2
            r14 = r16[r4]
            java.lang.Object r14 = r14.multiply(r3)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r15 = r64[r17]
            r15 = r15[r4]
            java.lang.Object r15 = r15.multiply(r7)
            java.lang.Object r14 = r14.add(r15)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r15 = r64[r4]
            r15 = r15[r4]
            java.lang.Object r4 = r15.multiply(r10)
            java.lang.Object r4 = r14.add(r4)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r14 = 0
            r15 = r64[r14]
            r15 = r15[r14]
            java.lang.Object r15 = r15.multiply(r5)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r17 = 1
            r18 = r64[r17]
            r24 = r10
            r10 = r18[r14]
            java.lang.Object r10 = r10.multiply(r8)
            java.lang.Object r10 = r15.add(r10)
            org.apache.commons.math3.RealFieldElement r10 = (org.apache.commons.math3.RealFieldElement) r10
            r15 = 2
            r18 = r64[r15]
            r15 = r18[r14]
            java.lang.Object r15 = r15.multiply(r11)
            java.lang.Object r10 = r10.add(r15)
            org.apache.commons.math3.RealFieldElement r10 = (org.apache.commons.math3.RealFieldElement) r10
            r15 = r64[r14]
            r14 = 1
            r15 = r15[r14]
            java.lang.Object r15 = r15.multiply(r5)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r17 = r64[r14]
            r25 = r7
            r7 = r17[r14]
            java.lang.Object r7 = r7.multiply(r8)
            java.lang.Object r7 = r15.add(r7)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r15 = 2
            r16 = r64[r15]
            r15 = r16[r14]
            java.lang.Object r15 = r15.multiply(r11)
            java.lang.Object r7 = r7.add(r15)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r15 = 0
            r16 = r64[r15]
            r15 = 2
            r14 = r16[r15]
            java.lang.Object r14 = r14.multiply(r5)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r16 = 1
            r18 = r64[r16]
            r28 = r7
            r7 = r18[r15]
            java.lang.Object r7 = r7.multiply(r8)
            java.lang.Object r7 = r14.add(r7)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r14 = r64[r15]
            r14 = r14[r15]
            java.lang.Object r14 = r14.multiply(r11)
            java.lang.Object r7 = r7.add(r14)
            org.apache.commons.math3.RealFieldElement r7 = (org.apache.commons.math3.RealFieldElement) r7
            r14 = 0
            r15 = r64[r14]
            r15 = r15[r14]
            java.lang.Object r15 = r15.multiply(r9)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r17 = 1
            r18 = r64[r17]
            r29 = r11
            r11 = r18[r14]
            java.lang.Object r11 = r11.multiply(r12)
            java.lang.Object r11 = r15.add(r11)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            r15 = 2
            r18 = r64[r15]
            r15 = r18[r14]
            java.lang.Object r15 = r15.multiply(r13)
            java.lang.Object r11 = r11.add(r15)
            org.apache.commons.math3.RealFieldElement r11 = (org.apache.commons.math3.RealFieldElement) r11
            r15 = r64[r14]
            r14 = 1
            r15 = r15[r14]
            java.lang.Object r15 = r15.multiply(r9)
            org.apache.commons.math3.RealFieldElement r15 = (org.apache.commons.math3.RealFieldElement) r15
            r17 = r64[r14]
            r30 = r8
            r8 = r17[r14]
            java.lang.Object r8 = r8.multiply(r12)
            java.lang.Object r8 = r15.add(r8)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r15 = 2
            r16 = r64[r15]
            r15 = r16[r14]
            java.lang.Object r15 = r15.multiply(r13)
            java.lang.Object r8 = r8.add(r15)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r15 = 0
            r16 = r64[r15]
            r15 = 2
            r14 = r16[r15]
            java.lang.Object r14 = r14.multiply(r9)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r16 = 1
            r18 = r64[r16]
            r33 = r8
            r8 = r18[r15]
            java.lang.Object r8 = r8.multiply(r12)
            java.lang.Object r8 = r14.add(r8)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r14 = r64[r15]
            r14 = r14[r15]
            java.lang.Object r14 = r14.multiply(r13)
            java.lang.Object r8 = r8.add(r14)
            org.apache.commons.math3.RealFieldElement r8 = (org.apache.commons.math3.RealFieldElement) r8
            r14 = 0
            r15 = r6[r14]
            java.lang.Object r18 = r3.multiply(r2)
            r14 = r18
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r35 = r13
            java.lang.Object r13 = r5.multiply(r1)
            java.lang.Object r13 = r14.add(r13)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            java.lang.Object r14 = r9.multiply(r4)
            java.lang.Object r13 = r13.add(r14)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            r14 = 0
            r18 = r64[r14]
            r36 = r4
            r4 = r18[r14]
            java.lang.Object r4 = r13.subtract(r4)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r37 = r15
            r14 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r4 = r4.multiply(r14)
            java.lang.Object r4 = r3.subtract(r4)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r13 = 0
            r37[r13] = r4
            r4 = r6[r13]
            java.lang.Object r18 = r3.multiply(r10)
            r14 = r18
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r15 = r28
            java.lang.Object r13 = r5.multiply(r15)
            java.lang.Object r13 = r14.add(r13)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            java.lang.Object r14 = r9.multiply(r7)
            java.lang.Object r13 = r13.add(r14)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            r14 = 0
            r18 = r64[r14]
            r17 = 1
            r14 = r18[r17]
            java.lang.Object r13 = r13.subtract(r14)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            r43 = r15
            r14 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r13 = r13.multiply(r14)
            java.lang.Object r13 = r5.subtract(r13)
            org.apache.commons.math3.RealFieldElement r13 = (org.apache.commons.math3.RealFieldElement) r13
            r4[r17] = r13
            r4 = 0
            r13 = r6[r4]
            java.lang.Object r14 = r3.multiply(r11)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r15 = r33
            java.lang.Object r4 = r5.multiply(r15)
            java.lang.Object r4 = r14.add(r4)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            java.lang.Object r14 = r9.multiply(r8)
            java.lang.Object r4 = r4.add(r14)
            org.apache.commons.math3.RealFieldElement r4 = (org.apache.commons.math3.RealFieldElement) r4
            r14 = 0
            r18 = r64[r14]
            r44 = r3
            r14 = 2
            r3 = r18[r14]
            java.lang.Object r3 = r4.subtract(r3)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r45 = r15
            r14 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r3 = r3.multiply(r14)
            java.lang.Object r3 = r9.subtract(r3)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r4 = 2
            r13[r4] = r3
            r3 = 1
            r4 = r6[r3]
            r13 = r25
            java.lang.Object r14 = r13.multiply(r2)
            org.apache.commons.math3.RealFieldElement r14 = (org.apache.commons.math3.RealFieldElement) r14
            r15 = r30
            java.lang.Object r3 = r15.multiply(r1)
            java.lang.Object r3 = r14.add(r3)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r46 = r5
            r14 = r36
            java.lang.Object r5 = r12.multiply(r14)
            java.lang.Object r3 = r3.add(r5)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r5 = 1
            r17 = r64[r5]
            r18 = 0
            r5 = r17[r18]
            java.lang.Object r3 = r3.subtract(r5)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r48 = r1
            r47 = r2
            r1 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r3 = r3.multiply(r1)
            java.lang.Object r1 = r13.subtract(r3)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r4[r18] = r1
            r1 = 1
            r2 = r6[r1]
            java.lang.Object r3 = r13.multiply(r10)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r4 = r43
            java.lang.Object r5 = r15.multiply(r4)
            java.lang.Object r3 = r3.add(r5)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            java.lang.Object r5 = r12.multiply(r7)
            java.lang.Object r3 = r3.add(r5)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r5 = r64[r1]
            r5 = r5[r1]
            java.lang.Object r3 = r3.subtract(r5)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r49 = r2
            r1 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r3 = r3.multiply(r1)
            java.lang.Object r1 = r15.subtract(r3)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r2 = 1
            r49[r2] = r1
            r1 = r6[r2]
            java.lang.Object r3 = r13.multiply(r11)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r5 = r45
            java.lang.Object r2 = r15.multiply(r5)
            java.lang.Object r2 = r3.add(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            java.lang.Object r3 = r12.multiply(r8)
            java.lang.Object r2 = r2.add(r3)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r3 = 1
            r18 = r64[r3]
            r50 = r9
            r3 = 2
            r9 = r18[r3]
            java.lang.Object r2 = r2.subtract(r9)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r51 = r4
            r3 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r2 = r2.multiply(r3)
            java.lang.Object r2 = r12.subtract(r2)
            org.apache.commons.math3.RealFieldElement r2 = (org.apache.commons.math3.RealFieldElement) r2
            r3 = 2
            r1[r3] = r2
            r1 = r6[r3]
            r2 = r24
            r4 = r47
            java.lang.Object r9 = r2.multiply(r4)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r52 = r4
            r53 = r12
            r4 = r29
            r3 = r48
            java.lang.Object r12 = r4.multiply(r3)
            java.lang.Object r9 = r9.add(r12)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r54 = r3
            r12 = r35
            java.lang.Object r3 = r12.multiply(r14)
            java.lang.Object r3 = r9.add(r3)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r9 = 2
            r16 = r64[r9]
            r18 = 0
            r9 = r16[r18]
            java.lang.Object r3 = r3.subtract(r9)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r55 = r13
            r56 = r14
            r13 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r3 = r3.multiply(r13)
            java.lang.Object r3 = r2.subtract(r3)
            org.apache.commons.math3.RealFieldElement r3 = (org.apache.commons.math3.RealFieldElement) r3
            r1[r18] = r3
            r1 = 2
            r3 = r6[r1]
            java.lang.Object r9 = r2.multiply(r10)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r13 = r51
            java.lang.Object r14 = r4.multiply(r13)
            java.lang.Object r9 = r9.add(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r14 = r12.multiply(r7)
            java.lang.Object r9 = r9.add(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r14 = r64[r1]
            r16 = 1
            r14 = r14[r16]
            java.lang.Object r9 = r9.subtract(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r57 = r2
            r1 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r9 = r9.multiply(r1)
            java.lang.Object r1 = r4.subtract(r9)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r3[r16] = r1
            r1 = 2
            r2 = r6[r1]
            r3 = r57
            java.lang.Object r9 = r3.multiply(r11)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r14 = r4.multiply(r5)
            java.lang.Object r9 = r9.add(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            java.lang.Object r14 = r12.multiply(r8)
            java.lang.Object r9 = r9.add(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r14 = r64[r1]
            r14 = r14[r1]
            java.lang.Object r9 = r9.subtract(r14)
            org.apache.commons.math3.RealFieldElement r9 = (org.apache.commons.math3.RealFieldElement) r9
            r58 = r2
            r1 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            java.lang.Object r1 = r9.multiply(r1)
            java.lang.Object r1 = r12.subtract(r1)
            org.apache.commons.math3.RealFieldElement r1 = (org.apache.commons.math3.RealFieldElement) r1
            r2 = 2
            r58[r2] = r1
            r1 = 0
            r2 = r6[r1]
            r2 = r2[r1]
            double r20 = r2.getReal()
            r2 = r64[r1]
            r2 = r2[r1]
            double r24 = r2.getReal()
            double r24 = r20 - r24
            r2 = r6[r1]
            r9 = 1
            r2 = r2[r9]
            double r20 = r2.getReal()
            r2 = r64[r1]
            r2 = r2[r9]
            double r26 = r2.getReal()
            double r26 = r20 - r26
            r2 = r6[r1]
            r9 = 2
            r2 = r2[r9]
            double r20 = r2.getReal()
            r2 = r64[r1]
            r2 = r2[r9]
            double r28 = r2.getReal()
            double r28 = r20 - r28
            r2 = 1
            r9 = r6[r2]
            r9 = r9[r1]
            double r17 = r9.getReal()
            r9 = r64[r2]
            r9 = r9[r1]
            double r30 = r9.getReal()
            double r30 = r17 - r30
            r1 = r6[r2]
            r1 = r1[r2]
            double r17 = r1.getReal()
            r1 = r64[r2]
            r1 = r1[r2]
            double r32 = r1.getReal()
            double r32 = r17 - r32
            r1 = r6[r2]
            r9 = 2
            r1 = r1[r9]
            double r34 = r1.getReal()
            r1 = r64[r2]
            r1 = r1[r9]
            double r1 = r1.getReal()
            double r34 = r34 - r1
            r1 = r6[r9]
            r2 = 0
            r1 = r1[r2]
            double r36 = r1.getReal()
            r1 = r64[r9]
            r1 = r1[r2]
            double r1 = r1.getReal()
            double r36 = r36 - r1
            r1 = r6[r9]
            r2 = 1
            r1 = r1[r2]
            double r38 = r1.getReal()
            r1 = r64[r9]
            r1 = r1[r2]
            double r1 = r1.getReal()
            double r38 = r38 - r1
            r1 = r6[r9]
            r1 = r1[r9]
            double r1 = r1.getReal()
            r14 = r64[r9]
            r14 = r14[r9]
            double r40 = r14.getReal()
            double r1 = r1 - r40
            double r40 = r24 * r24
            double r42 = r26 * r26
            double r40 = r40 + r42
            double r42 = r28 * r28
            double r40 = r40 + r42
            double r42 = r30 * r30
            double r40 = r40 + r42
            double r42 = r32 * r32
            double r40 = r40 + r42
            double r42 = r34 * r34
            double r40 = r40 + r42
            double r42 = r36 * r36
            double r40 = r40 + r42
            double r42 = r38 * r38
            double r40 = r40 + r42
            double r42 = r1 * r1
            double r40 = r40 + r42
            r59 = r1
            double r0 = r40 - r22
            double r0 = org.apache.commons.math3.util.FastMath.abs(r0)
            int r0 = (r0 > r65 ? 1 : (r0 == r65 ? 0 : -1))
            if (r0 > 0) goto L_0x04d3
            return r6
        L_0x04d3:
            r0 = 0
            r1 = r6[r0]
            r1 = r1[r0]
            r2 = r6[r0]
            r9 = 1
            r2 = r2[r9]
            r14 = r6[r0]
            r16 = 2
            r14 = r14[r16]
            r17 = r6[r9]
            r17 = r17[r0]
            r18 = r6[r9]
            r15 = r18[r9]
            r18 = r6[r9]
            r18 = r18[r16]
            r20 = r6[r16]
            r3 = r20[r0]
            r0 = r6[r16]
            r0 = r0[r9]
            r4 = r6[r16]
            r4 = r4[r16]
            r7 = r40
            r11 = r0
            r5 = r2
            r10 = r3
            r13 = r4
            r9 = r14
            r12 = r18
            r2 = r19
            r4 = 1
            r3 = r1
            r1 = 0
            r61 = r7
            r8 = r15
            r14 = r61
            r7 = r17
            goto L_0x003f
        L_0x0513:
            r19 = r2
            r44 = r3
            r46 = r5
            r55 = r7
            r50 = r9
            r3 = r10
            r4 = r11
            r53 = r12
            r12 = r13
            r22 = r14
            r9 = 1
            r15 = r8
            org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException r0 = new org.apache.commons.math3.geometry.euclidean.threed.NotARotationMatrixException
            org.apache.commons.math3.exception.util.LocalizedFormats r1 = org.apache.commons.math3.exception.util.LocalizedFormats.UNABLE_TO_ORTHOGONOLIZE_MATRIX
            java.lang.Object[] r2 = new java.lang.Object[r9]
            int r5 = r19 + -1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r7 = 0
            r2[r7] = r5
            r0.<init>(r1, r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.geometry.euclidean.threed.FieldRotation.orthogonalizeMatrix(org.apache.commons.math3.RealFieldElement[][], double):org.apache.commons.math3.RealFieldElement[][]");
    }

    public static <T extends RealFieldElement<T>> T distance(FieldRotation<T> r1, FieldRotation<T> r2) {
        return r1.composeInverseInternal(r2).getAngle();
    }
}
