package com.polidea.rxandroidble2.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.polidea.rxandroidble2.internal.scan.RxBleInternalScanResult;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public final class ScanFilter implements Parcelable {
    public static final Creator<ScanFilter> CREATOR = new Creator<ScanFilter>() {
        public ScanFilter[] newArray(int size) {
            return new ScanFilter[size];
        }

        public ScanFilter createFromParcel(Parcel in) {
            Builder builder = new Builder();
            if (in.readInt() == 1) {
                builder.setDeviceName(in.readString());
            }
            if (in.readInt() == 1) {
                builder.setDeviceAddress(in.readString());
            }
            if (in.readInt() == 1) {
                ParcelUuid uuid = (ParcelUuid) in.readParcelable(ParcelUuid.class.getClassLoader());
                builder.setServiceUuid(uuid);
                if (in.readInt() == 1) {
                    builder.setServiceUuid(uuid, (ParcelUuid) in.readParcelable(ParcelUuid.class.getClassLoader()));
                }
            }
            if (in.readInt() == 1) {
                ParcelUuid servcieDataUuid = (ParcelUuid) in.readParcelable(ParcelUuid.class.getClassLoader());
                if (in.readInt() == 1) {
                    byte[] serviceData = new byte[in.readInt()];
                    in.readByteArray(serviceData);
                    if (in.readInt() == 0) {
                        builder.setServiceData(servcieDataUuid, serviceData);
                    } else {
                        byte[] serviceDataMask = new byte[in.readInt()];
                        in.readByteArray(serviceDataMask);
                        builder.setServiceData(servcieDataUuid, serviceData, serviceDataMask);
                    }
                }
            }
            int manufacturerId = in.readInt();
            if (in.readInt() == 1) {
                byte[] manufacturerData = new byte[in.readInt()];
                in.readByteArray(manufacturerData);
                if (in.readInt() == 0) {
                    builder.setManufacturerData(manufacturerId, manufacturerData);
                } else {
                    byte[] manufacturerDataMask = new byte[in.readInt()];
                    in.readByteArray(manufacturerDataMask);
                    builder.setManufacturerData(manufacturerId, manufacturerData, manufacturerDataMask);
                }
            }
            return builder.build();
        }
    };
    private static final ScanFilter EMPTY = new Builder().build();
    @Nullable
    private final String mDeviceAddress;
    @Nullable
    private final String mDeviceName;
    @Nullable
    private final byte[] mManufacturerData;
    @Nullable
    private final byte[] mManufacturerDataMask;
    private final int mManufacturerId;
    @Nullable
    private final byte[] mServiceData;
    @Nullable
    private final byte[] mServiceDataMask;
    @Nullable
    private final ParcelUuid mServiceDataUuid;
    @Nullable
    private final ParcelUuid mServiceUuid;
    @Nullable
    private final ParcelUuid mServiceUuidMask;

    public static final class Builder {
        private String mDeviceAddress;
        private String mDeviceName;
        private byte[] mManufacturerData;
        private byte[] mManufacturerDataMask;
        private int mManufacturerId = -1;
        private byte[] mServiceData;
        private byte[] mServiceDataMask;
        private ParcelUuid mServiceDataUuid;
        private ParcelUuid mServiceUuid;
        private ParcelUuid mUuidMask;

        public Builder setDeviceName(String deviceName) {
            this.mDeviceName = deviceName;
            return this;
        }

        public Builder setDeviceAddress(String deviceAddress) {
            if (deviceAddress == null || BluetoothAdapter.checkBluetoothAddress(deviceAddress)) {
                this.mDeviceAddress = deviceAddress;
                return this;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("invalid device address ");
            sb.append(deviceAddress);
            throw new IllegalArgumentException(sb.toString());
        }

        public Builder setServiceUuid(ParcelUuid serviceUuid) {
            this.mServiceUuid = serviceUuid;
            this.mUuidMask = null;
            return this;
        }

        public Builder setServiceUuid(ParcelUuid serviceUuid, ParcelUuid uuidMask) {
            if (this.mUuidMask == null || this.mServiceUuid != null) {
                this.mServiceUuid = serviceUuid;
                this.mUuidMask = uuidMask;
                return this;
            }
            throw new IllegalArgumentException("uuid is null while uuidMask is not null!");
        }

        public Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData) {
            if (serviceDataUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null");
            }
            this.mServiceDataUuid = serviceDataUuid;
            this.mServiceData = serviceData;
            this.mServiceDataMask = null;
            return this;
        }

        public Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask) {
            if (serviceDataUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null");
            }
            if (this.mServiceDataMask != null) {
                if (this.mServiceData == null) {
                    throw new IllegalArgumentException("serviceData is null while serviceDataMask is not null");
                } else if (this.mServiceData.length != this.mServiceDataMask.length) {
                    throw new IllegalArgumentException("size mismatch for service data and service data mask");
                }
            }
            this.mServiceDataUuid = serviceDataUuid;
            this.mServiceData = serviceData;
            this.mServiceDataMask = serviceDataMask;
            return this;
        }

        public Builder setManufacturerData(int manufacturerId, byte[] manufacturerData) {
            if (manufacturerData == null || manufacturerId >= 0) {
                this.mManufacturerId = manufacturerId;
                this.mManufacturerData = manufacturerData;
                this.mManufacturerDataMask = null;
                return this;
            }
            throw new IllegalArgumentException("invalid manufacture id");
        }

        public Builder setManufacturerData(int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
            if (manufacturerData == null || manufacturerId >= 0) {
                if (this.mManufacturerDataMask != null) {
                    if (this.mManufacturerData == null) {
                        throw new IllegalArgumentException("manufacturerData is null while manufacturerDataMask is not null");
                    } else if (this.mManufacturerData.length != this.mManufacturerDataMask.length) {
                        throw new IllegalArgumentException("size mismatch for manufacturerData and manufacturerDataMask");
                    }
                }
                this.mManufacturerId = manufacturerId;
                this.mManufacturerData = manufacturerData;
                this.mManufacturerDataMask = manufacturerDataMask;
                return this;
            }
            throw new IllegalArgumentException("invalid manufacture id");
        }

        public ScanFilter build() {
            ScanFilter scanFilter = new ScanFilter(this.mDeviceName, this.mDeviceAddress, this.mServiceUuid, this.mUuidMask, this.mServiceDataUuid, this.mServiceData, this.mServiceDataMask, this.mManufacturerId, this.mManufacturerData, this.mManufacturerDataMask);
            return scanFilter;
        }
    }

    private ScanFilter(String name, String deviceAddress, ParcelUuid uuid, ParcelUuid uuidMask, ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask, int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
        this.mDeviceName = name;
        this.mServiceUuid = uuid;
        this.mServiceUuidMask = uuidMask;
        this.mDeviceAddress = deviceAddress;
        this.mServiceDataUuid = serviceDataUuid;
        this.mServiceData = serviceData;
        this.mServiceDataMask = serviceDataMask;
        this.mManufacturerId = manufacturerId;
        this.mManufacturerData = manufacturerData;
        this.mManufacturerDataMask = manufacturerDataMask;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeInt(this.mDeviceName == null ? 0 : 1);
        if (this.mDeviceName != null) {
            dest.writeString(this.mDeviceName);
        }
        dest.writeInt(this.mDeviceAddress == null ? 0 : 1);
        if (this.mDeviceAddress != null) {
            dest.writeString(this.mDeviceAddress);
        }
        dest.writeInt(this.mServiceUuid == null ? 0 : 1);
        if (this.mServiceUuid != null) {
            dest.writeParcelable(this.mServiceUuid, flags);
            dest.writeInt(this.mServiceUuidMask == null ? 0 : 1);
            if (this.mServiceUuidMask != null) {
                dest.writeParcelable(this.mServiceUuidMask, flags);
            }
        }
        dest.writeInt(this.mServiceDataUuid == null ? 0 : 1);
        if (this.mServiceDataUuid != null) {
            dest.writeParcelable(this.mServiceDataUuid, flags);
            dest.writeInt(this.mServiceData == null ? 0 : 1);
            if (this.mServiceData != null) {
                dest.writeInt(this.mServiceData.length);
                dest.writeByteArray(this.mServiceData);
                dest.writeInt(this.mServiceDataMask == null ? 0 : 1);
                if (this.mServiceDataMask != null) {
                    dest.writeInt(this.mServiceDataMask.length);
                    dest.writeByteArray(this.mServiceDataMask);
                }
            }
        }
        dest.writeInt(this.mManufacturerId);
        dest.writeInt(this.mManufacturerData == null ? 0 : 1);
        if (this.mManufacturerData != null) {
            dest.writeInt(this.mManufacturerData.length);
            dest.writeByteArray(this.mManufacturerData);
            if (this.mManufacturerDataMask == null) {
                i = 0;
            }
            dest.writeInt(i);
            if (this.mManufacturerDataMask != null) {
                dest.writeInt(this.mManufacturerDataMask.length);
                dest.writeByteArray(this.mManufacturerDataMask);
            }
        }
    }

    @Nullable
    public String getDeviceName() {
        return this.mDeviceName;
    }

    @Nullable
    public ParcelUuid getServiceUuid() {
        return this.mServiceUuid;
    }

    @Nullable
    public ParcelUuid getServiceUuidMask() {
        return this.mServiceUuidMask;
    }

    @Nullable
    public String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    @Nullable
    public byte[] getServiceData() {
        return this.mServiceData;
    }

    @Nullable
    public byte[] getServiceDataMask() {
        return this.mServiceDataMask;
    }

    @Nullable
    public ParcelUuid getServiceDataUuid() {
        return this.mServiceDataUuid;
    }

    public int getManufacturerId() {
        return this.mManufacturerId;
    }

    @Nullable
    public byte[] getManufacturerData() {
        return this.mManufacturerData;
    }

    @Nullable
    public byte[] getManufacturerDataMask() {
        return this.mManufacturerDataMask;
    }

    public boolean matches(RxBleInternalScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        BluetoothDevice device = scanResult.getBluetoothDevice();
        if (this.mDeviceAddress != null && (device == null || !this.mDeviceAddress.equals(device.getAddress()))) {
            return false;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord == null && (this.mDeviceName != null || this.mServiceUuid != null || this.mManufacturerData != null || this.mServiceData != null)) {
            return false;
        }
        if (this.mDeviceName != null && !this.mDeviceName.equals(scanRecord.getDeviceName()) && !this.mDeviceName.equals(device.getName())) {
            return false;
        }
        if (this.mServiceUuid != null && !matchesServiceUuids(this.mServiceUuid, this.mServiceUuidMask, scanRecord.getServiceUuids())) {
            return false;
        }
        if (this.mServiceDataUuid != null && !matchesPartialData(this.mServiceData, this.mServiceDataMask, scanRecord.getServiceData(this.mServiceDataUuid))) {
            return false;
        }
        if (this.mManufacturerId < 0 || matchesPartialData(this.mManufacturerData, this.mManufacturerDataMask, scanRecord.getManufacturerSpecificData(this.mManufacturerId))) {
            return true;
        }
        return false;
    }

    private boolean matchesServiceUuids(ParcelUuid uuid, ParcelUuid parcelUuidMask, List<ParcelUuid> uuids) {
        if (uuid == null) {
            return true;
        }
        if (uuids == null) {
            return false;
        }
        for (ParcelUuid parcelUuid : uuids) {
            if (matchesServiceUuid(uuid.getUuid(), parcelUuidMask == null ? null : parcelUuidMask.getUuid(), parcelUuid.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesServiceUuid(UUID uuid, UUID mask, UUID data) {
        if (mask == null) {
            return uuid.equals(data);
        }
        boolean z = false;
        if ((uuid.getLeastSignificantBits() & mask.getLeastSignificantBits()) != (data.getLeastSignificantBits() & mask.getLeastSignificantBits())) {
            return false;
        }
        if ((uuid.getMostSignificantBits() & mask.getMostSignificantBits()) == (data.getMostSignificantBits() & mask.getMostSignificantBits())) {
            z = true;
        }
        return z;
    }

    private boolean matchesPartialData(byte[] data, byte[] dataMask, byte[] parsedData) {
        if (parsedData == null || parsedData.length < data.length) {
            return false;
        }
        if (dataMask == null) {
            for (int i = 0; i < data.length; i++) {
                if (parsedData[i] != data[i]) {
                    return false;
                }
            }
            return true;
        }
        for (int i2 = 0; i2 < data.length; i2++) {
            if ((dataMask[i2] & parsedData[i2]) != (dataMask[i2] & data[i2])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BluetoothLeScanFilter [mDeviceName=");
        sb.append(this.mDeviceName);
        sb.append(", mDeviceAddress=");
        sb.append(this.mDeviceAddress);
        sb.append(", mUuid=");
        sb.append(this.mServiceUuid);
        sb.append(", mUuidMask=");
        sb.append(this.mServiceUuidMask);
        sb.append(", mServiceDataUuid=");
        sb.append(String.valueOf(this.mServiceDataUuid));
        sb.append(", mServiceData=");
        sb.append(Arrays.toString(this.mServiceData));
        sb.append(", mServiceDataMask=");
        sb.append(Arrays.toString(this.mServiceDataMask));
        sb.append(", mManufacturerId=");
        sb.append(this.mManufacturerId);
        sb.append(", mManufacturerData=");
        sb.append(Arrays.toString(this.mManufacturerData));
        sb.append(", mManufacturerDataMask=");
        sb.append(Arrays.toString(this.mManufacturerDataMask));
        sb.append("]");
        return sb.toString();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.mDeviceName, this.mDeviceAddress, Integer.valueOf(this.mManufacturerId), Integer.valueOf(Arrays.hashCode(this.mManufacturerData)), Integer.valueOf(Arrays.hashCode(this.mManufacturerDataMask)), this.mServiceDataUuid, Integer.valueOf(Arrays.hashCode(this.mServiceData)), Integer.valueOf(Arrays.hashCode(this.mServiceDataMask)), this.mServiceUuid, this.mServiceUuidMask});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScanFilter other = (ScanFilter) obj;
        if (!equals(this.mDeviceName, other.mDeviceName) || !equals(this.mDeviceAddress, other.mDeviceAddress) || this.mManufacturerId != other.mManufacturerId || !deepEquals(this.mManufacturerData, other.mManufacturerData) || !deepEquals(this.mManufacturerDataMask, other.mManufacturerDataMask) || !equals(this.mServiceDataUuid, other.mServiceDataUuid) || !deepEquals(this.mServiceData, other.mServiceData) || !deepEquals(this.mServiceDataMask, other.mServiceDataMask) || !equals(this.mServiceUuid, other.mServiceUuid) || !equals(this.mServiceUuidMask, other.mServiceUuidMask)) {
            z = false;
        }
        return z;
    }

    private static boolean equals(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    private static boolean deepEquals(byte[] a, byte[] b) {
        return a == b || !(a == null || b == null || !Arrays.equals(a, b));
    }

    public boolean isAllFieldsEmpty() {
        return equals(EMPTY);
    }

    public static ScanFilter empty() {
        return new Builder().build();
    }
}
