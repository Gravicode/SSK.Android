package com.google.android.gms.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.zzbq;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.internal.zza;
import com.google.firebase.auth.internal.zzb;
import com.google.firebase.auth.internal.zzh;

final class zzdwl extends zzdxx<AuthResult, zza> {
    @NonNull
    private final zzdyy zzmeq;

    public zzdwl(@NonNull AuthCredential authCredential) {
        super(2);
        zzbq.checkNotNull(authCredential, "credential cannot be null");
        this.zzmeq = zzb.zza(authCredential);
    }

    public final void dispatch() throws RemoteException {
        this.zzmfg.zza(this.zzmff.zzbrg(), this.zzmeq, (zzdxn) this.zzmfe);
    }

    public final void zzbrl() {
        zzh zzb = 
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0004: INVOKE  (r0v1 'zzb' com.google.firebase.auth.internal.zzh) = (wrap: com.google.firebase.FirebaseApp
              0x0000: IGET  (r0v0 com.google.firebase.FirebaseApp) = (r3v0 'this' com.google.android.gms.internal.zzdwl A[THIS]) com.google.android.gms.internal.zzdwl.zzmcx com.google.firebase.FirebaseApp), (wrap: com.google.android.gms.internal.zzdyk
              0x0002: IGET  (r1v0 com.google.android.gms.internal.zzdyk) = (r3v0 'this' com.google.android.gms.internal.zzdwl A[THIS]) com.google.android.gms.internal.zzdwl.zzmfp com.google.android.gms.internal.zzdyk) com.google.android.gms.internal.zzdwc.zzb(com.google.firebase.FirebaseApp, com.google.android.gms.internal.zzdyk):com.google.firebase.auth.internal.zzh type: STATIC in method: com.google.android.gms.internal.zzdwl.zzbrl():void, dex: classes3.dex
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
            Caused by: java.util.ConcurrentModificationException
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1714)
            	at java.base/java.util.ArrayList.removeIf(ArrayList.java:1689)
            	at jadx.core.dex.instructions.args.SSAVar.removeUse(SSAVar.java:86)
            	at jadx.core.dex.instructions.args.SSAVar.use(SSAVar.java:79)
            	at jadx.core.dex.nodes.InsnNode.attachArg(InsnNode.java:87)
            	at jadx.core.dex.nodes.InsnNode.addArg(InsnNode.java:73)
            	at jadx.core.dex.nodes.InsnNode.copyCommonParams(InsnNode.java:335)
            	at jadx.core.dex.instructions.InvokeNode.copy(InvokeNode.java:56)
            	at jadx.core.dex.nodes.InsnNode.copyCommonParams(InsnNode.java:333)
            	at jadx.core.dex.nodes.InsnNode.copy(InsnNode.java:350)
            	at jadx.core.codegen.InsnGen.inlineMethod(InsnGen.java:880)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:669)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:357)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:239)
            	... 19 more
            */
        /*
            this = this;
            com.google.firebase.FirebaseApp r0 = r3.zzmcx
            com.google.android.gms.internal.zzdyk r1 = r3.zzmfp
            com.google.firebase.auth.internal.zzh r0 = com.google.android.gms.internal.zzdwc.zza(r0, r1)
            java.lang.Object r1 = r3.zzmfh
            com.google.firebase.auth.internal.zza r1 = (com.google.firebase.auth.internal.zza) r1
            com.google.android.gms.internal.zzdym r2 = r3.zzmfo
            r1.zza(r2, r0)
            com.google.firebase.auth.internal.zze r1 = new com.google.firebase.auth.internal.zze
            r1.<init>(r0)
            r3.zzbd(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.zzdwl.zzbrl():void");
    }
}
