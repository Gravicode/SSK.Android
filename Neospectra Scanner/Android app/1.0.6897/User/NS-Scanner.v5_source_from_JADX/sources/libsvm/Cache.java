package libsvm;

/* compiled from: svm */
class Cache {
    private final head_t[] head = new head_t[this.f476l];

    /* renamed from: l */
    private final int f476l;
    private head_t lru_head;
    private long size;

    /* compiled from: svm */
    private final class head_t {
        float[] data;
        int len;
        head_t next;
        head_t prev;

        private head_t() {
        }
    }

    Cache(int l_, long size_) {
        this.f476l = l_;
        this.size = size_;
        for (int i = 0; i < this.f476l; i++) {
            this.head[i] = new head_t();
        }
        this.size /= 4;
        this.size -= (long) (this.f476l * 4);
        this.size = Math.max(this.size, ((long) this.f476l) * 2);
        this.lru_head = new head_t();
        head_t head_t2 = this.lru_head;
        head_t head_t3 = this.lru_head;
        head_t head_t4 = this.lru_head;
        head_t3.prev = head_t4;
        head_t2.next = head_t4;
    }

    private void lru_delete(head_t h) {
        h.prev.next = h.next;
        h.next.prev = h.prev;
    }

    private void lru_insert(head_t h) {
        h.next = this.lru_head;
        h.prev = this.lru_head.prev;
        h.prev.next = h;
        h.next.prev = h;
    }

    /* access modifiers changed from: 0000 */
    public int get_data(int index, float[][] data, int len) {
        head_t h = this.head[index];
        if (h.len > 0) {
            lru_delete(h);
        }
        int more = len - h.len;
        if (more > 0) {
            while (this.size < ((long) more)) {
                head_t old = this.lru_head.next;
                lru_delete(old);
                this.size += (long) old.len;
                old.data = null;
                old.len = 0;
            }
            float[] new_data = new float[len];
            if (h.data != null) {
                System.arraycopy(h.data, 0, new_data, 0, h.len);
            }
            h.data = new_data;
            this.size -= (long) more;
            int _ = h.len;
            h.len = len;
            len = _;
        }
        lru_insert(h);
        data[0] = h.data;
        return len;
    }

    /* access modifiers changed from: 0000 */
    public void swap_index(int i, int j) {
        if (i != j) {
            if (this.head[i].len > 0) {
                lru_delete(this.head[i]);
            }
            if (this.head[j].len > 0) {
                lru_delete(this.head[j]);
            }
            float[] _ = this.head[i].data;
            this.head[i].data = this.head[j].data;
            this.head[j].data = _;
            int _2 = this.head[i].len;
            this.head[i].len = this.head[j].len;
            this.head[j].len = _2;
            if (this.head[i].len > 0) {
                lru_insert(this.head[i]);
            }
            if (this.head[j].len > 0) {
                lru_insert(this.head[j]);
            }
            if (i > j) {
                int _3 = i;
                i = j;
                j = _3;
            }
            head_t h = this.lru_head;
            while (true) {
                h = h.next;
                if (h == this.lru_head) {
                    return;
                }
                if (h.len > i) {
                    if (h.len > j) {
                        float _4 = h.data[i];
                        h.data[i] = h.data[j];
                        h.data[j] = _4;
                    } else {
                        lru_delete(h);
                        this.size += (long) h.len;
                        h.data = null;
                        h.len = 0;
                    }
                }
            }
        }
    }
}
