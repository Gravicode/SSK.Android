package org.apache.commons.math3.stat.descriptive.rank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.interpolation.NevilleInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.stat.descriptive.AbstractStorelessUnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.StorelessUnivariateStatistic;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

public class PSquarePercentile extends AbstractStorelessUnivariateStatistic implements StorelessUnivariateStatistic, Serializable {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00.00");
    private static final double DEFAULT_QUANTILE_DESIRED = 50.0d;
    private static final int PSQUARE_CONSTANT = 5;
    private static final long serialVersionUID = 2283912083175715479L;
    private long countOfObservations;
    private final List<Double> initialFive;
    private transient double lastObservation;
    private PSquareMarkers markers;
    private double pValue;
    private final double quantile;

    private static class FixedCapacityList<E> extends ArrayList<E> implements Serializable {
        private static final long serialVersionUID = 2283952083075725479L;
        private final int capacity;

        FixedCapacityList(int fixedCapacity) {
            super(fixedCapacity);
            this.capacity = fixedCapacity;
        }

        public boolean add(E e) {
            if (size() < this.capacity) {
                return super.add(e);
            }
            return false;
        }

        public boolean addAll(Collection<? extends E> collection) {
            if (collection != null && collection.size() + size() <= this.capacity) {
                return super.addAll(collection);
            }
            return false;
        }
    }

    private static class Marker implements Serializable, Cloneable {
        private static final long serialVersionUID = -3575879478288538431L;
        private double desiredMarkerIncrement;
        private double desiredMarkerPosition;
        private int index;
        private double intMarkerPosition;
        private transient UnivariateInterpolator linear;
        /* access modifiers changed from: private */
        public double markerHeight;
        private transient Marker next;
        private final UnivariateInterpolator nonLinear;
        private transient Marker previous;

        private Marker() {
            this.nonLinear = new NevilleInterpolator();
            this.linear = new LinearInterpolator();
            this.previous = this;
            this.next = this;
        }

        private Marker(double heightOfMarker, double makerPositionDesired, double markerPositionIncrement, double markerPositionNumber) {
            this();
            this.markerHeight = heightOfMarker;
            this.desiredMarkerPosition = makerPositionDesired;
            this.desiredMarkerIncrement = markerPositionIncrement;
            this.intMarkerPosition = markerPositionNumber;
        }

        /* access modifiers changed from: private */
        public Marker previous(Marker previousMarker) {
            MathUtils.checkNotNull(previousMarker);
            this.previous = previousMarker;
            return this;
        }

        /* access modifiers changed from: private */
        public Marker next(Marker nextMarker) {
            MathUtils.checkNotNull(nextMarker);
            this.next = nextMarker;
            return this;
        }

        /* access modifiers changed from: private */
        public Marker index(int indexOfMarker) {
            this.index = indexOfMarker;
            return this;
        }

        /* access modifiers changed from: private */
        public void updateDesiredPosition() {
            this.desiredMarkerPosition += this.desiredMarkerIncrement;
        }

        /* access modifiers changed from: private */
        public void incrementPosition(int d) {
            this.intMarkerPosition += (double) d;
        }

        private double difference() {
            return this.desiredMarkerPosition - this.intMarkerPosition;
        }

        /* access modifiers changed from: private */
        public double estimate() {
            double di = difference();
            boolean isNextHigher = this.next.intMarkerPosition - this.intMarkerPosition > 1.0d;
            boolean isPreviousLower = this.previous.intMarkerPosition - this.intMarkerPosition < -1.0d;
            if ((di < 1.0d || !isNextHigher) && (di > -1.0d || !isPreviousLower)) {
            } else {
                int i = -1;
                int d = di >= 0.0d ? 1 : -1;
                double[] xval = {this.previous.intMarkerPosition, this.intMarkerPosition, this.next.intMarkerPosition};
                double[] yval = {this.previous.markerHeight, this.markerHeight, this.next.markerHeight};
                double xD = this.intMarkerPosition + ((double) d);
                this.markerHeight = this.nonLinear.interpolate(xval, yval).value(xD);
                if (isEstimateBad(yval, this.markerHeight)) {
                    if (xD - xval[1] > 0.0d) {
                        i = 1;
                    }
                    int delta = i;
                    double[] xBad = {xval[1], xval[delta + 1]};
                    double[] yBad = {yval[1], yval[delta + 1]};
                    MathArrays.sortInPlace(xBad, yBad);
                    double d2 = di;
                    this.markerHeight = this.linear.interpolate(xBad, yBad).value(xD);
                }
                incrementPosition(d);
            }
            return this.markerHeight;
        }

        private boolean isEstimateBad(double[] y, double yD) {
            return yD <= y[0] || yD >= y[2];
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Marker)) {
                return false;
            }
            Marker that = (Marker) o;
            boolean z = false;
            if ((((((Double.compare(this.markerHeight, that.markerHeight) == 0) && Double.compare(this.intMarkerPosition, that.intMarkerPosition) == 0) && Double.compare(this.desiredMarkerPosition, that.desiredMarkerPosition) == 0) && Double.compare(this.desiredMarkerIncrement, that.desiredMarkerIncrement) == 0) && this.next.index == that.next.index) && this.previous.index == that.previous.index) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return Arrays.hashCode(new double[]{this.markerHeight, this.intMarkerPosition, this.desiredMarkerIncrement, this.desiredMarkerPosition, (double) this.previous.index, (double) this.next.index});
        }

        private void readObject(ObjectInputStream anInstream) throws ClassNotFoundException, IOException {
            anInstream.defaultReadObject();
            this.next = this;
            this.previous = this;
            this.linear = new LinearInterpolator();
        }

        public Object clone() {
            Marker marker = new Marker(this.markerHeight, this.desiredMarkerPosition, this.desiredMarkerIncrement, this.intMarkerPosition);
            return marker;
        }

        public String toString() {
            return String.format("index=%.0f,n=%.0f,np=%.2f,q=%.2f,dn=%.2f,prev=%d,next=%d", new Object[]{Double.valueOf((double) this.index), Double.valueOf(Precision.round(this.intMarkerPosition, 0)), Double.valueOf(Precision.round(this.desiredMarkerPosition, 2)), Double.valueOf(Precision.round(this.markerHeight, 2)), Double.valueOf(Precision.round(this.desiredMarkerIncrement, 2)), Integer.valueOf(this.previous.index), Integer.valueOf(this.next.index)});
        }
    }

    private static class Markers implements PSquareMarkers, Serializable {
        private static final int HIGH = 4;
        private static final int LOW = 2;
        private static final long serialVersionUID = 1;

        /* renamed from: k */
        private transient int f786k;
        private final Marker[] markerArray;

        private Markers(Marker[] theMarkerArray) {
            this.f786k = -1;
            MathUtils.checkNotNull(theMarkerArray);
            this.markerArray = theMarkerArray;
            for (int i = 1; i < 5; i++) {
                this.markerArray[i].previous(this.markerArray[i - 1]).next(this.markerArray[i + 1]).index(i);
            }
            this.markerArray[0].previous(this.markerArray[0]).next(this.markerArray[1]).index(0);
            this.markerArray[5].previous(this.markerArray[4]).next(this.markerArray[5]).index(5);
        }

        private Markers(List<Double> initialFive, double p) {
            this(createMarkerArray(initialFive, p));
        }

        private static Marker[] createMarkerArray(List<Double> initialFive, double p) {
            List<Double> list = initialFive;
            int countObserved = list == null ? -1 : initialFive.size();
            if (countObserved < 5) {
                throw new InsufficientDataException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(countObserved), Integer.valueOf(5));
            }
            Collections.sort(initialFive);
            Marker marker = new Marker(((Double) list.get(0)).doubleValue(), 1.0d, 0.0d, 1.0d);
            Marker marker2 = new Marker(((Double) list.get(1)).doubleValue(), (p * 2.0d) + 1.0d, p / 2.0d, 2.0d);
            Marker marker3 = new Marker(((Double) list.get(2)).doubleValue(), (4.0d * p) + 1.0d, p, 3.0d);
            Marker marker4 = new Marker(((Double) list.get(3)).doubleValue(), (p * 2.0d) + 3.0d, (p + 1.0d) / 2.0d, 4.0d);
            Marker marker5 = new Marker(((Double) list.get(4)).doubleValue(), 5.0d, 1.0d, 5.0d);
            return new Marker[]{new Marker(), marker, marker2, marker3, marker4, marker5};
        }

        public int hashCode() {
            return Arrays.deepHashCode(this.markerArray);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || !(o instanceof Markers)) {
                return false;
            }
            return Arrays.deepEquals(this.markerArray, ((Markers) o).markerArray);
        }

        public double processDataPoint(double inputDataPoint) {
            incrementPositions(1, findCellAndUpdateMinMax(inputDataPoint) + 1, 5);
            updateDesiredPositions();
            adjustHeightsOfMarkers();
            return getPercentileValue();
        }

        public double getPercentileValue() {
            return height(3);
        }

        private int findCellAndUpdateMinMax(double observation) {
            this.f786k = -1;
            if (observation < height(1)) {
                this.markerArray[1].markerHeight = observation;
                this.f786k = 1;
            } else if (observation < height(2)) {
                this.f786k = 1;
            } else if (observation < height(3)) {
                this.f786k = 2;
            } else if (observation < height(4)) {
                this.f786k = 3;
            } else if (observation <= height(5)) {
                this.f786k = 4;
            } else {
                this.markerArray[5].markerHeight = observation;
                this.f786k = 4;
            }
            return this.f786k;
        }

        private void adjustHeightsOfMarkers() {
            for (int i = 2; i <= 4; i++) {
                estimate(i);
            }
        }

        public double estimate(int index) {
            if (index >= 2 && index <= 4) {
                return this.markerArray[index].estimate();
            }
            throw new OutOfRangeException(Integer.valueOf(index), Integer.valueOf(2), Integer.valueOf(4));
        }

        private void incrementPositions(int d, int startIndex, int endIndex) {
            for (int i = startIndex; i <= endIndex; i++) {
                this.markerArray[i].incrementPosition(d);
            }
        }

        private void updateDesiredPositions() {
            for (int i = 1; i < this.markerArray.length; i++) {
                this.markerArray[i].updateDesiredPosition();
            }
        }

        private void readObject(ObjectInputStream anInputStream) throws ClassNotFoundException, IOException {
            anInputStream.defaultReadObject();
            for (int i = 1; i < 5; i++) {
                this.markerArray[i].previous(this.markerArray[i - 1]).next(this.markerArray[i + 1]).index(i);
            }
            this.markerArray[0].previous(this.markerArray[0]).next(this.markerArray[1]).index(0);
            this.markerArray[5].previous(this.markerArray[4]).next(this.markerArray[5]).index(5);
        }

        public double height(int markerIndex) {
            if (markerIndex < this.markerArray.length && markerIndex > 0) {
                return this.markerArray[markerIndex].markerHeight;
            }
            throw new OutOfRangeException(Integer.valueOf(markerIndex), Integer.valueOf(1), Integer.valueOf(this.markerArray.length));
        }

        public Object clone() {
            return new Markers(new Marker[]{new Marker(), (Marker) this.markerArray[1].clone(), (Marker) this.markerArray[2].clone(), (Marker) this.markerArray[3].clone(), (Marker) this.markerArray[4].clone(), (Marker) this.markerArray[5].clone()});
        }

        public String toString() {
            return String.format("m1=[%s],m2=[%s],m3=[%s],m4=[%s],m5=[%s]", new Object[]{this.markerArray[1].toString(), this.markerArray[2].toString(), this.markerArray[3].toString(), this.markerArray[4].toString(), this.markerArray[5].toString()});
        }
    }

    protected interface PSquareMarkers extends Cloneable {
        Object clone();

        double estimate(int i);

        double getPercentileValue();

        double height(int i);

        double processDataPoint(double d);
    }

    public PSquarePercentile(double p) {
        this.initialFive = new FixedCapacityList(5);
        this.markers = null;
        this.pValue = Double.NaN;
        if (p > 100.0d || p < 0.0d) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE, Double.valueOf(p), Integer.valueOf(0), Integer.valueOf(100));
        }
        this.quantile = p / 100.0d;
    }

    PSquarePercentile() {
        this(DEFAULT_QUANTILE_DESIRED);
    }

    public int hashCode() {
        double result = getResult();
        return Arrays.hashCode(new double[]{Double.isNaN(result) ? 37.0d : result, this.quantile, this.markers == null ? 0.0d : (double) this.markers.hashCode(), (double) this.countOfObservations});
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof PSquarePercentile)) {
            return false;
        }
        PSquarePercentile that = (PSquarePercentile) o;
        boolean z = false;
        if ((this.markers != null && that.markers != null ? this.markers.equals(that.markers) : this.markers == null && that.markers == null) && getN() == that.getN()) {
            z = true;
        }
        return z;
    }

    public void increment(double observation) {
        this.countOfObservations++;
        this.lastObservation = observation;
        if (this.markers == null) {
            if (this.initialFive.add(Double.valueOf(observation))) {
                Collections.sort(this.initialFive);
                this.pValue = ((Double) this.initialFive.get((int) (this.quantile * ((double) (this.initialFive.size() - 1))))).doubleValue();
                return;
            }
            this.markers = newMarkers(this.initialFive, this.quantile);
        }
        this.pValue = this.markers.processDataPoint(observation);
    }

    public String toString() {
        if (this.markers == null) {
            return String.format("obs=%s pValue=%s", new Object[]{DECIMAL_FORMAT.format(this.lastObservation), DECIMAL_FORMAT.format(this.pValue)});
        }
        return String.format("obs=%s markers=%s", new Object[]{DECIMAL_FORMAT.format(this.lastObservation), this.markers.toString()});
    }

    public long getN() {
        return this.countOfObservations;
    }

    public StorelessUnivariateStatistic copy() {
        PSquarePercentile copy = new PSquarePercentile(this.quantile * 100.0d);
        if (this.markers != null) {
            copy.markers = (PSquareMarkers) this.markers.clone();
        }
        copy.countOfObservations = this.countOfObservations;
        copy.pValue = this.pValue;
        copy.initialFive.clear();
        copy.initialFive.addAll(this.initialFive);
        return copy;
    }

    public double quantile() {
        return this.quantile;
    }

    public void clear() {
        this.markers = null;
        this.initialFive.clear();
        this.countOfObservations = 0;
        this.pValue = Double.NaN;
    }

    public double getResult() {
        if (Double.compare(this.quantile, 1.0d) == 0) {
            this.pValue = maximum();
        } else if (Double.compare(this.quantile, 0.0d) == 0) {
            this.pValue = minimum();
        }
        return this.pValue;
    }

    private double maximum() {
        if (this.markers != null) {
            return this.markers.height(5);
        }
        if (!this.initialFive.isEmpty()) {
            return ((Double) this.initialFive.get(this.initialFive.size() - 1)).doubleValue();
        }
        return Double.NaN;
    }

    private double minimum() {
        if (this.markers != null) {
            return this.markers.height(1);
        }
        if (!this.initialFive.isEmpty()) {
            return ((Double) this.initialFive.get(0)).doubleValue();
        }
        return Double.NaN;
    }

    public static PSquareMarkers newMarkers(List<Double> initialFive2, double p) {
        return new Markers(initialFive2, p);
    }
}
