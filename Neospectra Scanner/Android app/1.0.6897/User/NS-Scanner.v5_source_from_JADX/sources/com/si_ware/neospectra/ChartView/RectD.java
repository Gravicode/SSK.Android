package com.si_ware.neospectra.ChartView;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.PrintWriter;

public class RectD implements Parcelable {
    public static final Creator<RectD> CREATOR = new Creator<RectD>() {
        public RectD createFromParcel(Parcel in) {
            RectD r = new RectD();
            r.readFromParcel(in);
            return r;
        }

        public RectD[] newArray(int size) {
            return new RectD[size];
        }
    };
    public double bottom;
    public double left;
    public double right;
    public double top;

    public RectD() {
    }

    public RectD(double left2, double top2, double right2, double bottom2) {
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
    }

    public RectD(RectD r) {
        this.left = r.left;
        this.top = r.top;
        this.right = r.right;
        this.bottom = r.bottom;
    }

    public RectD(Rect r) {
        this.left = (double) r.left;
        this.top = (double) r.top;
        this.right = (double) r.right;
        this.bottom = (double) r.bottom;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RectD r = (RectD) o;
        if (!(this.left == r.left && this.top == r.top && this.right == r.right && this.bottom == r.bottom)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        long j = 0;
        long doubleToLongBits = 31 * (((((this.left != 0.0d ? Double.doubleToLongBits(this.left) : 0) * 31) + (this.top != 0.0d ? Double.doubleToLongBits(this.top) : 0)) * 31) + (this.right != 0.0d ? Double.doubleToLongBits(this.right) : 0));
        if (this.bottom != 0.0d) {
            j = Double.doubleToLongBits(this.bottom);
        }
        return (int) (doubleToLongBits + j);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RectD(");
        sb.append(this.left);
        sb.append(", ");
        sb.append(this.top);
        sb.append(", ");
        sb.append(this.right);
        sb.append(", ");
        sb.append(this.bottom);
        sb.append(")");
        return sb.toString();
    }

    public String toShortString() {
        return toShortString(new StringBuilder(32));
    }

    public String toShortString(StringBuilder sb) {
        sb.setLength(0);
        sb.append('[');
        sb.append(this.left);
        sb.append(',');
        sb.append(this.top);
        sb.append("][");
        sb.append(this.right);
        sb.append(',');
        sb.append(this.bottom);
        sb.append(']');
        return sb.toString();
    }

    public void printShortString(PrintWriter pw) {
        pw.print('[');
        pw.print(this.left);
        pw.print(',');
        pw.print(this.top);
        pw.print("][");
        pw.print(this.right);
        pw.print(',');
        pw.print(this.bottom);
        pw.print(']');
    }

    public final boolean isEmpty() {
        return this.left >= this.right || this.top >= this.bottom;
    }

    public final double width() {
        return this.right - this.left;
    }

    public final double height() {
        return this.bottom - this.top;
    }

    public final double centerX() {
        return (this.left + this.right) * 0.5d;
    }

    public final double centerY() {
        return (this.top + this.bottom) * 0.5d;
    }

    public void setEmpty() {
        this.bottom = 0.0d;
        this.top = 0.0d;
        this.right = 0.0d;
        this.left = 0.0d;
    }

    public void set(double left2, double top2, double right2, double bottom2) {
        this.left = left2;
        this.top = top2;
        this.right = right2;
        this.bottom = bottom2;
    }

    public void set(RectD src) {
        this.left = src.left;
        this.top = src.top;
        this.right = src.right;
        this.bottom = src.bottom;
    }

    public void set(Rect src) {
        this.left = (double) src.left;
        this.top = (double) src.top;
        this.right = (double) src.right;
        this.bottom = (double) src.bottom;
    }

    public void offset(double dx, double dy) {
        this.left += dx;
        this.top += dy;
        this.right += dx;
        this.bottom += dy;
    }

    public void offsetTo(double newLeft, double newTop) {
        this.right += newLeft - this.left;
        this.bottom += newTop - this.top;
        this.left = newLeft;
        this.top = newTop;
    }

    public void inset(double dx, double dy) {
        this.left += dx;
        this.top += dy;
        this.right -= dx;
        this.bottom -= dy;
    }

    public boolean contains(double x, double y) {
        return this.left < this.right && this.top < this.bottom && x >= this.left && x < this.right && y >= this.top && y < this.bottom;
    }

    public boolean contains(double left2, double top2, double right2, double bottom2) {
        return this.left < this.right && this.top < this.bottom && this.left <= left2 && this.top <= top2 && this.right >= right2 && this.bottom >= bottom2;
    }

    public boolean contains(RectD r) {
        return this.left < this.right && this.top < this.bottom && this.left <= r.left && this.top <= r.top && this.right >= r.right && this.bottom >= r.bottom;
    }

    public boolean intersect(double left2, double top2, double right2, double bottom2) {
        if (this.left >= right2 || left2 >= this.right || this.top >= bottom2 || top2 >= this.bottom) {
            return false;
        }
        if (this.left < left2) {
            this.left = left2;
        }
        if (this.top < top2) {
            this.top = top2;
        }
        if (this.right > right2) {
            this.right = right2;
        }
        if (this.bottom > bottom2) {
            this.bottom = bottom2;
        }
        return true;
    }

    public boolean intersect(RectD r) {
        return intersect(r.left, r.top, r.right, r.bottom);
    }

    public boolean setIntersect(RectD a, RectD b) {
        if (a.left >= b.right || b.left >= a.right || a.top >= b.bottom || b.top >= a.bottom) {
            return false;
        }
        this.left = Math.max(a.left, b.left);
        this.top = Math.max(a.top, b.top);
        this.right = Math.min(a.right, b.right);
        this.bottom = Math.min(a.bottom, b.bottom);
        return true;
    }

    public boolean intersects(double left2, double top2, double right2, double bottom2) {
        return this.left < right2 && left2 < this.right && this.top < bottom2 && top2 < this.bottom;
    }

    public static boolean intersects(RectD a, RectD b) {
        return a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom;
    }

    public void union(double left2, double top2, double right2, double bottom2) {
        if (left2 < right2 && top2 < bottom2) {
            if (this.left >= this.right || this.top >= this.bottom) {
                this.left = left2;
                this.top = top2;
                this.right = right2;
                this.bottom = bottom2;
                return;
            }
            if (this.left > left2) {
                this.left = left2;
            }
            if (this.top > top2) {
                this.top = top2;
            }
            if (this.right < right2) {
                this.right = right2;
            }
            if (this.bottom < bottom2) {
                this.bottom = bottom2;
            }
        }
    }

    public void union(RectD r) {
        union(r.left, r.top, r.right, r.bottom);
    }

    public void union(double x, double y) {
        if (x < this.left) {
            this.left = x;
        } else if (x > this.right) {
            this.right = x;
        }
        if (y < this.top) {
            this.top = y;
        } else if (y > this.bottom) {
            this.bottom = y;
        }
    }

    public void sort() {
        if (this.left > this.right) {
            double temp = this.left;
            this.left = this.right;
            this.right = temp;
        }
        if (this.top > this.bottom) {
            double temp2 = this.top;
            this.top = this.bottom;
            this.bottom = temp2;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(this.left);
        out.writeDouble(this.top);
        out.writeDouble(this.right);
        out.writeDouble(this.bottom);
    }

    public void readFromParcel(Parcel in) {
        this.left = in.readDouble();
        this.top = in.readDouble();
        this.right = in.readDouble();
        this.bottom = in.readDouble();
    }
}
