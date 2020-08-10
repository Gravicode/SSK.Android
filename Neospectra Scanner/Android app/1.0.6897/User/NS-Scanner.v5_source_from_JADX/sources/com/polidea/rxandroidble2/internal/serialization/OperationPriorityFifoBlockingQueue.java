package com.polidea.rxandroidble2.internal.serialization;

import java.util.Iterator;
import java.util.concurrent.PriorityBlockingQueue;

class OperationPriorityFifoBlockingQueue {

    /* renamed from: q */
    private final PriorityBlockingQueue<FIFORunnableEntry> f65q = new PriorityBlockingQueue<>();

    OperationPriorityFifoBlockingQueue() {
    }

    public void add(FIFORunnableEntry fifoRunnableEntry) {
        this.f65q.add(fifoRunnableEntry);
    }

    public FIFORunnableEntry<?> take() throws InterruptedException {
        return (FIFORunnableEntry) this.f65q.take();
    }

    public FIFORunnableEntry<?> takeNow() {
        return (FIFORunnableEntry) this.f65q.poll();
    }

    public boolean isEmpty() {
        return this.f65q.isEmpty();
    }

    public boolean remove(FIFORunnableEntry fifoRunnableEntry) {
        Iterator it = this.f65q.iterator();
        while (it.hasNext()) {
            FIFORunnableEntry entry = (FIFORunnableEntry) it.next();
            if (entry == fifoRunnableEntry) {
                return this.f65q.remove(entry);
            }
        }
        return false;
    }
}
