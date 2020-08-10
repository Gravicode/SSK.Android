package p005io.reactivex.android.schedulers;

import android.os.Handler;
import android.os.Message;
import java.util.concurrent.TimeUnit;
import p005io.reactivex.Scheduler;
import p005io.reactivex.Scheduler.Worker;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.disposables.Disposables;
import p005io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.android.schedulers.HandlerScheduler */
final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$HandlerWorker */
    private static final class HandlerWorker extends Worker {
        private volatile boolean disposed;
        private final Handler handler;

        HandlerWorker(Handler handler2) {
            this.handler = handler2;
        }

        public Disposable schedule(Runnable run, long delay, TimeUnit unit) {
            if (run == null) {
                throw new NullPointerException("run == null");
            } else if (unit == null) {
                throw new NullPointerException("unit == null");
            } else if (this.disposed) {
                return Disposables.disposed();
            } else {
                ScheduledRunnable scheduled = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(run));
                Message message = Message.obtain(this.handler, scheduled);
                message.obj = this;
                this.handler.sendMessageDelayed(message, unit.toMillis(delay));
                if (!this.disposed) {
                    return scheduled;
                }
                this.handler.removeCallbacks(scheduled);
                return Disposables.disposed();
            }
        }

        public void dispose() {
            this.disposed = true;
            this.handler.removeCallbacksAndMessages(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$ScheduledRunnable */
    private static final class ScheduledRunnable implements Runnable, Disposable {
        private final Runnable delegate;
        private volatile boolean disposed;
        private final Handler handler;

        ScheduledRunnable(Handler handler2, Runnable delegate2) {
            this.handler = handler2;
            this.delegate = delegate2;
        }

        public void run() {
            try {
                this.delegate.run();
            } catch (Throwable t) {
                RxJavaPlugins.onError(t);
            }
        }

        public void dispose() {
            this.disposed = true;
            this.handler.removeCallbacks(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    HandlerScheduler(Handler handler2) {
        this.handler = handler2;
    }

    public Disposable scheduleDirect(Runnable run, long delay, TimeUnit unit) {
        if (run == null) {
            throw new NullPointerException("run == null");
        } else if (unit == null) {
            throw new NullPointerException("unit == null");
        } else {
            ScheduledRunnable scheduled = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(run));
            this.handler.postDelayed(scheduled, unit.toMillis(delay));
            return scheduled;
        }
    }

    public Worker createWorker() {
        return new HandlerWorker(this.handler);
    }
}
