package com.polidea.rxandroidble2.internal.util;

import bleshadow.dagger.internal.Factory;
import bleshadow.javax.inject.Provider;

public final class RxBleServicesLogger_Factory implements Factory<RxBleServicesLogger> {
    private final Provider<CharacteristicPropertiesParser> characteristicPropertiesParserProvider;

    public RxBleServicesLogger_Factory(Provider<CharacteristicPropertiesParser> characteristicPropertiesParserProvider2) {
        this.characteristicPropertiesParserProvider = characteristicPropertiesParserProvider2;
    }

    public RxBleServicesLogger get() {
        return new RxBleServicesLogger((CharacteristicPropertiesParser) this.characteristicPropertiesParserProvider.get());
    }

    public static RxBleServicesLogger_Factory create(Provider<CharacteristicPropertiesParser> characteristicPropertiesParserProvider2) {
        return new RxBleServicesLogger_Factory(characteristicPropertiesParserProvider2);
    }

    public static RxBleServicesLogger newRxBleServicesLogger(CharacteristicPropertiesParser characteristicPropertiesParser) {
        return new RxBleServicesLogger(characteristicPropertiesParser);
    }
}
