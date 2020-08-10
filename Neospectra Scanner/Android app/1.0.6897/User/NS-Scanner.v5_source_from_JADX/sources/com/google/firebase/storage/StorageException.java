package com.google.firebase.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.FirebaseException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class StorageException extends FirebaseException {
    public static final int ERROR_BUCKET_NOT_FOUND = -13011;
    public static final int ERROR_CANCELED = -13040;
    public static final int ERROR_INVALID_CHECKSUM = -13031;
    public static final int ERROR_NOT_AUTHENTICATED = -13020;
    public static final int ERROR_NOT_AUTHORIZED = -13021;
    public static final int ERROR_OBJECT_NOT_FOUND = -13010;
    public static final int ERROR_PROJECT_NOT_FOUND = -13012;
    public static final int ERROR_QUOTA_EXCEEDED = -13013;
    public static final int ERROR_RETRY_LIMIT_EXCEEDED = -13030;
    public static final int ERROR_UNKNOWN = -13000;
    private static IOException zzoii = new IOException("The operation was canceled.");
    private final int mErrorCode;
    private final int zzoij;
    private String zzoik;
    private Throwable zzoil;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    private StorageException(int i, Throwable th, int i2) {
        String str;
        switch (i) {
            case ERROR_CANCELED /*-13040*/:
                str = "The operation was cancelled.";
                break;
            case ERROR_INVALID_CHECKSUM /*-13031*/:
                str = "Object has a checksum which does not match. Please retry the operation.";
                break;
            case ERROR_RETRY_LIMIT_EXCEEDED /*-13030*/:
                str = "The operation retry limit has been exceeded.";
                break;
            case ERROR_NOT_AUTHORIZED /*-13021*/:
                str = "User does not have permission to access this object.";
                break;
            case ERROR_NOT_AUTHENTICATED /*-13020*/:
                str = "User is not authenticated, please authenticate using Firebase Authentication and try again.";
                break;
            case ERROR_QUOTA_EXCEEDED /*-13013*/:
                str = "Quota for bucket exceeded, please view quota on www.firebase.google.com/storage.";
                break;
            case ERROR_PROJECT_NOT_FOUND /*-13012*/:
                str = "Project does not exist.";
                break;
            case ERROR_BUCKET_NOT_FOUND /*-13011*/:
                str = "Bucket does not exist.";
                break;
            case ERROR_OBJECT_NOT_FOUND /*-13010*/:
                str = "Object does not exist at location.";
                break;
            default:
                str = "An unknown error occurred, please check the HTTP result code and inner exception for server response.";
                break;
        }
        this.zzoik = str;
        this.zzoil = th;
        this.mErrorCode = i;
        this.zzoij = i2;
        String str2 = this.zzoik;
        String num = Integer.toString(this.mErrorCode);
        String num2 = Integer.toString(this.zzoij);
        StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 52 + String.valueOf(num).length() + String.valueOf(num2).length());
        sb.append("StorageException has occurred.\n");
        sb.append(str2);
        sb.append("\n Code: ");
        sb.append(num);
        sb.append(" HttpResult: ");
        sb.append(num2);
        Log.e("StorageException", sb.toString());
        if (this.zzoil != null) {
            Log.e("StorageException", this.zzoil.getMessage(), this.zzoil);
        }
    }

    @NonNull
    public static StorageException fromErrorStatus(@NonNull Status status) {
        zzbq.checkNotNull(status);
        zzbq.checkArgument(!status.isSuccess());
        int i = status.isCanceled() ? ERROR_CANCELED : status == Status.zzfnl ? ERROR_RETRY_LIMIT_EXCEEDED : ERROR_UNKNOWN;
        return new StorageException(i, null, 0);
    }

    @NonNull
    public static StorageException fromException(@NonNull Throwable th) {
        return fromExceptionAndHttpCode(th, 0);
    }

    @Nullable
    public static StorageException fromExceptionAndHttpCode(@Nullable Throwable th, int i) {
        int i2;
        if (th instanceof StorageException) {
            return (StorageException) th;
        }
        if ((i == 0 || (i >= 200 && i < 300)) && th == null) {
            return null;
        }
        if (th instanceof zza) {
            i2 = ERROR_CANCELED;
        } else if (i == -2) {
            i2 = ERROR_RETRY_LIMIT_EXCEEDED;
        } else if (i == 401) {
            i2 = ERROR_NOT_AUTHENTICATED;
        } else if (i != 409) {
            switch (i) {
                case 403:
                    i2 = ERROR_NOT_AUTHORIZED;
                    break;
                case 404:
                    i2 = ERROR_OBJECT_NOT_FOUND;
                    break;
                default:
                    i2 = ERROR_UNKNOWN;
                    break;
            }
        } else {
            i2 = ERROR_INVALID_CHECKSUM;
        }
        return new StorageException(i2, th, i);
    }

    public Throwable getCause() {
        if (this.zzoil == this) {
            return null;
        }
        return this.zzoil;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public int getHttpResultCode() {
        return this.zzoij;
    }

    public boolean getIsRecoverableException() {
        return getErrorCode() == -13030;
    }

    public String getMessage() {
        return this.zzoik;
    }
}
