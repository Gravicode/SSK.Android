package com.polidea.rxandroidble2.internal.serialization;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.connection.ConnectionScope;
import com.polidea.rxandroidble2.internal.connection.ConnectionSubscriptionWatcher;
import com.polidea.rxandroidble2.internal.connection.DisconnectionRouterOutput;
import com.polidea.rxandroidble2.internal.operations.Operation;
import com.polidea.rxandroidble2.internal.util.OperationLogger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.ObservableOnSubscribe;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Cancellable;
import p005io.reactivex.observers.DisposableObserver;

@ConnectionScope
public class ConnectionOperationQueueImpl implements ConnectionOperationQueue, ConnectionSubscriptionWatcher {
    private final String deviceMacAddress;
    private BleException disconnectionException = null;
    private final DisconnectionRouterOutput disconnectionRouterOutput;
    private DisposableObserver<BleException> disconnectionThrowableSubscription;
    /* access modifiers changed from: private */
    public final OperationPriorityFifoBlockingQueue queue = new OperationPriorityFifoBlockingQueue();
    private final Future<?> runnableFuture;
    /* access modifiers changed from: private */
    public volatile boolean shouldRun = true;

    @Inject
    ConnectionOperationQueueImpl(@Named("mac-address") String deviceMacAddress2, DisconnectionRouterOutput disconnectionRouterOutput2, @Named("executor_connection_queue") ExecutorService executorService, @Named("bluetooth_interaction") final Scheduler callbackScheduler) {
        this.deviceMacAddress = deviceMacAddress2;
        this.disconnectionRouterOutput = disconnectionRouterOutput2;
        this.runnableFuture = executorService.submit(new Runnable() {
            public void run() {
                while (true) {
                    if (!ConnectionOperationQueueImpl.this.shouldRun) {
                        break;
                    }
                    try {
                        FIFORunnableEntry<?> entry = ConnectionOperationQueueImpl.this.queue.take();
                        Operation<T> operation = entry.operation;
                        long startedAtTime = System.currentTimeMillis();
                        OperationLogger.logOperationStarted(operation);
                        QueueSemaphore currentSemaphore = new QueueSemaphore();
                        entry.run(currentSemaphore, callbackScheduler);
                        currentSemaphore.awaitRelease();
                        OperationLogger.logOperationFinished(operation, startedAtTime, System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        synchronized (ConnectionOperationQueueImpl.this) {
                            if (!ConnectionOperationQueueImpl.this.shouldRun) {
                                break;
                            }
                            RxBleLog.m20e(e, "Error while processing connection operation queue", new Object[0]);
                        }
                    }
                }
                ConnectionOperationQueueImpl.this.flushQueue();
                RxBleLog.m17d("Terminated.", new Object[0]);
            }
        });
    }

    /* access modifiers changed from: private */
    public synchronized void flushQueue() {
        while (!this.queue.isEmpty()) {
            this.queue.takeNow().operationResultObserver.tryOnError(this.disconnectionException);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public synchronized <T> Observable<T> queue(final Operation<T> operation) {
        if (!this.shouldRun) {
            return Observable.error((Throwable) this.disconnectionException);
        }
        return Observable.create(new ObservableOnSubscribe<T>() {
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                final FIFORunnableEntry entry = new FIFORunnableEntry(operation, emitter);
                emitter.setCancellable(new Cancellable() {
                    public void cancel() throws Exception {
                        if (ConnectionOperationQueueImpl.this.queue.remove(entry)) {
                            OperationLogger.logOperationRemoved(operation);
                        }
                    }
                });
                OperationLogger.logOperationQueued(operation);
                ConnectionOperationQueueImpl.this.queue.add(entry);
            }
        });
    }

    public synchronized void terminate(BleException disconnectException) {
        if (this.disconnectionException == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Connection operations queue to be terminated (");
            sb.append(this.deviceMacAddress);
            sb.append(')');
            RxBleLog.m21i(sb.toString(), new Object[0]);
            this.shouldRun = false;
            this.disconnectionException = disconnectException;
            this.runnableFuture.cancel(true);
        }
    }

    public void onConnectionSubscribed() {
        this.disconnectionThrowableSubscription = (DisposableObserver) this.disconnectionRouterOutput.asValueOnlyObservable().subscribeWith(new DisposableObserver<BleException>() {
            public void onComplete() {
            }

            public void onNext(BleException bleException) {
                ConnectionOperationQueueImpl.this.terminate(bleException);
            }

            public void onError(Throwable throwable) {
            }
        });
    }

    public void onConnectionUnsubscribed() {
        this.disconnectionThrowableSubscription.dispose();
        this.disconnectionThrowableSubscription = null;
        terminate(new BleDisconnectedException(this.deviceMacAddress));
    }
}
