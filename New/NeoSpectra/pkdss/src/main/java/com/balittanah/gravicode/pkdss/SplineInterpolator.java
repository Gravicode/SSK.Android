package com.balittanah.gravicode.pkdss;

import java.util.*;

/**
 Spline interpolation class.
 */
public class SplineInterpolator
{
    private final Double[] _keys;

    private final Double[] _values;

    private final double[] _h;

    private final double[] _a;

    /**
     Class constructor.

     @param nodes Collection of known points for further interpolation.
     Should contain at least two items.
     */
    public SplineInterpolator(Map<Double, Double> nodes)
    {
        double[] _a1;
        if (nodes == null)
        {
            throw new NullPointerException("nodes");
        }

        int n = nodes.size();

        if (n < 2)
        {
            throw new IllegalArgumentException("At least two point required for interpolation.");
        }

        _keys = nodes.keySet().toArray(new Double[nodes.size()]);
        _values = nodes.values().toArray(new Double[nodes.size()]);
        _a1 = new double[n];
        _h = new double[n];

        for (int i = 1; i < n; i++)
        {
            _h[i] = _keys[i] - _keys[i - 1];
        }

        if (n > 2)
        {
            double[] sub = new double[n - 1];
            double[] diag = new double[n - 1];
            double[] sup = new double[n - 1];

            for (int i = 1; i <= n - 2; i++)
            {
                diag[i] = (_h[i] + _h[i + 1]) / 3;
                sup[i] = _h[i + 1] / 6;
                sub[i] = _h[i] / 6;
                _a1[i] = (_values[i + 1] - _values[i]) / _h[i + 1] - (_values[i] - _values[i - 1]) / _h[i];
            }

            RefObject<double[]> tempRef__a = new RefObject<double[]>(_a1);
            SolveTridiag(sub, diag, sup, tempRef__a, n - 2);
            _a1 = tempRef__a.refArgValue;
        }
        _a = _a1;
    }

    /**
     Gets interpolated value for specified argument.

     @param key Argument value for interpolation. Must be within
     the interval bounded by lowest ang highest <see cref="_keys"/> values.
     */
    public final double GetValue(double key)
    {
        int gap = 0;
        double previous = -Double.MAX_VALUE;

        // At the end of this iteration, "gap" will contain the index of the interval
        // between two known values, which contains the unknown z, and "previous" will
        // contain the biggest z value among the known samples, left of the unknown z
        for (int i = 0; i < _keys.length; i++)
        {
            if (_keys[i] < key && _keys[i] > previous)
            {
                previous = _keys[i];
                gap = i + 1;
            }
        }
        // these two lines are new
        gap = Math.max(gap, 1);
        gap = Math.min(gap, _h.length - 1);

        double x1 = key - previous;
        double x2 = _h[gap] - x1;

        return ((-_a[gap - 1] / 6 * (x2 + _h[gap]) * x1 + _values[gap - 1]) * x2 + (-_a[gap] / 6 * (x1 + _h[gap]) * x2 + _values[gap]) * x1) / _h[gap];
    }


    /**
     Solve linear system with tridiagonal n*n matrix "a"
     using Gaussian elimination without pivoting.
     */
    private static void SolveTridiag(double[] sub, double[] diag, double[] sup, RefObject<double[]> b, int n)
    {
        int i;

        for (i = 2; i <= n; i++)
        {
            sub[i] = sub[i] / diag[i - 1];
            diag[i] = diag[i] - sub[i] * sup[i - 1];
            b.refArgValue[i] = b.refArgValue[i] - sub[i] * b.refArgValue[i - 1];
        }

        b.refArgValue[n] = b.refArgValue[n] / diag[n];

        for (i = n - 1; i >= 1; i--)
        {
            b.refArgValue[i] = (b.refArgValue[i] - sup[i] * b.refArgValue[i + 1]) / diag[i];
        }
    }
}