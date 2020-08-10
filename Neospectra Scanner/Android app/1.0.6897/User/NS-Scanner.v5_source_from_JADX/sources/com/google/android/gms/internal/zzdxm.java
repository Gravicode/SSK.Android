package com.google.android.gms.internal;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.util.SparseArray;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.PhoneAuthCredential;

public final class zzdxm {
    private static SparseArray<Pair<String, String>> zzmfa;

    static {
        SparseArray<Pair<String, String>> sparseArray = new SparseArray<>();
        zzmfa = sparseArray;
        sparseArray.put(17000, new Pair("ERROR_INVALID_CUSTOM_TOKEN", "The custom token format is incorrect. Please check the documentation."));
        zzmfa.put(17002, new Pair("ERROR_CUSTOM_TOKEN_MISMATCH", "The custom token corresponds to a different audience."));
        zzmfa.put(17004, new Pair("ERROR_INVALID_CREDENTIAL", "The supplied auth credential is malformed or has expired."));
        zzmfa.put(17008, new Pair("ERROR_INVALID_EMAIL", "The email address is badly formatted."));
        zzmfa.put(17009, new Pair("ERROR_WRONG_PASSWORD", "The password is invalid or the user does not have a password."));
        zzmfa.put(17024, new Pair("ERROR_USER_MISMATCH", "The supplied credentials do not correspond to the previously signed in user."));
        zzmfa.put(17014, new Pair("ERROR_REQUIRES_RECENT_LOGIN", "This operation is sensitive and requires recent authentication. Log in again before retrying this request."));
        zzmfa.put(17012, new Pair("ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL", "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address."));
        zzmfa.put(17007, new Pair("ERROR_EMAIL_ALREADY_IN_USE", "The email address is already in use by another account."));
        zzmfa.put(17025, new Pair("ERROR_CREDENTIAL_ALREADY_IN_USE", "This credential is already associated with a different user account."));
        zzmfa.put(17005, new Pair("ERROR_USER_DISABLED", "The user account has been disabled by an administrator."));
        zzmfa.put(17021, new Pair("ERROR_USER_TOKEN_EXPIRED", "The user's credential is no longer valid. The user must sign in again."));
        zzmfa.put(17011, new Pair("ERROR_USER_NOT_FOUND", "There is no user record corresponding to this identifier. The user may have been deleted."));
        zzmfa.put(17017, new Pair("ERROR_INVALID_USER_TOKEN", "The user's credential is no longer valid. The user must sign in again."));
        zzmfa.put(17006, new Pair("ERROR_OPERATION_NOT_ALLOWED", "This operation is not allowed. You must enable this service in the console."));
        zzmfa.put(17026, new Pair("ERROR_WEAK_PASSWORD", "The given password is invalid."));
        zzmfa.put(17029, new Pair("ERROR_EXPIRED_ACTION_CODE", "The out of band code has expired."));
        zzmfa.put(17030, new Pair("ERROR_INVALID_ACTION_CODE", "The out of band code is invalid. This can happen if the code is malformed, expired, or has already been used."));
        zzmfa.put(17031, new Pair("ERROR_INVALID_MESSAGE_PAYLOAD", "The email template corresponding to this action contains invalid characters in its message. Please fix by going to the Auth email templates section in the Firebase Console."));
        zzmfa.put(17033, new Pair("ERROR_INVALID_RECIPIENT_EMAIL", "The email corresponding to this action failed to send as the provided recipient email address is invalid."));
        zzmfa.put(17032, new Pair("ERROR_INVALID_SENDER", "The email template corresponding to this action contains an invalid sender email or name. Please fix by going to the Auth email templates section in the Firebase Console."));
        zzmfa.put(17034, new Pair("ERROR_MISSING_EMAIL", "An email address must be provided."));
        zzmfa.put(17035, new Pair("ERROR_MISSING_PASSWORD", "A password must be provided."));
        zzmfa.put(17041, new Pair("ERROR_MISSING_PHONE_NUMBER", "To send verification codes, provide a phone number for the recipient."));
        zzmfa.put(17042, new Pair("ERROR_INVALID_PHONE_NUMBER", "The format of the phone number provided is incorrect. Please enter the phone number in a format that can be parsed into E.164 format. E.164 phone numbers are written in the format [+][country code][subscriber number including area code]."));
        zzmfa.put(17043, new Pair("ERROR_MISSING_VERIFICATION_CODE", "The Phone Auth Credential was created with an empty sms verification Code"));
        zzmfa.put(17044, new Pair("ERROR_INVALID_VERIFICATION_CODE", "The sms verification code used to create the phone auth credential is invalid. Please resend the verification code sms and be sure use the verification code provided by the user."));
        zzmfa.put(17045, new Pair("ERROR_MISSING_VERIFICATION_ID", "The Phone Auth Credential was created with an empty verification ID"));
        zzmfa.put(17046, new Pair("ERROR_INVALID_VERIFICATION_ID", "The verification ID used to create the phone auth credential is invalid."));
        zzmfa.put(17049, new Pair("ERROR_RETRY_PHONE_AUTH", "An error occurred during authentication using the PhoneAuthCredential. Please retry authentication."));
        zzmfa.put(17051, new Pair("ERROR_SESSION_EXPIRED", "The sms code has expired. Please re-send the verification code to try again."));
        zzmfa.put(17052, new Pair("ERROR_QUOTA_EXCEEDED", "The sms quota for this project has been exceeded."));
        zzmfa.put(17028, new Pair("ERROR_APP_NOT_AUTHORIZED", "This app is not authorized to use Firebase Authentication. Please verifythat the correct package name and SHA-1 are configured in the Firebase Console."));
        zzmfa.put(17056, new Pair("ERROR_API_NOT_AVAILABLE", "The API that you are calling is not available on devices without Google Play Services."));
    }

    private static String zza(String str, Status status) {
        if (TextUtils.isEmpty(status.getStatusMessage())) {
            return str;
        }
        return String.format(String.valueOf(str).concat(" [ %s ]"), new Object[]{status.getStatusMessage()});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0063, code lost:
        return new com.google.firebase.auth.FirebaseAuthInvalidCredentialsException(zzgm(r0), r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d3, code lost:
        return new com.google.firebase.auth.FirebaseAuthUserCollisionException(zzgm(r0), r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00e7, code lost:
        return new com.google.firebase.auth.FirebaseAuthInvalidUserException(zzgm(r0), r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x00f1, code lost:
        return new com.google.firebase.auth.FirebaseAuthInvalidCredentialsException(zzgm(r0), r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.firebase.FirebaseException zzao(com.google.android.gms.common.api.Status r3) {
        /*
            int r0 = r3.getStatusCode()
            java.lang.String r1 = zzgn(r0)
            java.lang.String r1 = zza(r1, r3)
            switch(r0) {
                case 17004: goto L_0x00e8;
                case 17005: goto L_0x00de;
                case 17006: goto L_0x00d4;
                case 17007: goto L_0x00ca;
                case 17008: goto L_0x00e8;
                case 17009: goto L_0x00e8;
                case 17010: goto L_0x00be;
                case 17011: goto L_0x00de;
                case 17012: goto L_0x00ca;
                default: goto L_0x000f;
            }
        L_0x000f:
            switch(r0) {
                case 17014: goto L_0x00b4;
                case 17015: goto L_0x00a8;
                case 17016: goto L_0x009c;
                case 17017: goto L_0x00de;
                default: goto L_0x0012;
            }
        L_0x0012:
            switch(r0) {
                case 17020: goto L_0x0090;
                case 17021: goto L_0x00de;
                default: goto L_0x0015;
            }
        L_0x0015:
            switch(r0) {
                case 17024: goto L_0x00e8;
                case 17025: goto L_0x00ca;
                case 17026: goto L_0x0082;
                default: goto L_0x0018;
            }
        L_0x0018:
            switch(r0) {
                case 17028: goto L_0x0078;
                case 17029: goto L_0x006e;
                case 17030: goto L_0x006e;
                case 17031: goto L_0x0064;
                case 17032: goto L_0x0064;
                case 17033: goto L_0x0064;
                case 17034: goto L_0x005a;
                case 17035: goto L_0x005a;
                default: goto L_0x001b;
            }
        L_0x001b:
            switch(r0) {
                case 17041: goto L_0x005a;
                case 17042: goto L_0x005a;
                case 17043: goto L_0x005a;
                case 17044: goto L_0x005a;
                case 17045: goto L_0x005a;
                case 17046: goto L_0x005a;
                default: goto L_0x001e;
            }
        L_0x001e:
            switch(r0) {
                case 17051: goto L_0x005a;
                case 17052: goto L_0x0054;
                default: goto L_0x0021;
            }
        L_0x0021:
            switch(r0) {
                case 17000: goto L_0x004a;
                case 17002: goto L_0x00e8;
                case 17049: goto L_0x005a;
                case 17056: goto L_0x0044;
                case 17495: goto L_0x0038;
                case 17499: goto L_0x002c;
                default: goto L_0x0024;
            }
        L_0x0024:
            com.google.firebase.FirebaseException r3 = new com.google.firebase.FirebaseException
            java.lang.String r0 = "An internal error has occurred."
            r3.<init>(r0)
            return r3
        L_0x002c:
            java.lang.String r0 = "An internal error has occurred."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.FirebaseException r0 = new com.google.firebase.FirebaseException
            r0.<init>(r3)
            return r0
        L_0x0038:
            java.lang.String r0 = "Please sign in before trying to get a token."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.internal.api.FirebaseNoSignedInUserException r0 = new com.google.firebase.internal.api.FirebaseNoSignedInUserException
            r0.<init>(r3)
            return r0
        L_0x0044:
            com.google.firebase.FirebaseApiNotAvailableException r3 = new com.google.firebase.FirebaseApiNotAvailableException
            r3.<init>(r1)
            return r3
        L_0x004a:
            com.google.firebase.auth.FirebaseAuthInvalidCredentialsException r3 = new com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x0054:
            com.google.firebase.FirebaseTooManyRequestsException r3 = new com.google.firebase.FirebaseTooManyRequestsException
            r3.<init>(r1)
            return r3
        L_0x005a:
            com.google.firebase.auth.FirebaseAuthInvalidCredentialsException r3 = new com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x0064:
            com.google.firebase.auth.FirebaseAuthEmailException r3 = new com.google.firebase.auth.FirebaseAuthEmailException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x006e:
            com.google.firebase.auth.FirebaseAuthActionCodeException r3 = new com.google.firebase.auth.FirebaseAuthActionCodeException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x0078:
            com.google.firebase.auth.FirebaseAuthException r3 = new com.google.firebase.auth.FirebaseAuthException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x0082:
            com.google.firebase.auth.FirebaseAuthWeakPasswordException r2 = new com.google.firebase.auth.FirebaseAuthWeakPasswordException
            java.lang.String r0 = zzgm(r0)
            java.lang.String r3 = r3.getStatusMessage()
            r2.<init>(r0, r1, r3)
            return r2
        L_0x0090:
            java.lang.String r0 = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.FirebaseNetworkException r0 = new com.google.firebase.FirebaseNetworkException
            r0.<init>(r3)
            return r0
        L_0x009c:
            java.lang.String r0 = "User was not linked to an account with the given provider."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.FirebaseException r0 = new com.google.firebase.FirebaseException
            r0.<init>(r3)
            return r0
        L_0x00a8:
            java.lang.String r0 = "User has already been linked to the given provider."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.FirebaseException r0 = new com.google.firebase.FirebaseException
            r0.<init>(r3)
            return r0
        L_0x00b4:
            com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException r3 = new com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x00be:
            java.lang.String r0 = "We have blocked all requests from this device due to unusual activity. Try again later."
            java.lang.String r3 = zza(r0, r3)
            com.google.firebase.FirebaseTooManyRequestsException r0 = new com.google.firebase.FirebaseTooManyRequestsException
            r0.<init>(r3)
            return r0
        L_0x00ca:
            com.google.firebase.auth.FirebaseAuthUserCollisionException r3 = new com.google.firebase.auth.FirebaseAuthUserCollisionException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x00d4:
            com.google.firebase.auth.FirebaseAuthException r3 = new com.google.firebase.auth.FirebaseAuthException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x00de:
            com.google.firebase.auth.FirebaseAuthInvalidUserException r3 = new com.google.firebase.auth.FirebaseAuthInvalidUserException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        L_0x00e8:
            com.google.firebase.auth.FirebaseAuthInvalidCredentialsException r3 = new com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
            java.lang.String r0 = zzgm(r0)
            r3.<init>(r0, r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdxm.zzao(com.google.android.gms.common.api.Status):com.google.firebase.FirebaseException");
    }

    public static FirebaseAuthUserCollisionException zzb(Status status, @NonNull PhoneAuthCredential phoneAuthCredential) {
        int statusCode = status.getStatusCode();
        return new FirebaseAuthUserCollisionException(zzgm(statusCode), zza(zzgn(statusCode), status), phoneAuthCredential);
    }

    private static String zzgm(int i) {
        Pair pair = (Pair) zzmfa.get(i);
        return pair != null ? (String) pair.first : "INTERNAL_ERROR";
    }

    private static String zzgn(int i) {
        Pair pair = (Pair) zzmfa.get(i);
        return pair != null ? (String) pair.second : "An internal error happened";
    }
}
