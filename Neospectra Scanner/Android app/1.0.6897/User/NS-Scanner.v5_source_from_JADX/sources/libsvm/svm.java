package libsvm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.Random;
import java.util.StringTokenizer;

public class svm {
    public static final int LIBSVM_VERSION = 310;
    static final String[] kernel_type_table = {"linear", "polynomial", "rbf", "sigmoid", "precomputed"};
    public static final Random rand = new Random();
    private static svm_print_interface svm_print_stdout = new svm_print_interface() {
        public void print(String s) {
            System.out.print(s);
            System.out.flush();
        }
    };
    private static svm_print_interface svm_print_string = svm_print_stdout;
    static final String[] svm_type_table = {"c_svc", "nu_svc", "one_class", "epsilon_svr", "nu_svr"};

    static class decision_function {
        double[] alpha;
        double rho;

        decision_function() {
        }
    }

    static void info(String s) {
        svm_print_string.print(s);
    }

    private static void solve_c_svc(svm_problem prob, svm_parameter param, double[] alpha, SolutionInfo si, double Cp, double Cn) {
        int i;
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int l = svm_problem.f498l;
        double[] minus_ones = new double[l];
        byte[] y = new byte[l];
        int i2 = 0;
        while (true) {
            i = i2;
            if (i >= l) {
                break;
            }
            alpha[i] = 0.0d;
            minus_ones[i] = -1.0d;
            if (svm_problem.f500y[i] > 0.0d) {
                y[i] = 1;
            } else {
                y[i] = -1;
            }
            i2 = i + 1;
        }
        Solver s = new Solver();
        SVC_Q svc_q = new SVC_Q(svm_problem, svm_parameter, y);
        double d = svm_parameter.eps;
        int i3 = svm_parameter.shrinking;
        byte[] y2 = y;
        int i4 = i;
        double d2 = d;
        double[] dArr = minus_ones;
        int l2 = l;
        s.Solve(l, svc_q, minus_ones, y, alpha, Cp, Cn, d2, si, i3);
        double sum_alpha = 0.0d;
        for (int i5 = 0; i5 < l2; i5++) {
            sum_alpha += alpha[i5];
        }
        if (Cp == Cn) {
            StringBuilder sb = new StringBuilder();
            sb.append("nu = ");
            sb.append(sum_alpha / (((double) svm_problem.f498l) * Cp));
            sb.append("\n");
            info(sb.toString());
        }
        for (int i6 = 0; i6 < l2; i6++) {
            alpha[i6] = alpha[i6] * ((double) y2[i6]);
        }
    }

    private static void solve_nu_svc(svm_problem prob, svm_parameter param, double[] alpha, SolutionInfo si) {
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        SolutionInfo solutionInfo = si;
        int l = svm_problem.f498l;
        double nu = svm_parameter.f496nu;
        byte[] y = new byte[l];
        for (int i = 0; i < l; i++) {
            if (svm_problem.f500y[i] > 0.0d) {
                y[i] = 1;
            } else {
                y[i] = -1;
            }
        }
        double sum_pos = (((double) l) * nu) / 2.0d;
        double sum_neg = (((double) l) * nu) / 2.0d;
        for (int i2 = 0; i2 < l; i2++) {
            if (y[i2] == 1) {
                alpha[i2] = Math.min(1.0d, sum_pos);
                sum_pos -= alpha[i2];
            } else {
                alpha[i2] = Math.min(1.0d, sum_neg);
                sum_neg -= alpha[i2];
            }
        }
        double[] zeros = new double[l];
        int i3 = 0;
        while (i3 < l) {
            zeros[i3] = 0.0d;
            i3++;
        }
        double d = sum_pos;
        int i4 = i3;
        double d2 = sum_neg;
        byte[] y2 = y;
        double[] dArr = zeros;
        double d3 = nu;
        int l2 = l;
        SolutionInfo solutionInfo2 = solutionInfo;
        new Solver_NU().Solve(l, new SVC_Q(svm_problem, svm_parameter, y), zeros, y, alpha, 1.0d, 1.0d, svm_parameter.eps, solutionInfo, svm_parameter.shrinking);
        double r = solutionInfo2.f491r;
        StringBuilder sb = new StringBuilder();
        sb.append("C = ");
        sb.append(1.0d / r);
        sb.append("\n");
        info(sb.toString());
        int i5 = 0;
        while (true) {
            int l3 = l2;
            if (i5 < l3) {
                alpha[i5] = alpha[i5] * (((double) y2[i5]) / r);
                i5++;
                l2 = l3;
            } else {
                solutionInfo2.rho /= r;
                solutionInfo2.obj /= r * r;
                solutionInfo2.upper_bound_p = 1.0d / r;
                solutionInfo2.upper_bound_n = 1.0d / r;
                return;
            }
        }
    }

    private static void solve_one_class(svm_problem prob, svm_parameter param, double[] alpha, SolutionInfo si) {
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int l = svm_problem.f498l;
        double[] zeros = new double[l];
        byte[] ones = new byte[l];
        int n = (int) (svm_parameter.f496nu * ((double) svm_problem.f498l));
        for (int i = 0; i < n; i++) {
            alpha[i] = 1.0d;
        }
        if (n < svm_problem.f498l) {
            alpha[n] = (svm_parameter.f496nu * ((double) svm_problem.f498l)) - ((double) n);
        }
        for (int i2 = n + 1; i2 < l; i2++) {
            alpha[i2] = 0.0d;
        }
        int i3 = 0;
        while (i3 < l) {
            zeros[i3] = 0.0d;
            ones[i3] = 1;
            i3++;
        }
        Solver s = new Solver();
        ONE_CLASS_Q one_class_q = new ONE_CLASS_Q(svm_problem, svm_parameter);
        int i4 = i3;
        byte[] bArr = ones;
        int i5 = n;
        double d = svm_parameter.eps;
        double[] dArr = zeros;
        int i6 = l;
        s.Solve(l, one_class_q, zeros, ones, alpha, 1.0d, 1.0d, d, si, svm_parameter.shrinking);
    }

    private static void solve_epsilon_svr(svm_problem prob, svm_parameter param, double[] alpha, SolutionInfo si) {
        int i;
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int l = svm_problem.f498l;
        double[] alpha2 = new double[(l * 2)];
        double[] linear_term = new double[(l * 2)];
        byte[] y = new byte[(l * 2)];
        int i2 = 0;
        while (true) {
            i = i2;
            if (i >= l) {
                break;
            }
            alpha2[i] = 0.0d;
            linear_term[i] = svm_parameter.f497p - svm_problem.f500y[i];
            y[i] = 1;
            alpha2[i + l] = 0.0d;
            linear_term[i + l] = svm_parameter.f497p + svm_problem.f500y[i];
            y[i + l] = -1;
            i2 = i + 1;
        }
        Solver s = new Solver();
        int i3 = l * 2;
        SVR_Q svr_q = new SVR_Q(svm_problem, svm_parameter);
        double d = svm_parameter.f495C;
        double d2 = svm_parameter.f495C;
        int l2 = l;
        double d3 = d2;
        byte[] bArr = y;
        int i4 = i;
        double[] dArr = linear_term;
        s.Solve(i3, svr_q, linear_term, y, alpha2, d, d3, svm_parameter.eps, si, svm_parameter.shrinking);
        double sum_alpha = 0.0d;
        int i5 = 0;
        while (true) {
            int l3 = l2;
            if (i5 < l3) {
                alpha[i5] = alpha2[i5] - alpha2[i5 + l3];
                sum_alpha += Math.abs(alpha[i5]);
                i5++;
                l2 = l3;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("nu = ");
                sb.append(sum_alpha / (svm_parameter.f495C * ((double) l3)));
                sb.append("\n");
                info(sb.toString());
                return;
            }
        }
    }

    private static void solve_nu_svr(svm_problem prob, svm_parameter param, double[] alpha, SolutionInfo si) {
        int i;
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int l = svm_problem.f498l;
        double C = svm_parameter.f495C;
        double[] alpha2 = new double[(l * 2)];
        double[] linear_term = new double[(l * 2)];
        byte[] y = new byte[(l * 2)];
        int i2 = 0;
        double sum = ((svm_parameter.f496nu * C) * ((double) l)) / 2.0d;
        while (true) {
            i = i2;
            if (i >= l) {
                break;
            }
            int i3 = i + l;
            double min = Math.min(sum, C);
            alpha2[i3] = min;
            alpha2[i] = min;
            sum -= alpha2[i];
            linear_term[i] = -svm_problem.f500y[i];
            y[i] = 1;
            linear_term[i + l] = svm_problem.f500y[i];
            y[i + l] = -1;
            i2 = i + 1;
        }
        Solver_NU s = new Solver_NU();
        int i4 = l * 2;
        SVR_Q svr_q = new SVR_Q(svm_problem, svm_parameter);
        int i5 = i;
        double d = sum;
        double sum2 = C;
        double[] dArr = linear_term;
        byte[] bArr = y;
        double d2 = C;
        double d3 = C;
        double C2 = svm_parameter.eps;
        double[] alpha22 = alpha2;
        s.Solve(i4, svr_q, linear_term, y, alpha2, sum2, d2, C2, si, svm_parameter.shrinking);
        StringBuilder sb = new StringBuilder();
        sb.append("epsilon = ");
        sb.append(-si.f491r);
        sb.append("\n");
        info(sb.toString());
        for (int i6 = 0; i6 < l; i6++) {
            alpha[i6] = alpha22[i6] - alpha22[i6 + l];
        }
    }

    static decision_function svm_train_one(svm_problem prob, svm_parameter param, double Cp, double Cn) {
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        double[] alpha = new double[svm_problem.f498l];
        SolutionInfo si = new SolutionInfo();
        switch (svm_parameter.svm_type) {
            case 0:
                solve_c_svc(svm_problem, svm_parameter, alpha, si, Cp, Cn);
                break;
            case 1:
                solve_nu_svc(svm_problem, svm_parameter, alpha, si);
                break;
            case 2:
                solve_one_class(svm_problem, svm_parameter, alpha, si);
                break;
            case 3:
                solve_epsilon_svr(svm_problem, svm_parameter, alpha, si);
                break;
            case 4:
                solve_nu_svr(svm_problem, svm_parameter, alpha, si);
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("obj = ");
        sb.append(si.obj);
        sb.append(", rho = ");
        sb.append(si.rho);
        sb.append("\n");
        info(sb.toString());
        int nSV = 0;
        int nBSV = 0;
        for (int i = 0; i < svm_problem.f498l; i++) {
            if (Math.abs(alpha[i]) > 0.0d) {
                nSV++;
                if (svm_problem.f500y[i] > 0.0d) {
                    if (Math.abs(alpha[i]) >= si.upper_bound_p) {
                        nBSV++;
                    }
                } else if (Math.abs(alpha[i]) >= si.upper_bound_n) {
                    nBSV++;
                }
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("nSV = ");
        sb2.append(nSV);
        sb2.append(", nBSV = ");
        sb2.append(nBSV);
        sb2.append("\n");
        info(sb2.toString());
        decision_function f = new decision_function();
        f.alpha = alpha;
        f.rho = si.rho;
        return f;
    }

    private static void sigmoid_train(int l, double[] dec_values, double[] labels, double[] probAB) {
        double d;
        int iter;
        int max_iter;
        double B;
        double g2;
        double dB;
        double g22;
        double B2;
        double p;
        double q;
        int i = l;
        double prior0 = 0.0d;
        double prior1 = 0.0d;
        int i2 = 0;
        while (true) {
            d = 0.0d;
            if (i2 >= i) {
                break;
            }
            if (labels[i2] > 0.0d) {
                prior1 += 1.0d;
            } else {
                prior0 += 1.0d;
            }
            i2++;
        }
        int max_iter2 = 100;
        double hiTarget = (prior1 + 1.0d) / (prior1 + 2.0d);
        double loTarget = 1.0d / (prior0 + 2.0d);
        double[] t = new double[i];
        double fval = 0.0d;
        double p2 = Math.log((prior0 + 1.0d) / (prior1 + 1.0d));
        double fval2 = 0.0d;
        int i3 = 0;
        while (i3 < i) {
            if (labels[i3] > d) {
                t[i3] = hiTarget;
            } else {
                t[i3] = loTarget;
            }
            double prior12 = prior1;
            double prior13 = (dec_values[i3] * 0.0d) + p2;
            if (prior13 >= d) {
                fval2 += (t[i3] * prior13) + Math.log(Math.exp(-prior13) + 1.0d);
                double d2 = prior13;
            } else {
                double d3 = prior13;
                fval2 += ((t[i3] - 1.0d) * prior13) + Math.log(Math.exp(prior13) + 1.0d);
            }
            i3++;
            prior1 = prior12;
            d = 0.0d;
        }
        int i4 = i3;
        int i5 = 0;
        while (true) {
            if (i5 >= max_iter2) {
                iter = i5;
                max_iter = max_iter2;
                double d4 = prior0;
                B = p2;
                break;
            }
            int i6 = 0;
            double prior02 = prior0;
            double h21 = 0.0d;
            double q2 = 0.0d;
            double h22 = 1.0E-12d;
            double h11 = 1.0E-12d;
            double g1 = 0.0d;
            while (i6 < i) {
                int max_iter3 = max_iter2;
                double fApB = (dec_values[i6] * fval) + p2;
                if (fApB >= 0.0d) {
                    B2 = p2;
                    g22 = q2;
                    p = Math.exp(-fApB) / (Math.exp(-fApB) + 1.0d);
                    q = 1.0d / (Math.exp(-fApB) + 1.0d);
                } else {
                    g22 = q2;
                    B2 = p2;
                    p = 1.0d / (Math.exp(fApB) + 1.0d);
                    q = Math.exp(fApB) / (Math.exp(fApB) + 1.0d);
                }
                double d22 = p * q;
                h11 += dec_values[i6] * dec_values[i6] * d22;
                h22 += d22;
                h21 += dec_values[i6] * d22;
                double d1 = t[i6] - p;
                g1 += dec_values[i6] * d1;
                i6++;
                max_iter2 = max_iter3;
                p2 = B2;
                q2 = g22 + d1;
            }
            max_iter = max_iter2;
            double g23 = q2;
            B = p2;
            if (Math.abs(g1) < 1.0E-5d) {
                g2 = g23;
                if (Math.abs(g2) < 1.0E-5d) {
                    iter = i5;
                    break;
                }
            } else {
                g2 = g23;
            }
            double det = (h11 * h22) - (h21 * h21);
            double dA = (-((h22 * g1) - (h21 * g2))) / det;
            iter = i5;
            int i7 = i6;
            double dB2 = (-(((-h21) * g1) + (h11 * g2))) / det;
            double gd = (g1 * dA) + (g2 * dB2);
            double stepsize = 1.0d;
            while (true) {
                if (stepsize < 1.0E-10d) {
                    double d5 = dB2;
                    int i8 = i7;
                    break;
                }
                double newA = fval + (stepsize * dA);
                double newB = B + (stepsize * dB2);
                double newf = 0.0d;
                double h212 = h21;
                int i9 = 0;
                while (i9 < i) {
                    double fApB2 = (dec_values[i9] * newA) + newB;
                    if (fApB2 >= 0.0d) {
                        dB = dB2;
                        newf += (t[i9] * fApB2) + Math.log(Math.exp(-fApB2) + 1.0d);
                        double d6 = fApB2;
                    } else {
                        dB = dB2;
                        double d7 = fApB2;
                        newf += ((t[i9] - 1.0d) * fApB2) + Math.log(Math.exp(fApB2) + 1.0d);
                    }
                    i9++;
                    dB2 = dB;
                    i = l;
                }
                double dB3 = dB2;
                if (newf < fval2 + (1.0E-4d * stepsize * gd)) {
                    int i10 = i9;
                    B = newB;
                    fval2 = newf;
                    fval = newA;
                    break;
                }
                stepsize /= 2.0d;
                i7 = i9;
                h21 = h212;
                dB2 = dB3;
                i = l;
            }
            if (stepsize < 1.0E-10d) {
                info("Line search fails in two-class probability estimates\n");
                break;
            }
            i5 = iter + 1;
            prior0 = prior02;
            max_iter2 = max_iter;
            p2 = B;
            i = l;
        }
        if (iter >= max_iter) {
            info("Reaching maximal iterations in two-class probability estimates\n");
        }
        probAB[0] = fval;
        probAB[1] = B;
    }

    private static double sigmoid_predict(double decision_value, double A, double B) {
        double fApB = (decision_value * A) + B;
        if (fApB >= 0.0d) {
            return Math.exp(-fApB) / (Math.exp(-fApB) + 1.0d);
        }
        return 1.0d / (Math.exp(fApB) + 1.0d);
    }

    private static void multiclass_probability(int k, double[][] r, double[] p) {
        int i = k;
        int max_iter = Math.max(100, i);
        double[][] Q = (double[][]) Array.newInstance(double.class, new int[]{i, i});
        double[] Qp = new double[i];
        double eps = 0.005d / ((double) i);
        for (int t = 0; t < i; t++) {
            p[t] = 1.0d / ((double) i);
            Q[t][t] = 0.0d;
            for (int j = 0; j < t; j++) {
                double[] dArr = Q[t];
                dArr[t] = dArr[t] + (r[j][t] * r[j][t]);
                Q[t][j] = Q[j][t];
            }
            for (int j2 = t + 1; j2 < i; j2++) {
                double[] dArr2 = Q[t];
                dArr2[t] = dArr2[t] + (r[j2][t] * r[j2][t]);
                Q[t][j2] = (-r[j2][t]) * r[t][j2];
            }
        }
        int iter = 0;
        while (iter < max_iter) {
            double pQp = 0.0d;
            for (int t2 = 0; t2 < i; t2++) {
                Qp[t2] = 0.0d;
                int j3 = 0;
                while (true) {
                    int j4 = j3;
                    if (j4 >= i) {
                        break;
                    }
                    Qp[t2] = Qp[t2] + (Q[t2][j4] * p[j4]);
                    j3 = j4 + 1;
                }
                pQp += p[t2] * Qp[t2];
            }
            double max_error = 0.0d;
            for (int t3 = 0; t3 < i; t3++) {
                double max_error2 = Math.abs(Qp[t3] - pQp);
                if (max_error2 > max_error) {
                    max_error = max_error2;
                }
            }
            if (max_error < eps) {
                break;
            }
            for (int t4 = 0; t4 < i; t4++) {
                double diff = ((-Qp[t4]) + pQp) / Q[t4][t4];
                p[t4] = p[t4] + diff;
                pQp = ((pQp + (((Q[t4][t4] * diff) + (Qp[t4] * 2.0d)) * diff)) / (diff + 1.0d)) / (diff + 1.0d);
                for (int j5 = 0; j5 < i; j5++) {
                    Qp[j5] = (Qp[j5] + (Q[t4][j5] * diff)) / (diff + 1.0d);
                    p[j5] = p[j5] / (diff + 1.0d);
                }
            }
            iter++;
        }
        if (iter >= max_iter) {
            info("Exceeds max_iter in multiclass_prob\n");
        }
    }

    private static void svm_binary_svc_probability(svm_problem prob, svm_parameter param, double Cp, double Cn, double[] probAB) {
        svm_problem svm_problem = prob;
        int nr_fold = 5;
        int[] perm = new int[svm_problem.f498l];
        double[] dec_values = new double[svm_problem.f498l];
        int i = 0;
        for (int i2 = 0; i2 < svm_problem.f498l; i2++) {
            perm[i2] = i2;
        }
        for (int i3 = 0; i3 < svm_problem.f498l; i3++) {
            int j = rand.nextInt(svm_problem.f498l - i3) + i3;
            int _ = perm[i3];
            perm[i3] = perm[j];
            perm[j] = _;
        }
        int i4 = 0;
        while (i4 < nr_fold) {
            int begin = (svm_problem.f498l * i4) / nr_fold;
            int end = ((i4 + 1) * svm_problem.f498l) / nr_fold;
            svm_problem subprob = new svm_problem();
            subprob.f498l = svm_problem.f498l - (end - begin);
            subprob.f499x = new svm_node[subprob.f498l][];
            subprob.f500y = new double[subprob.f498l];
            int k = 0;
            for (int j2 = 0; j2 < begin; j2++) {
                subprob.f499x[k] = svm_problem.f499x[perm[j2]];
                subprob.f500y[k] = svm_problem.f500y[perm[j2]];
                k++;
            }
            for (int j3 = end; j3 < svm_problem.f498l; j3++) {
                subprob.f499x[k] = svm_problem.f499x[perm[j3]];
                subprob.f500y[k] = svm_problem.f500y[perm[j3]];
                k++;
            }
            int p_count = 0;
            int n_count = 0;
            for (int j4 = 0; j4 < k; j4++) {
                if (subprob.f500y[j4] > 0.0d) {
                    p_count++;
                } else {
                    n_count++;
                }
            }
            if (p_count == 0 && n_count == 0) {
                for (int j5 = begin; j5 < end; j5++) {
                    dec_values[perm[j5]] = 0.0d;
                }
            } else if (p_count > 0 && n_count == 0) {
                for (int j6 = begin; j6 < end; j6++) {
                    dec_values[perm[j6]] = 1.0d;
                }
            } else if (p_count != 0 || n_count <= 0) {
                svm_parameter subparam = (svm_parameter) param.clone();
                subparam.probability = i;
                subparam.f495C = 1.0d;
                subparam.nr_weight = 2;
                subparam.weight_label = new int[2];
                subparam.weight = new double[2];
                int i5 = 1;
                subparam.weight_label[i] = 1;
                subparam.weight_label[1] = -1;
                subparam.weight[i] = Cp;
                subparam.weight[1] = Cn;
                svm_model submodel = svm_train(subprob, subparam);
                int j7 = begin;
                while (j7 < end) {
                    double[] dec_value = new double[i5];
                    svm_predict_values(submodel, svm_problem.f499x[perm[j7]], dec_value);
                    dec_values[perm[j7]] = dec_value[0];
                    int i6 = perm[j7];
                    int nr_fold2 = nr_fold;
                    int[] perm2 = perm;
                    dec_values[i6] = dec_values[i6] * ((double) submodel.label[0]);
                    j7++;
                    nr_fold = nr_fold2;
                    perm = perm2;
                    i5 = 1;
                }
            } else {
                for (int j8 = begin; j8 < end; j8++) {
                    dec_values[perm[j8]] = -1.0d;
                }
            }
            i4++;
            nr_fold = nr_fold;
            perm = perm;
            i = 0;
        }
        int[] iArr = perm;
        sigmoid_train(svm_problem.f498l, dec_values, svm_problem.f500y, probAB);
    }

    private static double svm_svr_probability(svm_problem prob, svm_parameter param) {
        double[] ymv = new double[prob.f498l];
        double mae = 0.0d;
        svm_parameter newparam = (svm_parameter) param.clone();
        newparam.probability = 0;
        svm_cross_validation(prob, newparam, 5, ymv);
        for (int i = 0; i < prob.f498l; i++) {
            ymv[i] = prob.f500y[i] - ymv[i];
            mae += Math.abs(ymv[i]);
        }
        double mae2 = mae / ((double) prob.f498l);
        double std = Math.sqrt(2.0d * mae2 * mae2);
        int count = 0;
        double mae3 = 0.0d;
        for (int i2 = 0; i2 < prob.f498l; i2++) {
            if (Math.abs(ymv[i2]) > 5.0d * std) {
                count++;
            } else {
                mae3 += Math.abs(ymv[i2]);
            }
        }
        double mae4 = mae3 / ((double) (prob.f498l - count));
        StringBuilder sb = new StringBuilder();
        sb.append("Prob. model for test data: target value = predicted value + z,\nz: Laplace distribution e^(-|z|/sigma)/(2sigma),sigma=");
        sb.append(mae4);
        sb.append("\n");
        info(sb.toString());
        return mae4;
    }

    private static void svm_group_classes(svm_problem prob, int[] nr_class_ret, int[][] label_ret, int[][] start_ret, int[][] count_ret, int[] perm) {
        svm_problem svm_problem = prob;
        int l = svm_problem.f498l;
        int nr_class = 0;
        int[] label = new int[16];
        int[] count = new int[16];
        int[] data_label = new int[l];
        int max_nr_class = 16;
        for (int i = 0; i < l; i++) {
            int this_label = (int) svm_problem.f500y[i];
            int j = 0;
            while (true) {
                if (j >= nr_class) {
                    break;
                } else if (this_label == label[j]) {
                    count[j] = count[j] + 1;
                    break;
                } else {
                    j++;
                }
            }
            data_label[i] = j;
            if (j == nr_class) {
                if (nr_class == max_nr_class) {
                    max_nr_class *= 2;
                    int[] new_data = new int[max_nr_class];
                    System.arraycopy(label, 0, new_data, 0, label.length);
                    label = new_data;
                    int[] new_data2 = new int[max_nr_class];
                    System.arraycopy(count, 0, new_data2, 0, count.length);
                    count = new_data2;
                }
                label[nr_class] = this_label;
                count[nr_class] = 1;
                nr_class++;
            }
        }
        int[] start = new int[nr_class];
        start[0] = 0;
        for (int i2 = 1; i2 < nr_class; i2++) {
            start[i2] = start[i2 - 1] + count[i2 - 1];
        }
        for (int i3 = 0; i3 < l; i3++) {
            perm[start[data_label[i3]]] = i3;
            int i4 = data_label[i3];
            start[i4] = start[i4] + 1;
        }
        start[0] = 0;
        for (int i5 = 1; i5 < nr_class; i5++) {
            start[i5] = start[i5 - 1] + count[i5 - 1];
        }
        nr_class_ret[0] = nr_class;
        label_ret[0] = label;
        start_ret[0] = start;
        count_ret[0] = count;
    }

    public static svm_model svm_train(svm_problem prob, svm_parameter param) {
        svm_model model;
        int i;
        int[] iArr;
        int i2;
        svm_problem svm_problem;
        int l;
        int nnz;
        int[] nz_start;
        int l2;
        int[] nz_count;
        svm_model model2;
        int l3;
        int[] tmp_nr_class;
        svm_problem sub_prob;
        int[][] tmp_label;
        int[][] tmp_start;
        int j;
        boolean z;
        int ci;
        svm_problem svm_problem2 = prob;
        svm_parameter svm_parameter = param;
        svm_model model3 = new svm_model();
        model3.param = svm_parameter;
        if (svm_parameter.svm_type == 2 || svm_parameter.svm_type == 3) {
            model = model3;
            i2 = 2;
            iArr = null;
            i = 1;
        } else if (svm_parameter.svm_type == 4) {
            model = model3;
            i2 = 2;
            iArr = null;
            i = 1;
        } else {
            int l4 = svm_problem2.f498l;
            int[] tmp_nr_class2 = new int[1];
            int[][] tmp_label2 = new int[1][];
            int[][] tmp_start2 = new int[1][];
            int[][] tmp_count = new int[1][];
            int[] perm = new int[l4];
            int[][] tmp_count2 = tmp_count;
            svm_group_classes(svm_problem2, tmp_nr_class2, tmp_label2, tmp_start2, tmp_count, perm);
            int nr_class = tmp_nr_class2[0];
            int[] label = tmp_label2[0];
            int[] start = tmp_start2[0];
            int[] count = tmp_count2[0];
            svm_node[][] x = new svm_node[l4][];
            for (int i3 = 0; i3 < l4; i3++) {
                x[i3] = svm_problem2.f499x[perm[i3]];
            }
            double[] weighted_C = new double[nr_class];
            for (int i4 = 0; i4 < nr_class; i4++) {
                weighted_C[i4] = svm_parameter.f495C;
            }
            int i5 = 0;
            while (i5 < svm_parameter.nr_weight) {
                int j2 = 0;
                while (j2 < nr_class && svm_parameter.weight_label[i5] != label[j2]) {
                    j2++;
                }
                if (j2 == nr_class) {
                    PrintStream printStream = System.err;
                    StringBuilder sb = new StringBuilder();
                    sb.append("warning: class label ");
                    sb.append(svm_parameter.weight_label[i5]);
                    sb.append(" specified in weight is not found\n");
                    printStream.print(sb.toString());
                } else {
                    weighted_C[j2] = weighted_C[j2] * svm_parameter.weight[i5];
                }
                i5++;
            }
            boolean[] nonzero = new boolean[l4];
            int i6 = 0;
            while (i6 < l4) {
                nonzero[i6] = false;
                i6++;
            }
            decision_function[] f = new decision_function[(((nr_class - 1) * nr_class) / 2)];
            double[] probA = null;
            double[] probB = null;
            int i7 = i6;
            if (svm_parameter.probability == 1) {
                probA = new double[(((nr_class - 1) * nr_class) / 2)];
                probB = new double[(((nr_class - 1) * nr_class) / 2)];
            }
            double[] probA2 = probA;
            double[] probB2 = probB;
            int i8 = 0;
            int j3 = 0;
            while (j3 < nr_class) {
                int i9 = j3 + 1;
                int p = i8;
                while (true) {
                    int j4 = i9;
                    if (j4 >= nr_class) {
                        break;
                    }
                    svm_problem sub_prob2 = new svm_problem();
                    int si = start[j3];
                    int sj = start[j4];
                    int ci2 = count[j3];
                    int cj = count[j4];
                    decision_function[] f2 = f;
                    sub_prob2.f498l = ci2 + cj;
                    sub_prob2.f499x = new svm_node[sub_prob2.f498l][];
                    sub_prob2.f500y = new double[sub_prob2.f498l];
                    int k = 0;
                    while (k < ci2) {
                        boolean[] nonzero2 = nonzero;
                        sub_prob2.f499x[k] = x[si + k];
                        sub_prob2.f500y[k] = 1.0d;
                        k++;
                        nonzero = nonzero2;
                    }
                    boolean[] nonzero3 = nonzero;
                    int k2 = 0;
                    while (k2 < cj) {
                        sub_prob2.f499x[ci2 + k2] = x[sj + k2];
                        sub_prob2.f500y[ci2 + k2] = -1.0d;
                        k2++;
                    }
                    int k3 = k2;
                    if (svm_parameter.probability == 1) {
                        double[] probAB = new double[2];
                        tmp_start = tmp_start2;
                        tmp_label = tmp_label2;
                        l3 = l4;
                        tmp_nr_class = tmp_nr_class2;
                        double d = weighted_C[j3];
                        sub_prob = sub_prob2;
                        ci = ci2;
                        model2 = model3;
                        z = true;
                        j = j4;
                        svm_binary_svc_probability(sub_prob2, svm_parameter, d, weighted_C[j4], probAB);
                        probA2[p] = probAB[0];
                        probB2[p] = probAB[1];
                    } else {
                        tmp_start = tmp_start2;
                        tmp_label = tmp_label2;
                        l3 = l4;
                        tmp_nr_class = tmp_nr_class2;
                        sub_prob = sub_prob2;
                        ci = ci2;
                        j = j4;
                        model2 = model3;
                        z = true;
                    }
                    double d2 = weighted_C[j3];
                    double d3 = weighted_C[j];
                    int i10 = j3;
                    int ci3 = ci;
                    decision_function[] f3 = f2;
                    double[] weighted_C2 = weighted_C;
                    int i11 = k3;
                    double d4 = d2;
                    int nr_class2 = nr_class;
                    svm_node[][] x2 = x;
                    f3[p] = svm_train_one(sub_prob, svm_parameter, d4, d3);
                    for (int k4 = 0; k4 < ci3; k4++) {
                        if (!nonzero3[si + k4] && Math.abs(f3[p].alpha[k4]) > 0.0d) {
                            nonzero3[si + k4] = z;
                        }
                    }
                    for (int k5 = 0; k5 < cj; k5++) {
                        if (!nonzero3[sj + k5] && Math.abs(f3[p].alpha[ci3 + k5]) > 0.0d) {
                            nonzero3[sj + k5] = z;
                        }
                    }
                    p++;
                    nr_class = nr_class2;
                    x = x2;
                    f = f3;
                    tmp_start2 = tmp_start;
                    tmp_label2 = tmp_label;
                    weighted_C = weighted_C2;
                    nonzero = nonzero3;
                    tmp_nr_class2 = tmp_nr_class;
                    l4 = l3;
                    model3 = model2;
                    svm_problem svm_problem3 = prob;
                    int i12 = i10;
                    i9 = j + 1;
                    j3 = i12;
                }
                decision_function[] decision_functionArr = f;
                double[] dArr = weighted_C;
                boolean[] zArr = nonzero;
                int[][] tmp_start3 = tmp_start2;
                int[][] tmp_label3 = tmp_label2;
                int i13 = l4;
                int[] iArr2 = tmp_nr_class2;
                int i14 = nr_class;
                svm_node[][] svm_nodeArr = x;
                j3++;
                tmp_start2 = tmp_start3;
                tmp_label2 = tmp_label3;
                i8 = p;
                model3 = model3;
                svm_problem svm_problem4 = prob;
            }
            int i15 = j3;
            decision_function[] f4 = f;
            double[] dArr2 = weighted_C;
            boolean[] nonzero4 = nonzero;
            int[][] iArr3 = tmp_start2;
            int[][] iArr4 = tmp_label2;
            int l5 = l4;
            int[] iArr5 = tmp_nr_class2;
            int nr_class3 = nr_class;
            svm_node[][] x3 = x;
            model = model3;
            model.nr_class = nr_class3;
            model.label = new int[nr_class3];
            for (int i16 = 0; i16 < nr_class3; i16++) {
                model.label[i16] = label[i16];
            }
            model.rho = new double[(((nr_class3 - 1) * nr_class3) / 2)];
            for (int i17 = 0; i17 < ((nr_class3 - 1) * nr_class3) / 2; i17++) {
                model.rho[i17] = f4[i17].rho;
            }
            if (svm_parameter.probability == 1) {
                model.probA = new double[(((nr_class3 - 1) * nr_class3) / 2)];
                model.probB = new double[(((nr_class3 - 1) * nr_class3) / 2)];
                for (int i18 = 0; i18 < ((nr_class3 - 1) * nr_class3) / 2; i18++) {
                    model.probA[i18] = probA2[i18];
                    model.probB[i18] = probB2[i18];
                }
            } else {
                model.probA = null;
                model.probB = null;
            }
            int nnz2 = 0;
            int[] nz_count2 = new int[nr_class3];
            model.nSV = new int[nr_class3];
            int i19 = 0;
            while (i19 < nr_class3) {
                int nSV = 0;
                int nnz3 = nnz2;
                for (int j5 = 0; j5 < count[i19]; j5++) {
                    if (nonzero4[start[i19] + j5]) {
                        nSV++;
                        nnz3++;
                    }
                }
                model.nSV[i19] = nSV;
                nz_count2[i19] = nSV;
                i19++;
                nnz2 = nnz3;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Total nSV = ");
            sb2.append(nnz2);
            sb2.append("\n");
            info(sb2.toString());
            model.f494l = nnz2;
            model.f493SV = new svm_node[nnz2][];
            int p2 = 0;
            int i20 = 0;
            while (true) {
                l = l5;
                if (i20 >= l) {
                    break;
                }
                if (nonzero4[i20]) {
                    int p3 = p2 + 1;
                    model.f493SV[p2] = x3[i20];
                    p2 = p3;
                }
                i20++;
                l5 = l;
            }
            int[] nz_start2 = new int[nr_class3];
            nz_start2[0] = 0;
            for (int i21 = 1; i21 < nr_class3; i21++) {
                nz_start2[i21] = nz_start2[i21 - 1] + nz_count2[i21 - 1];
            }
            model.sv_coef = new double[(nr_class3 - 1)][];
            for (int i22 = 0; i22 < nr_class3 - 1; i22++) {
                model.sv_coef[i22] = new double[nnz2];
            }
            int p4 = 0;
            for (int i23 = 0; i23 < nr_class3; i23++) {
                int j6 = i23 + 1;
                while (j6 < nr_class3) {
                    int si2 = start[i23];
                    int sj2 = start[j6];
                    int ci4 = count[i23];
                    int cj2 = count[j6];
                    int q = nz_start2[i23];
                    int q2 = 0;
                    while (true) {
                        nnz = nnz2;
                        int nnz4 = q2;
                        if (nnz4 >= ci4) {
                            break;
                        }
                        if (nonzero4[si2 + nnz4]) {
                            nz_count = nz_count2;
                            int q3 = q + 1;
                            l2 = l;
                            model.sv_coef[j6 - 1][q] = f4[p4].alpha[nnz4];
                            q = q3;
                        } else {
                            nz_count = nz_count2;
                            l2 = l;
                        }
                        q2 = nnz4 + 1;
                        nnz2 = nnz;
                        nz_count2 = nz_count;
                        l = l2;
                    }
                    int[] nz_count3 = nz_count2;
                    int l6 = l;
                    int q4 = nz_start2[j6];
                    int k6 = 0;
                    while (k6 < cj2) {
                        if (nonzero4[sj2 + k6]) {
                            int q5 = q4 + 1;
                            nz_start = nz_start2;
                            model.sv_coef[i23][q4] = f4[p4].alpha[ci4 + k6];
                            q4 = q5;
                        } else {
                            nz_start = nz_start2;
                        }
                        k6++;
                        nz_start2 = nz_start;
                    }
                    p4++;
                    j6++;
                    nnz2 = nnz;
                    nz_count2 = nz_count3;
                    l = l6;
                }
                int[] iArr6 = nz_count2;
                int i24 = l;
                int[] iArr7 = nz_start2;
            }
            svm_problem svm_problem5 = prob;
            return model;
        }
        model.nr_class = i2;
        model.label = iArr;
        model.nSV = iArr;
        model.probA = iArr;
        model.probB = iArr;
        model.sv_coef = new double[i][];
        if (svm_parameter.probability == i && (svm_parameter.svm_type == 3 || svm_parameter.svm_type == 4)) {
            model.probA = new double[i];
            svm_problem = prob;
            model.probA[0] = svm_svr_probability(prob, param);
        } else {
            svm_problem = prob;
        }
        decision_function f5 = svm_train_one(svm_problem, svm_parameter, 0.0d, 0.0d);
        model.rho = new double[i];
        model.rho[0] = f5.rho;
        int nSV2 = 0;
        for (int i25 = 0; i25 < svm_problem.f498l; i25++) {
            if (Math.abs(f5.alpha[i25]) > 0.0d) {
                nSV2++;
            }
        }
        model.f494l = nSV2;
        model.f493SV = new svm_node[nSV2][];
        model.sv_coef[0] = new double[nSV2];
        int j7 = 0;
        for (int i26 = 0; i26 < svm_problem.f498l; i26++) {
            if (Math.abs(f5.alpha[i26]) > 0.0d) {
                model.f493SV[j7] = svm_problem.f499x[i26];
                model.sv_coef[0][j7] = f5.alpha[i26];
                j7++;
            }
        }
        return model;
    }

    public static void svm_cross_validation(svm_problem prob, svm_parameter param, int nr_fold, double[] target) {
        int nr_class;
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int i = nr_fold;
        int[] fold_start = new int[(i + 1)];
        int l = svm_problem.f498l;
        int[] perm = new int[l];
        if ((svm_parameter.svm_type == 0 || svm_parameter.svm_type == 1) && i < l) {
            int[] tmp_nr_class = new int[1];
            int[][] tmp_label = new int[1][];
            int[][] tmp_start = new int[1][];
            int[][] tmp_count = new int[1][];
            int[][] tmp_start2 = tmp_start;
            int[][] iArr = tmp_label;
            svm_group_classes(svm_problem, tmp_nr_class, tmp_label, tmp_start, tmp_count, perm);
            int j = tmp_nr_class[0];
            int[] start = tmp_start2[0];
            int[] count = tmp_count[0];
            int[] fold_count = new int[i];
            int[] index = new int[l];
            int i2 = 0;
            while (i2 < l) {
                index[i2] = perm[i2];
                i2++;
            }
            int i3 = i2;
            int c = 0;
            while (c < j) {
                int i4 = 0;
                while (i4 < count[c]) {
                    int[] tmp_nr_class2 = tmp_nr_class;
                    int j2 = rand.nextInt(count[c] - i4) + i4;
                    int _ = index[start[c] + j2];
                    index[start[c] + j2] = index[start[c] + i4];
                    index[start[c] + i4] = _;
                    i4++;
                    tmp_nr_class = tmp_nr_class2;
                }
                c++;
                int i5 = i4;
            }
            for (int i6 = 0; i6 < i; i6++) {
                fold_count[i6] = 0;
                for (int c2 = 0; c2 < j; c2++) {
                    fold_count[i6] = fold_count[i6] + ((((i6 + 1) * count[c2]) / i) - ((count[c2] * i6) / i));
                }
            }
            fold_start[0] = 0;
            for (int i7 = 1; i7 <= i; i7++) {
                fold_start[i7] = fold_start[i7 - 1] + fold_count[i7 - 1];
            }
            for (int c3 = 0; c3 < j; c3++) {
                int i8 = 0;
                while (i8 < i) {
                    int end = start[c3] + (((i8 + 1) * count[c3]) / i);
                    int j3 = start[c3] + ((count[c3] * i8) / i);
                    while (true) {
                        nr_class = j;
                        int j4 = j3;
                        if (j4 >= end) {
                            break;
                        }
                        perm[fold_start[i8]] = index[j4];
                        fold_start[i8] = fold_start[i8] + 1;
                        j3 = j4 + 1;
                        j = nr_class;
                    }
                    i8++;
                    j = nr_class;
                }
                int nr_class2 = j;
            }
            int nr_class3 = j;
            fold_start[0] = 0;
            for (int i9 = 1; i9 <= i; i9++) {
                fold_start[i9] = fold_start[i9 - 1] + fold_count[i9 - 1];
            }
        } else {
            for (int i10 = 0; i10 < l; i10++) {
                perm[i10] = i10;
            }
            for (int i11 = 0; i11 < l; i11++) {
                int j5 = rand.nextInt(l - i11) + i11;
                int _2 = perm[i11];
                perm[i11] = perm[j5];
                perm[j5] = _2;
            }
            for (int i12 = 0; i12 <= i; i12++) {
                fold_start[i12] = (i12 * l) / i;
            }
        }
        for (int i13 = 0; i13 < i; i13++) {
            int begin = fold_start[i13];
            int end2 = fold_start[i13 + 1];
            svm_problem subprob = new svm_problem();
            subprob.f498l = l - (end2 - begin);
            subprob.f499x = new svm_node[subprob.f498l][];
            subprob.f500y = new double[subprob.f498l];
            int k = 0;
            for (int j6 = 0; j6 < begin; j6++) {
                subprob.f499x[k] = svm_problem.f499x[perm[j6]];
                subprob.f500y[k] = svm_problem.f500y[perm[j6]];
                k++;
            }
            for (int j7 = end2; j7 < l; j7++) {
                subprob.f499x[k] = svm_problem.f499x[perm[j7]];
                subprob.f500y[k] = svm_problem.f500y[perm[j7]];
                k++;
            }
            svm_model submodel = svm_train(subprob, svm_parameter);
            if (svm_parameter.probability == 1 && (svm_parameter.svm_type == 0 || svm_parameter.svm_type == 1)) {
                double[] prob_estimates = new double[svm_get_nr_class(submodel)];
                for (int j8 = begin; j8 < end2; j8++) {
                    target[perm[j8]] = svm_predict_probability(submodel, svm_problem.f499x[perm[j8]], prob_estimates);
                }
            } else {
                for (int j9 = begin; j9 < end2; j9++) {
                    target[perm[j9]] = svm_predict(submodel, svm_problem.f499x[perm[j9]]);
                }
            }
        }
    }

    public static int svm_get_svm_type(svm_model model) {
        return model.param.svm_type;
    }

    public static int svm_get_nr_class(svm_model model) {
        return model.nr_class;
    }

    public static void svm_get_labels(svm_model model, int[] label) {
        if (model.label != null) {
            for (int i = 0; i < model.nr_class; i++) {
                label[i] = model.label[i];
            }
        }
    }

    public static double svm_get_svr_probability(svm_model model) {
        if ((model.param.svm_type == 3 || model.param.svm_type == 4) && model.probA != null) {
            return model.probA[0];
        }
        System.err.print("Model doesn't contain information for SVR probability inference\n");
        return 0.0d;
    }

    public static double svm_predict_values(svm_model model, svm_node[] x, double[] dec_values) {
        svm_model svm_model = model;
        svm_node[] svm_nodeArr = x;
        if (svm_model.param.svm_type == 2 || svm_model.param.svm_type == 3 || svm_model.param.svm_type == 4) {
            double[] sv_coef = svm_model.sv_coef[0];
            double sum = 0.0d;
            for (int i = 0; i < svm_model.f494l; i++) {
                sum += sv_coef[i] * Kernel.k_function(svm_nodeArr, svm_model.f493SV[i], svm_model.param);
            }
            double sum2 = sum - svm_model.rho[0];
            dec_values[0] = sum2;
            if (svm_model.param.svm_type != 2) {
                return sum2;
            }
            return sum2 > 0.0d ? 1.0d : -1.0d;
        }
        int nr_class = svm_model.nr_class;
        int l = svm_model.f494l;
        double[] kvalue = new double[l];
        for (int i2 = 0; i2 < l; i2++) {
            kvalue[i2] = Kernel.k_function(svm_nodeArr, svm_model.f493SV[i2], svm_model.param);
        }
        int[] start = new int[nr_class];
        start[0] = 0;
        for (int i3 = 1; i3 < nr_class; i3++) {
            start[i3] = start[i3 - 1] + svm_model.nSV[i3 - 1];
        }
        int[] vote = new int[nr_class];
        for (int i4 = 0; i4 < nr_class; i4++) {
            vote[i4] = 0;
        }
        int p = 0;
        for (int i5 = 0; i5 < nr_class; i5++) {
            int j = i5 + 1;
            while (j < nr_class) {
                int si = start[i5];
                int sj = start[j];
                int ci = svm_model.nSV[i5];
                int cj = svm_model.nSV[j];
                double[] coef1 = svm_model.sv_coef[j - 1];
                int l2 = l;
                double[] coef2 = svm_model.sv_coef[i5];
                double sum3 = 0.0d;
                for (int k = 0; k < ci; k++) {
                    sum3 += coef1[si + k] * kvalue[si + k];
                }
                for (int k2 = 0; k2 < cj; k2++) {
                    sum3 += coef2[sj + k2] * kvalue[sj + k2];
                }
                dec_values[p] = sum3 - svm_model.rho[p];
                if (dec_values[p] > 0.0d) {
                    vote[i5] = vote[i5] + 1;
                } else {
                    vote[j] = vote[j] + 1;
                }
                p++;
                j++;
                l = l2;
            }
        }
        int vote_max_idx = 0;
        for (int i6 = 1; i6 < nr_class; i6++) {
            if (vote[i6] > vote[vote_max_idx]) {
                vote_max_idx = i6;
            }
        }
        return (double) svm_model.label[vote_max_idx];
    }

    public static double svm_predict(svm_model model, svm_node[] x) {
        double[] dec_values;
        int nr_class = model.nr_class;
        if (model.param.svm_type == 2 || model.param.svm_type == 3 || model.param.svm_type == 4) {
            dec_values = new double[1];
        } else {
            dec_values = new double[(((nr_class - 1) * nr_class) / 2)];
        }
        return svm_predict_values(model, x, dec_values);
    }

    public static double svm_predict_probability(svm_model model, svm_node[] x, double[] prob_estimates) {
        svm_model svm_model = model;
        double[] dArr = prob_estimates;
        if ((svm_model.param.svm_type != 0 && svm_model.param.svm_type != 1) || svm_model.probA == null || svm_model.probB == null) {
            return svm_predict(model, x);
        }
        int nr_class = svm_model.nr_class;
        double[] dec_values = new double[(((nr_class - 1) * nr_class) / 2)];
        svm_predict_values(svm_model, x, dec_values);
        double[][] pairwise_prob = (double[][]) Array.newInstance(double.class, new int[]{nr_class, nr_class});
        int k = 0;
        int i = 0;
        while (i < nr_class) {
            int j = i + 1;
            while (j < nr_class) {
                double[] dec_values2 = dec_values;
                pairwise_prob[i][j] = Math.min(Math.max(sigmoid_predict(dec_values[k], svm_model.probA[k], svm_model.probB[k]), 1.0E-7d), 1.0d - 1.0E-7d);
                pairwise_prob[j][i] = 1.0d - pairwise_prob[i][j];
                k++;
                j++;
                dec_values = dec_values2;
                svm_node[] svm_nodeArr = x;
            }
            i++;
            svm_node[] svm_nodeArr2 = x;
        }
        multiclass_probability(nr_class, pairwise_prob, dArr);
        int prob_max_idx = 0;
        for (int i2 = 1; i2 < nr_class; i2++) {
            if (dArr[i2] > dArr[prob_max_idx]) {
                prob_max_idx = i2;
            }
        }
        return (double) svm_model.label[prob_max_idx];
    }

    public static void svm_save_model(String model_file_name, svm_model model) throws IOException {
        DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(model_file_name)));
        svm_parameter param = model.param;
        StringBuilder sb = new StringBuilder();
        sb.append("svm_type ");
        sb.append(svm_type_table[param.svm_type]);
        sb.append("\n");
        fp.writeBytes(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("kernel_type ");
        sb2.append(kernel_type_table[param.kernel_type]);
        sb2.append("\n");
        fp.writeBytes(sb2.toString());
        if (param.kernel_type == 1) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("degree ");
            sb3.append(param.degree);
            sb3.append("\n");
            fp.writeBytes(sb3.toString());
        }
        if (param.kernel_type == 1 || param.kernel_type == 2 || param.kernel_type == 3) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("gamma ");
            sb4.append(param.gamma);
            sb4.append("\n");
            fp.writeBytes(sb4.toString());
        }
        if (param.kernel_type == 1 || param.kernel_type == 3) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("coef0 ");
            sb5.append(param.coef0);
            sb5.append("\n");
            fp.writeBytes(sb5.toString());
        }
        int nr_class = model.nr_class;
        int l = model.f494l;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("nr_class ");
        sb6.append(nr_class);
        sb6.append("\n");
        fp.writeBytes(sb6.toString());
        StringBuilder sb7 = new StringBuilder();
        sb7.append("total_sv ");
        sb7.append(l);
        sb7.append("\n");
        fp.writeBytes(sb7.toString());
        fp.writeBytes("rho");
        for (int i = 0; i < ((nr_class - 1) * nr_class) / 2; i++) {
            StringBuilder sb8 = new StringBuilder();
            sb8.append(" ");
            sb8.append(model.rho[i]);
            fp.writeBytes(sb8.toString());
        }
        fp.writeBytes("\n");
        if (model.label != null) {
            fp.writeBytes("label");
            for (int i2 = 0; i2 < nr_class; i2++) {
                StringBuilder sb9 = new StringBuilder();
                sb9.append(" ");
                sb9.append(model.label[i2]);
                fp.writeBytes(sb9.toString());
            }
            fp.writeBytes("\n");
        }
        if (model.probA != null) {
            fp.writeBytes("probA");
            for (int i3 = 0; i3 < ((nr_class - 1) * nr_class) / 2; i3++) {
                StringBuilder sb10 = new StringBuilder();
                sb10.append(" ");
                sb10.append(model.probA[i3]);
                fp.writeBytes(sb10.toString());
            }
            fp.writeBytes("\n");
        }
        if (model.probB != null) {
            fp.writeBytes("probB");
            for (int i4 = 0; i4 < ((nr_class - 1) * nr_class) / 2; i4++) {
                StringBuilder sb11 = new StringBuilder();
                sb11.append(" ");
                sb11.append(model.probB[i4]);
                fp.writeBytes(sb11.toString());
            }
            fp.writeBytes("\n");
        }
        if (model.nSV != null) {
            fp.writeBytes("nr_sv");
            for (int i5 = 0; i5 < nr_class; i5++) {
                StringBuilder sb12 = new StringBuilder();
                sb12.append(" ");
                sb12.append(model.nSV[i5]);
                fp.writeBytes(sb12.toString());
            }
            fp.writeBytes("\n");
        }
        fp.writeBytes("SV\n");
        double[][] sv_coef = model.sv_coef;
        svm_node[][] SV = model.f493SV;
        for (int i6 = 0; i6 < l; i6++) {
            for (int j = 0; j < nr_class - 1; j++) {
                StringBuilder sb13 = new StringBuilder();
                sb13.append(sv_coef[j][i6]);
                sb13.append(" ");
                fp.writeBytes(sb13.toString());
            }
            svm_node[] p = SV[i6];
            if (param.kernel_type == 4) {
                StringBuilder sb14 = new StringBuilder();
                sb14.append("0:");
                sb14.append((int) p[0].value);
                fp.writeBytes(sb14.toString());
            } else {
                for (int j2 = 0; j2 < p.length; j2++) {
                    StringBuilder sb15 = new StringBuilder();
                    sb15.append(p[j2].index);
                    sb15.append(":");
                    sb15.append(p[j2].value);
                    sb15.append(" ");
                    fp.writeBytes(sb15.toString());
                }
            }
            fp.writeBytes("\n");
        }
        fp.close();
    }

    private static double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    public static svm_model svm_load_model(String model_file_name) throws IOException {
        return svm_load_model(new BufferedReader(new FileReader(model_file_name)));
    }

    public static svm_model svm_load_model(BufferedReader fp) throws IOException {
        int i;
        int i2;
        svm_model model = new svm_model();
        svm_parameter param = new svm_parameter();
        model.param = param;
        model.rho = null;
        model.probA = null;
        model.probB = null;
        model.label = null;
        model.nSV = null;
        while (true) {
            String cmd = fp.readLine();
            String arg = cmd.substring(cmd.indexOf(32) + 1);
            int i3 = 0;
            if (cmd.startsWith("svm_type")) {
                while (true) {
                    i2 = i3;
                    if (i2 >= svm_type_table.length) {
                        break;
                    } else if (arg.indexOf(svm_type_table[i2]) != -1) {
                        param.svm_type = i2;
                        break;
                    } else {
                        i3 = i2 + 1;
                    }
                }
                if (i2 == svm_type_table.length) {
                    System.err.print("unknown svm type.\n");
                    return null;
                }
            } else if (cmd.startsWith("kernel_type")) {
                while (true) {
                    i = i3;
                    if (i >= kernel_type_table.length) {
                        break;
                    } else if (arg.indexOf(kernel_type_table[i]) != -1) {
                        param.kernel_type = i;
                        break;
                    } else {
                        i3 = i + 1;
                    }
                }
                if (i == kernel_type_table.length) {
                    System.err.print("unknown kernel function.\n");
                    return null;
                }
            } else if (cmd.startsWith("degree")) {
                param.degree = atoi(arg);
            } else if (cmd.startsWith("gamma")) {
                param.gamma = atof(arg);
            } else if (cmd.startsWith("coef0")) {
                param.coef0 = atof(arg);
            } else if (cmd.startsWith("nr_class")) {
                model.nr_class = atoi(arg);
            } else if (cmd.startsWith("total_sv")) {
                model.f494l = atoi(arg);
            } else if (cmd.startsWith("rho")) {
                int n = (model.nr_class * (model.nr_class - 1)) / 2;
                model.rho = new double[n];
                StringTokenizer st = new StringTokenizer(arg);
                while (i3 < n) {
                    model.rho[i3] = atof(st.nextToken());
                    i3++;
                }
            } else if (cmd.startsWith("label")) {
                int n2 = model.nr_class;
                model.label = new int[n2];
                StringTokenizer st2 = new StringTokenizer(arg);
                while (i3 < n2) {
                    model.label[i3] = atoi(st2.nextToken());
                    i3++;
                }
            } else if (cmd.startsWith("probA")) {
                int n3 = (model.nr_class * (model.nr_class - 1)) / 2;
                model.probA = new double[n3];
                StringTokenizer st3 = new StringTokenizer(arg);
                while (i3 < n3) {
                    model.probA[i3] = atof(st3.nextToken());
                    i3++;
                }
            } else if (cmd.startsWith("probB")) {
                int n4 = (model.nr_class * (model.nr_class - 1)) / 2;
                model.probB = new double[n4];
                StringTokenizer st4 = new StringTokenizer(arg);
                while (i3 < n4) {
                    model.probB[i3] = atof(st4.nextToken());
                    i3++;
                }
            } else if (cmd.startsWith("nr_sv")) {
                int n5 = model.nr_class;
                model.nSV = new int[n5];
                StringTokenizer st5 = new StringTokenizer(arg);
                while (i3 < n5) {
                    model.nSV[i3] = atoi(st5.nextToken());
                    i3++;
                }
            } else if (cmd.startsWith("SV")) {
                int m = model.nr_class - 1;
                int l = model.f494l;
                model.sv_coef = (double[][]) Array.newInstance(double.class, new int[]{m, l});
                model.f493SV = new svm_node[l][];
                for (int i4 = 0; i4 < l; i4++) {
                    StringTokenizer st6 = new StringTokenizer(fp.readLine(), " \t\n\r\f:");
                    for (int k = 0; k < m; k++) {
                        model.sv_coef[k][i4] = atof(st6.nextToken());
                    }
                    int n6 = st6.countTokens() / 2;
                    model.f493SV[i4] = new svm_node[n6];
                    for (int j = 0; j < n6; j++) {
                        model.f493SV[i4][j] = new svm_node();
                        model.f493SV[i4][j].index = atoi(st6.nextToken());
                        model.f493SV[i4][j].value = atof(st6.nextToken());
                    }
                }
                fp.close();
                return model;
            } else {
                PrintStream printStream = System.err;
                StringBuilder sb = new StringBuilder();
                sb.append("unknown text in model file: [");
                sb.append(cmd);
                sb.append("]\n");
                printStream.print(sb.toString());
                return null;
            }
        }
    }

    public static String svm_check_parameter(svm_problem prob, svm_parameter param) {
        svm_problem svm_problem = prob;
        svm_parameter svm_parameter = param;
        int svm_type = svm_parameter.svm_type;
        if (svm_type != 0 && svm_type != 1 && svm_type != 2 && svm_type != 3 && svm_type != 4) {
            return "unknown svm type";
        }
        int kernel_type = svm_parameter.kernel_type;
        if (kernel_type != 0 && kernel_type != 1 && kernel_type != 2 && kernel_type != 3 && kernel_type != 4) {
            return "unknown kernel type";
        }
        if (svm_parameter.gamma < 0.0d) {
            return "gamma < 0";
        }
        if (svm_parameter.degree < 0) {
            return "degree of polynomial kernel < 0";
        }
        if (svm_parameter.cache_size <= 0.0d) {
            return "cache_size <= 0";
        }
        if (svm_parameter.eps <= 0.0d) {
            return "eps <= 0";
        }
        if ((svm_type == 0 || svm_type == 3 || svm_type == 4) && svm_parameter.f495C <= 0.0d) {
            return "C <= 0";
        }
        if ((svm_type == 1 || svm_type == 2 || svm_type == 4) && (svm_parameter.f496nu <= 0.0d || svm_parameter.f496nu > 1.0d)) {
            return "nu <= 0 or nu > 1";
        }
        if (svm_type == 3 && svm_parameter.f497p < 0.0d) {
            return "p < 0";
        }
        if (svm_parameter.shrinking != 0 && svm_parameter.shrinking != 1) {
            return "shrinking != 0 and shrinking != 1";
        }
        if (svm_parameter.probability != 0 && svm_parameter.probability != 1) {
            return "probability != 0 and probability != 1";
        }
        if (svm_parameter.probability == 1 && svm_type == 2) {
            return "one-class SVM probability output not supported yet";
        }
        if (svm_type == 1) {
            int l = svm_problem.f498l;
            int nr_class = 0;
            int[] label = new int[16];
            int[] count = new int[16];
            int max_nr_class = 16;
            for (int i = 0; i < l; i++) {
                int this_label = (int) svm_problem.f500y[i];
                int j = 0;
                while (true) {
                    if (j >= nr_class) {
                        break;
                    } else if (this_label == label[j]) {
                        count[j] = count[j] + 1;
                        break;
                    } else {
                        j++;
                    }
                }
                if (j == nr_class) {
                    if (nr_class == max_nr_class) {
                        max_nr_class *= 2;
                        int[] new_data = new int[max_nr_class];
                        System.arraycopy(label, 0, new_data, 0, label.length);
                        label = new_data;
                        int[] new_data2 = new int[max_nr_class];
                        System.arraycopy(count, 0, new_data2, 0, count.length);
                        count = new_data2;
                    }
                    label[nr_class] = this_label;
                    count[nr_class] = 1;
                    nr_class++;
                }
            }
            int i2 = 0;
            while (i2 < nr_class) {
                int n1 = count[i2];
                int j2 = i2 + 1;
                while (j2 < nr_class) {
                    int n2 = count[j2];
                    if ((svm_parameter.f496nu * ((double) (n1 + n2))) / 2.0d > ((double) Math.min(n1, n2))) {
                        return "specified nu is infeasible";
                    }
                    j2++;
                    svm_problem svm_problem2 = prob;
                    svm_parameter = param;
                }
                i2++;
                svm_problem svm_problem3 = prob;
                svm_parameter = param;
            }
        }
        return null;
    }

    public static int svm_check_probability_model(svm_model model) {
        if (((model.param.svm_type == 0 || model.param.svm_type == 1) && model.probA != null && model.probB != null) || ((model.param.svm_type == 3 || model.param.svm_type == 4) && model.probA != null)) {
            return 1;
        }
        return 0;
    }

    public static void svm_set_print_string_function(svm_print_interface print_func) {
        if (print_func == null) {
            svm_print_string = svm_print_stdout;
        } else {
            svm_print_string = print_func;
        }
    }
}
