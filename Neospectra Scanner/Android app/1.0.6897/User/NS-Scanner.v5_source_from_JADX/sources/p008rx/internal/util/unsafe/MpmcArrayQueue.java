package p008rx.internal.util.unsafe;

import p008rx.internal.util.SuppressAnimalSniffer;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.MpmcArrayQueue */
public class MpmcArrayQueue<E> extends MpmcArrayQueueConsumerField<E> {
    long p30;
    long p31;
    long p32;
    long p33;
    long p34;
    long p35;
    long p36;
    long p37;
    long p40;
    long p41;
    long p42;
    long p43;
    long p44;
    long p45;
    long p46;

    public MpmcArrayQueue(int capacity) {
        super(Math.max(2, capacity));
    }

    public boolean offer(E e) {
        E e2 = e;
        if (e2 == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        long capacity = this.mask + 1;
        long[] lSequenceBuffer = this.sequenceBuffer;
        long cIndex = Long.MAX_VALUE;
        while (true) {
            long cIndex2 = cIndex;
            long currentProducerIndex = lvProducerIndex();
            long seqOffset = calcSequenceOffset(currentProducerIndex);
            long seq = lvSequence(lSequenceBuffer, seqOffset);
            long delta = seq - currentProducerIndex;
            if (delta == 0) {
                long j = seq;
                if (casProducerIndex(currentProducerIndex, currentProducerIndex + 1)) {
                    long elementOffset = calcElementOffset(currentProducerIndex);
                    spElement(elementOffset, e2);
                    long j2 = elementOffset;
                    long j3 = seqOffset;
                    soSequence(lSequenceBuffer, seqOffset, currentProducerIndex + 1);
                    return true;
                }
            } else {
                long j4 = seq;
                long j5 = seqOffset;
                if (delta < 0 && currentProducerIndex - capacity <= cIndex2) {
                    long j6 = currentProducerIndex - capacity;
                    long lvConsumerIndex = lvConsumerIndex();
                    cIndex2 = lvConsumerIndex;
                    if (j6 <= lvConsumerIndex) {
                        return false;
                    }
                }
            }
            cIndex = cIndex2;
        }
    }

    public E poll() {
        long[] lSequenceBuffer = this.sequenceBuffer;
        long seq = -1;
        while (true) {
            long pIndex = seq;
            long currentConsumerIndex = lvConsumerIndex();
            long seqOffset = calcSequenceOffset(currentConsumerIndex);
            long delta = lvSequence(lSequenceBuffer, seqOffset) - (currentConsumerIndex + 1);
            if (delta == 0) {
                if (casConsumerIndex(currentConsumerIndex, currentConsumerIndex + 1)) {
                    long offset = calcElementOffset(currentConsumerIndex);
                    E e = lpElement(offset);
                    spElement(offset, null);
                    soSequence(lSequenceBuffer, seqOffset, this.mask + currentConsumerIndex + 1);
                    return e;
                }
            } else if (delta < 0 && currentConsumerIndex >= pIndex) {
                long lvProducerIndex = lvProducerIndex();
                pIndex = lvProducerIndex;
                if (currentConsumerIndex == lvProducerIndex) {
                    return null;
                }
            }
            seq = pIndex;
        }
    }

    public E peek() {
        long currConsumerIndex;
        E e;
        do {
            currConsumerIndex = lvConsumerIndex();
            e = lpElement(calcElementOffset(currConsumerIndex));
            if (e != null) {
                break;
            }
        } while (currConsumerIndex != lvProducerIndex());
        return e;
    }

    public int size() {
        long before;
        long currentProducerIndex;
        long after = lvConsumerIndex();
        do {
            before = after;
            currentProducerIndex = lvProducerIndex();
            after = lvConsumerIndex();
        } while (before != after);
        return (int) (currentProducerIndex - after);
    }

    public boolean isEmpty() {
        return lvConsumerIndex() == lvProducerIndex();
    }
}
