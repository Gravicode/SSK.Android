package com.polidea.rxandroidble2.internal.serialization;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.operations.Operation;
import com.polidea.rxandroidble2.internal.util.OperationLogger;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableOnSubscribe;
import p005io.reactivex.Scheduler;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.functions.Action;

public class ClientOperationQueueImpl implements ClientOperationQueue {
    /* access modifiers changed from: private */
    public OperationPriorityFifoBlockingQueue queue = new OperationPriorityFifoBlockingQueue();

    @Inject
    public ClientOperationQueueImpl(@Named("bluetooth_interaction") final Scheduler callbackScheduler) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        FIFORunnableEntry<?> entry = ClientOperationQueueImpl.this.queue.take();
                        Operation<T> operation = entry.operation;
                        long startedAtTime = System.currentTimeMillis();
                        OperationLogger.logOperationStarted(operation);
                        QueueSemaphore clientOperationSemaphore = new QueueSemaphore();
                        entry.run(clientOperationSemaphore, callbackScheduler);
                        clientOperationSemaphore.awaitRelease();
                        OperationLogger.logOperationFinished(operation, startedAtTime, System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        RxBleLog.m20e(e, "Error while processing client operation queue", new Object[0]);
                    }
                }
            }
        }).start();
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public <T> Observable<T> queue(final Operation<T> operation) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            public void subscribe(ObservableEmitter<T> tEmitter) throws Exception {
                final FIFORunnableEntry entry = new FIFORunnableEntry(operation, tEmitter);
                tEmitter.setDisposable(Disposables.fromAction(new Action() {
                    public void run() throws Exception {
                        if (ClientOperationQueueImpl.this.queue.remove(entry)) {
                            OperationLogger.logOperationRemoved(operation);
                        }
                    }
                }));
                OperationLogger.logOperationQueued(operation);
                ClientOperationQueueImpl.this.queue.add(entry);
            }
        });
    }

    @RestrictTo({Scope.SUBCLASSES})
    private void log(String prefix, Operation operation) {
        if (RxBleLog.isAtLeast(3)) {
            RxBleLog.m17d("%8s %s(%d)", prefix, operation.getClass().getSimpleName(), Integer.valueOf(System.identityHashCode(operation)));
        }
    }
}
