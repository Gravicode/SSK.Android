package p000;

import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.StringTokenizer;
import java.util.Vector;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import org.apache.poi.p009ss.usermodel.ShapeTypes;

/* renamed from: svm_toy */
public class svm_toy extends Applet {
    static final String DEFAULT_PARAM = "-t 2 -c 100";
    static final Color[] colors = {new Color(0, 0, 0), new Color(0, ShapeTypes.CLOUD_CALLOUT, ShapeTypes.CLOUD_CALLOUT), new Color(ShapeTypes.CLOUD_CALLOUT, ShapeTypes.CLOUD_CALLOUT, 0), new Color(ShapeTypes.CLOUD_CALLOUT, 0, ShapeTypes.CLOUD_CALLOUT), new Color(0, Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION), new Color(Callback.DEFAULT_DRAG_ANIMATION_DURATION, Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0), new Color(Callback.DEFAULT_DRAG_ANIMATION_DURATION, 0, Callback.DEFAULT_DRAG_ANIMATION_DURATION)};
    int XLEN;
    int YLEN;
    Image buffer;
    Graphics buffer_gc;
    byte current_value = 1;
    Vector<point> point_list = new Vector<>();

    /* renamed from: svm_toy$point */
    class point {
        byte value;

        /* renamed from: x */
        double f821x;

        /* renamed from: y */
        double f822y;

        point(double x, double y, byte value2) {
            this.f821x = x;
            this.f822y = y;
            this.value = value2;
        }
    }

    public void init() {
        setSize(getSize());
        final Button button_change = new Button("Change");
        Button button_run = new Button("Run");
        Button button_clear = new Button("Clear");
        Button button_save = new Button("Save");
        Button button_load = new Button("Load");
        final TextField input_line = new TextField(DEFAULT_PARAM);
        setLayout(new BorderLayout());
        Panel p = new Panel();
        GridBagLayout gridbag = new GridBagLayout();
        p.setLayout(gridbag);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = 2;
        c.weightx = 1.0d;
        c.gridwidth = 1;
        gridbag.setConstraints(button_change, c);
        gridbag.setConstraints(button_run, c);
        gridbag.setConstraints(button_clear, c);
        gridbag.setConstraints(button_save, c);
        gridbag.setConstraints(button_load, c);
        c.weightx = 5.0d;
        c.gridwidth = 5;
        gridbag.setConstraints(input_line, c);
        button_change.setBackground(colors[this.current_value]);
        p.add(button_change);
        p.add(button_run);
        p.add(button_clear);
        p.add(button_save);
        p.add(button_load);
        p.add(input_line);
        add(p, "South");
        button_change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_change_clicked();
                button_change.setBackground(svm_toy.colors[svm_toy.this.current_value]);
            }
        });
        button_run.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_run_clicked(input_line.getText());
            }
        });
        button_clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_clear_clicked();
            }
        });
        button_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_save_clicked();
            }
        });
        button_load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_load_clicked();
            }
        });
        input_line.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                svm_toy.this.button_run_clicked(input_line.getText());
            }
        });
        enableEvents(16);
    }

    /* access modifiers changed from: 0000 */
    public void draw_point(point p) {
        Color c = colors[p.value + 3];
        Graphics window_gc = getGraphics();
        this.buffer_gc.setColor(c);
        this.buffer_gc.fillRect((int) (p.f821x * ((double) this.XLEN)), (int) (p.f822y * ((double) this.YLEN)), 4, 4);
        window_gc.setColor(c);
        window_gc.fillRect((int) (p.f821x * ((double) this.XLEN)), (int) (p.f822y * ((double) this.YLEN)), 4, 4);
    }

    /* access modifiers changed from: 0000 */
    public void clear_all() {
        this.point_list.removeAllElements();
        if (this.buffer != null) {
            this.buffer_gc.setColor(colors[0]);
            this.buffer_gc.fillRect(0, 0, this.XLEN, this.YLEN);
        }
        repaint();
    }

    /* access modifiers changed from: 0000 */
    public void draw_all_points() {
        int n = this.point_list.size();
        for (int i = 0; i < n; i++) {
            draw_point((point) this.point_list.elementAt(i));
        }
    }

    /* access modifiers changed from: 0000 */
    public void button_change_clicked() {
        this.current_value = (byte) (this.current_value + 1);
        if (this.current_value > 3) {
            this.current_value = 1;
        }
    }

    private static double atof(String s) {
        return Double.valueOf(s).doubleValue();
    }

    private static int atoi(String s) {
        return Integer.parseInt(s);
    }

    /* access modifiers changed from: 0000 */
    public void button_run_clicked(String args) {
        if (!this.point_list.isEmpty()) {
            svm_parameter param = new svm_parameter();
            char c = 0;
            param.svm_type = 0;
            param.kernel_type = 2;
            param.degree = 3;
            param.gamma = 0.0d;
            param.coef0 = 0.0d;
            param.f496nu = 0.5d;
            param.cache_size = 40.0d;
            param.f495C = 1.0d;
            param.eps = 0.001d;
            param.f497p = 0.1d;
            param.shrinking = 1;
            param.probability = 0;
            param.nr_weight = 0;
            param.weight_label = new int[0];
            param.weight = new double[0];
            StringTokenizer st = new StringTokenizer(args);
            String[] argv = new String[st.countTokens()];
            for (int i = 0; i < argv.length; i++) {
                argv[i] = st.nextToken();
            }
            int i2 = 0;
            while (true) {
                if (i2 < argv.length && argv[i2].charAt(0) == '-') {
                    int i3 = i2 + 1;
                    if (i3 >= argv.length) {
                        System.err.print("unknown option\n");
                    } else {
                        switch (argv[i3 - 1].charAt(1)) {
                            case 'b':
                                param.probability = atoi(argv[i3]);
                                break;
                            case 'c':
                                param.f495C = atof(argv[i3]);
                                break;
                            case 'd':
                                param.degree = atoi(argv[i3]);
                                break;
                            case 'e':
                                param.eps = atof(argv[i3]);
                                break;
                            case 'g':
                                param.gamma = atof(argv[i3]);
                                break;
                            case 'h':
                                param.shrinking = atoi(argv[i3]);
                                break;
                            case 'm':
                                param.cache_size = atof(argv[i3]);
                                break;
                            case 'n':
                                param.f496nu = atof(argv[i3]);
                                break;
                            case 'p':
                                param.f497p = atof(argv[i3]);
                                break;
                            case 'r':
                                param.coef0 = atof(argv[i3]);
                                break;
                            case 's':
                                param.svm_type = atoi(argv[i3]);
                                break;
                            case 't':
                                param.kernel_type = atoi(argv[i3]);
                                break;
                            case 'w':
                                param.nr_weight++;
                                int[] old = param.weight_label;
                                param.weight_label = new int[param.nr_weight];
                                System.arraycopy(old, 0, param.weight_label, 0, param.nr_weight - 1);
                                double[] old2 = param.weight;
                                param.weight = new double[param.nr_weight];
                                System.arraycopy(old2, 0, param.weight, 0, param.nr_weight - 1);
                                param.weight_label[param.nr_weight - 1] = atoi(argv[i3 - 1].substring(2));
                                param.weight[param.nr_weight - 1] = atof(argv[i3]);
                                break;
                            default:
                                System.err.print("unknown option\n");
                                break;
                        }
                        i2 = i3 + 1;
                    }
                }
            }
            svm_problem prob = new svm_problem();
            prob.f498l = this.point_list.size();
            prob.f500y = new double[prob.f498l];
            if (param.kernel_type == 4) {
                StringTokenizer stringTokenizer = st;
            } else {
                if (param.svm_type == 3) {
                } else if (param.svm_type == 4) {
                    StringTokenizer stringTokenizer2 = st;
                } else {
                    if (param.gamma == 0.0d) {
                        param.gamma = 0.5d;
                    }
                    prob.f499x = (svm_node[][]) Array.newInstance(svm_node.class, new int[]{prob.f498l, 2});
                    for (int i4 = 0; i4 < prob.f498l; i4++) {
                        point p = (point) this.point_list.elementAt(i4);
                        prob.f499x[i4][0] = new svm_node();
                        prob.f499x[i4][0].index = 1;
                        prob.f499x[i4][0].value = p.f821x;
                        prob.f499x[i4][1] = new svm_node();
                        prob.f499x[i4][1].index = 2;
                        prob.f499x[i4][1].value = p.f822y;
                        prob.f500y[i4] = (double) p.value;
                    }
                    svm_model model = svm.svm_train(prob, param);
                    svm_node[] x = {new svm_node(), new svm_node()};
                    x[0].index = 1;
                    x[1].index = 2;
                    Graphics window_gc = getGraphics();
                    int i5 = 0;
                    while (i5 < this.XLEN) {
                        int j = 0;
                        while (j < this.YLEN) {
                            StringTokenizer st2 = st;
                            x[c].value = ((double) i5) / ((double) this.XLEN);
                            x[1].value = ((double) j) / ((double) this.YLEN);
                            double d = svm.svm_predict(model, x);
                            if (param.svm_type == 2 && d < 0.0d) {
                                d = 2.0d;
                            }
                            this.buffer_gc.setColor(colors[(int) d]);
                            window_gc.setColor(colors[(int) d]);
                            this.buffer_gc.drawLine(i5, j, i5, j);
                            window_gc.drawLine(i5, j, i5, j);
                            j++;
                            st = st2;
                            c = 0;
                            String str = args;
                        }
                        i5++;
                        c = 0;
                        String str2 = args;
                    }
                }
                if (param.gamma == 0.0d) {
                    param.gamma = 1.0d;
                }
                prob.f499x = (svm_node[][]) Array.newInstance(svm_node.class, new int[]{prob.f498l, 1});
                for (int i6 = 0; i6 < prob.f498l; i6++) {
                    point p2 = (point) this.point_list.elementAt(i6);
                    prob.f499x[i6][0] = new svm_node();
                    prob.f499x[i6][0].index = 1;
                    prob.f499x[i6][0].value = p2.f821x;
                    prob.f500y[i6] = p2.f822y;
                }
                svm_model model2 = svm.svm_train(prob, param);
                svm_node[] x2 = {new svm_node()};
                x2[0].index = 1;
                int[] j2 = new int[this.XLEN];
                Graphics window_gc2 = getGraphics();
                int i7 = 0;
                while (i7 < this.XLEN) {
                    svm_node[] x3 = x2;
                    x2[0].value = ((double) i7) / ((double) this.XLEN);
                    svm_node[] x4 = x3;
                    j2[i7] = (int) (((double) this.YLEN) * svm.svm_predict(model2, x4));
                    i7++;
                    x2 = x4;
                }
                this.buffer_gc.setColor(colors[0]);
                this.buffer_gc.drawLine(0, 0, 0, this.YLEN - 1);
                window_gc2.setColor(colors[0]);
                window_gc2.drawLine(0, 0, 0, this.YLEN - 1);
                int p3 = (int) (param.f497p * ((double) this.YLEN));
                for (int i8 = 1; i8 < this.XLEN; i8++) {
                    this.buffer_gc.setColor(colors[0]);
                    this.buffer_gc.drawLine(i8, 0, i8, this.YLEN - 1);
                    window_gc2.setColor(colors[0]);
                    window_gc2.drawLine(i8, 0, i8, this.YLEN - 1);
                    this.buffer_gc.setColor(colors[5]);
                    window_gc2.setColor(colors[5]);
                    this.buffer_gc.drawLine(i8 - 1, j2[i8 - 1], i8, j2[i8]);
                    window_gc2.drawLine(i8 - 1, j2[i8 - 1], i8, j2[i8]);
                    if (param.svm_type == 3) {
                        this.buffer_gc.setColor(colors[2]);
                        window_gc2.setColor(colors[2]);
                        this.buffer_gc.drawLine(i8 - 1, j2[i8 - 1] + p3, i8, j2[i8] + p3);
                        window_gc2.drawLine(i8 - 1, j2[i8 - 1] + p3, i8, j2[i8] + p3);
                        this.buffer_gc.setColor(colors[2]);
                        window_gc2.setColor(colors[2]);
                        this.buffer_gc.drawLine(i8 - 1, j2[i8 - 1] - p3, i8, j2[i8] - p3);
                        window_gc2.drawLine(i8 - 1, j2[i8 - 1] - p3, i8, j2[i8] - p3);
                    }
                }
            }
            draw_all_points();
        }
    }

    /* access modifiers changed from: 0000 */
    public void button_clear_clicked() {
        clear_all();
    }

    /* access modifiers changed from: 0000 */
    public void button_save_clicked() {
        FileDialog dialog = new FileDialog(new Frame(), "Save", 1);
        dialog.setVisible(true);
        StringBuilder sb = new StringBuilder();
        sb.append(dialog.getDirectory());
        sb.append(dialog.getFile());
        String filename = sb.toString();
        if (filename != null) {
            try {
                DataOutputStream fp = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
                int n = this.point_list.size();
                for (int i = 0; i < n; i++) {
                    point p = (point) this.point_list.elementAt(i);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(p.value);
                    sb2.append(" 1:");
                    sb2.append(p.f821x);
                    sb2.append(" 2:");
                    sb2.append(p.f822y);
                    sb2.append("\n");
                    fp.writeBytes(sb2.toString());
                }
                fp.close();
            } catch (IOException e) {
                System.err.print(e);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void button_load_clicked() {
        FileDialog dialog = new FileDialog(new Frame(), "Load", 0);
        dialog.setVisible(true);
        StringBuilder sb = new StringBuilder();
        sb.append(dialog.getDirectory());
        sb.append(dialog.getFile());
        String filename = sb.toString();
        if (filename != null) {
            clear_all();
            try {
                BufferedReader fp = new BufferedReader(new FileReader(filename));
                while (true) {
                    String readLine = fp.readLine();
                    String line = readLine;
                    if (readLine == null) {
                        break;
                    }
                    StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
                    byte value = (byte) atoi(st.nextToken());
                    st.nextToken();
                    double x = atof(st.nextToken());
                    st.nextToken();
                    double y = atof(st.nextToken());
                    Vector<point> vector = this.point_list;
                    point point2 = new point(x, y, value);
                    vector.addElement(point2);
                }
                fp.close();
            } catch (IOException e) {
                System.err.print(e);
            }
            draw_all_points();
        }
    }

    /* access modifiers changed from: protected */
    public void processMouseEvent(MouseEvent e) {
        if (e.getID() == 501 && e.getX() < this.XLEN && e.getY() < this.YLEN) {
            point point2 = new point(((double) e.getX()) / ((double) this.XLEN), ((double) e.getY()) / ((double) this.YLEN), this.current_value);
            this.point_list.addElement(point2);
            draw_point(point2);
        }
    }

    public void paint(Graphics g) {
        if (this.buffer == null) {
            this.buffer = createImage(this.XLEN, this.YLEN);
            this.buffer_gc = this.buffer.getGraphics();
            this.buffer_gc.setColor(colors[0]);
            this.buffer_gc.fillRect(0, 0, this.XLEN, this.YLEN);
        }
        g.drawImage(this.buffer, 0, 0, this);
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.XLEN, this.YLEN + 50);
    }

    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    public void setSize(int w, int h) {
        svm_toy.super.setSize(w, h);
        this.XLEN = w;
        this.YLEN = h - 50;
        clear_all();
    }

    public static void main(String[] argv) {
        new AppletFrame("svm_toy", new svm_toy(), 500, 550);
    }
}
