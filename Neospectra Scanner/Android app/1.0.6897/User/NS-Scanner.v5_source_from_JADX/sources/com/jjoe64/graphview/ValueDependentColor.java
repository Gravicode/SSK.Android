package com.jjoe64.graphview;

import com.jjoe64.graphview.series.DataPointInterface;

public interface ValueDependentColor<T extends DataPointInterface> {
    int get(T t);
}
