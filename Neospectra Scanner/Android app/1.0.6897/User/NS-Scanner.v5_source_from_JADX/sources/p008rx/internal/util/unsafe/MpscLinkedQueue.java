package p008rx.internal.util.unsafe;

import p008rx.internal.util.SuppressAnimalSniffer;
import p008rx.internal.util.atomic.LinkedQueueNode;

@SuppressAnimalSniffer
/* renamed from: rx.internal.util.unsafe.MpscLinkedQueue */
public final class MpscLinkedQueue<E> extends BaseLinkedQueue<E> {
    public MpscLinkedQueue() {
        this.consumerNode = new LinkedQueueNode();
        xchgProducerNode(this.consumerNode);
    }

    /* access modifiers changed from: protected */
    public LinkedQueueNode<E> xchgProducerNode(LinkedQueueNode<E> newVal) {
        LinkedQueueNode linkedQueueNode;
        do {
            linkedQueueNode = this.producerNode;
        } while (!UnsafeAccess.UNSAFE.compareAndSwapObject(this, P_NODE_OFFSET, linkedQueueNode, newVal));
        return linkedQueueNode;
    }

    public boolean offer(E nextValue) {
        if (nextValue == null) {
            throw new NullPointerException("null elements not allowed");
        }
        LinkedQueueNode<E> nextNode = new LinkedQueueNode<>(nextValue);
        xchgProducerNode(nextNode).soNext(nextNode);
        return true;
    }

    public E poll() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        E currConsumerNode = lpConsumerNode();
        LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            E nextValue = nextNode.getAndNullValue();
            spConsumerNode(nextNode);
            return nextValue;
        } else if (currConsumerNode == lvProducerNode()) {
            return null;
        } else {
            do {
                lvNext = currConsumerNode.lvNext();
                linkedQueueNode = lvNext;
            } while (lvNext == null);
            E nextValue2 = linkedQueueNode.getAndNullValue();
            this.consumerNode = linkedQueueNode;
            return nextValue2;
        }
    }

    public E peek() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        LinkedQueueNode<E> currConsumerNode = this.consumerNode;
        LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            return nextNode.lpValue();
        }
        if (currConsumerNode == lvProducerNode()) {
            return null;
        }
        do {
            lvNext = currConsumerNode.lvNext();
            linkedQueueNode = lvNext;
        } while (lvNext == null);
        return linkedQueueNode.lpValue();
    }
}
