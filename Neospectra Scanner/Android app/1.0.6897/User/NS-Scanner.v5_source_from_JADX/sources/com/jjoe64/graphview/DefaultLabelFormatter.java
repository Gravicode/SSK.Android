package com.jjoe64.graphview;

import java.text.NumberFormat;

public class DefaultLabelFormatter implements LabelFormatter {
    protected NumberFormat[] mNumberFormatter = new NumberFormat[2];
    protected Viewport mViewport;

    public DefaultLabelFormatter() {
    }

    public DefaultLabelFormatter(NumberFormat xFormat, NumberFormat yFormat) {
        this.mNumberFormatter[0] = yFormat;
        this.mNumberFormatter[1] = xFormat;
    }

    public void setViewport(Viewport viewport) {
        this.mViewport = viewport;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=char, for r13v0, types: [char, boolean] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String formatLabel(double r11, char r13) {
        /*
            r10 = this;
            r0 = r13
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            if (r1 != 0) goto L_0x0080
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            java.text.NumberFormat r2 = java.text.NumberFormat.getNumberInstance()
            r1[r0] = r2
            r1 = 0
            if (r13 == 0) goto L_0x0019
            com.jjoe64.graphview.Viewport r2 = r10.mViewport
            double r2 = r2.getMaxX(r1)
            goto L_0x001f
        L_0x0019:
            com.jjoe64.graphview.Viewport r2 = r10.mViewport
            double r2 = r2.getMaxY(r1)
        L_0x001f:
            if (r13 == 0) goto L_0x0028
            com.jjoe64.graphview.Viewport r4 = r10.mViewport
            double r4 = r4.getMinX(r1)
            goto L_0x002e
        L_0x0028:
            com.jjoe64.graphview.Viewport r4 = r10.mViewport
            double r4 = r4.getMinY(r1)
        L_0x002e:
            r6 = 0
            double r6 = r2 - r4
            r8 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0043
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            r6 = 6
            r1.setMaximumFractionDigits(r6)
            goto L_0x0080
        L_0x0043:
            r6 = 0
            double r6 = r2 - r4
            r8 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0055
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            r6 = 4
            r1.setMaximumFractionDigits(r6)
            goto L_0x0080
        L_0x0055:
            r6 = 0
            double r6 = r2 - r4
            r8 = 4626322717216342016(0x4034000000000000, double:20.0)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0067
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            r6 = 3
            r1.setMaximumFractionDigits(r6)
            goto L_0x0080
        L_0x0067:
            r6 = 0
            double r6 = r2 - r4
            r8 = 4636737291354636288(0x4059000000000000, double:100.0)
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 >= 0) goto L_0x0079
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            r6 = 1
            r1.setMaximumFractionDigits(r6)
            goto L_0x0080
        L_0x0079:
            java.text.NumberFormat[] r6 = r10.mNumberFormatter
            r6 = r6[r0]
            r6.setMaximumFractionDigits(r1)
        L_0x0080:
            java.text.NumberFormat[] r1 = r10.mNumberFormatter
            r1 = r1[r0]
            java.lang.String r1 = r1.format(r11)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jjoe64.graphview.DefaultLabelFormatter.formatLabel(double, boolean):java.lang.String");
    }
}
