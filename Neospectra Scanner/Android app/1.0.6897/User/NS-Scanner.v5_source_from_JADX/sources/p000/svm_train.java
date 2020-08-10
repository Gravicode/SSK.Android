package p000;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;

/* renamed from: svm_train */
class svm_train {
    private static svm_print_interface svm_print_null = new svm_print_interface() {
        public void print(String s) {
        }
    };
    private int cross_validation;
    private String error_msg;
    private String input_file_name;
    private svm_model model;
    private String model_file_name;
    private int nr_fold;
    private svm_parameter param;
    private svm_problem prob;

    svm_train() {
    }

    private static void exit_with_help() {
        System.out.print("Usage: svm_train [options] training_set_file [model_file]\noptions:\n-s svm_type : set type of SVM (default 0)\n\t0 -- C-SVC\n\t1 -- nu-SVC\n\t2 -- one-class SVM\n\t3 -- epsilon-SVR\n\t4 -- nu-SVR\n-t kernel_type : set type of kernel function (default 2)\n\t0 -- linear: u'*v\n\t1 -- polynomial: (gamma*u'*v + coef0)^degree\n\t2 -- radial basis function: exp(-gamma*|u-v|^2)\n\t3 -- sigmoid: tanh(gamma*u'*v + coef0)\n\t4 -- precomputed kernel (kernel values in training_set_file)\n-d degree : set degree in kernel function (default 3)\n-g gamma : set gamma in kernel function (default 1/num_features)\n-r coef0 : set coef0 in kernel function (default 0)\n-c cost : set the parameter C of C-SVC, epsilon-SVR, and nu-SVR (default 1)\n-n nu : set the parameter nu of nu-SVC, one-class SVM, and nu-SVR (default 0.5)\n-p epsilon : set the epsilon in loss function of epsilon-SVR (default 0.1)\n-m cachesize : set cache memory size in MB (default 100)\n-e epsilon : set tolerance of termination criterion (default 0.001)\n-h shrinking : whether to use the shrinking heuristics, 0 or 1 (default 1)\n-b probability_estimates : whether to train a SVC or SVR model for probability estimates, 0 or 1 (default 0)\n-wi weight : set the parameter C of class i to weight*C, for C-SVC (default 1)\n-v n : n-fold cross validation mode\n-q : quiet mode (no outputs)\n");
        System.exit(1);
    }

    private void do_cross_validation() {
        double sumv;
        double sumy = 0.0d;
        double sumvv = 0.0d;
        double sumyy = 0.0d;
        double sumvy = 0.0d;
        double[] target = new double[this.prob.f498l];
        double total_error = 0.0d;
        svm.svm_cross_validation(this.prob, this.param, this.nr_fold, target);
        int i = 0;
        if (this.param.svm_type == 3) {
            sumv = 0.0d;
        } else if (this.param.svm_type == 4) {
            sumv = 0.0d;
        } else {
            int total_correct = 0;
            while (i < this.prob.f498l) {
                if (target[i] == this.prob.f500y[i]) {
                    total_correct++;
                }
                i++;
            }
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            int i2 = i;
            sb.append("Cross Validation Accuracy = ");
            int total_correct2 = total_correct;
            sb.append((((double) total_correct) * 100.0d) / ((double) this.prob.f498l));
            sb.append("%\n");
            printStream.print(sb.toString());
            double[] dArr = target;
            int i3 = total_correct2;
            return;
        }
        double sumv2 = sumv;
        while (i < this.prob.f498l) {
            double y = this.prob.f500y[i];
            double v = target[i];
            total_error += (v - y) * (v - y);
            sumv2 += v;
            sumy += y;
            sumvv += v * v;
            sumyy += y * y;
            sumvy += v * y;
            i++;
        }
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Cross Validation Mean squared error = ");
        double[] dArr2 = target;
        sb2.append(total_error / ((double) this.prob.f498l));
        sb2.append("\n");
        printStream2.print(sb2.toString());
        PrintStream printStream3 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Cross Validation Squared correlation coefficient = ");
        int i4 = i;
        PrintStream printStream4 = printStream3;
        double d = sumv2;
        sb3.append((((((double) this.prob.f498l) * sumvy) - (sumv2 * sumy)) * ((((double) this.prob.f498l) * sumvy) - (sumv2 * sumy))) / (((((double) this.prob.f498l) * sumvv) - (sumv2 * sumv2)) * ((((double) this.prob.f498l) * sumyy) - (sumy * sumy))));
        sb3.append("\n");
        printStream4.print(sb3.toString());
        int i5 = i4;
    }

    private void run(String[] argv) throws IOException {
        parse_command_line(argv);
        read_problem();
        this.error_msg = svm.svm_check_parameter(this.prob, this.param);
        if (this.error_msg != null) {
            PrintStream printStream = System.err;
            StringBuilder sb = new StringBuilder();
            sb.append("Error: ");
            sb.append(this.error_msg);
            sb.append("\n");
            printStream.print(sb.toString());
            System.exit(1);
        }
        if (this.cross_validation != 0) {
            do_cross_validation();
            return;
        }
        this.model = svm.svm_train(this.prob, this.param);
        svm.svm_save_model(this.model_file_name, this.model);
    }

    public static void main(String[] argv) throws IOException {
        new svm_train().run(argv);
    }

    private static double atof(String s) {
        double d = Double.valueOf(s).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            System.err.print("NaN or Infinity in input\n");
            System.exit(1);
        }
        return d;
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    private void parse_command_line(String[] argv) {
        this.param = new svm_parameter();
        this.param.svm_type = 0;
        this.param.kernel_type = 2;
        this.param.degree = 3;
        this.param.gamma = 0.0d;
        this.param.coef0 = 0.0d;
        this.param.f496nu = 0.5d;
        this.param.cache_size = 100.0d;
        this.param.f495C = 1.0d;
        this.param.eps = 0.001d;
        this.param.f497p = 0.1d;
        this.param.shrinking = 1;
        this.param.probability = 0;
        this.param.nr_weight = 0;
        this.param.weight_label = new int[0];
        this.param.weight = new double[0];
        this.cross_validation = 0;
        svm_print_interface print_func = null;
        int i = 0;
        while (i < argv.length && argv[i].charAt(0) == '-') {
            int i2 = i + 1;
            if (i2 >= argv.length) {
                exit_with_help();
            }
            switch (argv[i2 - 1].charAt(1)) {
                case 'b':
                    this.param.probability = atoi(argv[i2]);
                    break;
                case 'c':
                    this.param.f495C = atof(argv[i2]);
                    break;
                case 'd':
                    this.param.degree = atoi(argv[i2]);
                    break;
                case 'e':
                    this.param.eps = atof(argv[i2]);
                    break;
                case 'g':
                    this.param.gamma = atof(argv[i2]);
                    break;
                case 'h':
                    this.param.shrinking = atoi(argv[i2]);
                    break;
                case 'm':
                    this.param.cache_size = atof(argv[i2]);
                    break;
                case 'n':
                    this.param.f496nu = atof(argv[i2]);
                    break;
                case 'p':
                    this.param.f497p = atof(argv[i2]);
                    break;
                case 'q':
                    print_func = svm_print_null;
                    i2--;
                    break;
                case 'r':
                    this.param.coef0 = atof(argv[i2]);
                    break;
                case 's':
                    this.param.svm_type = atoi(argv[i2]);
                    break;
                case 't':
                    this.param.kernel_type = atoi(argv[i2]);
                    break;
                case 'v':
                    this.cross_validation = 1;
                    this.nr_fold = atoi(argv[i2]);
                    if (this.nr_fold >= 2) {
                        break;
                    } else {
                        System.err.print("n-fold cross validation: n must >= 2\n");
                        exit_with_help();
                        break;
                    }
                case 'w':
                    this.param.nr_weight++;
                    int[] old = this.param.weight_label;
                    this.param.weight_label = new int[this.param.nr_weight];
                    System.arraycopy(old, 0, this.param.weight_label, 0, this.param.nr_weight - 1);
                    double[] old2 = this.param.weight;
                    this.param.weight = new double[this.param.nr_weight];
                    System.arraycopy(old2, 0, this.param.weight, 0, this.param.nr_weight - 1);
                    this.param.weight_label[this.param.nr_weight - 1] = atoi(argv[i2 - 1].substring(2));
                    this.param.weight[this.param.nr_weight - 1] = atof(argv[i2]);
                    break;
                default:
                    PrintStream printStream = System.err;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown option: ");
                    sb.append(argv[i2 - 1]);
                    sb.append("\n");
                    printStream.print(sb.toString());
                    exit_with_help();
                    break;
            }
            i = i2 + 1;
        }
        svm.svm_set_print_string_function(print_func);
        if (i >= argv.length) {
            exit_with_help();
        }
        this.input_file_name = argv[i];
        if (i < argv.length - 1) {
            this.model_file_name = argv[i + 1];
            return;
        }
        int p = argv[i].lastIndexOf(47) + 1;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(argv[i].substring(p));
        sb2.append(".model");
        this.model_file_name = sb2.toString();
    }

    private void read_problem() throws IOException {
        BufferedReader fp = new BufferedReader(new FileReader(this.input_file_name));
        Vector<Double> vy = new Vector<>();
        Vector<svm_node[]> vx = new Vector<>();
        int max_index = 0;
        while (true) {
            String line = fp.readLine();
            if (line == null) {
                break;
            }
            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            vy.addElement(Double.valueOf(atof(st.nextToken())));
            int m = st.countTokens() / 2;
            svm_node[] x = new svm_node[m];
            for (int j = 0; j < m; j++) {
                x[j] = new svm_node();
                x[j].index = atoi(st.nextToken());
                x[j].value = atof(st.nextToken());
            }
            if (m > 0) {
                max_index = Math.max(max_index, x[m - 1].index);
            }
            vx.addElement(x);
        }
        this.prob = new svm_problem();
        this.prob.f498l = vy.size();
        this.prob.f499x = new svm_node[this.prob.f498l][];
        for (int i = 0; i < this.prob.f498l; i++) {
            this.prob.f499x[i] = (svm_node[]) vx.elementAt(i);
        }
        this.prob.f500y = new double[this.prob.f498l];
        for (int i2 = 0; i2 < this.prob.f498l; i2++) {
            this.prob.f500y[i2] = ((Double) vy.elementAt(i2)).doubleValue();
        }
        if (this.param.gamma == 0.0d && max_index > 0) {
            this.param.gamma = 1.0d / ((double) max_index);
        }
        if (this.param.kernel_type == 4) {
            for (int i3 = 0; i3 < this.prob.f498l; i3++) {
                if (this.prob.f499x[i3][0].index != 0) {
                    System.err.print("Wrong kernel matrix: first column must be 0:sample_serial_number\n");
                    System.exit(1);
                }
                if (((int) this.prob.f499x[i3][0].value) <= 0 || ((int) this.prob.f499x[i3][0].value) > max_index) {
                    System.err.print("Wrong input format: sample_serial_number out of range\n");
                    System.exit(1);
                }
            }
        }
        fp.close();
    }
}
