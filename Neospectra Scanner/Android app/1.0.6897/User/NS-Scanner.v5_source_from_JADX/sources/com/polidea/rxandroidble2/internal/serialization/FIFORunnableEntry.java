package com.polidea.rxandroidble2.internal.serialization;

import android.support.annotation.NonNull;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.operations.Operation;
import java.util.concurrent.atomic.AtomicLong;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.Observer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposable;

class FIFORunnableEntry<T> implements Comparable<FIFORunnableEntry> {
    private static final AtomicLong SEQUENCE = new AtomicLong(0);
    final Operation<T> operation;
    final ObservableEmitter<T> operationResultObserver;
    private final long seqNum = SEQUENCE.getAndIncrement();

    FIFORunnableEntry(Operation<T> operation2, ObservableEmitter<T> operationResultObserver2) {
        this.operation = operation2;
        this.operationResultObserver = operationResultObserver2;
    }

    public int compareTo(@NonNull FIFORunnableEntry other) {
        int res = this.operation.compareTo(other.operation);
        if (res != 0 || other.operation == this.operation) {
            return res;
        }
        return this.seqNum < other.seqNum ? -1 : 1;
    }

    public void run(QueueSemaphore semaphore, Scheduler subscribeScheduler) {
        if (this.operationResultObserver.isDisposed()) {
            StringBuilder sb = new StringBuilder();
            sb.append("The operation was about to be run but the observer had been already disposed: ");
            sb.append(this.operation);
            RxBleLog.m17d(sb.toString(), new Object[0]);
            semaphore.release();
            return;
        }
        this.operation.run(semaphore).subscribeOn(subscribeScheduler).unsubscribeOn(subscribeScheduler).subscribe((Observer<? super T>) new Observer<T>() {
            public void onSubscribe(Disposable disposable) {
                FIFORunnableEntry.this.operationResultObserver.setDisposable(disposable);
            }

            public void onNext(T item) {
                FIFORunnableEntry.this.operationResultObserver.onNext(item);
            }

            public void onError(Throwable e) {
                FIFORunnableEntry.this.operationResultObserver.tryOnError(e);
            }

            public void onComplete() {
                FIFORunnableEntry.this.operationResultObserver.onComplete();
            }
        });
    }
}
