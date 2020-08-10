package p008rx.internal.util.unsafe;

import java.util.Iterator;
import p008rx.internal.util.SuppressAnimalSniffer;
import p008rx.internal.util.atomic.LinkedQueueNode;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.BaseLinkedQueue */
abstract class BaseLinkedQueue<E> extends BaseLinkedQueueConsumerNodeRef<E> {
    long p00;
    long p01;
    long p02;
    long p03;
    long p04;
    long p05;
    long p06;
    long p07;
    long p30;
    long p31;
    long p32;
    long p33;
    long p34;
    long p35;
    long p36;
    long p37;

    BaseLinkedQueue() {
    }

    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public final int size() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        LinkedQueueNode<E> chaserNode = lvConsumerNode();
        LinkedQueueNode<E> producerNode = lvProducerNode();
        int size = 0;
        while (chaserNode != producerNode && size < Integer.MAX_VALUE) {
            do {
                lvNext = chaserNode.lvNext();
                linkedQueueNode = lvNext;
            } while (lvNext == null);
            chaserNode = linkedQueueNode;
            size++;
        }
        return size;
    }

    public final boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }
}
