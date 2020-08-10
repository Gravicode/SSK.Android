package p000;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import libsvm.svm;
import libsvm.svm_model;

/* renamed from: svm_predict */
class svm_predict {
    svm_predict() {
    }

    private static double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x00a1  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x014b  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x023d  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0240  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x009e A[EDGE_INSN: B:47:0x009e->B:16:0x009e ?: BREAK  
    EDGE_INSN: B:47:0x009e->B:16:0x009e ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void predict(java.io.BufferedReader r41, java.io.DataOutputStream r42, libsvm.svm_model r43, int r44) throws java.io.IOException {
        /*
            r0 = r42
            r1 = r43
            r2 = r44
            r3 = 0
            r4 = 0
            r5 = 0
            r7 = 0
            r9 = 0
            r11 = 0
            r13 = 0
            r15 = 0
            r17 = r3
            int r3 = libsvm.svm.svm_get_svm_type(r43)
            r18 = r4
            int r4 = libsvm.svm.svm_get_nr_class(r43)
            r19 = 0
            r20 = r5
            r6 = 3
            r22 = 0
            r5 = 1
            if (r2 != r5) goto L_0x0090
            if (r3 == r6) goto L_0x006e
            r5 = 4
            if (r3 != r5) goto L_0x0032
            r26 = r7
            goto L_0x0070
        L_0x0032:
            int[] r5 = new int[r4]
            libsvm.svm.svm_get_labels(r1, r5)
            double[] r6 = new double[r4]
            r25 = r6
            java.lang.String r6 = "labels"
            r0.writeBytes(r6)
            r6 = 0
        L_0x0041:
            if (r6 >= r4) goto L_0x0060
            r26 = r7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = " "
            r7.append(r8)
            r8 = r5[r6]
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            r0.writeBytes(r7)
            int r6 = r6 + 1
            r7 = r26
            goto L_0x0041
        L_0x0060:
            r26 = r7
            java.lang.String r6 = "\n"
            r0.writeBytes(r6)
            r6 = r17
            r5 = r18
            r7 = r25
            goto L_0x0098
        L_0x006e:
            r26 = r7
        L_0x0070:
            java.io.PrintStream r5 = java.lang.System.out
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma="
            r6.append(r7)
            double r7 = libsvm.svm.svm_get_svr_probability(r43)
            r6.append(r7)
            java.lang.String r7 = "\n"
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.print(r6)
            goto L_0x0092
        L_0x0090:
            r26 = r7
        L_0x0092:
            r6 = r17
            r5 = r18
            r7 = r19
        L_0x0098:
            java.lang.String r8 = r41.readLine()
            if (r8 != 0) goto L_0x014b
            r8 = 3
            if (r3 == r8) goto L_0x00e6
            r8 = 4
            if (r3 != r8) goto L_0x00a9
            r29 = r3
            r28 = r4
            goto L_0x00ea
        L_0x00a9:
            java.io.PrintStream r8 = java.lang.System.out
            r28 = r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = "Accuracy = "
            r4.append(r0)
            double r0 = (double) r6
            r29 = r3
            double r2 = (double) r5
            double r0 = r0 / r2
            r2 = 4636737291354636288(0x4059000000000000, double:100.0)
            double r0 = r0 * r2
            r4.append(r0)
            java.lang.String r0 = "% ("
            r4.append(r0)
            r4.append(r6)
            java.lang.String r0 = "/"
            r4.append(r0)
            r4.append(r5)
            java.lang.String r0 = ") (classification)\n"
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            r8.print(r0)
            r30 = r6
            r31 = r7
            r32 = r11
            goto L_0x014a
        L_0x00e6:
            r29 = r3
            r28 = r4
        L_0x00ea:
            java.io.PrintStream r0 = java.lang.System.out
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Mean squared error = "
            r1.append(r2)
            double r2 = (double) r5
            double r2 = r20 / r2
            r1.append(r2)
            java.lang.String r2 = " (regression)\n"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.print(r1)
            java.io.PrintStream r0 = java.lang.System.out
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Squared correlation coefficient = "
            r1.append(r2)
            double r2 = (double) r5
            double r2 = r2 * r15
            double r17 = r26 * r9
            double r2 = r2 - r17
            r30 = r6
            r31 = r7
            double r6 = (double) r5
            double r6 = r6 * r15
            double r17 = r26 * r9
            double r6 = r6 - r17
            double r2 = r2 * r6
            double r6 = (double) r5
            double r6 = r6 * r11
            double r17 = r26 * r26
            double r6 = r6 - r17
            r32 = r11
            double r11 = (double) r5
            double r11 = r11 * r13
            double r17 = r9 * r9
            double r11 = r11 - r17
            double r6 = r6 * r11
            double r2 = r2 / r6
            r1.append(r2)
            java.lang.String r2 = " (regression)\n"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.print(r1)
        L_0x014a:
            return
        L_0x014b:
            r29 = r3
            r28 = r4
            r30 = r6
            r31 = r7
            r32 = r11
            r0 = 3
            r1 = 4
            java.util.StringTokenizer r2 = new java.util.StringTokenizer
            java.lang.String r3 = " \t\n\r\f:"
            r2.<init>(r8, r3)
            java.lang.String r3 = r2.nextToken()
            double r3 = atof(r3)
            int r6 = r2.countTokens()
            int r6 = r6 / 2
            libsvm.svm_node[] r7 = new libsvm.svm_node[r6]
            r11 = 0
        L_0x016f:
            if (r11 >= r6) goto L_0x0199
            libsvm.svm_node r12 = new libsvm.svm_node
            r12.<init>()
            r7[r11] = r12
            r12 = r7[r11]
            java.lang.String r0 = r2.nextToken()
            int r0 = atoi(r0)
            r12.index = r0
            r0 = r7[r11]
            java.lang.String r12 = r2.nextToken()
            r34 = r2
            double r1 = atof(r12)
            r0.value = r1
            int r11 = r11 + 1
            r2 = r34
            r0 = 3
            r1 = 4
            goto L_0x016f
        L_0x0199:
            r34 = r2
            r0 = r44
            r1 = 1
            if (r0 != r1) goto L_0x0211
            if (r29 == 0) goto L_0x01b7
            r2 = r29
            if (r2 != r1) goto L_0x01a7
            goto L_0x01b9
        L_0x01a7:
            r35 = r2
            r39 = r5
            r40 = r6
            r38 = r28
            r12 = r31
            r1 = r42
            r11 = r43
            goto L_0x021f
        L_0x01b7:
            r2 = r29
        L_0x01b9:
            r35 = r2
            r12 = r31
            r11 = r43
            double r1 = libsvm.svm.svm_predict_probability(r11, r7, r12)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r1)
            r36 = r1
            java.lang.String r1 = " "
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = r42
            r1.writeBytes(r0)
            r0 = 0
        L_0x01dc:
            r2 = r28
            if (r0 >= r2) goto L_0x0205
            r38 = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r39 = r5
            r40 = r6
            r5 = r12[r0]
            r2.append(r5)
            java.lang.String r5 = " "
            r2.append(r5)
            java.lang.String r2 = r2.toString()
            r1.writeBytes(r2)
            int r0 = r0 + 1
            r28 = r38
            r5 = r39
            r6 = r40
            goto L_0x01dc
        L_0x0205:
            r38 = r2
            r39 = r5
            r40 = r6
            java.lang.String r0 = "\n"
            r1.writeBytes(r0)
            goto L_0x0239
        L_0x0211:
            r39 = r5
            r40 = r6
            r38 = r28
            r35 = r29
            r12 = r31
            r1 = r42
            r11 = r43
        L_0x021f:
            double r5 = libsvm.svm.svm_predict(r11, r7)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            java.lang.String r2 = "\n"
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            r1.writeBytes(r0)
            r36 = r5
        L_0x0239:
            int r0 = (r36 > r3 ? 1 : (r36 == r3 ? 0 : -1))
            if (r0 != 0) goto L_0x0240
            int r6 = r30 + 1
            goto L_0x0242
        L_0x0240:
            r6 = r30
        L_0x0242:
            r0 = 0
            double r17 = r36 - r3
            double r23 = r36 - r3
            double r17 = r17 * r23
            double r20 = r20 + r17
            double r26 = r26 + r36
            double r9 = r9 + r3
            double r17 = r36 * r36
            double r17 = r32 + r17
            double r23 = r3 * r3
            double r13 = r13 + r23
            double r23 = r36 * r3
            double r15 = r15 + r23
            int r5 = r39 + 1
            r0 = r1
            r1 = r11
            r7 = r12
            r11 = r17
            r3 = r35
            r4 = r38
            r2 = r44
            goto L_0x0098
        */
        throw new UnsupportedOperationException("Method not decompiled: p000.svm_predict.predict(java.io.BufferedReader, java.io.DataOutputStream, libsvm.svm_model, int):void");
    }

    private static void exit_with_help() {
        System.err.print("usage: svm_predict [options] test_file model_file output_file\noptions:\n-b probability_estimates: whether to predict probability estimates, 0 or 1 (default 0); one-class SVM not supported yet\n");
        System.exit(1);
    }

    public static void main(String[] argv) throws IOException {
        int predict_probability = 0;
        int i = 0;
        while (i < argv.length && argv[i].charAt(0) == '-') {
            int i2 = i + 1;
            if (argv[i2 - 1].charAt(1) != 'b') {
                PrintStream printStream = System.err;
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown option: ");
                sb.append(argv[i2 - 1]);
                sb.append("\n");
                printStream.print(sb.toString());
                exit_with_help();
            } else {
                predict_probability = atoi(argv[i2]);
            }
            i = i2 + 1;
        }
        if (i >= argv.length - 2) {
            exit_with_help();
        }
        try {
            BufferedReader input = new BufferedReader(new FileReader(argv[i]));
            DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(argv[i + 2])));
            svm_model model = svm.svm_load_model(argv[i + 1]);
            if (predict_probability == 1) {
                if (svm.svm_check_probability_model(model) == 0) {
                    System.err.print("Model does not support probabiliy estimates\n");
                    System.exit(1);
                }
            } else if (svm.svm_check_probability_model(model) != 0) {
                System.out.print("Model supports probability estimates, but disabled in prediction.\n");
            }
            predict(input, output, model, predict_probability);
            input.close();
            output.close();
        } catch (FileNotFoundException e) {
            exit_with_help();
        } catch (ArrayIndexOutOfBoundsException e2) {
            exit_with_help();
        }
    }
}
