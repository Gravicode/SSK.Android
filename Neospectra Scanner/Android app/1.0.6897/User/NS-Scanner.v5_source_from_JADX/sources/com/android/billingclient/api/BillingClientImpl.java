package com.android.billingclient.api;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.android.billingclient.api.BillingClient.FeatureType;
import com.android.billingclient.api.BillingClient.SkuType;
import com.android.billingclient.api.Purchase.PurchasesResult;
import com.android.billingclient.util.BillingHelper;
import com.android.vending.billing.IInAppBillingService;
import com.android.vending.billing.IInAppBillingService.Stub;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

class BillingClientImpl extends BillingClient {
    private static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    private static final String KEY_VR = "vr";
    private static final String LIBRARY_VERSION = "1.0";
    private static final String LIBRARY_VERSION_KEY = "libraryVersion";
    private static final int MAX_SKU_DETAILS_ITEMS_PER_REQUEST = 20;
    private static final String TAG = "BillingClient";
    /* access modifiers changed from: private */
    public final Context mApplicationContext;
    /* access modifiers changed from: private */
    public final BillingBroadcastManager mBroadcastManager;
    /* access modifiers changed from: private */
    public int mClientState = 0;
    private ExecutorService mExecutorService;
    /* access modifiers changed from: private */
    public boolean mIABv6Supported;
    /* access modifiers changed from: private */
    public IInAppBillingService mService;
    private ServiceConnection mServiceConnection;
    /* access modifiers changed from: private */
    public boolean mSubscriptionUpdateSupported;
    /* access modifiers changed from: private */
    public boolean mSubscriptionsSupported;
    private final Handler mUiThreadHandler = new Handler();
    private final BroadcastReceiver onPurchaseFinishedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            PurchasesUpdatedListener purchasesUpdatedListener = BillingClientImpl.this.mBroadcastManager.getListener();
            if (purchasesUpdatedListener == null) {
                BillingHelper.logWarn(BillingClientImpl.TAG, "PurchasesUpdatedListener is null - no way to return the response.");
            } else {
                purchasesUpdatedListener.onPurchasesUpdated(intent.getIntExtra("response_code_key", 6), BillingHelper.extractPurchases(intent.getBundleExtra("response_bundle_key")));
            }
        }
    };

    private final class BillingServiceConnection implements ServiceConnection {
        private final BillingClientStateListener mListener;

        private BillingServiceConnection(@NonNull BillingClientStateListener listener) {
            if (listener == null) {
                throw new RuntimeException("Please specify a listener to know when init is done.");
            }
            this.mListener = listener;
        }

        public void onServiceDisconnected(ComponentName name) {
            BillingHelper.logWarn(BillingClientImpl.TAG, "Billing service disconnected.");
            BillingClientImpl.this.mService = null;
            BillingClientImpl.this.mClientState = 0;
            this.mListener.onBillingServiceDisconnected();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            BillingHelper.logVerbose(BillingClientImpl.TAG, "Billing service connected.");
            BillingClientImpl.this.mService = Stub.asInterface(service);
            String packageName = BillingClientImpl.this.mApplicationContext.getPackageName();
            BillingClientImpl.this.mSubscriptionsSupported = false;
            BillingClientImpl.this.mSubscriptionUpdateSupported = false;
            BillingClientImpl.this.mIABv6Supported = false;
            try {
                int response = BillingClientImpl.this.mService.isBillingSupported(6, packageName, SkuType.SUBS);
                if (response == 0) {
                    BillingHelper.logVerbose(BillingClientImpl.TAG, "In-app billing API version 6 with subs is supported.");
                    BillingClientImpl.this.mIABv6Supported = true;
                    BillingClientImpl.this.mSubscriptionsSupported = true;
                    BillingClientImpl.this.mSubscriptionUpdateSupported = true;
                } else {
                    if (BillingClientImpl.this.mService.isBillingSupported(6, packageName, SkuType.INAPP) == 0) {
                        BillingHelper.logVerbose(BillingClientImpl.TAG, "In-app billing API without subs version 6 supported.");
                        BillingClientImpl.this.mIABv6Supported = true;
                    }
                    response = BillingClientImpl.this.mService.isBillingSupported(5, packageName, SkuType.SUBS);
                    if (response == 0) {
                        BillingHelper.logVerbose(BillingClientImpl.TAG, "In-app billing API version 5 supported.");
                        BillingClientImpl.this.mSubscriptionUpdateSupported = true;
                        BillingClientImpl.this.mSubscriptionsSupported = true;
                    } else {
                        response = BillingClientImpl.this.mService.isBillingSupported(3, packageName, SkuType.SUBS);
                        if (response == 0) {
                            BillingHelper.logVerbose(BillingClientImpl.TAG, "In-app billing API version 3 with subscriptions is supported.");
                            BillingClientImpl.this.mSubscriptionsSupported = true;
                        } else if (BillingClientImpl.this.mIABv6Supported) {
                            response = 0;
                        } else {
                            response = BillingClientImpl.this.mService.isBillingSupported(3, packageName, SkuType.INAPP);
                            if (response == 0) {
                                BillingHelper.logVerbose(BillingClientImpl.TAG, "In-app billing API version 3 with in-app items is supported.");
                            } else {
                                BillingHelper.logWarn(BillingClientImpl.TAG, "Even billing API version 3 is not supported on this device.");
                            }
                        }
                    }
                }
                if (response == 0) {
                    BillingClientImpl.this.mClientState = 2;
                } else {
                    BillingClientImpl.this.mClientState = 0;
                    BillingClientImpl.this.mService = null;
                }
                this.mListener.onBillingSetupFinished(response);
            } catch (RemoteException e) {
                String str = BillingClientImpl.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("RemoteException while setting up in-app billing");
                sb.append(e);
                BillingHelper.logWarn(str, sb.toString());
                BillingClientImpl.this.mClientState = 0;
                BillingClientImpl.this.mService = null;
                this.mListener.onBillingSetupFinished(-1);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ClientState {
        public static final int CLOSED = 3;
        public static final int CONNECTED = 2;
        public static final int CONNECTING = 1;
        public static final int DISCONNECTED = 0;
    }

    @UiThread
    BillingClientImpl(@NonNull Context context, @NonNull PurchasesUpdatedListener listener) {
        this.mApplicationContext = context.getApplicationContext();
        this.mBroadcastManager = new BillingBroadcastManager(this.mApplicationContext, listener);
    }

    public int isFeatureSupported(String feature) {
        char c = 65535;
        if (!isReady()) {
            return -1;
        }
        int hashCode = feature.hashCode();
        if (hashCode != -422092961) {
            if (hashCode != 292218239) {
                if (hashCode != 1219490065) {
                    if (hashCode == 1987365622 && feature.equals(FeatureType.SUBSCRIPTIONS)) {
                        c = 0;
                    }
                } else if (feature.equals(FeatureType.SUBSCRIPTIONS_ON_VR)) {
                    c = 3;
                }
            } else if (feature.equals(FeatureType.IN_APP_ITEMS_ON_VR)) {
                c = 2;
            }
        } else if (feature.equals(FeatureType.SUBSCRIPTIONS_UPDATE)) {
            c = 1;
        }
        int i = -2;
        switch (c) {
            case 0:
                if (this.mSubscriptionsSupported) {
                    i = 0;
                }
                return i;
            case 1:
                if (this.mSubscriptionUpdateSupported) {
                    i = 0;
                }
                return i;
            case 2:
                return isBillingSupportedOnVr(SkuType.INAPP);
            case 3:
                return isBillingSupportedOnVr(SkuType.SUBS);
            default:
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unsupported feature: ");
                sb.append(feature);
                BillingHelper.logWarn(str, sb.toString());
                return 5;
        }
    }

    public boolean isReady() {
        return (this.mClientState != 2 || this.mService == null || this.mServiceConnection == null) ? false : true;
    }

    public void startConnection(@NonNull BillingClientStateListener listener) {
        if (isReady()) {
            BillingHelper.logVerbose(TAG, "Service connection is valid. No need to re-initialize.");
            listener.onBillingSetupFinished(0);
        } else if (this.mClientState == 1) {
            BillingHelper.logWarn(TAG, "Client is already in the process of connecting to billing service.");
            listener.onBillingSetupFinished(5);
        } else if (this.mClientState == 3) {
            BillingHelper.logWarn(TAG, "Client was already closed and can't be reused. Please create another instance.");
            listener.onBillingSetupFinished(5);
        } else {
            this.mClientState = 1;
            this.mBroadcastManager.registerReceiver();
            LocalBroadcastManager.getInstance(this.mApplicationContext).registerReceiver(this.onPurchaseFinishedReceiver, new IntentFilter("proxy_activity_response_intent_action"));
            BillingHelper.logVerbose(TAG, "Starting in-app billing setup.");
            this.mServiceConnection = new BillingServiceConnection(listener);
            Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            List<ResolveInfo> intentServices = this.mApplicationContext.getPackageManager().queryIntentServices(serviceIntent, 0);
            if (intentServices != null && !intentServices.isEmpty()) {
                ResolveInfo resolveInfo = (ResolveInfo) intentServices.get(0);
                if (resolveInfo.serviceInfo != null) {
                    String packageName = resolveInfo.serviceInfo.packageName;
                    String className = resolveInfo.serviceInfo.name;
                    if (!"com.android.vending".equals(packageName) || className == null) {
                        BillingHelper.logWarn(TAG, "The device doesn't have valid Play Store.");
                    } else {
                        ComponentName component = new ComponentName(packageName, className);
                        Intent explicitServiceIntent = new Intent(serviceIntent);
                        explicitServiceIntent.setComponent(component);
                        explicitServiceIntent.putExtra(LIBRARY_VERSION_KEY, "1.0");
                        if (this.mApplicationContext.bindService(explicitServiceIntent, this.mServiceConnection, 1)) {
                            BillingHelper.logVerbose(TAG, "Service was bonded successfully.");
                            return;
                        }
                        BillingHelper.logWarn(TAG, "Connection to Billing service is blocked.");
                    }
                }
            }
            this.mClientState = 0;
            BillingHelper.logVerbose(TAG, "Billing service unavailable on device.");
            listener.onBillingSetupFinished(3);
        }
    }

    public void endConnection() {
        LocalBroadcastManager.getInstance(this.mApplicationContext).unregisterReceiver(this.onPurchaseFinishedReceiver);
        this.mBroadcastManager.destroy();
        this.mClientState = 3;
        if (this.mServiceConnection != null) {
            BillingHelper.logVerbose(TAG, "Unbinding from service.");
            this.mApplicationContext.unbindService(this.mServiceConnection);
            this.mServiceConnection = null;
        }
        this.mService = null;
        if (this.mExecutorService != null) {
            this.mExecutorService.shutdownNow();
            this.mExecutorService = null;
        }
    }

    public int launchBillingFlow(Activity activity, BillingFlowParams params) {
        Bundle buyIntentBundle;
        if (!isReady()) {
            return -1;
        }
        String skuType = params.getSkuType();
        String newSku = params.getSku();
        if (newSku == null) {
            BillingHelper.logWarn(TAG, "Please fix the input params. SKU can't be null.");
            return 5;
        } else if (skuType == null) {
            BillingHelper.logWarn(TAG, "Please fix the input params. SkuType can't be null.");
            return 5;
        } else {
            boolean z = true;
            if (params.getOldSkus() != null && params.getOldSkus().size() < 1) {
                BillingHelper.logWarn(TAG, "Please fix the input params. OldSkus size can't be 0.");
                return 5;
            } else if (!skuType.equals(SkuType.SUBS) || this.mSubscriptionsSupported) {
                if (params.getOldSkus() == null) {
                    z = false;
                }
                boolean isSubscriptionUpdate = z;
                if (isSubscriptionUpdate && !this.mSubscriptionUpdateSupported) {
                    BillingHelper.logWarn(TAG, "Current client doesn't support subscriptions update.");
                    return -2;
                } else if (!params.hasExtraParams() || this.mIABv6Supported) {
                    String str = TAG;
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Constructing buy intent for ");
                        sb.append(newSku);
                        sb.append(", item type: ");
                        sb.append(skuType);
                        BillingHelper.logVerbose(str, sb.toString());
                        if (this.mIABv6Supported) {
                            Bundle extraParams = constructExtraParams(params);
                            extraParams.putString(LIBRARY_VERSION_KEY, "1.0");
                            buyIntentBundle = this.mService.getBuyIntentExtraParams(params.getVrPurchaseFlow() ? 7 : 6, this.mApplicationContext.getPackageName(), newSku, skuType, null, extraParams);
                        } else if (isSubscriptionUpdate) {
                            buyIntentBundle = this.mService.getBuyIntentToReplaceSkus(5, this.mApplicationContext.getPackageName(), params.getOldSkus(), newSku, SkuType.SUBS, null);
                        } else {
                            buyIntentBundle = this.mService.getBuyIntent(3, this.mApplicationContext.getPackageName(), newSku, skuType, null);
                        }
                        int responseCode = BillingHelper.getResponseCodeFromBundle(buyIntentBundle, TAG);
                        if (responseCode != 0) {
                            String str2 = TAG;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unable to buy item, Error response code: ");
                            sb2.append(responseCode);
                            BillingHelper.logWarn(str2, sb2.toString());
                            return responseCode;
                        }
                        Intent intent = new Intent(activity, ProxyBillingActivity.class);
                        intent.putExtra(BillingHelper.RESPONSE_BUY_INTENT, buyIntentBundle.getParcelable(BillingHelper.RESPONSE_BUY_INTENT));
                        activity.startActivity(intent);
                        return 0;
                    } catch (RemoteException e) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("RemoteException while launching launching replace subscriptions flow: ; for sku: ");
                        sb3.append(newSku);
                        sb3.append("; try to reconnect");
                        BillingHelper.logWarn(TAG, sb3.toString());
                        return -1;
                    }
                } else {
                    BillingHelper.logWarn(TAG, "Current client doesn't support extra params for buy intent.");
                    return -2;
                }
            } else {
                BillingHelper.logWarn(TAG, "Current client doesn't support subscriptions.");
                return -2;
            }
        }
    }

    public PurchasesResult queryPurchases(String skuType) {
        if (!isReady()) {
            return new PurchasesResult(-1, null);
        }
        if (!TextUtils.isEmpty(skuType)) {
            return queryPurchasesInternal(skuType, false);
        }
        BillingHelper.logWarn(TAG, "Please provide a valid SKU type.");
        return new PurchasesResult(5, null);
    }

    public void querySkuDetailsAsync(SkuDetailsParams params, final SkuDetailsResponseListener listener) {
        if (!isReady()) {
            listener.onSkuDetailsResponse(-1, null);
        }
        final String skuType = params.getSkuType();
        final List<String> skusList = params.getSkusList();
        if (TextUtils.isEmpty(skuType)) {
            BillingHelper.logWarn(TAG, "Please fix the input params. SKU type can't be empty.");
            listener.onSkuDetailsResponse(5, null);
        } else if (skusList == null) {
            BillingHelper.logWarn(TAG, "Please fix the input params. The list of SKUs can't be empty.");
            listener.onSkuDetailsResponse(5, null);
        } else {
            executeAsync(new Runnable() {
                public void run() {
                    final SkuDetailsResult result = BillingClientImpl.this.querySkuDetailsInternal(skuType, skusList);
                    BillingClientImpl.this.postToUiThread(new Runnable() {
                        public void run() {
                            listener.onSkuDetailsResponse(result.getResponseCode(), result.getSkuDetailsList());
                        }
                    });
                }
            });
        }
    }

    public void consumeAsync(final String purchaseToken, final ConsumeResponseListener listener) {
        if (!isReady()) {
            listener.onConsumeResponse(-1, null);
        }
        if (TextUtils.isEmpty(purchaseToken)) {
            BillingHelper.logWarn(TAG, "Please provide a valid purchase token got from queryPurchases result.");
            listener.onConsumeResponse(5, purchaseToken);
            return;
        }
        executeAsync(new Runnable() {
            public void run() {
                BillingClientImpl.this.consumeInternal(purchaseToken, listener);
            }
        });
    }

    public void queryPurchaseHistoryAsync(final String skuType, final PurchaseHistoryResponseListener listener) {
        if (!isReady()) {
            listener.onPurchaseHistoryResponse(-1, null);
        }
        executeAsync(new Runnable() {
            public void run() {
                final PurchasesResult result = BillingClientImpl.this.queryPurchasesInternal(skuType, true);
                BillingClientImpl.this.postToUiThread(new Runnable() {
                    public void run() {
                        listener.onPurchaseHistoryResponse(result.getResponseCode(), result.getPurchasesList());
                    }
                });
            }
        });
    }

    private Bundle constructExtraParams(BillingFlowParams params) {
        Bundle extraParams = new Bundle();
        if (!params.getReplaceSkusProration()) {
            extraParams.putBoolean("replaceSkusProration", false);
        }
        if (params.getAccountId() != null) {
            extraParams.putString("accountId", params.getAccountId());
        }
        if (params.getVrPurchaseFlow()) {
            extraParams.putBoolean(KEY_VR, true);
        }
        if (params.getOldSkus() != null) {
            extraParams.putStringArrayList("skusToReplace", params.getOldSkus());
        }
        return extraParams;
    }

    private void executeAsync(Runnable runnable) {
        if (this.mExecutorService == null) {
            this.mExecutorService = Executors.newFixedThreadPool(BillingHelper.NUMBER_OF_CORES);
        }
        this.mExecutorService.submit(runnable);
    }

    private int isBillingSupportedOnVr(String skuType) {
        try {
            return this.mService.isBillingSupportedExtraParams(7, this.mApplicationContext.getPackageName(), skuType, generateVrBundle()) == 0 ? 0 : -2;
        } catch (RemoteException e) {
            BillingHelper.logWarn(TAG, "RemoteException while checking if billing is supported; try to reconnect");
            return -1;
        }
    }

    private Bundle generateVrBundle() {
        Bundle result = new Bundle();
        result.putBoolean(KEY_VR, true);
        return result;
    }

    /* access modifiers changed from: 0000 */
    @VisibleForTesting
    public SkuDetailsResult querySkuDetailsInternal(String skuType, List<String> skuList) {
        BillingClientImpl billingClientImpl = this;
        List<SkuDetails> resultList = new ArrayList<>();
        int startIndex = 0;
        int listSize = skuList.size();
        while (true) {
            int i = 0;
            if (startIndex < listSize) {
                int endIndex = startIndex + 20;
                if (endIndex > listSize) {
                    endIndex = listSize;
                }
                ArrayList<String> curSkuList = new ArrayList<>(skuList.subList(startIndex, endIndex));
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList(GET_SKU_DETAILS_ITEM_LIST, curSkuList);
                querySkus.putString(LIBRARY_VERSION_KEY, "1.0");
                try {
                    try {
                        Bundle skuDetails = billingClientImpl.mService.getSkuDetails(3, billingClientImpl.mApplicationContext.getPackageName(), skuType, querySkus);
                        if (skuDetails == null) {
                            BillingHelper.logWarn(TAG, "querySkuDetailsAsync got null sku details list");
                            return new SkuDetailsResult(4, null);
                        } else if (!skuDetails.containsKey(BillingHelper.RESPONSE_GET_SKU_DETAILS_LIST)) {
                            int responseCode = BillingHelper.getResponseCodeFromBundle(skuDetails, TAG);
                            if (responseCode != 0) {
                                String str = TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("getSkuDetails() failed. Response code: ");
                                sb.append(responseCode);
                                BillingHelper.logWarn(str, sb.toString());
                                return new SkuDetailsResult(responseCode, resultList);
                            }
                            BillingHelper.logWarn(TAG, "getSkuDetails() returned a bundle with neither an error nor a detail list.");
                            return new SkuDetailsResult(6, resultList);
                        } else {
                            ArrayList<String> skuDetailsJsonList = skuDetails.getStringArrayList(BillingHelper.RESPONSE_GET_SKU_DETAILS_LIST);
                            if (skuDetailsJsonList == null) {
                                BillingHelper.logWarn(TAG, "querySkuDetailsAsync got null response list");
                                return new SkuDetailsResult(4, null);
                            }
                            while (i < skuDetailsJsonList.size()) {
                                try {
                                    SkuDetails currentSkuDetails = new SkuDetails((String) skuDetailsJsonList.get(i));
                                    String str2 = TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    int listSize2 = listSize;
                                    sb2.append("Got sku details: ");
                                    sb2.append(currentSkuDetails);
                                    BillingHelper.logVerbose(str2, sb2.toString());
                                    resultList.add(currentSkuDetails);
                                    i++;
                                    listSize = listSize2;
                                } catch (JSONException e) {
                                    int i2 = listSize;
                                    JSONException jSONException = e;
                                    BillingHelper.logWarn(TAG, "Got a JSON exception trying to decode SkuDetails");
                                    return new SkuDetailsResult(6, null);
                                }
                            }
                            startIndex += 20;
                            billingClientImpl = this;
                        }
                    } catch (RemoteException e2) {
                        e = e2;
                        int i3 = listSize;
                        RemoteException e3 = e;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("querySkuDetailsAsync got a remote exception (try to reconnect): ");
                        sb3.append(e3);
                        BillingHelper.logWarn(TAG, sb3.toString());
                        return new SkuDetailsResult(-1, null);
                    }
                } catch (RemoteException e4) {
                    e = e4;
                    String str3 = skuType;
                    int i32 = listSize;
                    RemoteException e32 = e;
                    StringBuilder sb32 = new StringBuilder();
                    sb32.append("querySkuDetailsAsync got a remote exception (try to reconnect): ");
                    sb32.append(e32);
                    BillingHelper.logWarn(TAG, sb32.toString());
                    return new SkuDetailsResult(-1, null);
                }
            } else {
                String str4 = skuType;
                List<String> list = skuList;
                int i4 = listSize;
                return new SkuDetailsResult(0, resultList);
            }
        }
    }

    /* access modifiers changed from: private */
    public PurchasesResult queryPurchasesInternal(String skuType, boolean queryHistory) {
        Bundle ownedItems;
        BillingClientImpl billingClientImpl = this;
        String str = skuType;
        boolean z = queryHistory;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Querying owned items, item type: ");
        sb.append(str);
        sb.append("; history: ");
        sb.append(z);
        BillingHelper.logVerbose(str2, sb.toString());
        ArrayList arrayList = new ArrayList();
        String continueToken = null;
        while (true) {
            ArrayList arrayList2 = arrayList;
            if (z) {
                try {
                    if (!billingClientImpl.mIABv6Supported) {
                        BillingHelper.logWarn(TAG, "getPurchaseHistory is not supported on current device");
                        return new PurchasesResult(-2, null);
                    }
                    ownedItems = billingClientImpl.mService.getPurchaseHistory(6, billingClientImpl.mApplicationContext.getPackageName(), str, continueToken, null);
                } catch (RemoteException e) {
                    RemoteException e2 = e;
                    String str3 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Got exception trying to get purchases: ");
                    sb2.append(e2);
                    sb2.append("; try to reconnect");
                    BillingHelper.logWarn(str3, sb2.toString());
                    return new PurchasesResult(-1, null);
                }
            } else {
                ownedItems = billingClientImpl.mService.getPurchases(3, billingClientImpl.mApplicationContext.getPackageName(), str, continueToken);
            }
            if (ownedItems == null) {
                BillingHelper.logWarn(TAG, "queryPurchases got null owned items list");
                return new PurchasesResult(6, null);
            }
            int responseCode = BillingHelper.getResponseCodeFromBundle(ownedItems, TAG);
            if (responseCode != 0) {
                String str4 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("getPurchases() failed. Response code: ");
                sb3.append(responseCode);
                BillingHelper.logWarn(str4, sb3.toString());
                return new PurchasesResult(responseCode, null);
            } else if (!ownedItems.containsKey(BillingHelper.RESPONSE_INAPP_ITEM_LIST) || !ownedItems.containsKey(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST)) {
                int i = responseCode;
            } else if (!ownedItems.containsKey(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST)) {
                int i2 = responseCode;
                break;
            } else {
                ArrayList<String> ownedSkus = ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_ITEM_LIST);
                ArrayList<String> purchaseDataList = ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST);
                ArrayList<String> signatureList = ownedItems.getStringArrayList(BillingHelper.RESPONSE_INAPP_SIGNATURE_LIST);
                if (ownedSkus == null) {
                    BillingHelper.logWarn(TAG, "Bundle returned from getPurchases() contains null SKUs list.");
                    return new PurchasesResult(6, null);
                } else if (purchaseDataList == null) {
                    BillingHelper.logWarn(TAG, "Bundle returned from getPurchases() contains null purchases list.");
                    return new PurchasesResult(6, null);
                } else if (signatureList == null) {
                    BillingHelper.logWarn(TAG, "Bundle returned from getPurchases() contains null signatures list.");
                    return new PurchasesResult(6, null);
                } else {
                    int i3 = 0;
                    while (i3 < purchaseDataList.size()) {
                        String purchaseData = (String) purchaseDataList.get(i3);
                        String signature = (String) signatureList.get(i3);
                        String sku = (String) ownedSkus.get(i3);
                        String str5 = TAG;
                        StringBuilder sb4 = new StringBuilder();
                        int responseCode2 = responseCode;
                        sb4.append("Sku is owned: ");
                        sb4.append(sku);
                        BillingHelper.logVerbose(str5, sb4.toString());
                        try {
                            Purchase purchase = new Purchase(purchaseData, signature);
                            if (TextUtils.isEmpty(purchase.getPurchaseToken())) {
                                BillingHelper.logWarn(TAG, "BUG: empty/null token!");
                            }
                            arrayList2.add(purchase);
                            i3++;
                            responseCode = responseCode2;
                        } catch (JSONException e3) {
                            JSONException e4 = e3;
                            String str6 = TAG;
                            StringBuilder sb5 = new StringBuilder();
                            String str7 = sku;
                            sb5.append("Got an exception trying to decode the purchase: ");
                            sb5.append(e4);
                            BillingHelper.logWarn(str6, sb5.toString());
                            return new PurchasesResult(6, null);
                        }
                    }
                    continueToken = ownedItems.getString(BillingHelper.INAPP_CONTINUATION_TOKEN);
                    String str8 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Continuation token: ");
                    sb6.append(continueToken);
                    BillingHelper.logVerbose(str8, sb6.toString());
                    if (TextUtils.isEmpty(continueToken)) {
                        return new PurchasesResult(0, arrayList2);
                    }
                    arrayList = arrayList2;
                    billingClientImpl = this;
                }
            }
        }
        BillingHelper.logWarn(TAG, "Bundle returned from getPurchases() doesn't contain required fields.");
        return new PurchasesResult(6, null);
    }

    /* access modifiers changed from: private */
    public void postToUiThread(Runnable runnable) {
        this.mUiThreadHandler.post(runnable);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void consumeInternal(final String purchaseToken, final ConsumeResponseListener listener) {
        String str = TAG;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Consuming purchase with token: ");
            sb.append(purchaseToken);
            BillingHelper.logVerbose(str, sb.toString());
            final int responseCode = this.mService.consumePurchase(3, this.mApplicationContext.getPackageName(), purchaseToken);
            if (responseCode == 0) {
                BillingHelper.logVerbose(TAG, "Successfully consumed purchase.");
                if (listener != null) {
                    postToUiThread(new Runnable() {
                        public void run() {
                            listener.onConsumeResponse(responseCode, purchaseToken);
                        }
                    });
                    return;
                }
                return;
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Error consuming purchase with token. Response code: ");
            sb2.append(responseCode);
            BillingHelper.logWarn(str2, sb2.toString());
            postToUiThread(new Runnable() {
                public void run() {
                    BillingHelper.logWarn(BillingClientImpl.TAG, "Error consuming purchase.");
                    listener.onConsumeResponse(responseCode, purchaseToken);
                }
            });
        } catch (RemoteException e) {
            postToUiThread(new Runnable() {
                public void run() {
                    String str = BillingClientImpl.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Error consuming purchase; ex: ");
                    sb.append(e);
                    BillingHelper.logWarn(str, sb.toString());
                    listener.onConsumeResponse(-1, purchaseToken);
                }
            });
        }
    }
}
