package p008rx.internal.util.atomic;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

/* renamed from: rx.internal.util.atomic.BaseLinkedAtomicQueue */
abstract class BaseLinkedAtomicQueue<E> extends AbstractQueue<E> {
    private final AtomicReference<LinkedQueueNode<E>> consumerNode = new AtomicReference<>();
    private final AtomicReference<LinkedQueueNode<E>> producerNode = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lvProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lpProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    /* access modifiers changed from: protected */
    public final void spProducerNode(LinkedQueueNode<E> node) {
        this.producerNode.lazySet(node);
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> node) {
        return (LinkedQueueNode) this.producerNode.getAndSet(node);
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lvConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: protected */
    public final LinkedQueueNode<E> lpConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: protected */
    public final void spConsumerNode(LinkedQueueNode<E> node) {
        this.consumerNode.lazySet(node);
    }

    public final Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    public final int size() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        LinkedQueueNode<E> chaserNode = lvConsumerNode();
        LinkedQueueNode<E> producerNode2 = lvProducerNode();
        int size = 0;
        while (chaserNode != producerNode2 && size < Integer.MAX_VALUE) {
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
