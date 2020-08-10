package com.android.vending.billing;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IMyAidlInterface extends IInterface {

    public static abstract class Stub extends Binder implements IMyAidlInterface {
        private static final String DESCRIPTOR = "com.android.vending.billing.IMyAidlInterface";
        static final int TRANSACTION_consumePurchase = 5;
        static final int TRANSACTION_getBuyIntent = 3;
        static final int TRANSACTION_getBuyIntentExtraParams = 8;
        static final int TRANSACTION_getBuyIntentToReplaceSkus = 7;
        static final int TRANSACTION_getPurchaseHistory = 9;
        static final int TRANSACTION_getPurchases = 4;
        static final int TRANSACTION_getSkuDetails = 2;
        static final int TRANSACTION_isBillingSupported = 1;
        static final int TRANSACTION_isBillingSupportedExtraParams = 10;
        static final int TRANSACTION_stub = 6;

        private static class Proxy implements IMyAidlInterface {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public int isBillingSupported(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getSkuDetails(int apiVersion, String packageName, String type, Bundle skusBundle) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if (skusBundle != null) {
                        _data.writeInt(1);
                        skusBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getBuyIntent(int apiVersion, String packageName, String sku, String type, String developerPayload) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getPurchases(int apiVersion, String packageName, String type, String continuationToken) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int consumePurchase(int apiVersion, String packageName, String purchaseToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(purchaseToken);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int stub(int apiVersion, String packageName, String type) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getBuyIntentToReplaceSkus(int apiVersion, String packageName, List<String> oldSkus, String newSku, String type, String developerPayload) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeStringList(oldSkus);
                    _data.writeString(newSku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getBuyIntentExtraParams(int apiVersion, String packageName, String sku, String type, String developerPayload, Bundle extraParams) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(sku);
                    _data.writeString(type);
                    _data.writeString(developerPayload);
                    if (extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getPurchaseHistory(int apiVersion, String packageName, String type, String continuationToken, Bundle extraParams) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    _data.writeString(continuationToken);
                    if (extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int isBillingSupportedExtraParams(int apiVersion, String packageName, String type, Bundle extraParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(apiVersion);
                    _data.writeString(packageName);
                    _data.writeString(type);
                    if (extraParams != null) {
                        _data.writeInt(1);
                        extraParams.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMyAidlInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMyAidlInterface)) {
                return new Proxy(obj);
            }
            return (IMyAidlInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            if (i != 1598968902) {
                Bundle _arg3 = null;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _result = isBillingSupported(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg0 = data.readInt();
                        String _arg1 = data.readString();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        Bundle _result2 = getSkuDetails(_arg0, _arg1, _arg2, _arg3);
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle _result3 = getBuyIntent(data.readInt(), data.readString(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle _result4 = getPurchases(data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _result5 = consumePurchase(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _result6 = stub(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 7:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle _result7 = getBuyIntentToReplaceSkus(data.readInt(), data.readString(), data.createStringArrayList(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg02 = data.readInt();
                        String _arg12 = data.readString();
                        String _arg22 = data.readString();
                        String _arg32 = data.readString();
                        String _arg4 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        Bundle _result8 = getBuyIntentExtraParams(_arg02, _arg12, _arg22, _arg32, _arg4, _arg3);
                        reply.writeNoException();
                        if (_result8 != null) {
                            parcel2.writeInt(1);
                            _result8.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 9:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg03 = data.readInt();
                        String _arg13 = data.readString();
                        String _arg23 = data.readString();
                        String _arg33 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        Bundle _result9 = getPurchaseHistory(_arg03, _arg13, _arg23, _arg33, _arg3);
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(DESCRIPTOR);
                        int _arg04 = data.readInt();
                        String _arg14 = data.readString();
                        String _arg24 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        }
                        int _result10 = isBillingSupportedExtraParams(_arg04, _arg14, _arg24, _arg3);
                        reply.writeNoException();
                        parcel2.writeInt(_result10);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }
    }

    int consumePurchase(int i, String str, String str2) throws RemoteException;

    Bundle getBuyIntent(int i, String str, String str2, String str3, String str4) throws RemoteException;

    Bundle getBuyIntentExtraParams(int i, String str, String str2, String str3, String str4, Bundle bundle) throws RemoteException;

    Bundle getBuyIntentToReplaceSkus(int i, String str, List<String> list, String str2, String str3, String str4) throws RemoteException;

    Bundle getPurchaseHistory(int i, String str, String str2, String str3, Bundle bundle) throws RemoteException;

    Bundle getPurchases(int i, String str, String str2, String str3) throws RemoteException;

    Bundle getSkuDetails(int i, String str, String str2, Bundle bundle) throws RemoteException;

    int isBillingSupported(int i, String str, String str2) throws RemoteException;

    int isBillingSupportedExtraParams(int i, String str, String str2, Bundle bundle) throws RemoteException;

    int stub(int i, String str, String str2) throws RemoteException;
}
