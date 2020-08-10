package com.polidea.rxandroidble2.internal;

import android.support.annotation.Nullable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RxBleLog {
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    private static final ThreadLocal<String> NEXT_TAG = new ThreadLocal<>();
    public static final int NONE = Integer.MAX_VALUE;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int logLevel = Integer.MAX_VALUE;
    private static Logger logcatLogger = new Logger() {
        public void log(int level, String tag, String msg) {
            Log.println(level, tag, msg);
        }
    };
    private static Logger logger = logcatLogger;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LogLevel {
    }

    public interface Logger {
        void log(int i, String str, String str2);
    }

    private RxBleLog() {
    }

    public static void setLogger(@Nullable Logger logger2) {
        if (logger2 == null) {
            logger = logcatLogger;
        } else {
            logger = logger2;
        }
    }

    public static void setLogLevel(int logLevel2) {
        logLevel = logLevel2;
    }

    private static String createTag() {
        String tag = (String) NEXT_TAG.get();
        if (tag != null) {
            NEXT_TAG.remove();
            return tag;
        }
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length < 5) {
            throw new IllegalStateException("Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }
        String tag2 = stackTrace[4].getClassName();
        Matcher m = ANONYMOUS_CLASS.matcher(tag2);
        if (m.find()) {
            tag2 = m.replaceAll("");
        }
        String tag3 = tag2.replace("Impl", "").replace("RxBle", "");
        StringBuilder sb = new StringBuilder();
        sb.append("RxBle#");
        sb.append(tag3.substring(tag3.lastIndexOf(46) + 1));
        return sb.toString();
    }

    private static String formatString(String message, Object... args) {
        return args.length == 0 ? message : String.format(message, args);
    }

    /* renamed from: v */
    public static void m23v(String message, Object... args) {
        throwShade(2, null, message, args);
    }

    /* renamed from: v */
    public static void m24v(Throwable t, String message, Object... args) {
        throwShade(2, t, message, args);
    }

    /* renamed from: d */
    public static void m17d(String message, Object... args) {
        throwShade(3, null, message, args);
    }

    /* renamed from: d */
    public static void m18d(Throwable t, String message, Object... args) {
        throwShade(3, t, message, args);
    }

    /* renamed from: i */
    public static void m21i(String message, Object... args) {
        throwShade(4, null, message, args);
    }

    /* renamed from: i */
    public static void m22i(Throwable t, String message, Object... args) {
        throwShade(4, t, message, args);
    }

    /* renamed from: w */
    public static void m25w(String message, Object... args) {
        throwShade(5, null, message, args);
    }

    /* renamed from: w */
    public static void m26w(Throwable t, String message, Object... args) {
        throwShade(5, t, message, args);
    }

    /* renamed from: e */
    public static void m19e(String message, Object... args) {
        throwShade(6, null, message, args);
    }

    /* renamed from: e */
    public static void m20e(Throwable t, String message, Object... args) {
        throwShade(6, t, message, args);
    }

    private static void throwShade(int priority, Throwable t, String message, Object... args) {
        String finalMessage;
        if (priority >= logLevel) {
            String formattedMessage = formatString(message, args);
            if (formattedMessage == null || formattedMessage.length() == 0) {
                if (t != null) {
                    finalMessage = Log.getStackTraceString(t);
                } else {
                    return;
                }
            } else if (t != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(formattedMessage);
                sb.append("\n");
                sb.append(Log.getStackTraceString(t));
                finalMessage = sb.toString();
            } else {
                finalMessage = formattedMessage;
            }
            println(priority, createTag(), finalMessage);
        }
    }

    private static void println(int priority, String tag, String message) {
        if (message.length() < 4000) {
            logger.log(priority, tag, message);
            return;
        }
        for (String line : message.split("\n")) {
            logger.log(priority, tag, line);
        }
    }

    public static boolean isAtLeast(int expectedLogLevel) {
        return logLevel <= expectedLogLevel;
    }
}
