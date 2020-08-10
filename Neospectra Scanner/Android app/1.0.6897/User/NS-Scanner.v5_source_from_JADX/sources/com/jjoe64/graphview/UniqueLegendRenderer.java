package com.jjoe64.graphview;

import android.util.Pair;
import com.jjoe64.graphview.series.Series;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniqueLegendRenderer extends LegendRenderer {
    public UniqueLegendRenderer(GraphView graphView) {
        super(graphView);
    }

    /* access modifiers changed from: protected */
    public List<Series> getAllSeries() {
        List<Series> originalSeries = super.getAllSeries();
        List<Series> distinctSeries = new ArrayList<>();
        Set<Pair<Integer, String>> uniqueSeriesKeys = new HashSet<>();
        for (Series series : originalSeries) {
            if (uniqueSeriesKeys.add(new Pair(Integer.valueOf(series.getColor()), series.getTitle()))) {
                distinctSeries.add(series);
            }
        }
        return distinctSeries;
    }
}
