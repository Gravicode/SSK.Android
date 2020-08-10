package p008rx.internal.util.unsafe;

/* renamed from: rx.internal.util.unsafe.SpscArrayQueueColdField */
/* compiled from: SpscArrayQueue */
abstract class SpscArrayQueueColdField<E> extends ConcurrentCircularArrayQueue<E> {
    private static final Integer MAX_LOOK_AHEAD_STEP = Integer.getInteger("jctools.spsc.max.lookahead.step", 4096);
    protected final int lookAheadStep;

    public SpscArrayQueueColdField(int capacity) {
        super(capacity);
        this.lookAheadStep = Math.min(capacity / 4, MAX_LOOK_AHEAD_STEP.intValue());
    }
}
