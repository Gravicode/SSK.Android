package com.polidea.rxandroidble2.helpers;

import android.support.annotation.NonNull;
import java.nio.ByteBuffer;
import org.reactivestreams.Subscriber;
import p005io.reactivex.Emitter;
import p005io.reactivex.Flowable;
import p005io.reactivex.functions.Consumer;

public class ByteArrayBatchObservable extends Flowable<byte[]> {
    /* access modifiers changed from: private */
    @NonNull
    public final ByteBuffer byteBuffer;
    /* access modifiers changed from: private */
    public final int maxBatchSize;

    public ByteArrayBatchObservable(@NonNull byte[] bytes, int maxBatchSize2) {
        if (maxBatchSize2 <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("maxBatchSize must be > 0 but found: ");
            sb.append(maxBatchSize2);
            throw new IllegalArgumentException(sb.toString());
        }
        this.byteBuffer = ByteBuffer.wrap(bytes);
        this.maxBatchSize = maxBatchSize2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super byte[]> subscriber) {
        Flowable.generate(new Consumer<Emitter<byte[]>>() {
            public void accept(Emitter<byte[]> emitter) throws Exception {
                int nextBatchSize = Math.min(ByteArrayBatchObservable.this.byteBuffer.remaining(), ByteArrayBatchObservable.this.maxBatchSize);
                if (nextBatchSize == 0) {
                    emitter.onComplete();
                    return;
                }
                byte[] nextBatch = new byte[nextBatchSize];
                ByteArrayBatchObservable.this.byteBuffer.get(nextBatch);
                emitter.onNext(nextBatch);
            }
        }).subscribe(subscriber);
    }
}
