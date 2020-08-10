package com.polidea.rxandroidble2.internal.scan;

import android.annotation.SuppressLint;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.bluetooth.le.ScanSettings.Builder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import bleshadow.javax.inject.Inject;
import bleshadow.javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AndroidScanObjectsConverter {
    private final int deviceSdk;

    @Inject
    public AndroidScanObjectsConverter(@Named("device-sdk") int deviceSdk2) {
        this.deviceSdk = deviceSdk2;
    }

    @SuppressLint({"NewApi"})
    @RequiresApi(api = 21)
    public ScanSettings toNativeSettings(com.polidea.rxandroidble2.scan.ScanSettings scanSettings) {
        Builder builder = new Builder();
        if (this.deviceSdk >= 23) {
            setMarshmallowSettings(scanSettings, builder);
        }
        return builder.setReportDelay(scanSettings.getReportDelayMillis()).setScanMode(scanSettings.getScanMode()).build();
    }

    @RequiresApi(api = 23)
    private void setMarshmallowSettings(com.polidea.rxandroidble2.scan.ScanSettings scanSettings, Builder builder) {
        builder.setCallbackType(scanSettings.getCallbackType()).setMatchMode(scanSettings.getMatchMode()).setNumOfMatches(scanSettings.getNumOfMatches());
    }

    @Nullable
    @RequiresApi(api = 21)
    public List<ScanFilter> toNativeFilters(com.polidea.rxandroidble2.scan.ScanFilter... scanFilters) {
        List<ScanFilter> returnList;
        if (scanFilters != null && scanFilters.length > 0) {
            returnList = new ArrayList<>(scanFilters.length);
            for (com.polidea.rxandroidble2.scan.ScanFilter scanFilter : scanFilters) {
                returnList.add(toNative(scanFilter));
            }
        } else {
            returnList = null;
        }
        return returnList;
    }

    @RequiresApi(api = 21)
    private ScanFilter toNative(com.polidea.rxandroidble2.scan.ScanFilter scanFilter) {
        ScanFilter.Builder builder = new ScanFilter.Builder();
        if (scanFilter.getServiceDataUuid() != null) {
            builder.setServiceData(scanFilter.getServiceDataUuid(), scanFilter.getServiceData(), scanFilter.getServiceDataMask());
        }
        return builder.setDeviceAddress(scanFilter.getDeviceAddress()).setDeviceName(scanFilter.getDeviceName()).setManufacturerData(scanFilter.getManufacturerId(), scanFilter.getManufacturerData(), scanFilter.getManufacturerDataMask()).setServiceUuid(scanFilter.getServiceUuid(), scanFilter.getServiceUuidMask()).build();
    }
}
