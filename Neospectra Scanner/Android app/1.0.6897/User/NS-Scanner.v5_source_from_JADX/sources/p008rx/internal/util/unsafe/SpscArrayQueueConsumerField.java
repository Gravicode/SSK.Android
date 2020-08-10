package p008rx.internal.util.unsafe;

import p008rx.internal.util.SuppressAnimalSniffer;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.SpscArrayQueueConsumerField */
/* compiled from: SpscArrayQueue */
abstract class SpscArrayQueueConsumerField<E> extends SpscArrayQueueL2Pad<E> {
    protected static final long C_INDEX_OFFSET = UnsafeAccess.addressOf(SpscArrayQueueConsumerField.class, "consumerIndex");
    protected long consumerIndex;

    public SpscArrayQueueConsumerField(int capacity) {
        super(capacity);
    }
}
