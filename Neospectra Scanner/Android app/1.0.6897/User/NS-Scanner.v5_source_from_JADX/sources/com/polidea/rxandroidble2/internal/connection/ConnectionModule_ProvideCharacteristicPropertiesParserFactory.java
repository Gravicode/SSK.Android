package com.polidea.rxandroidble2.internal.connection;

import bleshadow.dagger.internal.Factory;
import bleshadow.dagger.internal.Preconditions;
import com.polidea.rxandroidble2.internal.util.CharacteristicPropertiesParser;

public final class ConnectionModule_ProvideCharacteristicPropertiesParserFactory implements Factory<CharacteristicPropertiesParser> {
    private final ConnectionModule module;

    public ConnectionModule_ProvideCharacteristicPropertiesParserFactory(ConnectionModule module2) {
        this.module = module2;
    }

    public CharacteristicPropertiesParser get() {
        return (CharacteristicPropertiesParser) Preconditions.checkNotNull(this.module.provideCharacteristicPropertiesParser(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static ConnectionModule_ProvideCharacteristicPropertiesParserFactory create(ConnectionModule module2) {
        return new ConnectionModule_ProvideCharacteristicPropertiesParserFactory(module2);
    }

    public static CharacteristicPropertiesParser proxyProvideCharacteristicPropertiesParser(ConnectionModule instance) {
        return (CharacteristicPropertiesParser) Preconditions.checkNotNull(instance.provideCharacteristicPropertiesParser(), "Cannot return null from a non-@Nullable @Provides method");
    }
}
