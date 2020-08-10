package p008rx.internal.util.atomic;

/* renamed from: rx.internal.util.atomic.MpscLinkedAtomicQueue */
public final class MpscLinkedAtomicQueue<E> extends BaseLinkedAtomicQueue<E> {
    public MpscLinkedAtomicQueue() {
        LinkedQueueNode<E> node = new LinkedQueueNode<>();
        spConsumerNode(node);
        xchgProducerNode(node);
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
            spConsumerNode(linkedQueueNode);
            return nextValue2;
        }
    }

    public E peek() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        LinkedQueueNode<E> currConsumerNode = lpConsumerNode();
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
