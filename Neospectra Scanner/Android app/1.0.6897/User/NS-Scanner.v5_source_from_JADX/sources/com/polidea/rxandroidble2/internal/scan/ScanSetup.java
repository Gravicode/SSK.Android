package com.polidea.rxandroidble2.internal.scan;

import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import com.polidea.rxandroidble2.internal.operations.Operation;
import p005io.reactivex.ObservableTransformer;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ScanSetup {
    public final Operation<RxBleInternalScanResult> scanOperation;
    public final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanOperationBehaviourEmulatorTransformer;

    public ScanSetup(Operation<RxBleInternalScanResult> scanOperation2, ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanOperationBehaviourEmulatorTransformer2) {
        this.scanOperation = scanOperation2;
        this.scanOperationBehaviourEmulatorTransformer = scanOperationBehaviourEmulatorTransformer2;
    }
}
