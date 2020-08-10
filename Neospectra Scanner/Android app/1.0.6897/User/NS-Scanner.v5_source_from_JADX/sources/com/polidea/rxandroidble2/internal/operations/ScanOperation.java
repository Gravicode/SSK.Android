package com.polidea.rxandroidble2.internal.operations;

import android.os.DeadObjectException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.RxBleAdapterWrapper;
import p005io.reactivex.Emitter;
import p005io.reactivex.ObservableEmitter;
import p005io.reactivex.functions.Cancellable;

public abstract class ScanOperation<SCAN_RESULT_TYPE, SCAN_CALLBACK_TYPE> extends QueueOperation<SCAN_RESULT_TYPE> {
    /* access modifiers changed from: private */
    public final RxBleAdapterWrapper rxBleAdapterWrapper;

    /* access modifiers changed from: 0000 */
    public abstract SCAN_CALLBACK_TYPE createScanCallback(Emitter<SCAN_RESULT_TYPE> emitter);

    /* access modifiers changed from: 0000 */
    public abstract boolean startScan(RxBleAdapterWrapper rxBleAdapterWrapper2, SCAN_CALLBACK_TYPE scan_callback_type);

    /* access modifiers changed from: 0000 */
    public abstract void stopScan(RxBleAdapterWrapper rxBleAdapterWrapper2, SCAN_CALLBACK_TYPE scan_callback_type);

    ScanOperation(RxBleAdapterWrapper rxBleAdapterWrapper2) {
        this.rxBleAdapterWrapper = rxBleAdapterWrapper2;
    }

    /* access modifiers changed from: protected */
    public final void protectedRun(ObservableEmitter<SCAN_RESULT_TYPE> emitter, QueueReleaseInterface queueReleaseInterface) {
        final SCAN_CALLBACK_TYPE scanCallback = createScanCallback(emitter);
        try {
            emitter.setCancellable(new Cancellable() {
                public void cancel() throws Exception {
                    RxBleLog.m21i("Scan operation is requested to stop.", new Object[0]);
                    ScanOperation.this.stopScan(ScanOperation.this.rxBleAdapterWrapper, scanCallback);
                }
            });
            RxBleLog.m21i("Scan operation is requested to start.", new Object[0]);
            if (!startScan(this.rxBleAdapterWrapper, scanCallback)) {
                emitter.tryOnError(new BleScanException(0));
            }
        } catch (Throwable th) {
            queueReleaseInterface.release();
            throw th;
        }
        queueReleaseInterface.release();
    }

    /* access modifiers changed from: protected */
    public BleException provideException(DeadObjectException deadObjectException) {
        return new BleScanException(1, (Throwable) deadObjectException);
    }
}
