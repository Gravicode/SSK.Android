package p005io.reactivex.internal.queue;

import java.util.concurrent.atomic.AtomicReference;
import p005io.reactivex.annotations.Nullable;
import p005io.reactivex.internal.fuseable.SimplePlainQueue;

/* renamed from: io.reactivex.internal.queue.MpscLinkedQueue */
public final class MpscLinkedQueue<T> implements SimplePlainQueue<T> {
    private final AtomicReference<LinkedQueueNode<T>> consumerNode = new AtomicReference<>();
    private final AtomicReference<LinkedQueueNode<T>> producerNode = new AtomicReference<>();

    /* renamed from: io.reactivex.internal.queue.MpscLinkedQueue$LinkedQueueNode */
    static final class LinkedQueueNode<E> extends AtomicReference<LinkedQueueNode<E>> {
        private static final long serialVersionUID = 2404266111789071508L;
        private E value;

        LinkedQueueNode() {
        }

        LinkedQueueNode(E val) {
            spValue(val);
        }

        public E getAndNullValue() {
            E temp = lpValue();
            spValue(null);
            return temp;
        }

        public E lpValue() {
            return this.value;
        }

        public void spValue(E newValue) {
            this.value = newValue;
        }

        public void soNext(LinkedQueueNode<E> n) {
            lazySet(n);
        }

        public LinkedQueueNode<E> lvNext() {
            return (LinkedQueueNode) get();
        }
    }

    public MpscLinkedQueue() {
        LinkedQueueNode<T> node = new LinkedQueueNode<>();
        spConsumerNode(node);
        xchgProducerNode(node);
    }

    public boolean offer(T e) {
        if (e == null) {
            throw new NullPointerException("Null is not a valid element");
        }
        LinkedQueueNode<T> nextNode = new LinkedQueueNode<>(e);
        xchgProducerNode(nextNode).soNext(nextNode);
        return true;
    }

    @Nullable
    public T poll() {
        LinkedQueueNode lvNext;
        LinkedQueueNode linkedQueueNode;
        T currConsumerNode = lpConsumerNode();
        LinkedQueueNode<T> nextNode = currConsumerNode.lvNext();
        if (nextNode != null) {
            T nextValue = nextNode.getAndNullValue();
            spConsumerNode(nextNode);
            return nextValue;
        } else if (currConsumerNode == lvProducerNode()) {
            return null;
        } else {
            do {
                lvNext = currConsumerNode.lvNext();
                linkedQueueNode = lvNext;
            } while (lvNext == null);
            T nextValue2 = linkedQueueNode.getAndNullValue();
            spConsumerNode(linkedQueueNode);
            return nextValue2;
        }
    }

    public boolean offer(T v1, T v2) {
        offer(v1);
        offer(v2);
        return true;
    }

    public void clear() {
        while (poll() != null) {
            if (isEmpty()) {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode<T> lvProducerNode() {
        return (LinkedQueueNode) this.producerNode.get();
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode<T> xchgProducerNode(LinkedQueueNode<T> node) {
        return (LinkedQueueNode) this.producerNode.getAndSet(node);
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode<T> lvConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: 0000 */
    public LinkedQueueNode<T> lpConsumerNode() {
        return (LinkedQueueNode) this.consumerNode.get();
    }

    /* access modifiers changed from: 0000 */
    public void spConsumerNode(LinkedQueueNode<T> node) {
        this.consumerNode.lazySet(node);
    }

    public boolean isEmpty() {
        return lvConsumerNode() == lvProducerNode();
    }
}
