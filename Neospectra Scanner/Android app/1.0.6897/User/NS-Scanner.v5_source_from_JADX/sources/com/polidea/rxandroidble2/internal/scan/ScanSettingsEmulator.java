package com.polidea.rxandroidble2.internal.scan;

import android.support.annotation.IntRange;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.internal.util.ObservableUtil;
import com.polidea.rxandroidble2.scan.ScanCallbackType;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.ObservableTransformer;
import p005io.reactivex.Scheduler;
import p005io.reactivex.functions.Function;
import p005io.reactivex.observables.GroupedObservable;

public class ScanSettingsEmulator {
    /* access modifiers changed from: private */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateFirstMatch;
    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateFirstMatchAndMatchLost = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
        public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
            return observable.publish(new Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() {
                public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                    return Observable.merge((ObservableSource<? extends T>) observable.compose(ScanSettingsEmulator.this.emulateFirstMatch), (ObservableSource<? extends T>) observable.compose(ScanSettingsEmulator.this.emulateMatchLost));
                }
            });
        }
    };
    /* access modifiers changed from: private */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateMatchLost = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
        public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
            return observable.debounce(10, TimeUnit.SECONDS, ScanSettingsEmulator.this.scheduler).map(ScanSettingsEmulator.this.toMatchLost());
        }
    };
    /* access modifiers changed from: private */
    public final Scheduler scheduler;

    @Inject
    public ScanSettingsEmulator(@Named("computation") final Scheduler scheduler2) {
        this.scheduler = scheduler2;
        this.emulateFirstMatch = new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            /* access modifiers changed from: private */
            public final Function<RxBleInternalScanResult, Observable<?>> emitAfterTimerFunc = new Function<RxBleInternalScanResult, Observable<?>>() {
                public Observable<?> apply(RxBleInternalScanResult internalScanResult) {
                    return C07991.this.timerObservable;
                }
            };
            /* access modifiers changed from: private */
            public final Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>> takeFirstFromEachWindowFunc = new Function<Observable<RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() {
                public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> rxBleInternalScanResultObservable) {
                    return rxBleInternalScanResultObservable.take(1);
                }
            };
            /* access modifiers changed from: private */
            public final Observable<Long> timerObservable = Observable.timer(10, TimeUnit.SECONDS, scheduler2);
            /* access modifiers changed from: private */
            public Function<RxBleInternalScanResult, RxBleInternalScanResult> toFirstMatchFunc = ScanSettingsEmulator.this.toFirstMatch();

            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                return observable.publish(new Function<Observable<RxBleInternalScanResult>, ObservableSource<RxBleInternalScanResult>>() {
                    public ObservableSource<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> publishedObservable) throws Exception {
                        return publishedObservable.window((ObservableSource<B>) publishedObservable.switchMap(C07991.this.emitAfterTimerFunc)).flatMap(C07991.this.takeFirstFromEachWindowFunc).map(C07991.this.toFirstMatchFunc);
                    }
                });
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateScanMode(int scanMode) {
        switch (scanMode) {
            case -1:
                RxBleLog.m17d("Cannot emulate opportunistic scan mode since it is OS dependent - fallthrough to low power", new Object[0]);
                break;
            case 0:
                break;
            case 1:
                return scanModeBalancedTransformer();
            default:
                return ObservableUtil.identityTransformer();
        }
        return scanModeLowPowerTransformer();
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanModeBalancedTransformer() {
        return repeatedWindowTransformer(2500);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> scanModeLowPowerTransformer() {
        return repeatedWindowTransformer(500);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> repeatedWindowTransformer(@IntRange(from = 0, mo190to = 4999) final int windowInMillis) {
        final long delayToNextWindow = Math.max(TimeUnit.SECONDS.toMillis(5) - ((long) windowInMillis), 0);
        return new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> rxBleInternalScanResultObservable) {
                return rxBleInternalScanResultObservable.take((long) windowInMillis, TimeUnit.MILLISECONDS, ScanSettingsEmulator.this.scheduler).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    public ObservableSource<?> apply(Observable<Object> observable) throws Exception {
                        return observable.delay(delayToNextWindow, TimeUnit.MILLISECONDS, ScanSettingsEmulator.this.scheduler);
                    }
                });
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> emulateCallbackType(int callbackType) {
        if (callbackType == 2) {
            return splitByAddressAndForEach(this.emulateFirstMatch);
        }
        if (callbackType == 4) {
            return splitByAddressAndForEach(this.emulateMatchLost);
        }
        if (callbackType != 6) {
            return ObservableUtil.identityTransformer();
        }
        return splitByAddressAndForEach(this.emulateFirstMatchAndMatchLost);
    }

    private ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> splitByAddressAndForEach(final ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult> compose) {
        return new ObservableTransformer<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public Observable<RxBleInternalScanResult> apply(Observable<RxBleInternalScanResult> observable) {
                return observable.groupBy(new Function<RxBleInternalScanResult, String>() {
                    public String apply(RxBleInternalScanResult rxBleInternalScanResult) {
                        return rxBleInternalScanResult.getBluetoothDevice().getAddress();
                    }
                }).flatMap(new Function<GroupedObservable<String, RxBleInternalScanResult>, Observable<RxBleInternalScanResult>>() {
                    public Observable<RxBleInternalScanResult> apply(GroupedObservable<String, RxBleInternalScanResult> groupedObservable) {
                        return groupedObservable.compose(compose);
                    }
                });
            }
        };
    }

    /* access modifiers changed from: private */
    public Function<RxBleInternalScanResult, RxBleInternalScanResult> toFirstMatch() {
        return new Function<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public RxBleInternalScanResult apply(RxBleInternalScanResult rxBleInternalScanResult) {
                RxBleInternalScanResult rxBleInternalScanResult2 = new RxBleInternalScanResult(rxBleInternalScanResult.getBluetoothDevice(), rxBleInternalScanResult.getRssi(), rxBleInternalScanResult.getTimestampNanos(), rxBleInternalScanResult.getScanRecord(), ScanCallbackType.CALLBACK_TYPE_FIRST_MATCH);
                return rxBleInternalScanResult2;
            }
        };
    }

    /* access modifiers changed from: private */
    public Function<RxBleInternalScanResult, RxBleInternalScanResult> toMatchLost() {
        return new Function<RxBleInternalScanResult, RxBleInternalScanResult>() {
            public RxBleInternalScanResult apply(RxBleInternalScanResult rxBleInternalScanResult) {
                RxBleInternalScanResult rxBleInternalScanResult2 = new RxBleInternalScanResult(rxBleInternalScanResult.getBluetoothDevice(), rxBleInternalScanResult.getRssi(), rxBleInternalScanResult.getTimestampNanos(), rxBleInternalScanResult.getScanRecord(), ScanCallbackType.CALLBACK_TYPE_MATCH_LOST);
                return rxBleInternalScanResult2;
            }
        };
    }
}
