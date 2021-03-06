package android.support.p004v7.util;

import android.support.annotation.NonNull;
import android.support.p004v7.widget.RecyclerView.Adapter;

/* renamed from: android.support.v7.util.AdapterListUpdateCallback */
public final class AdapterListUpdateCallback implements ListUpdateCallback {
    @NonNull
    private final Adapter mAdapter;

    public AdapterListUpdateCallback(@NonNull Adapter adapter) {
        this.mAdapter = adapter;
    }

    public void onInserted(int position, int count) {
        this.mAdapter.notifyItemRangeInserted(position, count);
    }

    public void onRemoved(int position, int count) {
        this.mAdapter.notifyItemRangeRemoved(position, count);
    }

    public void onMoved(int fromPosition, int toPosition) {
        this.mAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    public void onChanged(int position, int count, Object payload) {
        this.mAdapter.notifyItemRangeChanged(position, count, payload);
    }
}
