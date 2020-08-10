package com.polidea.rxandroidble2.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ScanSettings implements Parcelable {
    public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
    public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
    public static final int CALLBACK_TYPE_MATCH_LOST = 4;
    public static final Creator<ScanSettings> CREATOR = new Creator<ScanSettings>() {
        public ScanSettings[] newArray(int size) {
            return new ScanSettings[size];
        }

        public ScanSettings createFromParcel(Parcel in) {
            return new ScanSettings(in);
        }
    };
    public static final int MATCH_MODE_AGGRESSIVE = 1;
    public static final int MATCH_MODE_STICKY = 2;
    public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
    public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
    public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
    public static final int SCAN_MODE_BALANCED = 1;
    public static final int SCAN_MODE_LOW_LATENCY = 2;
    public static final int SCAN_MODE_LOW_POWER = 0;
    public static final int SCAN_MODE_OPPORTUNISTIC = -1;
    private int mCallbackType;
    private int mMatchMode;
    private int mNumOfMatchesPerFilter;
    private long mReportDelayMillis;
    private int mScanMode;

    public static final class Builder {
        private int mCallbackType = 1;
        private int mMatchMode = 1;
        private int mNumOfMatchesPerFilter = 3;
        private long mReportDelayMillis = 0;
        private int mScanMode = 0;

        public Builder setScanMode(int scanMode) {
            if (scanMode < -1 || scanMode > 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("invalid scan mode ");
                sb.append(scanMode);
                throw new IllegalArgumentException(sb.toString());
            }
            this.mScanMode = scanMode;
            return this;
        }

        public Builder setCallbackType(int callbackType) {
            if (!isValidCallbackType(callbackType)) {
                StringBuilder sb = new StringBuilder();
                sb.append("invalid callback type - ");
                sb.append(callbackType);
                throw new IllegalArgumentException(sb.toString());
            }
            this.mCallbackType = callbackType;
            return this;
        }

        private boolean isValidCallbackType(int callbackType) {
            boolean z = true;
            if (callbackType == 1 || callbackType == 2 || callbackType == 4) {
                return true;
            }
            if (callbackType != 6) {
                z = false;
            }
            return z;
        }

        public ScanSettings build() {
            ScanSettings scanSettings = new ScanSettings(this.mScanMode, this.mCallbackType, this.mReportDelayMillis, this.mMatchMode, this.mNumOfMatchesPerFilter);
            return scanSettings;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CallbackType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MatchNum {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ScanMode {
    }

    public int getScanMode() {
        return this.mScanMode;
    }

    public int getCallbackType() {
        return this.mCallbackType;
    }

    public int getMatchMode() {
        return this.mMatchMode;
    }

    public int getNumOfMatches() {
        return this.mNumOfMatchesPerFilter;
    }

    public long getReportDelayMillis() {
        return this.mReportDelayMillis;
    }

    private ScanSettings(int scanMode, int callbackType, long reportDelayMillis, int matchMode, int numOfMatchesPerFilter) {
        this.mScanMode = scanMode;
        this.mCallbackType = callbackType;
        this.mReportDelayMillis = reportDelayMillis;
        this.mNumOfMatchesPerFilter = numOfMatchesPerFilter;
        this.mMatchMode = matchMode;
    }

    private ScanSettings(Parcel in) {
        this.mScanMode = in.readInt();
        this.mCallbackType = in.readInt();
        this.mReportDelayMillis = in.readLong();
        this.mMatchMode = in.readInt();
        this.mNumOfMatchesPerFilter = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mScanMode);
        dest.writeInt(this.mCallbackType);
        dest.writeLong(this.mReportDelayMillis);
        dest.writeInt(this.mMatchMode);
        dest.writeInt(this.mNumOfMatchesPerFilter);
    }

    public int describeContents() {
        return 0;
    }
}
