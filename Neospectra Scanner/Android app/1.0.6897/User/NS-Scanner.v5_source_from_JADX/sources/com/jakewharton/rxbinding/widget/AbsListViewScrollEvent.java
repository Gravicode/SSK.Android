package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.AbsListView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class AbsListViewScrollEvent extends ViewEvent<AbsListView> {
    private final int firstVisibleItem;
    private final int scrollState;
    private final int totalItemCount;
    private final int visibleItemCount;

    @CheckResult
    @NonNull
    public static AbsListViewScrollEvent create(AbsListView listView, int scrollState2, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
        AbsListViewScrollEvent absListViewScrollEvent = new AbsListViewScrollEvent(listView, scrollState2, firstVisibleItem2, visibleItemCount2, totalItemCount2);
        return absListViewScrollEvent;
    }

    private AbsListViewScrollEvent(@NonNull AbsListView view, int scrollState2, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
        super(view);
        this.scrollState = scrollState2;
        this.firstVisibleItem = firstVisibleItem2;
        this.visibleItemCount = visibleItemCount2;
        this.totalItemCount = totalItemCount2;
    }

    public int scrollState() {
        return this.scrollState;
    }

    public int firstVisibleItem() {
        return this.firstVisibleItem;
    }

    public int visibleItemCount() {
        return this.visibleItemCount;
    }

    public int totalItemCount() {
        return this.totalItemCount;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbsListViewScrollEvent that = (AbsListViewScrollEvent) o;
        if (this.scrollState != that.scrollState || this.firstVisibleItem != that.firstVisibleItem || this.visibleItemCount != that.visibleItemCount) {
            return false;
        }
        if (this.totalItemCount != that.totalItemCount) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((this.scrollState * 31) + this.firstVisibleItem) * 31) + this.visibleItemCount) * 31) + this.totalItemCount;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AbsListViewScrollEvent{scrollState=");
        sb.append(this.scrollState);
        sb.append(", firstVisibleItem=");
        sb.append(this.firstVisibleItem);
        sb.append(", visibleItemCount=");
        sb.append(this.visibleItemCount);
        sb.append(", totalItemCount=");
        sb.append(this.totalItemCount);
        sb.append('}');
        return sb.toString();
    }
}
