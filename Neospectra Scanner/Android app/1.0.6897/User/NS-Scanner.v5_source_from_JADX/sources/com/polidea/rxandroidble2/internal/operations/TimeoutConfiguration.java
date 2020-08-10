package com.polidea.rxandroidble2.internal.operations;

import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;

public class TimeoutConfiguration {
    public final long timeout;
    public final Scheduler timeoutScheduler;
    public final TimeUnit timeoutTimeUnit;

    public TimeoutConfiguration(long timeout2, TimeUnit timeoutTimeUnit2, Scheduler timeoutScheduler2) {
        this.timeout = timeout2;
        this.timeoutTimeUnit = timeoutTimeUnit2;
        this.timeoutScheduler = timeoutScheduler2;
    }
}
