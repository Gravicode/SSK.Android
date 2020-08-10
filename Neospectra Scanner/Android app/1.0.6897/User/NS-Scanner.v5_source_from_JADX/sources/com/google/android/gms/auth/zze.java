package com.google.android.gms.auth;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import java.io.IOException;

final class zze implements zzj<TokenData> {
    private /* synthetic */ Bundle val$options;
    private /* synthetic */ Account zzecd;
    private /* synthetic */ String zzece;

    zze(Account account, String str, Bundle bundle) {
        this.zzecd = account;
        this.zzece = str;
        this.val$options = bundle;
    }

    public final /* synthetic */ Object zzac(IBinder iBinder) throws RemoteException, IOException, GoogleAuthException {
        Bundle bundle = (Bundle) 
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0012: CHECK_CAST  (r9v4 'bundle' android.os.Bundle) = (android.os.Bundle) (wrap: java.lang.Object
              0x000e: INVOKE  (r9v3 java.lang.Object) = (wrap: android.os.Bundle
              0x000a: INVOKE  (r9v2 android.os.Bundle) = (wrap: com.google.android.gms.internal.zzex
              0x0000: INVOKE  (r9v1 com.google.android.gms.internal.zzex) = (r9v0 'iBinder' android.os.IBinder) com.google.android.gms.internal.zzey.zza(android.os.IBinder):com.google.android.gms.internal.zzex type: STATIC), (wrap: android.accounts.Account
              0x0004: IGET  (r0v0 android.accounts.Account) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.zzecd android.accounts.Account), (wrap: java.lang.String
              0x0006: IGET  (r1v0 java.lang.String) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.zzece java.lang.String), (wrap: android.os.Bundle
              0x0008: IGET  (r2v0 android.os.Bundle) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.val$options android.os.Bundle) com.google.android.gms.internal.zzex.zza(android.accounts.Account, java.lang.String, android.os.Bundle):android.os.Bundle type: INTERFACE) com.google.android.gms.auth.zzd.zzq(java.lang.Object):java.lang.Object type: STATIC) in method: com.google.android.gms.auth.zze.zzac(android.os.IBinder):java.lang.Object, dex: classes3.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:213)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
            	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:210)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:203)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:316)
            	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:262)
            	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:225)
            	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:110)
            	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:76)
            	at jadx.core.codegen.CodeGen.wrapCodeGen(CodeGen.java:44)
            	at jadx.core.codegen.CodeGen.generateJavaCode(CodeGen.java:32)
            	at jadx.core.codegen.CodeGen.generate(CodeGen.java:20)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
            	at jadx.api.JavaClass.decompile(JavaClass.java:62)
            	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
            Caused by: jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: INVOKE  (r9v3 java.lang.Object) = (wrap: android.os.Bundle
              0x000a: INVOKE  (r9v2 android.os.Bundle) = (wrap: com.google.android.gms.internal.zzex
              0x0000: INVOKE  (r9v1 com.google.android.gms.internal.zzex) = (r9v0 'iBinder' android.os.IBinder) com.google.android.gms.internal.zzey.zza(android.os.IBinder):com.google.android.gms.internal.zzex type: STATIC), (wrap: android.accounts.Account
              0x0004: IGET  (r0v0 android.accounts.Account) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.zzecd android.accounts.Account), (wrap: java.lang.String
              0x0006: IGET  (r1v0 java.lang.String) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.zzece java.lang.String), (wrap: android.os.Bundle
              0x0008: IGET  (r2v0 android.os.Bundle) = (r8v0 'this' com.google.android.gms.auth.zze A[THIS]) com.google.android.gms.auth.zze.val$options android.os.Bundle) com.google.android.gms.internal.zzex.zza(android.accounts.Account, java.lang.String, android.os.Bundle):android.os.Bundle type: INTERFACE) com.google.android.gms.auth.zzd.zzq(java.lang.Object):java.lang.Object type: STATIC in method: com.google.android.gms.auth.zze.zzac(android.os.IBinder):java.lang.Object, dex: classes3.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:245)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:105)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:280)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:239)
            	... 19 more
            Caused by: java.util.ConcurrentModificationException
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1714)
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1689)
            	at jadx.core.dex.instructions.args.SSAVar.removeUse(SSAVar.java:86)
            	at jadx.core.utils.InsnRemover.unbindArgUsage(InsnRemover.java:90)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:130)
            	at jadx.core.dex.nodes.InsnNode.replaceArg(InsnNode.java:134)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:892)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:669)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:223)
            	... 22 more
            */
        /*
            this = this;
            com.google.android.gms.internal.zzex r9 = com.google.android.gms.internal.zzey.zza(r9)
            android.accounts.Account r0 = r8.zzecd
            java.lang.String r1 = r8.zzece
            android.os.Bundle r2 = r8.val$options
            android.os.Bundle r9 = r9.zza(r0, r1, r2)
            java.lang.Object r9 = com.google.android.gms.auth.zzd.zzp(r9)
            android.os.Bundle r9 = (android.os.Bundle) r9
            java.lang.String r0 = "tokenDetails"
            com.google.android.gms.auth.TokenData r0 = com.google.android.gms.auth.TokenData.zzd(r9, r0)
            if (r0 == 0) goto L_0x001d
            return r0
        L_0x001d:
            java.lang.String r0 = "Error"
            java.lang.String r0 = r9.getString(r0)
            java.lang.String r1 = "userRecoveryIntent"
            android.os.Parcelable r9 = r9.getParcelable(r1)
            android.content.Intent r9 = (android.content.Intent) r9
            com.google.android.gms.internal.zzaxb r1 = com.google.android.gms.internal.zzaxb.zzfb(r0)
            boolean r2 = com.google.android.gms.internal.zzaxb.zza(r1)
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x0069
            com.google.android.gms.internal.zzbgg r2 = com.google.android.gms.auth.zzd.zzecc
            java.lang.String r5 = "GoogleAuthUtil"
            java.lang.Object[] r4 = new java.lang.Object[r4]
            java.lang.String r1 = java.lang.String.valueOf(r1)
            java.lang.String r6 = java.lang.String.valueOf(r1)
            int r6 = r6.length()
            int r6 = r6 + 31
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>(r6)
            java.lang.String r6 = "isUserRecoverableError status: "
            r7.append(r6)
            r7.append(r1)
            java.lang.String r1 = r7.toString()
            r4[r3] = r1
            r2.zzf(r5, r4)
            com.google.android.gms.auth.UserRecoverableAuthException r1 = new com.google.android.gms.auth.UserRecoverableAuthException
            r1.<init>(r0, r9)
            throw r1
        L_0x0069:
            com.google.android.gms.internal.zzaxb r9 = com.google.android.gms.internal.zzaxb.NETWORK_ERROR
            boolean r9 = r9.equals(r1)
            if (r9 != 0) goto L_0x0079
            com.google.android.gms.internal.zzaxb r9 = com.google.android.gms.internal.zzaxb.SERVICE_UNAVAILABLE
            boolean r9 = r9.equals(r1)
            if (r9 == 0) goto L_0x007a
        L_0x0079:
            r3 = 1
        L_0x007a:
            if (r3 == 0) goto L_0x0082
            java.io.IOException r9 = new java.io.IOException
            r9.<init>(r0)
            throw r9
        L_0x0082:
            com.google.android.gms.auth.GoogleAuthException r9 = new com.google.android.gms.auth.GoogleAuthException
            r9.<init>(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.zze.zzac(android.os.IBinder):java.lang.Object");
    }
}
