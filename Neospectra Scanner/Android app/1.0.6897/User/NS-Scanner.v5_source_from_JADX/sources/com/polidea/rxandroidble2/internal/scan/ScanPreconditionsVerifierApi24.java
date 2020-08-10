package com.polidea.rxandroidble2.internal.scan;

import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;

public class ScanPreconditionsVerifierApi24 implements ScanPreconditionsVerifier {
    private static final long EXCESSIVE_SCANNING_PERIOD = TimeUnit.SECONDS.toMillis(30);
    private static final int SCANS_LENGTH = 5;
    private final long[] previousChecks = new long[5];
    private final ScanPreconditionsVerifierApi18 scanPreconditionVerifierApi18;
    private final Scheduler timeScheduler;

    @Inject
    public ScanPreconditionsVerifierApi24(ScanPreconditionsVerifierApi18 scanPreconditionVerifierApi182, @Named("computation") Scheduler timeScheduler2) {
        this.scanPreconditionVerifierApi18 = scanPreconditionVerifierApi182;
        this.timeScheduler = timeScheduler2;
    }

    public void verify() {
        this.scanPreconditionVerifierApi18.verify();
        int oldestCheckTimestampIndex = getOldestCheckTimestampIndex();
        long oldestCheckTimestamp = this.previousChecks[oldestCheckTimestampIndex];
        long currentCheckTimestamp = this.timeScheduler.now(TimeUnit.MILLISECONDS);
        if (currentCheckTimestamp - oldestCheckTimestamp < EXCESSIVE_SCANNING_PERIOD) {
            throw new BleScanException((int) BleScanException.UNDOCUMENTED_SCAN_THROTTLE, new Date(EXCESSIVE_SCANNING_PERIOD + oldestCheckTimestamp));
        }
        this.previousChecks[oldestCheckTimestampIndex] = currentCheckTimestamp;
    }

    private int getOldestCheckTimestampIndex() {
        long oldestTimestamp = Long.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < 5; i++) {
            long previousCheckTimestamp = this.previousChecks[i];
            if (previousCheckTimestamp < oldestTimestamp) {
                index = i;
                oldestTimestamp = previousCheckTimestamp;
            }
        }
        return index;
    }
}
