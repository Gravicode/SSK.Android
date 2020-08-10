package libsvm;

/* compiled from: svm */
final class Solver_NU extends Solver {

    /* renamed from: si */
    private SolutionInfo f492si;

    Solver_NU() {
    }

    /* access modifiers changed from: 0000 */
    public void Solve(int l, QMatrix Q, double[] p, byte[] y, double[] alpha, double Cp, double Cn, double eps, SolutionInfo si, int shrinking) {
        this.f492si = si;
        super.Solve(l, Q, p, y, alpha, Cp, Cn, eps, si, shrinking);
    }

    /* access modifiers changed from: 0000 */
    public int select_working_set(int[] working_set) {
        int Gmaxn_idx;
        int i;
        int ip;
        double d;
        int Gmin_idx;
        double obj_diff;
        double obj_diff2;
        double Gmaxp2;
        double Gmaxp22 = Double.NEGATIVE_INFINITY;
        int Gmaxp_idx = -1;
        int Gmaxn_idx2 = -1;
        double Gmaxn = Double.NEGATIVE_INFINITY;
        double Gmaxp = Double.NEGATIVE_INFINITY;
        int t = 0;
        while (t < this.active_size) {
            if (this.f490y[t] != 1) {
                Gmaxp2 = Gmaxp22;
                if (!is_lower_bound(t) && this.f485G[t] >= Gmaxn) {
                    Gmaxn_idx2 = t;
                    Gmaxn = this.f485G[t];
                }
            } else if (!is_upper_bound(t)) {
                Gmaxp2 = Gmaxp22;
                if ((-this.f485G[t]) >= Gmaxp) {
                    Gmaxp = -this.f485G[t];
                    Gmaxp_idx = t;
                }
            } else {
                Gmaxp2 = Gmaxp22;
            }
            t++;
            Gmaxp22 = Gmaxp2;
        }
        double Gmaxp23 = Gmaxp22;
        int ip2 = Gmaxp_idx;
        int in = Gmaxn_idx2;
        float[] Q_ip = null;
        float[] Q_in = null;
        if (ip2 != -1) {
            Q_ip = this.f486Q.get_Q(ip2, this.active_size);
        }
        if (in != -1) {
            Q_in = this.f486Q.get_Q(in, this.active_size);
        }
        double obj_diff_min = Double.POSITIVE_INFINITY;
        int Gmin_idx2 = -1;
        double Gmaxn2 = Double.NEGATIVE_INFINITY;
        int j = 0;
        while (true) {
            Gmaxn_idx = Gmaxn_idx2;
            if (j >= this.active_size) {
                break;
            }
            if (this.f490y[j] != 1) {
                ip = ip2;
                if (!is_upper_bound(j)) {
                    double grad_diff = Gmaxn - this.f485G[j];
                    int Gmin_idx3 = Gmin_idx2;
                    if ((-this.f485G[j]) >= Gmaxn2) {
                        double d2 = Gmaxn2;
                        Gmaxn2 = -this.f485G[j];
                    } else {
                        double d3 = Gmaxn2;
                    }
                    if (grad_diff > 0.0d) {
                        double quad_coef = (this.f487QD[in] + this.f487QD[j]) - ((double) (Q_in[j] * 2.0f));
                        if (quad_coef > 0.0d) {
                            obj_diff = (-(grad_diff * grad_diff)) / quad_coef;
                        } else {
                            obj_diff = (-(grad_diff * grad_diff)) / 1.0E-12d;
                        }
                        if (obj_diff <= obj_diff_min) {
                            obj_diff_min = obj_diff;
                            Gmin_idx2 = j;
                            j++;
                            Gmaxn_idx2 = Gmaxn_idx;
                            ip2 = ip;
                        }
                    }
                    Gmin_idx2 = Gmin_idx3;
                    j++;
                    Gmaxn_idx2 = Gmaxn_idx;
                    ip2 = ip;
                } else {
                    Gmin_idx = Gmin_idx2;
                    d = Gmaxn2;
                }
            } else if (!is_lower_bound(j)) {
                double grad_diff2 = Gmaxp + this.f485G[j];
                if (this.f485G[j] >= Gmaxp23) {
                    Gmaxp23 = this.f485G[j];
                }
                if (grad_diff2 > 0.0d) {
                    ip = ip2;
                    double quad_coef2 = (this.f487QD[ip2] + this.f487QD[j]) - ((double) (Q_ip[j] * 2.0f));
                    if (quad_coef2 > 0.0d) {
                        obj_diff2 = (-(grad_diff2 * grad_diff2)) / quad_coef2;
                    } else {
                        obj_diff2 = (-(grad_diff2 * grad_diff2)) / 1.0E-12d;
                    }
                    if (obj_diff2 <= obj_diff_min) {
                        Gmin_idx2 = j;
                        obj_diff_min = obj_diff2;
                    }
                } else {
                    ip = ip2;
                }
                j++;
                Gmaxn_idx2 = Gmaxn_idx;
                ip2 = ip;
            } else {
                ip = ip2;
                Gmin_idx = Gmin_idx2;
                d = Gmaxn2;
            }
            Gmin_idx2 = Gmin_idx;
            Gmaxn2 = d;
            j++;
            Gmaxn_idx2 = Gmaxn_idx;
            ip2 = ip;
        }
        int Gmin_idx4 = Gmin_idx2;
        if (Math.max(Gmaxp + Gmaxp23, Gmaxn + Gmaxn2) < this.eps) {
            return 1;
        }
        if (this.f490y[Gmin_idx4] == 1) {
            i = 0;
            working_set[0] = Gmaxp_idx;
        } else {
            i = 0;
            working_set[0] = Gmaxn_idx;
        }
        working_set[1] = Gmin_idx4;
        return i;
    }

    private boolean be_shrunk(int i, double Gmax1, double Gmax2, double Gmax3, double Gmax4) {
        boolean z = false;
        if (is_upper_bound(i)) {
            if (this.f490y[i] == 1) {
                if ((-this.f485G[i]) > Gmax1) {
                    z = true;
                }
                return z;
            }
            if ((-this.f485G[i]) > Gmax4) {
                z = true;
            }
            return z;
        } else if (!is_lower_bound(i)) {
            return false;
        } else {
            if (this.f490y[i] == 1) {
                if (this.f485G[i] > Gmax2) {
                    z = true;
                }
                return z;
            }
            if (this.f485G[i] > Gmax3) {
                z = true;
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    public void do_shrinking() {
        int i = 0;
        double Gmax1 = Double.NEGATIVE_INFINITY;
        double Gmax2 = Double.NEGATIVE_INFINITY;
        double Gmax3 = Double.NEGATIVE_INFINITY;
        double Gmax4 = Double.NEGATIVE_INFINITY;
        while (true) {
            int i2 = i;
            if (i2 >= this.active_size) {
                break;
            }
            if (!is_upper_bound(i2)) {
                if (this.f490y[i2] == 1) {
                    if ((-this.f485G[i2]) > Gmax1) {
                        Gmax1 = -this.f485G[i2];
                    }
                } else if ((-this.f485G[i2]) > Gmax4) {
                    Gmax4 = -this.f485G[i2];
                }
            }
            if (!is_lower_bound(i2)) {
                if (this.f490y[i2] == 1) {
                    if (this.f485G[i2] > Gmax2) {
                        Gmax2 = this.f485G[i2];
                    }
                } else if (this.f485G[i2] > Gmax3) {
                    Gmax3 = this.f485G[i2];
                }
            }
            i = i2 + 1;
        }
        if (!this.unshrink && Math.max(Gmax1 + Gmax2, Gmax3 + Gmax4) <= this.eps * 10.0d) {
            this.unshrink = true;
            reconstruct_gradient();
            this.active_size = this.f488l;
        }
        int i3 = 0;
        while (i3 < this.active_size) {
            double d = Gmax3;
            double Gmax32 = Gmax3;
            int i4 = i3;
            if (be_shrunk(i3, Gmax1, Gmax2, d, Gmax4)) {
                this.active_size--;
                while (true) {
                    if (this.active_size <= i4) {
                        break;
                    }
                    if (!be_shrunk(this.active_size, Gmax1, Gmax2, Gmax32, Gmax4)) {
                        swap_index(i4, this.active_size);
                        break;
                    }
                    this.active_size--;
                }
            }
            i3 = i4 + 1;
            Gmax3 = Gmax32;
        }
        int i5 = i3;
    }

    /* access modifiers changed from: 0000 */
    public double calculate_rho() {
        double sum_free2;
        double r1;
        double d;
        double ub1;
        double lb2;
        int nr_free1 = 0;
        int nr_free2 = 0;
        double ub12 = Double.POSITIVE_INFINITY;
        double ub2 = Double.POSITIVE_INFINITY;
        double lb1 = Double.NEGATIVE_INFINITY;
        double lb22 = Double.NEGATIVE_INFINITY;
        double sum_free1 = 0.0d;
        double sum_free22 = 0.0d;
        int i = 0;
        while (true) {
            sum_free2 = sum_free22;
            if (i >= this.active_size) {
                break;
            }
            if (this.f490y[i] != 1) {
                lb2 = lb22;
                if (is_lower_bound(i)) {
                    ub2 = Math.min(ub2, this.f485G[i]);
                } else {
                    if (is_upper_bound(i)) {
                        lb22 = Math.max(lb2, this.f485G[i]);
                        sum_free22 = sum_free2;
                    } else {
                        lb22 = lb2;
                        nr_free2++;
                        sum_free22 = sum_free2 + this.f485G[i];
                    }
                    i++;
                }
            } else if (is_lower_bound(i)) {
                lb2 = lb22;
                ub12 = Math.min(ub12, this.f485G[i]);
            } else {
                lb2 = lb22;
                if (is_upper_bound(i)) {
                    lb1 = Math.max(lb1, this.f485G[i]);
                } else {
                    nr_free1++;
                    sum_free1 += this.f485G[i];
                }
            }
            sum_free22 = sum_free2;
            lb22 = lb2;
            i++;
        }
        if (nr_free1 > 0) {
            r1 = sum_free1 / ((double) nr_free1);
        } else {
            r1 = (ub12 + lb1) / 2.0d;
        }
        if (nr_free2 > 0) {
            double d2 = ub12;
            ub1 = sum_free2 / ((double) nr_free2);
            d = 2.0d;
        } else {
            d = 2.0d;
            ub1 = (ub2 + lb22) / 2.0d;
        }
        int i2 = nr_free1;
        this.f492si.f491r = (r1 + ub1) / d;
        return (r1 - ub1) / d;
    }
}
