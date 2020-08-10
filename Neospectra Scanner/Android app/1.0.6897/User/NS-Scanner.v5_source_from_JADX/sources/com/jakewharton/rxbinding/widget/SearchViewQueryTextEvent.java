package com.jakewharton.rxbinding.widget;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.widget.SearchView;
import com.jakewharton.rxbinding.view.ViewEvent;

public final class SearchViewQueryTextEvent extends ViewEvent<SearchView> {
    private final CharSequence queryText;
    private final boolean submitted;

    @CheckResult
    @NonNull
    public static SearchViewQueryTextEvent create(@NonNull SearchView view, @NonNull CharSequence queryText2, boolean submitted2) {
        return new SearchViewQueryTextEvent(view, queryText2, submitted2);
    }

    private SearchViewQueryTextEvent(@NonNull SearchView view, @NonNull CharSequence queryText2, boolean submitted2) {
        super(view);
        this.queryText = queryText2;
        this.submitted = submitted2;
    }

    @NonNull
    public CharSequence queryText() {
        return this.queryText;
    }

    public boolean isSubmitted() {
        return this.submitted;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof SearchViewQueryTextEvent)) {
            return false;
        }
        SearchViewQueryTextEvent other = (SearchViewQueryTextEvent) o;
        if (!(other.view() == view() && other.queryText.equals(this.queryText) && other.submitted == this.submitted)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((17 * 37) + ((SearchView) view()).hashCode()) * 37) + this.queryText.hashCode()) * 37) + (this.submitted ? 1 : 0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SearchViewQueryTextEvent{view=");
        sb.append(view());
        sb.append(", queryText=");
        sb.append(this.queryText);
        sb.append(", submitted=");
        sb.append(this.submitted);
        sb.append('}');
        return sb.toString();
    }
}
