package com.si_ware.neospectra.BluetoothSDK;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.p001v4.app.ActivityCompat;
import android.support.p001v4.app.NotificationCompat;
import android.support.p004v7.app.AlertDialog;
import android.util.Log;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleConnection.RxBleConnectionState;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings.Builder;
import com.polidea.rxandroidble2.utils.ConnectionSharingAdapter;
import com.si_ware.neospectra.Activities.SettingsActivity;
import com.si_ware.neospectra.Global.GlobalVariables;
import com.si_ware.neospectra.Global.MethodsFactory;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import p005io.reactivex.Observable;
import p005io.reactivex.ObservableSource;
import p005io.reactivex.android.schedulers.AndroidSchedulers;
import p005io.reactivex.disposables.Disposable;
import p005io.reactivex.subjects.PublishSubject;

public class SWS_P3ConnectionServices {
    public static final UUID BATTERY_SERVICE_UUID = UUID.fromString("E100E100-E100-E100-E100-E100E100E100");
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID DIS_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID FIRMWARE_REVISON_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final UUID MEM_RX_CHAR_UUID = UUID.fromString("C102C102-C102-C102-C102-C102C102C102");
    public static final UUID MEM_SERVICE_UUID = UUID.fromString("C100C100-C100-C100-C100-C100C100C100");
    public static final UUID MEM_TX_CHAR_UUID = UUID.fromString("C101C101-C101-C101-C101-C101C101C101");
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final UUID OTA_RX_CHAR_UUID = UUID.fromString("D102D102-D102-D102-D102-D102D102D102");
    public static final UUID OTA_SERVICE_UUID = UUID.fromString("D100D100-D100-D100-D100-D100D100D100");
    public static final UUID OTA_TX_CHAR_UUID = UUID.fromString("D101D101-D101-D101-D101-D101D101D101");
    public static final UUID P3_RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID P3_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID P3_TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID SYS_STAT_RX_CHAR_UUID = UUID.fromString("B102B102-B102-B102-B102-B102B102B102");
    public static final UUID SYS_STAT_SERVICE_UUID = UUID.fromString("B100B100-B100-B100-B100-B100B100B100");
    public static final UUID SYS_STAT_TX_CHAR_UUID = UUID.fromString("B101B101-B101-B101-B101-B101B101B101");
    private static final String TAG = "P3_Connection";
    public static final UUID TX_POWER_LEVEL_UUID = UUID.fromString("00002a07-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_POWER_UUID = UUID.fromString("00001804-0000-1000-8000-00805f9b34fb");
    @NonNull
    List<SWS_P3BLEDevice> DevicesList = new ArrayList();
    @NonNull
    List<String> DevicesMacAddress = new ArrayList();
    private Context HomeActivityContext;
    private Context MainActivityContext;
    private Activity MainActivityInstance;
    List<UUID> characteristicUUIDsList = new ArrayList();
    public ConnectionStatus connectionStatus = ConnectionStatus.ready;
    List<UUID> descriptorUUIDsList = new ArrayList();
    @NonNull
    private PublishSubject<Boolean> disconnectTriggerSubject = PublishSubject.create();
    boolean isConnectedToGatt = false;
    private ArrayList<BLEAction> listeners = new ArrayList<>();
    int mDataLength = 0;
    private boolean mHeaderMemPacketDone = false;
    boolean mHeaderPacketDone = false;
    private boolean mHeaderSysPacketDone = false;
    int mNumberOfPackets = 0;
    SWS_P3PacketResponse mPacketResponse;
    int mReceivedPacketsCounter = 0;
    private RxBleClient mRxBleClient;
    private Observable<RxBleConnection> mRxBleConnection;
    private RxBleDevice mRxBleDevice;
    private BluetoothGattCallback myGattCallback = new BluetoothGattCallback() {
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            String str = SWS_P3ConnectionServices.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Service discovered with status ");
            sb.append(status);
            Log.d(str, sb.toString());
        }

        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState != 2) {
                Log.d(SWS_P3ConnectionServices.TAG, "Gatt state: not connected");
                SWS_P3ConnectionServices.this.isConnectedToGatt = false;
                SWS_P3ConnectionServices.this.raiseonDisconnect();
                return;
            }
            Log.d(SWS_P3ConnectionServices.TAG, "Gatt state: connected");
            gatt.discoverServices();
            SWS_P3ConnectionServices.this.isConnectedToGatt = true;
            SWS_P3ConnectionServices.this.raiseonConnect();
        }

        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String str = SWS_P3ConnectionServices.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Write successful: ");
            sb.append(Arrays.toString(characteristic.getValue()));
            Log.d(str, sb.toString());
            SWS_P3ConnectionServices.this.raiseonWrite(gatt, characteristic, status);
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            String str = SWS_P3ConnectionServices.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Read successful: ");
            sb.append(Arrays.toString(characteristic.getValue()));
            Log.d(str, sb.toString());
            SWS_P3ConnectionServices.this.raiseonRead(gatt, characteristic, status);
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            String str = SWS_P3ConnectionServices.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(" - Notifiaction UUID: ");
            sb.append(characteristic.getUuid().toString());
            Log.d(str, sb.toString());
            String str2 = SWS_P3ConnectionServices.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" - Notifiaction value: ");
            sb2.append(Arrays.toString(characteristic.getValue()));
            Log.d(str2, sb2.toString());
            SWS_P3ConnectionServices.this.raiseonNotification(gatt, characteristic);
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };
    private int packetsType = 0;
    private byte[] scanBytes;
    private int scanBytesIterator;
    private Disposable scanSubscription;
    List<UUID> serviceUUIDsList = new ArrayList();
    int status;
    boolean still_fail = true;

    public interface BLEAction {
        void onConnect();

        void onDisconnect();

        void onNotification(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic);

        void onRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);

        void onWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i);
    }

    public enum ConnectionStatus {
        ready,
        findingChannel,
        failedToGetChannel,
        gotChannel,
        connecting,
        failedToConnect,
        connected,
        disconnected
    }

    public ConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus2) {
        this.connectionStatus = connectionStatus2;
    }

    public SWS_P3ConnectionServices(Activity mMainActivity, Context mMainContext) {
        this.MainActivityInstance = mMainActivity;
        this.MainActivityContext = mMainContext;
    }

    public RxBleDevice getmRxBleDevice() {
        return this.mRxBleDevice;
    }

    public void setmRxBleDevice(RxBleDevice mRxBleDevice2) {
        this.mRxBleDevice = mRxBleDevice2;
    }

    @NonNull
    public List<SWS_P3BLEDevice> ScanBTDevices() {
        this.mRxBleClient = RxBleClient.create(this.MainActivityContext);
        RxBleLog.setLogLevel(2);
        this.scanSubscription = this.mRxBleClient.scanBleDevices(new Builder().setScanMode(2).setCallbackType(1).build(), new ScanFilter[0]).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$0(this), new SWS_P3ConnectionServices$$Lambda$1(this));
        do {
        } while (this.DevicesList.size() < 1);
        return this.DevicesList;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$ScanBTDevices$0$SWS_P3ConnectionServices(ScanResult scanResult) throws Exception {
        SWS_P3BLEDevice tmp = new SWS_P3BLEDevice(scanResult.getBleDevice().getName(), scanResult.getBleDevice().getMacAddress(), scanResult.getRssi(), scanResult.getBleDevice());
        if (!this.DevicesMacAddress.contains(scanResult.getBleDevice().getMacAddress()) && scanResult.getBleDevice().getName() != null) {
            if (scanResult.getBleDevice().getName().contains("NeoSpectra") || scanResult.getBleDevice().getName().contains("NS-")) {
                String simpleName = getClass().getSimpleName();
                StringBuilder sb = new StringBuilder();
                sb.append("# Added New Element:");
                sb.append(scanResult.getBleDevice().getName());
                Log.i(simpleName, sb.toString());
                this.DevicesList.add(tmp);
                this.DevicesMacAddress.add(scanResult.getBleDevice().getMacAddress());
                SettingsActivity.found_device(this.DevicesList);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$ScanBTDevices$1$SWS_P3ConnectionServices(Throwable throwable) throws Exception {
        Log.i(getClass().getSimpleName(), "Found Problem in Scanning  ... ");
    }

    public boolean isConnecting(String from) {
        StringBuilder sb = new StringBuilder();
        sb.append("get is connecting from: ");
        sb.append(from);
        sb.append(", Value: ");
        sb.append(GlobalVariables.isConnecting);
        MethodsFactory.logMessage("bluetooth", sb.toString());
        return GlobalVariables.isConnecting;
    }

    public void setConnecting(String from, boolean connecting) {
        GlobalVariables.isConnecting = connecting;
    }

    public void ConnectToP3() {
        setConnecting("ConnectToP3()", true);
        this.mRxBleConnection = prepareConnectionObservable();
        this.mRxBleConnection.flatMapSingle(SWS_P3ConnectionServices$$Lambda$2.$instance).observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(new SWS_P3ConnectionServices$$Lambda$3(this)).subscribe(new SWS_P3ConnectionServices$$Lambda$4(this), new SWS_P3ConnectionServices$$Lambda$5(this), new SWS_P3ConnectionServices$$Lambda$6(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$ConnectToP3$2$SWS_P3ConnectionServices(Disposable disposable) throws Exception {
        Log.i(getClass().getSimpleName(), "We're Good Man!");
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$ConnectToP3$3$SWS_P3ConnectionServices(RxBleDeviceServices characteristic) throws Exception {
        Log.i(getClass().getSimpleName(), "Hey, connection has been established!");
        setConnecting("connection established!", false);
        broadcastNotificationconnected("connection established!");
    }

    public void DisconnectFromP3() {
        setConnecting("DisconnectFromP3()", false);
        this.disconnectTriggerSubject.onNext(Boolean.valueOf(true));
    }

    /* access modifiers changed from: private */
    /* renamed from: onConnectionFailure */
    public void bridge$lambda$0$SWS_P3ConnectionServices(Throwable throwable) {
        setConnecting("onConnectionFailure()", false);
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("Connection error: ");
        sb.append(throwable);
        Log.i(simpleName, sb.toString());
    }

    /* access modifiers changed from: private */
    /* renamed from: onConnectionFinished */
    public void bridge$lambda$1$SWS_P3ConnectionServices() {
        setConnecting("onConnectionFinished()", false);
    }

    public void ReadFromP3() {
        if (isConnected()) {
            this.mRxBleConnection.firstOrError().flatMap(SWS_P3ConnectionServices$$Lambda$7.$instance).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$8(this), new SWS_P3ConnectionServices$$Lambda$9(this));
        } else {
            MethodsFactory.showAlertMessage(getMainActivityContext(), "Device not connected", "Please! Ensure that you have a connected device firstly");
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onReadFailure */
    public void bridge$lambda$3$SWS_P3ConnectionServices(Throwable throwable) {
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("Read error: ");
        sb.append(throwable);
        Log.i(simpleName, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(throwable.getMessage());
        sb2.append(" onReadFailure");
        broadcastNotificationFailure(sb2.toString(), "read_failure", 0);
    }

    /* access modifiers changed from: private */
    /* renamed from: onReadSuccess */
    public void bridge$lambda$2$SWS_P3ConnectionServices(byte[] bytes) {
        Log.i(getClass().getSimpleName(), "Read Success!");
    }

    public void WriteToP3(@NonNull byte[] byteArray) {
        if (isConnected()) {
            this.mRxBleConnection.firstOrError().flatMap(new SWS_P3ConnectionServices$$Lambda$10(byteArray)).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$11(this), new SWS_P3ConnectionServices$$Lambda$12(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$WriteToP3$6$SWS_P3ConnectionServices(byte[] bytes) throws Exception {
        onWriteSuccess();
    }

    public void addListener(BLEAction toAdd) {
        this.listeners.add(toAdd);
    }

    public void removeListener(BLEAction toDel) {
        this.listeners.remove(toDel);
    }

    public void raiseonNotification(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((BLEAction) it.next()).onNotification(gatt, characteristic);
        }
    }

    public void raiseonRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status2) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((BLEAction) it.next()).onRead(gatt, characteristic, status2);
        }
    }

    public void raiseonWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status2) {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((BLEAction) it.next()).onWrite(gatt, characteristic, status2);
        }
    }

    public void raiseonDisconnect() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((BLEAction) it.next()).onDisconnect();
        }
    }

    public void raiseonConnect() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((BLEAction) it.next()).onConnect();
        }
    }

    public void writeData(byte[] data) {
        BluetoothDevice activeDevice = null;
        BluetoothGatt myGatBand = null;
        if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Iterator it = BluetoothAdapter.getDefaultAdapter().getBondedDevices().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                BluetoothDevice pairedDevice = (BluetoothDevice) it.next();
                if (pairedDevice.getName().contains(this.mRxBleDevice.getName())) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("\tDevice Name: ");
                    sb.append(pairedDevice.getName());
                    Log.d(str, sb.toString());
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("\tDevice MAC: ");
                    sb2.append(pairedDevice.getAddress());
                    Log.d(str2, sb2.toString());
                    activeDevice = pairedDevice;
                    break;
                }
            }
        }
        if (activeDevice != null) {
            Log.d(TAG, "(*) Establishing connection to gatt");
            myGatBand = activeDevice.connectGatt(this.MainActivityContext, true, this.myGattCallback);
        } else {
            Log.d(TAG, "(*) Cant Establish connection to gatt, device is null");
        }
        if (!this.isConnectedToGatt || myGatBand == null) {
            Log.d(TAG, "Cant read from BLE, not initialized.");
            return;
        }
        String str3 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("* Getting gatt service, UUID:");
        sb3.append(OTA_SERVICE_UUID);
        Log.d(str3, sb3.toString());
        BluetoothGattService myGatService = myGatBand.getService(OTA_SERVICE_UUID);
        if (myGatService != null) {
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("* Getting gatt Characteristic. UUID: ");
            sb4.append(OTA_RX_CHAR_UUID);
            Log.d(str4, sb4.toString());
            BluetoothGattCharacteristic myGatChar = myGatService.getCharacteristic(OTA_RX_CHAR_UUID);
            if (myGatChar != null) {
                Log.d(TAG, "* Writing trigger");
                myGatChar.setValue(data);
                boolean status2 = myGatBand.writeCharacteristic(myGatChar);
                String str5 = TAG;
                StringBuilder sb5 = new StringBuilder();
                sb5.append("* Writting trigger status :");
                sb5.append(status2);
                Log.d(str5, sb5.toString());
            }
        }
    }

    public void WriteToMemoryService(@NonNull byte[] byteArray) {
        if (isConnected()) {
            this.mRxBleConnection.firstOrError().flatMap(new SWS_P3ConnectionServices$$Lambda$13(byteArray)).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$14(this), new SWS_P3ConnectionServices$$Lambda$15(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$WriteToMemoryService$8$SWS_P3ConnectionServices(byte[] bytes) throws Exception {
        onWriteSuccess();
    }

    public void WriteToSystemService(@NonNull byte[] byteArray) {
        if (isConnected()) {
            this.mRxBleConnection.firstOrError().flatMap(new SWS_P3ConnectionServices$$Lambda$16(byteArray)).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$17(this), new SWS_P3ConnectionServices$$Lambda$18(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$WriteToSystemService$10$SWS_P3ConnectionServices(byte[] bytes) throws Exception {
        onWriteSuccess();
    }

    public void WriteOTAService(@NonNull byte[] byteArray) {
        if (isConnected()) {
            this.mRxBleConnection.firstOrError().flatMap(new SWS_P3ConnectionServices$$Lambda$19(byteArray)).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$20(this), new SWS_P3ConnectionServices$$Lambda$21(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$WriteOTAService$12$SWS_P3ConnectionServices(byte[] bytes) throws Exception {
        onWriteSuccess();
    }

    /* access modifiers changed from: private */
    /* renamed from: onWriteFailure */
    public void bridge$lambda$4$SWS_P3ConnectionServices(Throwable throwable) {
        String simpleName = getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("Write error: ");
        sb.append(throwable);
        Log.i(simpleName, sb.toString());
        broadcastWriteFailure(throwable.getMessage());
    }

    private void onWriteSuccess() {
        Log.i(getClass().getSimpleName(), "Write success");
        broadcastWriteSuccess();
    }

    private void broadcastWriteFailure(String msg) {
        Intent iWriteData = new Intent();
        iWriteData.setAction(GlobalVariables.INTENT_ACTION);
        iWriteData.putExtra("iName", "sensorWriting");
        iWriteData.putExtra("isWriteSuccess", false);
        iWriteData.putExtra(NotificationCompat.CATEGORY_ERROR, msg);
    }

    private void broadcastWriteSuccess() {
        Intent iWriteData = new Intent();
        iWriteData.setAction(GlobalVariables.INTENT_ACTION);
        iWriteData.putExtra("iName", "sensorWriting");
        iWriteData.putExtra("isWriteSuccess", true);
    }

    private Observable<RxBleConnection> prepareConnectionObservable() {
        return this.mRxBleDevice.establishConnection(false).takeUntil((ObservableSource<U>) this.disconnectTriggerSubject).compose(new ConnectionSharingAdapter());
    }

    public void SetNotificationOnTXInP3() {
        if (isConnected()) {
            this.mRxBleConnection.flatMap(SWS_P3ConnectionServices$$Lambda$22.$instance).doOnNext(new SWS_P3ConnectionServices$$Lambda$23(this)).doOnError(new SWS_P3ConnectionServices$$Lambda$24(this)).flatMap(SWS_P3ConnectionServices$$Lambda$25.$instance).retryWhen(SWS_P3ConnectionServices$$Lambda$26.$instance).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$27(this), new SWS_P3ConnectionServices$$Lambda$28(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnTXInP3$14$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$53(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnTXInP3$15$SWS_P3ConnectionServices(Throwable setupThrowable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$52(this));
    }

    static final /* synthetic */ ObservableSource lambda$SetNotificationOnTXInP3$16$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        return notificationObservable;
    }

    static final /* synthetic */ ObservableSource lambda$null$17$SWS_P3ConnectionServices(Throwable error) throws Exception {
        if (!(error instanceof BleDisconnectedException)) {
            return Observable.error(error);
        }
        Log.d("Retry", "Retrying");
        return Observable.just(null);
    }

    public void SetNotificationOnMemTx() {
        if (isConnected()) {
            this.mRxBleConnection.flatMap(SWS_P3ConnectionServices$$Lambda$29.$instance).doOnNext(new SWS_P3ConnectionServices$$Lambda$30(this)).doOnError(new SWS_P3ConnectionServices$$Lambda$31(this)).flatMap(SWS_P3ConnectionServices$$Lambda$32.$instance).retryWhen(SWS_P3ConnectionServices$$Lambda$33.$instance).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$34(this), new SWS_P3ConnectionServices$$Lambda$35(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnMemTx$20$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$50(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnMemTx$21$SWS_P3ConnectionServices(Throwable setupThrowable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$49(this));
    }

    static final /* synthetic */ ObservableSource lambda$SetNotificationOnMemTx$22$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        return notificationObservable;
    }

    static final /* synthetic */ ObservableSource lambda$null$23$SWS_P3ConnectionServices(Throwable error) throws Exception {
        if (!(error instanceof BleDisconnectedException)) {
            return Observable.error(error);
        }
        Log.d("Retry", "Retrying");
        return Observable.just(null);
    }

    public void SetNotificationOnBatTx() {
        if (isConnected()) {
            this.mRxBleConnection.flatMap(SWS_P3ConnectionServices$$Lambda$36.$instance).doOnNext(new SWS_P3ConnectionServices$$Lambda$37(this)).doOnError(new SWS_P3ConnectionServices$$Lambda$38(this)).flatMap(SWS_P3ConnectionServices$$Lambda$39.$instance).retryWhen(SWS_P3ConnectionServices$$Lambda$40.$instance).observeOn(AndroidSchedulers.mainThread()).subscribe(new SWS_P3ConnectionServices$$Lambda$41(this), new SWS_P3ConnectionServices$$Lambda$42(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnBatTx$26$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$47(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$SetNotificationOnBatTx$27$SWS_P3ConnectionServices(Throwable setupThrowable) throws Exception {
        this.MainActivityInstance.runOnUiThread(new SWS_P3ConnectionServices$$Lambda$46(this));
    }

    static final /* synthetic */ ObservableSource lambda$SetNotificationOnBatTx$28$SWS_P3ConnectionServices(Observable notificationObservable) throws Exception {
        return notificationObservable;
    }

    static final /* synthetic */ ObservableSource lambda$null$29$SWS_P3ConnectionServices(Throwable error) throws Exception {
        if (!(error instanceof BleDisconnectedException)) {
            return Observable.error(error);
        }
        Log.d("Retry", "Retrying");
        return Observable.just(null);
    }

    public void setHomeActivityContext(Context context) {
        this.HomeActivityContext = context;
    }

    /* access modifiers changed from: private */
    /* renamed from: onNotificationReceived */
    public void bridge$lambda$5$SWS_P3ConnectionServices(@NonNull byte[] bytes) {
        if (this.mHeaderPacketDone) {
            this.mPacketResponse.addNewResponse(bytes);
            this.mReceivedPacketsCounter++;
            String simpleName = getClass().getSimpleName();
            StringBuilder sb = new StringBuilder();
            sb.append("#--> Received packets: ");
            sb.append(this.mReceivedPacketsCounter);
            Log.i(simpleName, sb.toString());
            PrintStream printStream = System.out;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("mDataLength = ");
            sb2.append(this.mDataLength);
            sb2.append(", mNumberOfPackets = ");
            sb2.append(this.mNumberOfPackets);
            printStream.println(sb2.toString());
            if (this.mNumberOfPackets == this.mReceivedPacketsCounter) {
                this.mReceivedPacketsCounter = 0;
                Log.i(getClass().getSimpleName(), "#--> Received packets: Done !!");
                Log.i(getClass().getSimpleName(), "-------------------------------------------------------------------------------------");
                this.mPacketResponse.interpretByteStream();
                broadcastNotificationData(this.mPacketResponse.getmInterpretedPacketResponse(), this.mPacketResponse.convertByteArrayToString());
                MethodsFactory.logMessage(TAG, "A PACKET IS BROADCASTED");
                this.mHeaderPacketDone = false;
            }
        } else if (bytes[0] == 0) {
            Log.i(getClass().getSimpleName(), "%--> NO Error in Received Packet.");
            this.mDataLength = ((bytes[2] & 255) << 8) + (bytes[1] & 255);
            String simpleName2 = getClass().getSimpleName();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("%--> Length: ");
            sb3.append(this.mDataLength);
            sb3.append("  mInterpolationEnabled = ");
            sb3.append(GlobalVariables.gIsInterpolationEnabled);
            Log.i(simpleName2, sb3.toString());
            if (this.mDataLength == 1 || this.mDataLength == 2) {
                this.mNumberOfPackets = 1;
            } else if (GlobalVariables.gIsInterpolationEnabled) {
                this.mNumberOfPackets = (int) Math.ceil((((double) (this.mDataLength + 2)) * 8.0d) / 20.0d);
            } else {
                this.mNumberOfPackets = (int) Math.ceil((((double) (this.mDataLength * 2)) * 8.0d) / 20.0d);
            }
            String simpleName3 = getClass().getSimpleName();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("%--> Packets: ");
            sb4.append(this.mNumberOfPackets);
            Log.i(simpleName3, sb4.toString());
            this.mPacketResponse = new SWS_P3PacketResponse(this.mNumberOfPackets, this.mDataLength, GlobalVariables.gIsInterpolationEnabled);
            this.mPacketResponse.prepareArraySize();
            this.mHeaderPacketDone = true;
        } else {
            Log.i(getClass().getSimpleName(), "#--> Error in Received Packet.");
            broadcastNotificationFailure("#--> Error in Received Packet.", "packet_failure", bytes[0]);
        }
        String simpleName4 = getClass().getSimpleName();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Notification Received - ");
        sb5.append(String.valueOf(bytes.length));
        Log.i(simpleName4, sb5.toString());
    }

    /* access modifiers changed from: private */
    /* renamed from: onMemNotificationReceived */
    public void bridge$lambda$7$SWS_P3ConnectionServices(@NonNull byte[] bytes) {
        byte[] bArr = bytes;
        int i = 2;
        if (!this.mHeaderMemPacketDone) {
            this.status = bArr[0];
            this.mDataLength = ((bArr[1] & 255) << 0) | ((bArr[2] & 255) << 8);
            this.scanBytes = new byte[this.mDataLength];
            this.scanBytesIterator = 0;
            this.mHeaderMemPacketDone = true;
        } else if (this.mDataLength == 1 && this.status == 0) {
            broadcastHomeNotification(0, "OperationDone");
            this.mHeaderMemPacketDone = false;
            return;
        } else if (this.mDataLength != 1 || this.status == 0) {
            if (this.scanBytesIterator == 0) {
                this.packetsType = (int) (((((long) bArr[0]) & 255) << 0) | ((((long) bArr[1]) & 255) << 8) | ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[3]) & 255) << 24));
            }
            switch (this.packetsType) {
                case 0:
                    long FWVersion = ((((long) bArr[11]) & 255) << 24) | ((((long) bArr[8]) & 255) << 0) | ((((long) bArr[9]) & 255) << 8) | ((((long) bArr[10]) & 255) << 16);
                    broadcastHomeNotification(((((long) bArr[4]) & 255) << 0) | ((((long) bArr[5]) & 255) << 8) | ((((long) bArr[6]) & 255) << 16) | ((((long) bArr[7]) & 255) << 24), "Memory");
                    broadcastHomeNotification(FWVersion, "FWVersion");
                    this.mHeaderMemPacketDone = false;
                    break;
                case 1:
                    if (this.scanBytesIterator == 0) {
                        System.arraycopy(bArr, 4, this.scanBytes, this.scanBytesIterator, 16);
                        this.scanBytesIterator += 16;
                    } else if (this.scanBytesIterator + 20 > this.mDataLength - 4) {
                        System.arraycopy(bArr, 0, this.scanBytes, this.scanBytesIterator, (this.mDataLength - this.scanBytesIterator) - 4);
                        this.scanBytesIterator = this.mDataLength - 4;
                    } else {
                        System.arraycopy(bArr, 0, this.scanBytes, this.scanBytesIterator, 20);
                        this.scanBytesIterator += 20;
                    }
                    if (this.scanBytesIterator == this.mDataLength - 4) {
                        ByteBuffer buffer = ByteBuffer.wrap(this.scanBytes);
                        buffer.order(ByteOrder.LITTLE_ENDIAN);
                        double[] doubleValues = new double[(this.scanBytes.length / 8)];
                        int i2 = 0;
                        for (int i3 = 16; i2 < this.scanBytes.length / i3; i3 = 16) {
                            doubleValues[i2] = ((double) buffer.getLong(i2 * 8)) / Math.pow(2.0d, 33.0d);
                            doubleValues[(doubleValues.length / i) + i2] = ((double) buffer.getLong(((doubleValues.length / i) + i2) * 8)) / Math.pow(2.0d, 30.0d);
                            i2++;
                            i = 2;
                        }
                        this.mHeaderMemPacketDone = false;
                        broadcastNotificationMemoryData(doubleValues);
                        break;
                    }
                    break;
            }
            long Fnum = ((((long) bArr[0]) & 255) << 0) | ((((long) bArr[1]) & 255) << 8) | ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[3]) & 255) << 24);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("===========");
            sb.append(Fnum);
            sb.append("=========");
            sb.append(this.scanBytesIterator);
            sb.append("==========");
            printStream.println(sb.toString());
        } else {
            broadcastHomeNotification((long) bArr[0], "Error");
            this.mHeaderMemPacketDone = false;
            return;
        }
        String simpleName = getClass().getSimpleName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Notification Received - ");
        sb2.append(String.valueOf(bArr.length));
        Log.i(simpleName, sb2.toString());
    }

    /* access modifiers changed from: private */
    /* renamed from: onSystemNotificationReceived */
    public void bridge$lambda$8$SWS_P3ConnectionServices(@NonNull byte[] bytes) {
        byte[] bArr = bytes;
        if (!this.mHeaderSysPacketDone) {
            this.mHeaderSysPacketDone = true;
        } else {
            int i = (int) (((((long) bArr[0]) & 255) << 0) | ((((long) bArr[1]) & 255) << 8) | ((((long) bArr[2]) & 255) << 16) | ((((long) bArr[3]) & 255) << 24));
            if (i != 0) {
                switch (i) {
                    case 128:
                        broadcastHomeNotification(((255 & ((long) bArr[11])) << 56) | ((((long) bArr[4]) & 255) << 0) | ((((long) bArr[5]) & 255) << 8) | ((((long) bArr[6]) & 255) << 16) | ((((long) bArr[7]) & 255) << 24) | ((((long) bArr[8]) & 255) << 32) | ((((long) bArr[9]) & 255) << 40) | ((((long) bArr[10]) & 255) << 48), "P3_ID");
                        break;
                    case 129:
                        broadcastHomeNotification(((255 & ((long) bArr[7])) << 24) | ((((long) bArr[5]) & 255) << 8) | ((((long) bArr[4]) & 255) << 0) | ((((long) bArr[6]) & 255) << 16), "Temperature");
                        break;
                    case 130:
                        int v = ((bArr[4] & 255) << 0) | ((bArr[5] & 255) << 8);
                        int i2 = ((bArr[6] & 255) << 0) | ((bArr[7] & 255) << 8);
                        int c = ((bArr[8] & 255) << 0) | ((bArr[9] & 255) << 8);
                        int fcc = ((bArr[10] & 255) << 0) | ((bArr[11] & 255) << 8);
                        int t = ((bArr[12] & 255) << 0) | ((bArr[13] & 255) << 8);
                        int v1 = ((bArr[14] & 255) << 0) | ((bArr[15] & 255) << 8);
                        int v2 = ((bArr[16] & 255) << 0) | ((bArr[17] & 255) << 8);
                        int cc = ((bArr[19] & 255) << 8) | ((bArr[18] & 255) << 0);
                        StringBuilder sb = new StringBuilder();
                        sb.append("Battery Voltage =  ");
                        sb.append(v);
                        sb.append(" mv\nBattery Current =  ");
                        sb.append(i2);
                        sb.append(" mA\nBattery ChargingCurrent =  ");
                        sb.append(cc);
                        sb.append(" mA\nBattery Capacity =  ");
                        sb.append(c);
                        sb.append(" mAhr\nBattery Full Capacity =  ");
                        sb.append(fcc);
                        sb.append(" mAhr\nBattery Temperature =  ");
                        sb.append(t);
                        sb.append(" cel\nBattery CellVoltage1 =  ");
                        sb.append(v1);
                        sb.append(" mv\nBattery CellVoltage2 =  ");
                        sb.append(v2);
                        sb.append(" mv");
                        broadcastHomeNotification(sb.toString(), "Battery_info");
                        break;
                }
            } else {
                int charging = (bArr[8] & 255) << 0;
                broadcastHomeNotification(((255 & ((long) bArr[7])) << 24) | ((((long) bArr[4]) & 255) << 0) | ((((long) bArr[5]) & 255) << 8) | ((((long) bArr[6]) & 255) << 16), "BatCapacity");
                broadcastHomeNotification((long) charging, "ChargingStatus");
            }
            this.mHeaderSysPacketDone = false;
        }
        String simpleName = getClass().getSimpleName();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Notification Received - ");
        sb2.append(String.valueOf(bArr.length));
        Log.i(simpleName, sb2.toString());
    }

    /* access modifiers changed from: private */
    /* renamed from: onNotificationSetupFailure */
    public void bridge$lambda$6$SWS_P3ConnectionServices(Throwable throwable) {
        Log.i(getClass().getSimpleName(), "Notifications Error");
    }

    /* access modifiers changed from: private */
    /* renamed from: notificationHasBeenSetUp */
    public void bridge$lambda$10$SWS_P3ConnectionServices() {
        Log.i(getClass().getSimpleName(), "Notifications has been set up");
        this.still_fail = false;
    }

    /* access modifiers changed from: private */
    /* renamed from: dataNotificationSetupError */
    public void bridge$lambda$9$SWS_P3ConnectionServices() {
        Log.i(getClass().getSimpleName(), "dataNotificationSetupError");
    }

    private void broadcastHomeNotification(long data, @Nullable String iName) {
        MethodsFactory.logMessage(TAG, "INSIDE BROADCAST NOTIFICATION DATA");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.HOME_INTENT_ACTION);
        iGotData.putExtra("iName", iName);
        iGotData.putExtra("isNotificationSuccess", true);
        iGotData.putExtra("data", data);
        iGotData.putExtra("reason", "gotData");
        iGotData.putExtra("from", "broadcastHomeNotification");
        MethodsFactory.sendBroadCast(this.HomeActivityContext, iGotData);
    }

    private void broadcastHomeNotification(String input, @Nullable String iName) {
        MethodsFactory.logMessage(TAG, "INSIDE BROADCAST NOTIFICATION DATA");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.HOME_INTENT_ACTION);
        iGotData.putExtra("iName", iName);
        iGotData.putExtra("isNotificationSuccess", true);
        iGotData.putExtra("data", input);
        iGotData.putExtra("reason", "gotData");
        iGotData.putExtra("from", "broadcastHomeNotification");
        MethodsFactory.sendBroadCast(this.HomeActivityContext, iGotData);
    }

    private void broadcastNotificationData(double[] mDoubles, @Nullable String streamByte) {
        MethodsFactory.logMessage(TAG, "INSIDE BROADCAST NOTIFICATION DATA");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.INTENT_ACTION);
        iGotData.putExtra("iName", "sensorNotification_data");
        iGotData.putExtra("isNotificationSuccess", true);
        iGotData.putExtra("data", mDoubles);
        iGotData.putExtra("stream", streamByte);
        iGotData.putExtra("reason", "gotData");
        iGotData.putExtra("from", "broadcastNotificationData");
        MethodsFactory.sendBroadCast(getMainActivityContext(), iGotData);
    }

    private void broadcastNotificationMemoryData(double[] mDoubles) {
        MethodsFactory.logMessage(TAG, "INSIDE BROADCAST NOTIFICATION Memory DATA");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.HOME_INTENT_ACTION);
        iGotData.putExtra("iName", "MemoryScanData");
        iGotData.putExtra("isNotificationSuccess", true);
        iGotData.putExtra("data", mDoubles);
        iGotData.putExtra("reason", "gotData");
        iGotData.putExtra("from", "broadcastNotificationData");
        MethodsFactory.sendBroadCast(this.HomeActivityContext, iGotData);
    }

    private void broadcastNotificationFailure(String msg, String reason, int errorCode) {
        System.out.println("inside broadcastNotificationFailure");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.INTENT_ACTION);
        iGotData.putExtra("iName", "sensorNotification_failure");
        iGotData.putExtra("isNotificationSuccess", false);
        iGotData.putExtra(NotificationCompat.CATEGORY_ERROR, msg);
        iGotData.putExtra("reason", reason);
        iGotData.putExtra("data", errorCode);
        MethodsFactory.sendBroadCast(getMainActivityContext(), iGotData);
    }

    private void broadcastNotificationconnected(String msg) {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("inside broadcastNotificationconnected ");
        sb.append(msg);
        printStream.println(sb.toString());
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.INTENT_ACTION);
        iGotData.putExtra("iName", "sensorNotification_connection");
        iGotData.putExtra("isNotificationSuccess", true);
        iGotData.putExtra(NotificationCompat.CATEGORY_ERROR, msg);
        iGotData.putExtra("reason", "connected");
        MethodsFactory.sendBroadCast(getMainActivityContext(), iGotData);
    }

    public void broadcastdisconnectionNotification() {
        System.out.println("inside broadcastdisconnectionNotification");
        Intent iGotData = new Intent();
        iGotData.setAction(GlobalVariables.INTENT_ACTION);
        iGotData.putExtra("iName", "Disconnection_Notification");
        iGotData.putExtra("reason", "disconnected");
        MethodsFactory.sendBroadCast(getMainActivityContext(), iGotData);
        Intent iGotData2 = new Intent();
        iGotData2.setAction(GlobalVariables.HOME_INTENT_ACTION);
        iGotData2.putExtra("iName", "Disconnection_Notification");
        iGotData2.putExtra("reason", "disconnected");
        MethodsFactory.sendBroadCast(this.HomeActivityContext, iGotData2);
    }

    private void updateUI(BluetoothGattCharacteristic characteristic) {
    }

    private boolean hasProperty(@Nullable BluetoothGattCharacteristic characteristic, int property) {
        return characteristic != null && (characteristic.getProperties() & property) > 0;
    }

    public boolean isConnected() {
        boolean z = false;
        if (this.mRxBleDevice == null) {
            return false;
        }
        setConnecting("isConnected()", false);
        if (this.mRxBleDevice.getConnectionState() == RxBleConnectionState.CONNECTED) {
            z = true;
        }
        return z;
    }

    public boolean isBluetoothEnabled() {
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    @NonNull
    public Intent enableBluetooth() {
        return new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
    }

    public void disableBluetooth() {
        setConnecting("disableBluetooth()", false);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
    }

    public void askForLocationPermissions() {
        MethodsFactory.logMessage("bluetooth", "Ask for permission");
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.MainActivityInstance, "android.permission.ACCESS_FINE_LOCATION")) {
            new AlertDialog.Builder(this.MainActivityInstance).setTitle((CharSequence) "Location permissions needed").setMessage((CharSequence) "you need to allow this permission!").setPositiveButton((CharSequence) "Sure", (OnClickListener) new SWS_P3ConnectionServices$$Lambda$43(this)).setNegativeButton((CharSequence) "Not now", SWS_P3ConnectionServices$$Lambda$44.$instance).show();
        } else {
            ActivityCompat.requestPermissions(this.MainActivityInstance, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$askForLocationPermissions$31$SWS_P3ConnectionServices(DialogInterface dialog, int which) {
        ActivityCompat.requestPermissions(this.MainActivityInstance, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
    }

    static final /* synthetic */ void lambda$askForLocationPermissions$32$SWS_P3ConnectionServices(DialogInterface dialog, int which) {
    }

    public Activity getMainActivityInstance() {
        return this.MainActivityInstance;
    }

    public void setMainActivityInstance(Activity mainActivityInstance) {
        this.MainActivityInstance = mainActivityInstance;
    }

    public Context getMainActivityContext() {
        return this.MainActivityContext;
    }

    public void setMainActivityContext(Context mainActivityContext) {
        this.MainActivityContext = mainActivityContext;
    }
}
