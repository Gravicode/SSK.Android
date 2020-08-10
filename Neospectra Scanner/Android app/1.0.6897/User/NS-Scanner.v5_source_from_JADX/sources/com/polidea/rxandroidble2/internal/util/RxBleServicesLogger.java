package com.polidea.rxandroidble2.internal.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import bleshadow.javax.inject.Inject;
import com.polidea.rxandroidble2.RxBleDeviceServices;
import com.polidea.rxandroidble2.internal.RxBleLog;
import com.polidea.rxandroidble2.utils.StandardUUIDsParser;

public class RxBleServicesLogger {
    private final CharacteristicPropertiesParser characteristicPropertiesParser;

    @Inject
    RxBleServicesLogger(CharacteristicPropertiesParser characteristicPropertiesParser2) {
        this.characteristicPropertiesParser = characteristicPropertiesParser2;
    }

    public void log(RxBleDeviceServices rxBleDeviceServices, BluetoothDevice device) {
        if (RxBleLog.isAtLeast(2)) {
            RxBleLog.m23v("Preparing services description", new Object[0]);
            RxBleLog.m23v(prepareServicesDescription(rxBleDeviceServices, device), new Object[0]);
        }
    }

    private String prepareServicesDescription(RxBleDeviceServices rxBleDeviceServices, BluetoothDevice device) {
        StringBuilder descriptionBuilder = new StringBuilder();
        appendDeviceHeader(device, descriptionBuilder);
        for (BluetoothGattService bluetoothGattService : rxBleDeviceServices.getBluetoothGattServices()) {
            descriptionBuilder.append(10);
            appendServiceDescription(descriptionBuilder, bluetoothGattService);
        }
        descriptionBuilder.append("\n--------------- ====== Finished peripheral content ====== ---------------");
        return descriptionBuilder.toString();
    }

    private void appendServiceDescription(StringBuilder descriptionBuilder, BluetoothGattService bluetoothGattService) {
        appendServiceHeader(descriptionBuilder, bluetoothGattService);
        descriptionBuilder.append("-> Characteristics:");
        for (BluetoothGattCharacteristic characteristic : bluetoothGattService.getCharacteristics()) {
            appendCharacteristicNameHeader(descriptionBuilder, characteristic);
            appendCharacteristicProperties(descriptionBuilder, characteristic);
            appendDescriptors(descriptionBuilder, characteristic);
        }
    }

    private void appendDescriptors(StringBuilder descriptionBuilder, BluetoothGattCharacteristic characteristic) {
        if (!characteristic.getDescriptors().isEmpty()) {
            appendDescriptorsHeader(descriptionBuilder);
            for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
                appendDescriptorNameHeader(descriptionBuilder, descriptor);
            }
        }
    }

    private void appendDescriptorsHeader(StringBuilder descriptionBuilder) {
        descriptionBuilder.append(10);
        descriptionBuilder.append(9);
        descriptionBuilder.append("  ");
        descriptionBuilder.append("-> Descriptors: ");
    }

    private void appendCharacteristicNameHeader(StringBuilder descriptionBuilder, BluetoothGattCharacteristic characteristic) {
        descriptionBuilder.append(10);
        descriptionBuilder.append(9);
        descriptionBuilder.append("* ");
        descriptionBuilder.append(createCharacteristicName(characteristic));
        descriptionBuilder.append(" (");
        descriptionBuilder.append(characteristic.getUuid().toString());
        descriptionBuilder.append(")");
    }

    private void appendDescriptorNameHeader(StringBuilder descriptionBuilder, BluetoothGattDescriptor descriptor) {
        descriptionBuilder.append(10);
        descriptionBuilder.append(9);
        descriptionBuilder.append(9);
        descriptionBuilder.append("* ");
        descriptionBuilder.append(createDescriptorName(descriptor));
        descriptionBuilder.append(" (");
        descriptionBuilder.append(descriptor.getUuid().toString());
        descriptionBuilder.append(")");
    }

    private String createDescriptorName(BluetoothGattDescriptor descriptor) {
        String descriptorName = StandardUUIDsParser.getDescriptorName(descriptor.getUuid());
        return descriptorName == null ? "Unknown descriptor" : descriptorName;
    }

    private void appendCharacteristicProperties(StringBuilder descriptionBuilder, BluetoothGattCharacteristic characteristic) {
        descriptionBuilder.append(10);
        descriptionBuilder.append(9);
        descriptionBuilder.append("  ");
        descriptionBuilder.append("Properties: ");
        descriptionBuilder.append(this.characteristicPropertiesParser.propertiesIntToString(characteristic.getProperties()));
    }

    private String createCharacteristicName(BluetoothGattCharacteristic characteristic) {
        String characteristicName = StandardUUIDsParser.getCharacteristicName(characteristic.getUuid());
        return characteristicName == null ? "Unknown characteristic" : characteristicName;
    }

    private void appendDeviceHeader(BluetoothDevice device, StringBuilder descriptionBuilder) {
        descriptionBuilder.append("--------------- ====== Printing peripheral content ====== ---------------\n");
        descriptionBuilder.append("PERIPHERAL ADDRESS: ");
        descriptionBuilder.append(device.getAddress());
        descriptionBuilder.append(10);
        descriptionBuilder.append("PERIPHERAL NAME: ");
        descriptionBuilder.append(device.getName());
        descriptionBuilder.append(10);
        descriptionBuilder.append("-------------------------------------------------------------------------");
    }

    private void appendServiceHeader(StringBuilder descriptionBuilder, BluetoothGattService bluetoothGattService) {
        descriptionBuilder.append("\n");
        descriptionBuilder.append(createServiceType(bluetoothGattService));
        descriptionBuilder.append(" - ");
        descriptionBuilder.append(createServiceName(bluetoothGattService));
        descriptionBuilder.append(" (");
        descriptionBuilder.append(bluetoothGattService.getUuid().toString());
        descriptionBuilder.append(")\n");
        descriptionBuilder.append("Instance ID: ");
        descriptionBuilder.append(bluetoothGattService.getInstanceId());
        descriptionBuilder.append(10);
    }

    private String createServiceName(BluetoothGattService bluetoothGattService) {
        String serviceName = StandardUUIDsParser.getServiceName(bluetoothGattService.getUuid());
        return serviceName == null ? "Unknown service" : serviceName;
    }

    private String createServiceType(BluetoothGattService bluetoothGattService) {
        if (bluetoothGattService.getType() == 0) {
            return "Primary Service";
        }
        return "Secondary Service";
    }
}
