package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

public class ArrayRealVector extends RealVector implements Serializable {
    private static final RealVectorFormat DEFAULT_FORMAT = RealVectorFormat.getInstance();
    private static final long serialVersionUID = -1097961340710804027L;
    private double[] data;

    public ArrayRealVector() {
        this.data = new double[0];
    }

    public ArrayRealVector(int size) {
        this.data = new double[size];
    }

    public ArrayRealVector(int size, double preset) {
        this.data = new double[size];
        Arrays.fill(this.data, preset);
    }

    public ArrayRealVector(double[] d) {
        this.data = (double[]) d.clone();
    }

    public ArrayRealVector(double[] d, boolean copyArray) throws NullArgumentException {
        if (d == null) {
            throw new NullArgumentException();
        }
        this.data = copyArray ? (double[]) d.clone() : d;
    }

    public ArrayRealVector(double[] d, int pos, int size) throws NullArgumentException, NumberIsTooLargeException {
        if (d == null) {
            throw new NullArgumentException();
        } else if (d.length < pos + size) {
            throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
        } else {
            this.data = new double[size];
            System.arraycopy(d, pos, this.data, 0, size);
        }
    }

    public ArrayRealVector(Double[] d) {
        this.data = new double[d.length];
        for (int i = 0; i < d.length; i++) {
            this.data[i] = d[i].doubleValue();
        }
    }

    public ArrayRealVector(Double[] d, int pos, int size) throws NullArgumentException, NumberIsTooLargeException {
        if (d == null) {
            throw new NullArgumentException();
        } else if (d.length < pos + size) {
            throw new NumberIsTooLargeException(Integer.valueOf(pos + size), Integer.valueOf(d.length), true);
        } else {
            this.data = new double[size];
            for (int i = pos; i < pos + size; i++) {
                this.data[i - pos] = d[i].doubleValue();
            }
        }
    }

    public ArrayRealVector(RealVector v) throws NullArgumentException {
        if (v == null) {
            throw new NullArgumentException();
        }
        this.data = new double[v.getDimension()];
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = v.getEntry(i);
        }
    }

    public ArrayRealVector(ArrayRealVector v) throws NullArgumentException {
        this(v, true);
    }

    public ArrayRealVector(ArrayRealVector v, boolean deep) {
        this.data = deep ? (double[]) v.data.clone() : v.data;
    }

    public ArrayRealVector(ArrayRealVector v1, ArrayRealVector v2) {
        this.data = new double[(v1.data.length + v2.data.length)];
        System.arraycopy(v1.data, 0, this.data, 0, v1.data.length);
        System.arraycopy(v2.data, 0, this.data, v1.data.length, v2.data.length);
    }

    public ArrayRealVector(ArrayRealVector v1, RealVector v2) {
        int l1 = v1.data.length;
        int l2 = v2.getDimension();
        this.data = new double[(l1 + l2)];
        int i = 0;
        System.arraycopy(v1.data, 0, this.data, 0, l1);
        while (true) {
            int i2 = i;
            if (i2 < l2) {
                this.data[l1 + i2] = v2.getEntry(i2);
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    public ArrayRealVector(RealVector v1, ArrayRealVector v2) {
        int l1 = v1.getDimension();
        int l2 = v2.data.length;
        this.data = new double[(l1 + l2)];
        for (int i = 0; i < l1; i++) {
            this.data[i] = v1.getEntry(i);
        }
        System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(ArrayRealVector v1, double[] v2) {
        int l1 = v1.getDimension();
        int l2 = v2.length;
        this.data = new double[(l1 + l2)];
        System.arraycopy(v1.data, 0, this.data, 0, l1);
        System.arraycopy(v2, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, ArrayRealVector v2) {
        int l1 = v1.length;
        int l2 = v2.getDimension();
        this.data = new double[(l1 + l2)];
        System.arraycopy(v1, 0, this.data, 0, l1);
        System.arraycopy(v2.data, 0, this.data, l1, l2);
    }

    public ArrayRealVector(double[] v1, double[] v2) {
        int l1 = v1.length;
        int l2 = v2.length;
        this.data = new double[(l1 + l2)];
        System.arraycopy(v1, 0, this.data, 0, l1);
        System.arraycopy(v2, 0, this.data, l1, l2);
    }

    public ArrayRealVector copy() {
        return new ArrayRealVector(this, true);
    }

    public ArrayRealVector add(RealVector v) throws DimensionMismatchException {
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = this.data[i] + vData[i];
            }
            return result;
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        Iterator<Entry> it = v.iterator();
        while (it.hasNext()) {
            Entry e = (Entry) it.next();
            int index = e.getIndex();
            out[index] = out[index] + e.getValue();
        }
        return new ArrayRealVector(out, false);
    }

    public ArrayRealVector subtract(RealVector v) throws DimensionMismatchException {
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = this.data[i] - vData[i];
            }
            return result;
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        Iterator<Entry> it = v.iterator();
        while (it.hasNext()) {
            Entry e = (Entry) it.next();
            int index = e.getIndex();
            out[index] = out[index] - e.getValue();
        }
        return new ArrayRealVector(out, false);
    }

    public ArrayRealVector map(UnivariateFunction function) {
        return copy().mapToSelf(function);
    }

    public ArrayRealVector mapToSelf(UnivariateFunction function) {
        for (int i = 0; i < this.data.length; i++) {
            this.data[i] = function.value(this.data[i]);
        }
        return this;
    }

    public RealVector mapAddToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            double[] dArr = this.data;
            dArr[i] = dArr[i] + d;
        }
        return this;
    }

    public RealVector mapSubtractToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            double[] dArr = this.data;
            dArr[i] = dArr[i] - d;
        }
        return this;
    }

    public RealVector mapMultiplyToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            double[] dArr = this.data;
            dArr[i] = dArr[i] * d;
        }
        return this;
    }

    public RealVector mapDivideToSelf(double d) {
        for (int i = 0; i < this.data.length; i++) {
            double[] dArr = this.data;
            dArr[i] = dArr[i] / d;
        }
        return this;
    }

    public ArrayRealVector ebeMultiply(RealVector v) throws DimensionMismatchException {
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = this.data[i] * vData[i];
            }
            return result;
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        for (int i2 = 0; i2 < this.data.length; i2++) {
            out[i2] = out[i2] * v.getEntry(i2);
        }
        return new ArrayRealVector(out, false);
    }

    public ArrayRealVector ebeDivide(RealVector v) throws DimensionMismatchException {
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            int dim = vData.length;
            checkVectorDimensions(dim);
            ArrayRealVector result = new ArrayRealVector(dim);
            double[] resultData = result.data;
            for (int i = 0; i < dim; i++) {
                resultData[i] = this.data[i] / vData[i];
            }
            return result;
        }
        checkVectorDimensions(v);
        double[] out = (double[]) this.data.clone();
        for (int i2 = 0; i2 < this.data.length; i2++) {
            out[i2] = out[i2] / v.getEntry(i2);
        }
        return new ArrayRealVector(out, false);
    }

    public double[] getDataRef() {
        return this.data;
    }

    public double dotProduct(RealVector v) throws DimensionMismatchException {
        if (!(v instanceof ArrayRealVector)) {
            return super.dotProduct(v);
        }
        double[] vData = ((ArrayRealVector) v).data;
        checkVectorDimensions(vData.length);
        double dot = 0.0d;
        for (int i = 0; i < this.data.length; i++) {
            dot += this.data[i] * vData[i];
        }
        return dot;
    }

    public double getNorm() {
        double sum = 0.0d;
        for (double a : this.data) {
            sum += a * a;
        }
        return FastMath.sqrt(sum);
    }

    public double getL1Norm() {
        double sum = 0.0d;
        for (double a : this.data) {
            sum += FastMath.abs(a);
        }
        return sum;
    }

    public double getLInfNorm() {
        double max = 0.0d;
        for (double a : this.data) {
            max = FastMath.max(max, FastMath.abs(a));
        }
        return max;
    }

    public double getDistance(RealVector v) throws DimensionMismatchException {
        int i = 0;
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double sum = 0.0d;
            while (i < this.data.length) {
                double delta = this.data[i] - vData[i];
                sum += delta * delta;
                i++;
            }
            return FastMath.sqrt(sum);
        }
        checkVectorDimensions(v);
        double sum2 = 0.0d;
        while (true) {
            int i2 = i;
            if (i2 >= this.data.length) {
                return FastMath.sqrt(sum2);
            }
            double delta2 = this.data[i2] - v.getEntry(i2);
            sum2 += delta2 * delta2;
            i = i2 + 1;
        }
    }

    public double getL1Distance(RealVector v) throws DimensionMismatchException {
        int i = 0;
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double sum = 0.0d;
            while (i < this.data.length) {
                sum += FastMath.abs(this.data[i] - vData[i]);
                i++;
            }
            return sum;
        }
        checkVectorDimensions(v);
        double sum2 = 0.0d;
        while (true) {
            int i2 = i;
            if (i2 >= this.data.length) {
                return sum2;
            }
            sum2 += FastMath.abs(this.data[i2] - v.getEntry(i2));
            i = i2 + 1;
        }
    }

    public double getLInfDistance(RealVector v) throws DimensionMismatchException {
        int i = 0;
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            checkVectorDimensions(vData.length);
            double max = 0.0d;
            while (i < this.data.length) {
                max = FastMath.max(max, FastMath.abs(this.data[i] - vData[i]));
                i++;
            }
            return max;
        }
        checkVectorDimensions(v);
        double max2 = 0.0d;
        while (true) {
            int i2 = i;
            if (i2 >= this.data.length) {
                return max2;
            }
            max2 = FastMath.max(max2, FastMath.abs(this.data[i2] - v.getEntry(i2)));
            i = i2 + 1;
        }
    }

    public RealMatrix outerProduct(RealVector v) {
        if (v instanceof ArrayRealVector) {
            double[] vData = ((ArrayRealVector) v).data;
            int m = this.data.length;
            int n = vData.length;
            RealMatrix out = MatrixUtils.createRealMatrix(m, n);
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    out.setEntry(i, j, this.data[i] * vData[j]);
                }
            }
            return out;
        }
        int m2 = this.data.length;
        int n2 = v.getDimension();
        RealMatrix out2 = MatrixUtils.createRealMatrix(m2, n2);
        for (int i2 = 0; i2 < m2; i2++) {
            for (int j2 = 0; j2 < n2; j2++) {
                out2.setEntry(i2, j2, this.data[i2] * v.getEntry(j2));
            }
        }
        return out2;
    }

    public double getEntry(int index) throws OutOfRangeException {
        try {
            return this.data[index];
        } catch (IndexOutOfBoundsException e) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(getDimension() - 1));
        }
    }

    public int getDimension() {
        return this.data.length;
    }

    public RealVector append(RealVector v) {
        try {
            return new ArrayRealVector(this, (ArrayRealVector) v);
        } catch (ClassCastException e) {
            return new ArrayRealVector(this, v);
        }
    }

    public ArrayRealVector append(ArrayRealVector v) {
        return new ArrayRealVector(this, v);
    }

    public RealVector append(double in) {
        double[] out = new double[(this.data.length + 1)];
        System.arraycopy(this.data, 0, out, 0, this.data.length);
        out[this.data.length] = in;
        return new ArrayRealVector(out, false);
    }

    public RealVector getSubVector(int index, int n) throws OutOfRangeException, NotPositiveException {
        if (n < 0) {
            throw new NotPositiveException(LocalizedFormats.NUMBER_OF_ELEMENTS_SHOULD_BE_POSITIVE, Integer.valueOf(n));
        }
        ArrayRealVector out = new ArrayRealVector(n);
        try {
            System.arraycopy(this.data, index, out.data, 0, n);
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((index + n) - 1);
        }
        return out;
    }

    public void setEntry(int index, double value) throws OutOfRangeException {
        try {
            this.data[index] = value;
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
        }
    }

    public void addToEntry(int index, double increment) throws OutOfRangeException {
        try {
            double[] dArr = this.data;
            dArr[index] = dArr[index] + increment;
        } catch (IndexOutOfBoundsException e) {
            throw new OutOfRangeException(LocalizedFormats.INDEX, Integer.valueOf(index), Integer.valueOf(0), Integer.valueOf(this.data.length - 1));
        }
    }

    public void setSubVector(int index, RealVector v) throws OutOfRangeException {
        if (v instanceof ArrayRealVector) {
            setSubVector(index, ((ArrayRealVector) v).data);
            return;
        }
        int i = index;
        while (i < v.getDimension() + index) {
            try {
                this.data[i] = v.getEntry(i - index);
                i++;
            } catch (IndexOutOfBoundsException e) {
                checkIndex(index);
                checkIndex((v.getDimension() + index) - 1);
                return;
            }
        }
    }

    public void setSubVector(int index, double[] v) throws OutOfRangeException {
        try {
            System.arraycopy(v, 0, this.data, index, v.length);
        } catch (IndexOutOfBoundsException e) {
            checkIndex(index);
            checkIndex((v.length + index) - 1);
        }
    }

    public void set(double value) {
        Arrays.fill(this.data, value);
    }

    public double[] toArray() {
        return (double[]) this.data.clone();
    }

    public String toString() {
        return DEFAULT_FORMAT.format(this);
    }

    /* access modifiers changed from: protected */
    public void checkVectorDimensions(RealVector v) throws DimensionMismatchException {
        checkVectorDimensions(v.getDimension());
    }

    /* access modifiers changed from: protected */
    public void checkVectorDimensions(int n) throws DimensionMismatchException {
        if (this.data.length != n) {
            throw new DimensionMismatchException(this.data.length, n);
        }
    }

    public boolean isNaN() {
        for (double v : this.data) {
            if (Double.isNaN(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean isInfinite() {
        if (isNaN()) {
            return false;
        }
        for (double v : this.data) {
            if (Double.isInfinite(v)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RealVector)) {
            return false;
        }
        RealVector rhs = (RealVector) other;
        if (this.data.length != rhs.getDimension()) {
            return false;
        }
        if (rhs.isNaN()) {
            return isNaN();
        }
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] != rhs.getEntry(i)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (isNaN()) {
            return 9;
        }
        return MathUtils.hash(this.data);
    }

    public ArrayRealVector combine(double a, double b, RealVector y) throws DimensionMismatchException {
        return copy().combineToSelf(a, b, y);
    }

    public ArrayRealVector combineToSelf(double a, double b, RealVector y) throws DimensionMismatchException {
        int i = 0;
        if (!(y instanceof ArrayRealVector)) {
            checkVectorDimensions(y);
            while (true) {
                int i2 = i;
                if (i2 >= this.data.length) {
                    break;
                }
                this.data[i2] = (this.data[i2] * a) + (y.getEntry(i2) * b);
                i = i2 + 1;
            }
        } else {
            double[] yData = ((ArrayRealVector) y).data;
            checkVectorDimensions(yData.length);
            while (i < this.data.length) {
                this.data[i] = (this.data[i] * a) + (yData[i] * b);
                i++;
            }
        }
        return this;
    }

    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor) {
        int i = 0;
        visitor.start(this.data.length, 0, this.data.length - 1);
        while (true) {
            int i2 = i;
            if (i2 >= this.data.length) {
                return visitor.end();
            }
            visitor.visit(i2, this.data[i2]);
            i = i2 + 1;
        }
    }

    public double walkInDefaultOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(this.data.length, start, end);
        for (int i = start; i <= end; i++) {
            visitor.visit(i, this.data[i]);
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    public double walkInOptimizedOrder(RealVectorPreservingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }

    public double walkInDefaultOrder(RealVectorChangingVisitor visitor) {
        int i = 0;
        visitor.start(this.data.length, 0, this.data.length - 1);
        while (true) {
            int i2 = i;
            if (i2 >= this.data.length) {
                return visitor.end();
            }
            this.data[i2] = visitor.visit(i2, this.data[i2]);
            i = i2 + 1;
        }
    }

    public double walkInDefaultOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        checkIndices(start, end);
        visitor.start(this.data.length, start, end);
        for (int i = start; i <= end; i++) {
            this.data[i] = visitor.visit(i, this.data[i]);
        }
        return visitor.end();
    }

    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor) {
        return walkInDefaultOrder(visitor);
    }

    public double walkInOptimizedOrder(RealVectorChangingVisitor visitor, int start, int end) throws NumberIsTooSmallException, OutOfRangeException {
        return walkInDefaultOrder(visitor, start, end);
    }
}
