package com.si_ware.neospectra.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.si_ware.neospectra.ChartView.LabelAdapter;

public class ValueLabelAdapter extends LabelAdapter {
    private Context mContext;
    private LabelOrientation mOrientation;

    public enum LabelOrientation {
        HORIZONTAL,
        VERTICAL
    }

    public ValueLabelAdapter(Context context, LabelOrientation orientation) {
        this.mContext = context;
        this.mOrientation = orientation;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new TextView(this.mContext);
        }
        TextView labelTextView = (TextView) convertView;
        int gravity = 17;
        if (this.mOrientation == LabelOrientation.VERTICAL) {
            gravity = position == 0 ? 85 : position == getCount() - 1 ? 53 : 21;
        } else if (this.mOrientation == LabelOrientation.HORIZONTAL) {
            if (position == 0) {
                gravity = 19;
            } else if (position == getCount() - 1) {
                gravity = 21;
            }
        }
        labelTextView.setGravity(gravity);
        labelTextView.setPadding(8, 0, 8, 0);
        if (this.mOrientation == LabelOrientation.VERTICAL) {
            labelTextView.setText(String.format("%.3f", new Object[]{getItem(position)}));
        } else if (this.mOrientation == LabelOrientation.HORIZONTAL) {
            labelTextView.setText(String.format("%4f", new Object[]{getItem(position)}));
        }
        return convertView;
    }
}
