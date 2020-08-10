package p000;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/* renamed from: svm_scale */
class svm_scale {
    private double[] feature_max;
    private double[] feature_min;
    private String line = null;
    private double lower = -1.0d;
    private int max_index;
    private long new_num_nonzeros = 0;
    private long num_nonzeros = 0;
    private double upper = 1.0d;
    private double y_lower;
    private double y_max = -1.7976931348623157E308d;
    private double y_min = Double.MAX_VALUE;
    private boolean y_scaling = false;
    private double y_upper;

    svm_scale() {
    }

    private static void exit_with_help() {
        System.out.print("Usage: svm-scale [options] data_filename\noptions:\n-l lower : x scaling lower limit (default -1)\n-u upper : x scaling upper limit (default +1)\n-y y_lower y_upper : y scaling limits (default: no y scaling)\n-s save_filename : save scaling parameters to save_filename\n-r restore_filename : restore scaling parameters from restore_filename\n");
        System.exit(1);
    }

    private BufferedReader rewind(BufferedReader fp, String filename) throws IOException {
        fp.close();
        return new BufferedReader(new FileReader(filename));
    }

    private void output_target(double value) {
        if (this.y_scaling) {
            if (value == this.y_min) {
                value = this.y_lower;
            } else if (value == this.y_max) {
                value = this.y_upper;
            } else {
                value = this.y_lower + (((this.y_upper - this.y_lower) * (value - this.y_min)) / (this.y_max - this.y_min));
            }
        }
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        sb.append(" ");
        printStream.print(sb.toString());
    }

    private void output(int index, double value) {
        double value2;
        if (this.feature_max[index] != this.feature_min[index]) {
            if (value == this.feature_min[index]) {
                value2 = this.lower;
            } else if (value == this.feature_max[index]) {
                value2 = this.upper;
            } else {
                value2 = this.lower + (((this.upper - this.lower) * (value - this.feature_min[index])) / (this.feature_max[index] - this.feature_min[index]));
            }
            if (value2 != 0.0d) {
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append(index);
                sb.append(":");
                sb.append(value2);
                sb.append(" ");
                printStream.print(sb.toString());
                this.new_num_nonzeros++;
            }
        }
    }

    private String readline(BufferedReader fp) throws IOException {
        this.line = fp.readLine();
        return this.line;
    }

    /* JADX WARNING: Removed duplicated region for block: B:112:0x0372  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x03ac  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x03d2  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x041d  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0478  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void run(java.lang.String[] r26) throws java.io.IOException {
        /*
            r25 = this;
            r1 = r25
            r2 = r26
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = r5
            r5 = 0
        L_0x000c:
            int r10 = r2.length
            r11 = 121(0x79, float:1.7E-43)
            r12 = 1
            if (r5 >= r10) goto L_0x006d
            r10 = r2[r5]
            char r10 = r10.charAt(r8)
            r13 = 45
            if (r10 == r13) goto L_0x001d
            goto L_0x006d
        L_0x001d:
            int r5 = r5 + 1
            int r10 = r5 + -1
            r10 = r2[r10]
            char r10 = r10.charAt(r12)
            r13 = 108(0x6c, float:1.51E-43)
            if (r10 == r13) goto L_0x0063
            r13 = 117(0x75, float:1.64E-43)
            if (r10 == r13) goto L_0x005a
            if (r10 == r11) goto L_0x0045
            switch(r10) {
                case 114: goto L_0x0042;
                case 115: goto L_0x003f;
                default: goto L_0x0034;
            }
        L_0x0034:
            java.io.PrintStream r10 = java.lang.System.err
            java.lang.String r11 = "unknown option"
            r10.println(r11)
            exit_with_help()
            goto L_0x006b
        L_0x003f:
            r9 = r2[r5]
            goto L_0x006b
        L_0x0042:
            r6 = r2[r5]
            goto L_0x006b
        L_0x0045:
            r10 = r2[r5]
            double r10 = java.lang.Double.parseDouble(r10)
            r1.y_lower = r10
            int r5 = r5 + 1
            r10 = r2[r5]
            double r10 = java.lang.Double.parseDouble(r10)
            r1.y_upper = r10
            r1.y_scaling = r12
            goto L_0x006b
        L_0x005a:
            r10 = r2[r5]
            double r10 = java.lang.Double.parseDouble(r10)
            r1.upper = r10
            goto L_0x006b
        L_0x0063:
            r10 = r2[r5]
            double r10 = java.lang.Double.parseDouble(r10)
            r1.lower = r10
        L_0x006b:
            int r5 = r5 + r12
            goto L_0x000c
        L_0x006d:
            double r13 = r1.upper
            r16 = r9
            double r8 = r1.lower
            int r8 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1))
            if (r8 <= 0) goto L_0x0083
            boolean r8 = r1.y_scaling
            if (r8 == 0) goto L_0x008d
            double r8 = r1.y_upper
            double r13 = r1.y_lower
            int r8 = (r8 > r13 ? 1 : (r8 == r13 ? 0 : -1))
            if (r8 > 0) goto L_0x008d
        L_0x0083:
            java.io.PrintStream r8 = java.lang.System.err
            java.lang.String r9 = "inconsistent lower/upper specification"
            r8.println(r9)
            java.lang.System.exit(r12)
        L_0x008d:
            if (r6 == 0) goto L_0x009b
            if (r16 == 0) goto L_0x009b
            java.io.PrintStream r8 = java.lang.System.err
            java.lang.String r9 = "cannot use -r and -s simultaneously"
            r8.println(r9)
            java.lang.System.exit(r12)
        L_0x009b:
            int r8 = r2.length
            int r9 = r5 + 1
            if (r8 == r9) goto L_0x00a3
            exit_with_help()
        L_0x00a3:
            r7 = r2[r5]
            java.io.BufferedReader r8 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00b1 }
            java.io.FileReader r9 = new java.io.FileReader     // Catch:{ Exception -> 0x00b1 }
            r9.<init>(r7)     // Catch:{ Exception -> 0x00b1 }
            r8.<init>(r9)     // Catch:{ Exception -> 0x00b1 }
            r3 = r8
            goto L_0x00cc
        L_0x00b1:
            r0 = move-exception
            r8 = r0
            java.io.PrintStream r9 = java.lang.System.err
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r13 = "can't open file "
            r10.append(r13)
            r10.append(r7)
            java.lang.String r10 = r10.toString()
            r9.println(r10)
            java.lang.System.exit(r12)
        L_0x00cc:
            r8 = 0
            r1.max_index = r8
            if (r6 == 0) goto L_0x0130
            java.io.BufferedReader r9 = new java.io.BufferedReader     // Catch:{ Exception -> 0x00dd }
            java.io.FileReader r10 = new java.io.FileReader     // Catch:{ Exception -> 0x00dd }
            r10.<init>(r6)     // Catch:{ Exception -> 0x00dd }
            r9.<init>(r10)     // Catch:{ Exception -> 0x00dd }
            r4 = r9
            goto L_0x00f8
        L_0x00dd:
            r0 = move-exception
            r9 = r0
            java.io.PrintStream r10 = java.lang.System.err
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = "can't open file "
            r13.append(r14)
            r13.append(r6)
            java.lang.String r13 = r13.toString()
            r10.println(r13)
            java.lang.System.exit(r12)
        L_0x00f8:
            int r9 = r4.read()
            r10 = r9
            if (r9 != r11) goto L_0x0108
            r4.readLine()
            r4.readLine()
            r4.readLine()
        L_0x0108:
            r4.readLine()
            r4.readLine()
            r9 = 0
        L_0x010f:
            java.lang.String r13 = r4.readLine()
            r9 = r13
            if (r13 == 0) goto L_0x012c
            java.util.StringTokenizer r13 = new java.util.StringTokenizer
            r13.<init>(r9)
            java.lang.String r14 = r13.nextToken()
            int r14 = java.lang.Integer.parseInt(r14)
            int r15 = r1.max_index
            int r15 = java.lang.Math.max(r15, r14)
            r1.max_index = r15
            goto L_0x010f
        L_0x012c:
            java.io.BufferedReader r4 = r1.rewind(r4, r6)
        L_0x0130:
            java.lang.String r9 = r1.readline(r3)
            if (r9 == 0) goto L_0x0165
            java.util.StringTokenizer r9 = new java.util.StringTokenizer
            java.lang.String r10 = r1.line
            java.lang.String r13 = " \t\n\r\f:"
            r9.<init>(r10, r13)
            r9.nextToken()
        L_0x0142:
            boolean r10 = r9.hasMoreTokens()
            if (r10 == 0) goto L_0x0164
            java.lang.String r10 = r9.nextToken()
            int r10 = java.lang.Integer.parseInt(r10)
            int r13 = r1.max_index
            int r13 = java.lang.Math.max(r13, r10)
            r1.max_index = r13
            r9.nextToken()
            long r13 = r1.num_nonzeros
            r17 = 1
            long r13 = r13 + r17
            r1.num_nonzeros = r13
            goto L_0x0142
        L_0x0164:
            goto L_0x0130
        L_0x0165:
            int r9 = r1.max_index     // Catch:{ OutOfMemoryError -> 0x0174 }
            int r9 = r9 + r12
            double[] r9 = new double[r9]     // Catch:{ OutOfMemoryError -> 0x0174 }
            r1.feature_max = r9     // Catch:{ OutOfMemoryError -> 0x0174 }
            int r9 = r1.max_index     // Catch:{ OutOfMemoryError -> 0x0174 }
            int r9 = r9 + r12
            double[] r9 = new double[r9]     // Catch:{ OutOfMemoryError -> 0x0174 }
            r1.feature_min = r9     // Catch:{ OutOfMemoryError -> 0x0174 }
            goto L_0x0180
        L_0x0174:
            r0 = move-exception
            r9 = r0
            java.io.PrintStream r10 = java.lang.System.err
            java.lang.String r13 = "can't allocate enough memory"
            r10.println(r13)
            java.lang.System.exit(r12)
        L_0x0180:
            r5 = 0
        L_0x0181:
            int r9 = r1.max_index
            if (r5 > r9) goto L_0x019a
            double[] r9 = r1.feature_max
            r13 = -4503599627370497(0xffefffffffffffff, double:-1.7976931348623157E308)
            r9[r5] = r13
            double[] r9 = r1.feature_min
            r13 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            r9[r5] = r13
            int r5 = r5 + 1
            goto L_0x0181
        L_0x019a:
            java.io.BufferedReader r3 = r1.rewind(r3, r7)
        L_0x019e:
            java.lang.String r9 = r1.readline(r3)
            if (r9 == 0) goto L_0x0273
            r9 = 1
            java.util.StringTokenizer r10 = new java.util.StringTokenizer
            java.lang.String r15 = r1.line
            java.lang.String r8 = " \t\n\r\f:"
            r10.<init>(r15, r8)
            r8 = r10
            java.lang.String r10 = r8.nextToken()
            double r11 = java.lang.Double.parseDouble(r10)
            double r13 = r1.y_max
            double r13 = java.lang.Math.max(r13, r11)
            r1.y_max = r13
            double r13 = r1.y_min
            double r13 = java.lang.Math.min(r13, r11)
            r1.y_min = r13
        L_0x01c7:
            boolean r10 = r8.hasMoreTokens()
            if (r10 == 0) goto L_0x023a
            java.lang.String r10 = r8.nextToken()
            int r10 = java.lang.Integer.parseInt(r10)
            java.lang.String r13 = r8.nextToken()
            double r13 = java.lang.Double.parseDouble(r13)
            r5 = r9
        L_0x01de:
            if (r5 >= r10) goto L_0x020f
            double[] r15 = r1.feature_max
            double[] r2 = r1.feature_max
            r19 = r11
            r11 = r2[r5]
            r21 = r3
            r2 = 0
            double r11 = java.lang.Math.max(r11, r2)
            r15[r5] = r11
            double[] r11 = r1.feature_min
            double[] r12 = r1.feature_min
            r22 = r6
            r23 = r7
            r6 = r12[r5]
            double r6 = java.lang.Math.min(r6, r2)
            r11[r5] = r6
            int r5 = r5 + 1
            r11 = r19
            r3 = r21
            r6 = r22
            r7 = r23
            r2 = r26
            goto L_0x01de
        L_0x020f:
            r21 = r3
            r22 = r6
            r23 = r7
            r19 = r11
            double[] r2 = r1.feature_max
            double[] r3 = r1.feature_max
            r6 = r3[r10]
            double r6 = java.lang.Math.max(r6, r13)
            r2[r10] = r6
            double[] r2 = r1.feature_min
            double[] r3 = r1.feature_min
            r6 = r3[r10]
            double r6 = java.lang.Math.min(r6, r13)
            r2[r10] = r6
            int r9 = r10 + 1
            r3 = r21
            r6 = r22
            r7 = r23
            r2 = r26
            goto L_0x01c7
        L_0x023a:
            r21 = r3
            r22 = r6
            r23 = r7
            r19 = r11
            r2 = r9
            r5 = r2
        L_0x0244:
            int r2 = r1.max_index
            if (r5 > r2) goto L_0x0265
            double[] r2 = r1.feature_max
            double[] r3 = r1.feature_max
            r6 = r3[r5]
            r10 = 0
            double r6 = java.lang.Math.max(r6, r10)
            r2[r5] = r6
            double[] r2 = r1.feature_min
            double[] r3 = r1.feature_min
            r6 = r3[r5]
            double r6 = java.lang.Math.min(r6, r10)
            r2[r5] = r6
            int r5 = r5 + 1
            goto L_0x0244
        L_0x0265:
            r3 = r21
            r6 = r22
            r7 = r23
            r2 = r26
            r11 = 121(0x79, float:1.7E-43)
            r12 = 1
            goto L_0x019e
        L_0x0273:
            r21 = r3
            r22 = r6
            r23 = r7
            r2 = r23
            java.io.BufferedReader r3 = r1.rewind(r3, r2)
            r6 = 2
            if (r22 == 0) goto L_0x0332
            r4.mark(r6)
            int r7 = r4.read()
            r8 = r7
            r9 = 121(0x79, float:1.7E-43)
            if (r7 != r9) goto L_0x02d0
            r4.readLine()
            java.util.StringTokenizer r7 = new java.util.StringTokenizer
            java.lang.String r9 = r4.readLine()
            r7.<init>(r9)
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.y_lower = r9
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.y_upper = r9
            java.util.StringTokenizer r9 = new java.util.StringTokenizer
            java.lang.String r10 = r4.readLine()
            r9.<init>(r10)
            r7 = r9
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.y_min = r9
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.y_max = r9
            r9 = 1
            r1.y_scaling = r9
            goto L_0x02d3
        L_0x02d0:
            r4.reset()
        L_0x02d3:
            int r7 = r4.read()
            r9 = 120(0x78, float:1.68E-43)
            if (r7 != r9) goto L_0x032f
            r4.readLine()
            java.util.StringTokenizer r7 = new java.util.StringTokenizer
            java.lang.String r9 = r4.readLine()
            r7.<init>(r9)
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.lower = r9
            java.lang.String r9 = r7.nextToken()
            double r9 = java.lang.Double.parseDouble(r9)
            r1.upper = r9
            r9 = 0
        L_0x02fc:
            java.lang.String r10 = r4.readLine()
            r9 = r10
            if (r10 == 0) goto L_0x032f
            java.util.StringTokenizer r10 = new java.util.StringTokenizer
            r10.<init>(r9)
            java.lang.String r11 = r10.nextToken()
            int r11 = java.lang.Integer.parseInt(r11)
            java.lang.String r12 = r10.nextToken()
            double r12 = java.lang.Double.parseDouble(r12)
            java.lang.String r14 = r10.nextToken()
            double r14 = java.lang.Double.parseDouble(r14)
            int r6 = r1.max_index
            if (r11 > r6) goto L_0x032c
            double[] r6 = r1.feature_min
            r6[r11] = r12
            double[] r6 = r1.feature_max
            r6[r11] = r14
        L_0x032c:
            r6 = 2
            goto L_0x02fc
        L_0x032f:
            r4.close()
        L_0x0332:
            if (r16 == 0) goto L_0x0415
            java.util.Formatter r6 = new java.util.Formatter
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r6.<init>(r7)
            r7 = 0
            java.io.BufferedWriter r8 = new java.io.BufferedWriter     // Catch:{ IOException -> 0x0350 }
            java.io.FileWriter r9 = new java.io.FileWriter     // Catch:{ IOException -> 0x0350 }
            r10 = r16
            r9.<init>(r10)     // Catch:{ IOException -> 0x034d }
            r8.<init>(r9)     // Catch:{ IOException -> 0x034d }
            r7 = r8
            goto L_0x036e
        L_0x034d:
            r0 = move-exception
            r8 = r0
            goto L_0x0354
        L_0x0350:
            r0 = move-exception
            r10 = r16
            r8 = r0
        L_0x0354:
            java.io.PrintStream r9 = java.lang.System.err
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "can't open file "
            r11.append(r12)
            r11.append(r10)
            java.lang.String r11 = r11.toString()
            r9.println(r11)
            r9 = 1
            java.lang.System.exit(r9)
        L_0x036e:
            boolean r8 = r1.y_scaling
            if (r8 == 0) goto L_0x03ac
            java.lang.String r8 = "y\n"
            r9 = 0
            java.lang.Object[] r11 = new java.lang.Object[r9]
            r6.format(r8, r11)
            java.lang.String r8 = "%.16g %.16g\n"
            r11 = 2
            java.lang.Object[] r12 = new java.lang.Object[r11]
            double r13 = r1.y_lower
            java.lang.Double r13 = java.lang.Double.valueOf(r13)
            r12[r9] = r13
            double r13 = r1.y_upper
            java.lang.Double r13 = java.lang.Double.valueOf(r13)
            r14 = 1
            r12[r14] = r13
            r6.format(r8, r12)
            java.lang.String r8 = "%.16g %.16g\n"
            java.lang.Object[] r12 = new java.lang.Object[r11]
            double r14 = r1.y_min
            java.lang.Double r11 = java.lang.Double.valueOf(r14)
            r12[r9] = r11
            double r13 = r1.y_max
            java.lang.Double r11 = java.lang.Double.valueOf(r13)
            r13 = 1
            r12[r13] = r11
            r6.format(r8, r12)
            goto L_0x03ad
        L_0x03ac:
            r9 = 0
        L_0x03ad:
            java.lang.String r8 = "x\n"
            java.lang.Object[] r11 = new java.lang.Object[r9]
            r6.format(r8, r11)
            java.lang.String r8 = "%.16g %.16g\n"
            r11 = 2
            java.lang.Object[] r12 = new java.lang.Object[r11]
            double r13 = r1.lower
            java.lang.Double r11 = java.lang.Double.valueOf(r13)
            r12[r9] = r11
            double r13 = r1.upper
            java.lang.Double r9 = java.lang.Double.valueOf(r13)
            r11 = 1
            r12[r11] = r9
            r6.format(r8, r12)
            r5 = 1
        L_0x03ce:
            int r8 = r1.max_index
            if (r5 > r8) goto L_0x040a
            double[] r8 = r1.feature_min
            r11 = r8[r5]
            double[] r8 = r1.feature_max
            r13 = r8[r5]
            int r8 = (r11 > r13 ? 1 : (r11 == r13 ? 0 : -1))
            if (r8 == 0) goto L_0x0404
            java.lang.String r8 = "%d %.16g %.16g\n"
            r9 = 3
            java.lang.Object[] r9 = new java.lang.Object[r9]
            java.lang.Integer r11 = java.lang.Integer.valueOf(r5)
            r12 = 0
            r9[r12] = r11
            double[] r11 = r1.feature_min
            r13 = r11[r5]
            java.lang.Double r11 = java.lang.Double.valueOf(r13)
            r13 = 1
            r9[r13] = r11
            double[] r11 = r1.feature_max
            r14 = r11[r5]
            java.lang.Double r11 = java.lang.Double.valueOf(r14)
            r14 = 2
            r9[r14] = r11
            r6.format(r8, r9)
            goto L_0x0407
        L_0x0404:
            r12 = 0
            r13 = 1
            r14 = 2
        L_0x0407:
            int r5 = r5 + 1
            goto L_0x03ce
        L_0x040a:
            java.lang.String r8 = r6.toString()
            r7.write(r8)
            r7.close()
            goto L_0x0417
        L_0x0415:
            r10 = r16
        L_0x0417:
            java.lang.String r6 = r1.readline(r3)
            if (r6 == 0) goto L_0x0470
            r6 = 1
            java.util.StringTokenizer r7 = new java.util.StringTokenizer
            java.lang.String r8 = r1.line
            java.lang.String r9 = " \t\n\r\f:"
            r7.<init>(r8, r9)
            java.lang.String r8 = r7.nextToken()
            double r8 = java.lang.Double.parseDouble(r8)
            r1.output_target(r8)
        L_0x0432:
            boolean r11 = r7.hasMoreElements()
            if (r11 == 0) goto L_0x0459
            java.lang.String r11 = r7.nextToken()
            int r11 = java.lang.Integer.parseInt(r11)
            java.lang.String r12 = r7.nextToken()
            double r12 = java.lang.Double.parseDouble(r12)
            r5 = r6
        L_0x0449:
            if (r5 >= r11) goto L_0x0453
            r14 = 0
            r1.output(r5, r14)
            int r5 = r5 + 1
            goto L_0x0449
        L_0x0453:
            r1.output(r11, r12)
            int r6 = r11 + 1
            goto L_0x0432
        L_0x0459:
            r5 = r6
        L_0x045a:
            int r11 = r1.max_index
            if (r5 > r11) goto L_0x0466
            r11 = 0
            r1.output(r5, r11)
            int r5 = r5 + 1
            goto L_0x045a
        L_0x0466:
            r11 = 0
            java.io.PrintStream r13 = java.lang.System.out
            java.lang.String r14 = "\n"
            r13.print(r14)
            goto L_0x0417
        L_0x0470:
            long r6 = r1.new_num_nonzeros
            long r8 = r1.num_nonzeros
            int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r6 <= 0) goto L_0x04a9
            java.io.PrintStream r6 = java.lang.System.err
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Warning: original #nonzeros "
            r7.append(r8)
            long r8 = r1.num_nonzeros
            r7.append(r8)
            java.lang.String r8 = "\n"
            r7.append(r8)
            java.lang.String r8 = "         new      #nonzeros "
            r7.append(r8)
            long r8 = r1.new_num_nonzeros
            r7.append(r8)
            java.lang.String r8 = "\n"
            r7.append(r8)
            java.lang.String r8 = "Use -l 0 if many original feature values are zeros\n"
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            r6.print(r7)
        L_0x04a9:
            r3.close()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p000.svm_scale.run(java.lang.String[]):void");
    }

    public static void main(String[] argv) throws IOException {
        new svm_scale().run(argv);
    }
}
