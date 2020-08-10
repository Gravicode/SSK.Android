package com.polidea.rxandroidble2.internal.connection;

import android.bluetooth.BluetoothGattCharacteristic;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.internal.util.CharacteristicPropertiesParser;
import java.util.Locale;

public class IllegalOperationMessageCreator {
    private CharacteristicPropertiesParser propertiesParser;

    @Inject
    public IllegalOperationMessageCreator(CharacteristicPropertiesParser propertiesParser2) {
        this.propertiesParser = propertiesParser2;
    }

    public String createMismatchMessage(BluetoothGattCharacteristic characteristic, int neededProperties) {
        return String.format(Locale.getDefault(), "Characteristic %s supports properties: %s (%d) does not have any property matching %s (%d)", new Object[]{characteristic.getUuid(), this.propertiesParser.propertiesIntToString(characteristic.getProperties()), Integer.valueOf(characteristic.getProperties()), this.propertiesParser.propertiesIntToString(neededProperties), Integer.valueOf(neededProperties)});
    }
}
