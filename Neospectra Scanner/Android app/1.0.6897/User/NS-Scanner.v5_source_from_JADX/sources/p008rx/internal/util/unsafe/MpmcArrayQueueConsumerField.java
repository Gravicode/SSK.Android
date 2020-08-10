package p008rx.internal.util.unsafe;

import p008rx.internal.util.SuppressAnimalSniffer;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.MpmcArrayQueueConsumerField */
/* compiled from: MpmcArrayQueue */
abstract class MpmcArrayQueueConsumerField<E> extends MpmcArrayQueueL2Pad<E> {
    private static final long C_INDEX_OFFSET = UnsafeAccess.addressOf(MpmcArrayQueueConsumerField.class, "consumerIndex");
    private volatile long consumerIndex;

    public MpmcArrayQueueConsumerField(int capacity) {
        super(capacity);
    }

    /* access modifiers changed from: protected */
    public final long lvConsumerIndex() {
        return this.consumerIndex;
    }

    /* access modifiers changed from: protected */
    public final boolean casConsumerIndex(long expect, long newValue) {
        return UnsafeAccess.UNSAFE.compareAndSwapLong(this, C_INDEX_OFFSET, expect, newValue);
    }
}
